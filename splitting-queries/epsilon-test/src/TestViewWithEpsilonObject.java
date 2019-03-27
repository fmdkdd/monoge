import java.io.File;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.emc.emf.InMemoryEmfModel;
import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.eol.execute.operations.EolOperationFactory;

import org.atlanmod.emfviews.core.EpsilonResource;
import org.atlanmod.emfviews.core.ViewResource;
import org.atlanmod.emfviews.virtuallinks.VirtualLinksPackage;

public class TestViewWithEpsilonObject {
  static String here = new File(".").getAbsolutePath();

  static URI resourceURI(String relativePath) {
    return URI.createFileURI(here + relativePath);
  }

  static Stream<EObject> contentsStream(Resource r) {
    Iterable<EObject> contents = () -> r.getAllContents();
    return StreamSupport.stream(contents.spliterator(), true);
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

    ViewResource r = new ViewResource(resourceURI("/resources/test.eview"));

    r.load(null);

    // Check access to virtual features through EMF API

    Iterable<EObject> eClasses = () -> contentsStream(r)
      .filter(o -> o.eClass().getName().equals("EClass"))
      .iterator();

    for (EObject o : eClasses) {
      {
        EStructuralFeature f = o.eClass().getEStructuralFeature("name");
        System.out.println(o.eGet(f));
      }
      {
        EStructuralFeature f = o.eClass().getEStructuralFeature("rows");
        if (f != null) {
          System.out.println(o.eGet(f));
        }
      }
    }

    Iterable<EObject> rows = () -> contentsStream(r)
      .filter(o -> o.eClass().getName().equals("Row"))
      .iterator();

    for (EObject o : rows) {
      {
        EStructuralFeature f = o.eClass().getEStructuralFeature("name");
        System.out.println(o.eGet(f));
      }
      {
        EStructuralFeature f = o.eClass().getEStructuralFeature("eclasses");
        if (f != null) {
          System.out.println(o.eGet(f));
        }
      }
    }

    // Run EOL query on the view

    EolModule module = new EolModule();
    module.parse("VIEW!Row.all.eclasses.name.println();");
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
    module.getContext().getModelRepository().addModel(new InMemoryEmfModel("VIEW", r, p));

    // Execute the module
    System.out.format("EOL result: %s", module.execute());
  }
}
