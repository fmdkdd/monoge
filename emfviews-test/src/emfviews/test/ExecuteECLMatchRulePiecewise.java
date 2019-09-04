package emfviews.test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.ecl.EclModule;
import org.eclipse.epsilon.ecl.execute.EclOperationFactory;
import org.eclipse.epsilon.ecl.execute.context.IEclContext;
import org.eclipse.epsilon.emc.emf.InMemoryEmfModel;
import org.eclipse.epsilon.eol.dom.Parameter;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.rmf.reqif10.ReqIF10Package;
import org.obeonetwork.dsl.bpmn2.Bpmn2Package;

public class ExecuteECLMatchRulePiecewise {

  static String here = new File(".").getAbsolutePath();

  static URI resourceURI(String relativePath) {
    return URI.createFileURI(here + relativePath);
  }

  static List<EPackage> getAllPackages(EPackage p) {
    List<EPackage> allPackages = new ArrayList<>();
    allPackages.add(p);
    for (EPackage sub : p.getESubpackages()) {
      allPackages.addAll(getAllPackages(sub));
    }
    return allPackages;
  }

  static List<EObject> allInstances(Parameter param, Map<String,Resource> resources, IEclContext context) throws EolRuntimeException {
    // Find the corresponding EClass
    var typeName = param.getTypeExpression().getName().split("!");
    var model = resources.get(typeName[0]);
    var eclassPath = typeName[1];
    var metamodel = model.getContents().get(0).eClass().getEPackage();

    // package::class form
    if (eclassPath.contains("::")) {
      var p = eclassPath.split("::");
      eclassPath = p[1];
      // Find the corresponding package
      Optional<EPackage> pack = getAllPackages(metamodel).stream().filter(pk -> p[0].equals(pk.getName())).findFirst();
      if (pack.isPresent()) {
        metamodel = pack.get();
      }
    }
    var eclass = metamodel.getEClassifier(eclassPath);

    // Find all instances of that class
    var it = model.getAllContents();
    List<EObject> instances = new ArrayList<>();
    while (it.hasNext()) {
      var obj = it.next();
      if (eclass.isInstance(obj)) {
        instances.add(obj);
      }
    }

    return instances;
  }

  public static void main(String[] args) throws Exception {
    Map<String, Object> map = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap();
    map.put("xmi", new XMIResourceFactoryImpl());
    map.put("ecore", new EcoreResourceFactoryImpl());

    // Load metamodels
    {
      var rs = new ResourceSetImpl();
      var path = "/resources/metamodels/contentfwk.ecore";
      var resource = rs.getResource(resourceURI(path), true);
      var pack = (EPackage) resource.getContents().get(0);
      EPackage.Registry.INSTANCE.put(pack.getNsURI(), pack);
    }
    Bpmn2Package.eINSTANCE.eClass();
    ReqIF10Package.eINSTANCE.eClass();

    // Parse ECL
    var module = new EclModule();
    module.parse(new File(resourceURI("/resources/matching.ecl").toFileString()));
    if (module.getParseProblems().size() > 0) {
      System.err.println("Parse errors occured...");
      for (ParseProblem problem : module.getParseProblems()) {
        System.err.println(problem.toString());
      }
      throw new Exception("Error in parsing ECL file.  See stderr for details");
    }

    // Prepare ECL module with input models
    var context = module.getContext();
    context.setOperationFactory(new EclOperationFactory());

    String[] inModels[] = {
      {"/resources/models/travelAgencyEA.xmi", "cfw"},
      {"/resources/models/bookingTripProcess.xmi", "bpmn"},
      {"/resources/models/bookingTripRequirements.xmi", "reqif"},
    };

    Map<String,Resource> resources = new HashMap<>();

    for (var m: inModels) {
      var rs = new ResourceSetImpl();
      var path = m[0];
      var resource = rs.getResource(resourceURI(path), true);
      var model = new InMemoryEmfModel(resource);
      model.setName(m[1]);
      context.getModelRepository().addModel(model);
      resources.put(m[1], resource);
    }

    /*

    // Execute matching rules manually using Epsilon getAllInstances
    for (var rule: module.getMatchRules()) {
      System.out.println(rule.getName());

      var leftParam = (Parameter) rule.getChildren().get(1);
      var rightParam = (Parameter) rule.getChildren().get(2);

      for (var left: rule.getAllInstances(leftParam, context, false)) {
        for (var right: rule.getAllInstances(rightParam, context, false)) {
          var match = rule.match(left, right, context, false, null, false);
          System.out.println(match.isMatching());
        }
      }
    }

    */

    // Execute matching rules manually using allInstances
    for (var rule: module.getMatchRules()) {
      System.out.println(rule.getName());

      var leftParam = (Parameter) rule.getChildren().get(1);
      var rightParam = (Parameter) rule.getChildren().get(2);

      for (var left: allInstances(leftParam, resources, context)) {
        for (var right: allInstances(rightParam, resources, context)) {
          var match = rule.match(left, right, context, false, null, false);
          System.out.println(match.isMatching());
        }
      }
    }
  }
}
