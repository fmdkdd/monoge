import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.emc.emfviews.EMFViewsModel;
import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.eol.execute.operations.EolOperationFactory;

import org.atlanmod.emfviews.core.EmfViewsFactory;
import org.atlanmod.emfviews.core.EpsilonResource;
import org.atlanmod.emfviews.core.ViewResource;
import org.atlanmod.emfviews.virtuallinks.VirtualLinksPackage;

public class TestEMFViewsEMC {
  static String here = new File(".").getAbsolutePath();

  static URI resourceURI(String relativePath) {
    return URI.createFileURI(here + relativePath);
  }

  public static void main(String[] args) throws Exception {
    EcorePackage.eINSTANCE.eClass();
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
    VirtualLinksPackage.eINSTANCE.eClass();
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
    Resource.Factory epsRF = new Resource.Factory() {
      @Override
      public Resource createResource(URI uri) {
        return new EpsilonResource(uri);
      }
    };
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("csv", epsRF);
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("bib", epsRF);
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("eview", new EmfViewsFactory());

    ViewResource r = new ViewResource(resourceURI("/resources/test.eview"));
    r.load(null);

    // Run EOL query on the view
    EolModule module = new EolModule();
    module.parse("VIEW!Row.all.collect(r | Sequence{ r.name, r.eclasses.name }).println();");
    if (module.getParseProblems().size() > 0) {
      System.err.println("Parse errors occured...");
      for (ParseProblem problem : module.getParseProblems()) {
        System.err.println(problem.toString());
      }
      throw new Exception("Error in parsing ECL file.  See stderr for details");
    }
    module.getContext().setOperationFactory(new EolOperationFactory());

    // Add view
    // Need the viewpoint as well
    EPackage p = r.getContents().get(0).eClass().getEPackage();
    EMFViewsModel m = new EMFViewsModel();
    m.setName("VIEW");
    m.setModelFileUri(resourceURI("/resources/test.eview"));
    m.setMetamodelUri(p.getNsURI());
    m.load();
    module.getContext().getModelRepository().addModel(m);

    // Execute the module
    System.out.format("EOL result: %s", module.execute());
  }
}
