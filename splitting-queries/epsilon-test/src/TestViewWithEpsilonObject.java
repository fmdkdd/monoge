import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;

import org.atlanmod.emfviews.core.EpsilonResource;
import org.atlanmod.emfviews.core.ViewResource;

public class TestViewWithEpsilonObject {
  static String here = new File(".").getAbsolutePath();

  static URI resourceURI(String relativePath) {
    return URI.createFileURI(here + relativePath);
  }

  public static void main(String[] args) throws IOException {
    EcorePackage.eINSTANCE.eClass();
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());

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

    for (EObject o : r.getContents()) {
      EStructuralFeature f = o.eClass().getEStructuralFeature("foo");
      if (f != null) {
        System.out.println(o.eGet(f));
      }
    }
  }
}
