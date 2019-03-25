import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

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

  public static void main(String[] args) throws IOException {
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
  }
}
