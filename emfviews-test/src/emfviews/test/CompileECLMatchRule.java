package emfviews.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.epsilon.common.module.ModuleElement;
import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.common.util.ReflectionUtil;
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.ecl.EclModule;
import org.eclipse.epsilon.ecl.execute.EclOperationFactory;
import org.eclipse.epsilon.ecl.execute.context.IEclContext;
import org.eclipse.epsilon.emc.emf.InMemoryEmfModel;
import org.eclipse.epsilon.eol.compile.m3.Metamodel;
import org.eclipse.epsilon.eol.dom.Expression;
import org.eclipse.epsilon.eol.dom.Parameter;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.exceptions.models.EolEnumerationValueNotFoundException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelElementTypeNotFoundException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.exceptions.models.EolNotInstantiableModelElementTypeException;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.introspection.IPropertyGetter;
import org.eclipse.epsilon.eol.execute.introspection.IPropertySetter;
import org.eclipse.epsilon.eol.execute.introspection.java.ObjectMethod;
import org.eclipse.epsilon.eol.execute.operations.contributors.IOperationContributorProvider;
import org.eclipse.epsilon.eol.execute.operations.contributors.OperationContributor;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.models.IRelativePathResolver;
import org.eclipse.epsilon.eol.models.Model;
import org.eclipse.epsilon.eol.models.transactions.IModelTransactionSupport;
import org.eclipse.rmf.reqif10.ReqIF10Package;
import org.obeonetwork.dsl.bpmn2.Bpmn2Package;

public class CompileECLMatchRule {

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

  static interface Lambda {
    Object exec(Object o) throws EolRuntimeException;
  }

  static abstract class LambdaCollection implements Lambda, Collection<Object> {

    @Override
    public int size() {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Object> iterator() {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
      // @RIP: can't do anything here!
      throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(Object e) {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends Object> c) {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }
  }

  static class SockPropGetter implements IPropertyGetter {

    IEolContext context;
    IPropertyGetter delegate;

    @Override
    public boolean hasProperty(Object object, String property) {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public Object invoke(Object object, String property) throws EolRuntimeException {
      System.out.printf("invoke %s on %s\n", property, object);
      return new LambdaCollection() {
        @Override
        public Object exec(Object o) throws EolRuntimeException {
          return delegate.invoke(object, property);
        }
      };
    }

    @Override
    public ModuleElement getAst() {
      return null;
    }

    @Override
    public void setAst(ModuleElement ast) {
    }

    @Override
    public void setContext(IEolContext context) {
      this.context = context;
    }

    @Override
    public IEolContext getContext() {
      return context;
    }

  }

  static class SockObjectMethod extends ObjectMethod {

    String name;
    Lambda receiver;

    SockObjectMethod(Object receiver, String name) {
      this.receiver = (Lambda) receiver;
      this.name = name;
    }

    @Override
    public Object execute(Object[] parameters, ModuleElement ast) throws EolRuntimeException {
      return new LambdaCollection() {
        @Override
        public Object exec(Object obj) throws EolRuntimeException {
          Object o = receiver.exec(obj);
          try {
            return ReflectionUtil.invokeMethod(o, name, Arrays.asList(parameters));
          } catch (Exception ex) {
            throw new EolRuntimeException("Failed to execute reflected method " + name);
          }
        }
      };
    }

  }

  static class SockOpContributor extends OperationContributor {

    @Override
    public boolean contributesTo(Object target) {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public ObjectMethod
      findContributedMethodForUnevaluatedParameters(Object target, String name,
                                                    List<Expression> parameterExpressions,
                                                    IEolContext context) {
      return null;
    }

    @Override
    public ObjectMethod
      findContributedMethodForEvaluatedParameters(Object target, String name,
                                                  Object[] parameters,
                                                  IEolContext context) {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public ObjectMethod
      findContributedMethodForEvaluatedParameters(Object target, String name, Object[] parameters,
                                                  IEolContext context,
                                                  boolean overrideContextOperationContributorRegistry) {
      return new SockObjectMethod(target, name);
    }



  }

  static class SockModel implements IModel, IOperationContributorProvider {

    String name;

    @Override
    public void load(StringProperties properties) throws EolModelLoadingException {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public void load(StringProperties properties, String basePath) throws EolModelLoadingException {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public void load(StringProperties properties,
                     IRelativePathResolver relativePathResolver) throws EolModelLoadingException {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public void load() throws EolModelLoadingException {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public void setName(String name) {
      this.name = name;
    }

    @Override
    public List<String> getAliases() {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public Object getEnumerationValue(String enumeration,
                                      String label) throws EolEnumerationValueNotFoundException {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public Collection<?> allContents() {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public Collection<?> getAllOfType(String type) throws EolModelElementTypeNotFoundException {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public Collection<?> getAllOfKind(String type) throws EolModelElementTypeNotFoundException {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public Object getTypeOf(Object instance) {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public String getTypeNameOf(Object instance) {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public String getFullyQualifiedTypeNameOf(Object instance) {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public Object createInstance(String type) throws EolModelElementTypeNotFoundException,
                                              EolNotInstantiableModelElementTypeException {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public Object
      createInstance(String type,
                     Collection<Object> parameters) throws EolModelElementTypeNotFoundException,
                                                    EolNotInstantiableModelElementTypeException {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public Object getElementById(String id) {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public String getElementId(Object instance) {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public void setElementId(Object instance, String newId) {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public void deleteElement(Object instance) throws EolRuntimeException {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOfKind(Object instance,
                            String type) throws EolModelElementTypeNotFoundException {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOfType(Object instance,
                            String type) throws EolModelElementTypeNotFoundException {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean owns(Object instance) {
      return true;
    }

    @Override
    public boolean knowsAboutProperty(Object instance, String property) {
      return true;
    }

    @Override
    public boolean isPropertySet(Object instance, String property) throws EolRuntimeException {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean isInstantiable(String type) {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean isModelElement(Object instance) {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasType(String type) {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean store(String location) {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean store() {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public void dispose() {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public IPropertyGetter getPropertyGetter() {
      return new SockPropGetter();
    }

    @Override
    public IPropertySetter getPropertySetter() {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean isStoredOnDisposal() {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public void setStoredOnDisposal(boolean storedOnDisposal) {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean isReadOnLoad() {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public void setReadOnLoad(boolean readOnLoad) {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public IModelTransactionSupport getTransactionSupport() {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public Metamodel getMetamodel(StringProperties properties, IRelativePathResolver resolver) {
      // TODO: Auto-generated method stub
      throw new UnsupportedOperationException();
    }

    @Override
    public OperationContributor getOperationContributor() {
      return new SockOpContributor();
    }


  }

  public static void main(String[] args) throws Exception {
    /*
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
    */

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

    //Map<String,Resource> resources = new HashMap<>();

    for (var m: inModels) {
      /*
      var rs = new ResourceSetImpl();
      var path = m[0];
      var resource = rs.getResource(resourceURI(path), true);
      */
      var model = new SockModel();
      model.setName(m[1]);
      context.getModelRepository().addModel(model);
      //resources.put(m[1], resource);
    }

    context.getIntrospectionManager().getPropertyGetterFor("blah", "name", context);

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
      System.out.printf("Executing rule %s\n", rule.getName());

      //var leftParam = (Parameter) rule.getChildren().get(1);
      //var rightParam = (Parameter) rule.getChildren().get(2);

      var left = "left";
      var right = "right";

      var match = rule.match(left, right, context, false, null, false);
      System.out.printf("rule is matching: %s\n", match.isMatching());

    }
  }
}
