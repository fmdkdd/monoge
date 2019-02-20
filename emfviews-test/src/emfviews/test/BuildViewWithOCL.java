package emfviews.test;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.ocl.pivot.Property;
import org.eclipse.ocl.pivot.utilities.OCL;
import org.eclipse.ocl.pivot.utilities.ParserException;
import org.eclipse.ocl.pivot.utilities.Query;
import org.eclipse.ocl.xtext.completeocl.CompleteOCLStandaloneSetup;

public class BuildViewWithOCL {
  static String here = new File(".").getAbsolutePath();

  static URI resourceURI(String relativePath) {
    return URI.createFileURI(here + relativePath);
  }

  static <T extends EObject> Stream<T> allInstances(Resource r, Class<T> eClass) {
    Iterable<EObject> contents = () -> r.getAllContents();
    return StreamSupport.stream(contents.spliterator(), true)
      .filter(eClass::isInstance)
      .map(eClass::cast);
  }

  static Stream<EObject> allInstances(Resource r, EClassifier eClass) {
    Iterable<EObject> contents = () -> r.getAllContents();
    return StreamSupport.stream(contents.spliterator(), true)
      .filter(eClass::isInstance);
  }

  public static void main(String[] args) throws ParserException {
    Map<String, Object> map = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap();
    map.put("ecore", new EcoreResourceFactoryImpl());
    map.put("xmi", new XMIResourceFactoryImpl());

    CompleteOCLStandaloneSetup.doSetup();
    //map.put("ocl", new OCLASResourceFactory());

    ResourceSet rs = new ResourceSetImpl();

    // Load metamodel
    String metamodelPath = "/resources/metamodels/Log.ecore";
    Resource metamodel = rs.getResource(resourceURI(metamodelPath), true);
    EPackage pack = (EPackage) metamodel.getContents().get(0);
    EPackage.Registry.INSTANCE.put(pack.getNsURI(), pack);

    // Load model
    String modelPath = "/resources/models/shortoutputs.xmi";
    Resource model = rs.getResource(resourceURI(modelPath), true);

    // Load OCL file
    String builderPath = "/resources/build.ocl";
    //Resource ocl = rs.getResource(resourceURI(modelPath), true);

    // Execute it, navigate results
    OCL ocl = OCL.newInstance(rs);
    //Resource builder = rs.getResource(resourceURI(builderPath), true);
    Resource builder = ocl.parse(resourceURI(builderPath));

    // @Correctness: If builder is null, we may have parsing or validation
    // errors. Instead, we could open the resource directly and get a concrete
    // syntax tree. If we only have validation errors, we should still be able
    // to execute the body of properties.
    //
    // For now, we assume the OCL file parses and validates correctly.

    if (builder == null) {
      System.err.println("Parse or validation error in OCL file");
      System.exit(1);
    }

    // For each property, get the model instances corresponding to the context
    // class, execute the query on each one and populate the view
    for (Property prop : (Iterable<Property>) allInstances(builder, Property.class)::iterator) {
      System.out.println("Executing prop: " + prop.getName());
      // Using the 'init' as a pre-condition
      String pre = prop.getOwnedExpression().getBody();
      if (pre == null) {
        break;
      }
      System.out.println("Pre: " + pre);

      // And the 'derive' as query body
      String queryText = prop.getOwningClass().getOwnedInvariants().get(0).getOwnedSpecification().getBody();
      System.out.println("Query: " + queryText);

      String className = prop.getOwningClass().getName();
      EClass eClass = (EClass) pack.getEClassifier(className);
      System.out.println("EClassifier: " + eClass);

      // Filter elements that obey the pre-condition
      Stream<EObject> candidates = allInstances(model, eClass)
        .filter(o -> {
          //return "fallback".equals(o.eGet(eClass.getEStructuralFeature("variable")));
          try {
            Query q = ocl.createQuery(ocl.createQuery(eClass, pre));
            Object result = q.evaluateEcore(o);
            if (result instanceof Boolean) {
              return (boolean) result;
            } else {
              return false;
            }
          } catch (ParserException ex) {
            ex.printStackTrace();
            return false;
          }
        });

      for (EObject elem : (Iterable<EObject>) candidates::iterator) {
        Query query = ocl.createQuery(ocl.createQuery(eClass, queryText));
        System.out.println(elem);
        Object result = query.evaluateEcore(elem);
        System.out.println(result);
      }
    }

    System.out.println("Done");

    ocl.dispose();
  }

}
