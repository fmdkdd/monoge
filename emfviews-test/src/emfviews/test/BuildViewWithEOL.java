package emfviews.test;

import java.io.File;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.emc.emf.InMemoryEmfModel;
import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.eol.execute.operations.EolOperationFactory;

public class BuildViewWithEOL {

  static String here = new File(".").getAbsolutePath();

  static URI resourceURI(String relativePath) {
    return URI.createFileURI(here + relativePath);
  }

  public static void main(String[] args) throws Exception {
    Map<String, Object> map = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap();
    map.put("ecore", new EcoreResourceFactoryImpl());
    map.put("xmi", new XMIResourceFactoryImpl());

    ResourceSet rs = new ResourceSetImpl();

    // Load metamodel
    String metamodelPath = "/resources/metamodels/Log.ecore";
    Resource metamodel = rs.getResource(resourceURI(metamodelPath), true);
    EPackage pack = (EPackage) metamodel.getContents().get(0);
    EPackage.Registry.INSTANCE.put(pack.getNsURI(), pack);

    // Load model
    String modelPath = "/resources/models/shortoutputs.xmi";
    Resource model = rs.getResource(resourceURI(modelPath), true);

    // Parse EOL
    EolModule module = new EolModule();
    module.parse(new File(resourceURI("/resources/build.eol").toFileString()));
    if (module.getParseProblems().size() > 0) {
      System.err.println("Parse errors occured...");
      for (ParseProblem problem : module.getParseProblems()) {
        System.err.println(problem.toString());
      }
      throw new Exception("Error in parsing ECL file.  See stderr for details");
    }
    module.getContext().setOperationFactory(new EolOperationFactory());

    // Add model as IModel
    InMemoryEmfModel m = new InMemoryEmfModel(model);
    m.setName("IN");
    module.getContext().getModelRepository().addModel(m);

    // Execute EOL
    module.execute();
  }

}
