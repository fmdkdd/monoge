/*******************************************************************************
 * Copyright (c) 2017, 2018 Armines
 * Copyright (c) 2013 INRIA
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License, v. 2.0 are satisfied: GNU General Public License, version 3
 * which is available at https://www.gnu.org/licenses/gpl-3.0.txt
 *
 * Contributors:
 *   fmdkdd - refactoring and extension
 *   Juan David Villa Calle - initial API and implementation
 *******************************************************************************/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.ecl.EclModule;
import org.eclipse.epsilon.ecl.execute.EclOperationFactory;
import org.eclipse.epsilon.ecl.trace.Match;
import org.eclipse.epsilon.ecl.trace.MatchTrace;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;

import fr.inria.atlanmod.neoemf.data.PersistenceBackendFactoryRegistry;
import fr.inria.atlanmod.neoemf.data.blueprints.BlueprintsPersistenceBackendFactory;
import fr.inria.atlanmod.neoemf.data.blueprints.util.BlueprintsURI;
import fr.inria.atlanmod.neoemf.resource.PersistentResourceFactory;

import org.atlanmod.emfviews.virtuallinks.ConcreteConcept;
import org.atlanmod.emfviews.virtuallinks.ContributingModel;
import org.atlanmod.emfviews.virtuallinks.VirtualAssociation;
import org.atlanmod.emfviews.virtuallinks.VirtualLinksFactory;
import org.atlanmod.emfviews.virtuallinks.WeavingModel;

public class EclDelegateMut {

  public WeavingModel createWeavingModel(WeavingModel weavingModel, URI linksDslURI, List<Resource> inputModels) throws Exception {

    File f;

    // Need to turn an EMF URI into an actual File location. We cannot use the URIConverter.INSTANCE since it only
    // provides InputStream, and EclModule needs an actual file.
    // Assume a regular file path. Will throw if it cannot be found anyway.
    f = new File(linksDslURI.toFileString());

    // Collect the contributing metamodels from the header of the ECL file
    // @Refactor: I'm not sure why this is even needed.
    // We could pass the metamodels directly as arguments to the ECL delegate.
    FileReader fr = new FileReader(f);
    BufferedReader br = new BufferedReader(fr);
    String sCurrentLine = "";

    Map<String, Resource> inputMetamodelAliasToModel = new HashMap<>();
    Map<String, String> inputMetamodelAliasToMetamodelNsURI = new HashMap<>();

    // @Correctness: this ad-hoc parser does not signal errors, and will fail with whitespace between `alias`
    // and the value.
    while (((sCurrentLine = br.readLine()) != null) && sCurrentLine.startsWith("//alias")) {
      String metamodelAlias =
          sCurrentLine.substring(sCurrentLine.indexOf("_") + 1, sCurrentLine.indexOf("="));
      String metamodelNsURI = sCurrentLine.substring(sCurrentLine.indexOf("=") + 1);

      Resource model = inputModels.stream().filter(r ->
      // @Refactor: is there an easier way to get the NsURI of the metamodel?
      metamodelNsURI.compareToIgnoreCase(r.getContents().get(0).eClass().getEPackage().getNsURI()) == 0)
          .findFirst()
          .orElseThrow(() -> new IllegalArgumentException(String.format("No models correspond to the metamodel %s of alias %s",
                                                                        metamodelNsURI, metamodelAlias)));

      inputMetamodelAliasToModel.put(metamodelAlias, model);
      inputMetamodelAliasToMetamodelNsURI.put(metamodelAlias, metamodelNsURI);
    }
    br.close();

    // @Correctness: ECL needs to know about the neo-blueprints scheme in order to load
    // GraphDB resources.
    PersistenceBackendFactoryRegistry.register(BlueprintsURI.SCHEME,
                                               BlueprintsPersistenceBackendFactory.getInstance());
    Resource.Factory.Registry.INSTANCE
    .getProtocolToFactoryMap()
    .put(BlueprintsURI.SCHEME,
         PersistentResourceFactory.getInstance());

    // Prepare the ECL Module
    EclModule module = new EclModule();
    module.parse(f);
    if (module.getParseProblems().size() > 0) {
      System.err.println("Parse errors occured...");
      for (ParseProblem problem : module.getParseProblems()) {
        System.err.println(problem.toString());
      }
      throw new Exception("Error in parsing ECL file.  See stderr for details");
    }
    EclOperationFactory operationFactory = new EclOperationFactory();
    module.getContext().setOperationFactory(operationFactory);

    Iterator<Map.Entry<String, Resource>> iter =
        inputMetamodelAliasToModel.entrySet().iterator();
    while (iter.hasNext()) {
      Entry<String, Resource> tempEntry = iter.next();
      Resource modelResource = tempEntry.getValue();
      EmfModel inputModel = createEmfModelByURI(tempEntry.getKey(), modelResource,
                                                inputMetamodelAliasToMetamodelNsURI.get(tempEntry.getKey()),
                                                true, false);

      module.getContext().getModelRepository().addModel(inputModel);
    }

    // Execute the module
    MatchTrace mt = (MatchTrace) module.execute();

    List<Match> matches = mt.getMatches();

    // Use the matches to construct a weaving model
    VirtualLinksFactory vLinksFactory = VirtualLinksFactory.eINSTANCE;

    HashMap<String, ContributingModel> modelsByURI = new HashMap<>();

    for (Match match : matches) {
      if (match.isMatching()) {
        EObject left = (EObject) match.getLeft();
        EObject right = (EObject) match.getRight();

        VirtualAssociation vAsso = vLinksFactory.createVirtualAssociation();
        vAsso.setName(match.getRule().getName());
        vAsso.setLowerBound(0);
        vAsso.setUpperBound(1);

        ConcreteConcept lSource = vLinksFactory.createConcreteConcept();
        lSource.setPath(left.eResource().getURIFragment(left));

        String sourceModelURI = left.eClass().getEPackage().getNsURI();
        if (!modelsByURI.containsKey(sourceModelURI)) {
          ContributingModel m = vLinksFactory.createContributingModel();
          m.setURI(sourceModelURI);
          modelsByURI.put(sourceModelURI, m);
          weavingModel.getContributingModels().add(m);
        }
        lSource.setModel(modelsByURI.get(sourceModelURI));

        vAsso.setSource(lSource);

        ConcreteConcept lTarget = vLinksFactory.createConcreteConcept();
        lTarget.setPath(right.eResource().getURIFragment(right));
        // TODO: check the linked elements are concepts

        String targetModelURI = right.eClass().getEPackage().getNsURI();
        if (!modelsByURI.containsKey(targetModelURI)) {
          ContributingModel m = vLinksFactory.createContributingModel();
          m.setURI(targetModelURI);
          modelsByURI.put(targetModelURI, m);
          weavingModel.getContributingModels().add(m);
        }
        lTarget.setModel(modelsByURI.get(targetModelURI));

        vAsso.setTarget(lTarget);

        weavingModel.getVirtualLinks().add(vAsso);
      }
    }

    return weavingModel;
  }

  protected EmfModel createEmfModelByURI(String name, Resource model, String metamodelURI,
                                         boolean readOnLoad,
                                         boolean storeOnDisposal) throws EolModelLoadingException {
    EmfModel emfModel = new EmfModel();
    StringProperties properties = new StringProperties();
    properties.put(EmfModel.PROPERTY_NAME, name);
    properties.put(EmfModel.PROPERTY_METAMODEL_URI, metamodelURI);
    properties.put(EmfModel.PROPERTY_MODEL_URI, model.getURI().toString());
    properties.put(EmfModel.PROPERTY_READONLOAD, false);
    properties.put(EmfModel.PROPERTY_STOREONDISPOSAL, storeOnDisposal);
    emfModel.load(properties);
    emfModel.setModelImpl(model);
    return emfModel;
  }

}
