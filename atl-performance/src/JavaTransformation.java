import fr.inria.atlanmod.emfviews.mel.MelFactory;
import fr.inria.atlanmod.emfviews.mel.MelPackage;
import fr.inria.atlanmod.emfviews.virtuallinks.VirtualLinksFactory;
import fr.inria.atlanmod.emfviews.virtuallinks.VirtualLinksPackage;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.m2m.atl.emftvm.EmftvmFactory;
import org.eclipse.m2m.atl.emftvm.ExecEnv;
import org.eclipse.m2m.atl.emftvm.Metamodel;
import org.eclipse.m2m.atl.emftvm.Model;
import org.eclipse.m2m.atl.emftvm.impl.resource.EMFTVMResourceFactoryImpl;
import org.eclipse.m2m.atl.emftvm.util.DefaultModuleResolver;
import org.eclipse.m2m.atl.emftvm.util.ModuleResolver;
import org.eclipse.m2m.atl.emftvm.util.TimingData;

public class JavaTransformation {
  public static void main(String args[]) {
    Map<String, Object> extensionFactory = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap();
    extensionFactory.put("xmi", new XMIResourceFactoryImpl());
    extensionFactory.put("emftvm", new EMFTVMResourceFactoryImpl());

    EPackage.Registry.INSTANCE.put(MelPackage.eNS_URI, MelPackage.eINSTANCE);
    EPackage.Registry.INSTANCE.put(VirtualLinksPackage.eNS_URI, VirtualLinksPackage.eINSTANCE);

    // Create and populate input model
    Resource input = new ResourceSetImpl().createResource(URI.createURI("models/mel.xmi"));

    MelFactory factory = MelFactory.eINSTANCE;
    int nbInputElements = 10000000;
    for (int i=0; i < nbInputElements; ++i) {
      input.getContents().add(factory.createMetamodel());
    }

    // Create output model
    Resource output = new ResourceSetImpl().createResource(URI.createURI("models/mel-out.xmi"));

    // Run transformation
    runEmftvmTransformation(input, output);

/*
    TimingData timing = new TimingData();
    runJavaTransformation(input, output);
    timing.finish();
    System.out.format("finish: %.2fms\n", timing.getFinished() / 1000000f);
*/
    // Ensure the output is correct
    //System.out.format("%d/%d\n", countElements(output), countElements(input));
  }

  static int countElements(Resource r) {
    int c = 0;
    Iterator<EObject> it = r.getAllContents();
    while (it.hasNext()) {
      it.next();
      c++;
    }
    return c;
  }

  static void runJavaTransformation(Resource input, Resource output) {
    VirtualLinksFactory factory = VirtualLinksFactory.eINSTANCE;

    for (EObject o : input.getContents()) {
      //((fr.inria.atlanmod.emfviews.mel.Metamodel) o)
      output.getContents().add(factory.createContributingModel());
    }
  }

  static void runEmftvmTransformation(Resource input, Resource output) {
    EmftvmFactory factory = EmftvmFactory.eINSTANCE;
    ExecEnv env = factory.createExecEnv();

    ResourceSet rs = new ResourceSetImpl();
    // Load metamodels
    Metamodel sourceMM = factory.createMetamodel();
    sourceMM.setResource(rs.getResource(URI.createURI("http://www.inria.fr/atlanmod/emfviews/mel"), true));
    env.registerMetaModel("MEL", sourceMM);

    Metamodel targetMM = factory.createMetamodel();
    targetMM.setResource(rs.getResource(URI.createURI("http://inria.fr/virtualLinks"), true));
    env.registerMetaModel("VirtualLinks", targetMM);

    // Load models
    Model sourceModel = factory.createModel();
    sourceModel.setResource(input);
    env.registerInputModel("IN", sourceModel);

    Model targetModel = factory.createModel();
    // The URI does not actually matter here, as we save the resource to a String
    targetModel.setResource(output);
    env.registerOutputModel("OUT", targetModel);

    // Run the transformation
    ModuleResolver mr = new DefaultModuleResolver("transformations/", new ResourceSetImpl());

    TimingData timing = new TimingData();
    env.loadModule(mr, "Minimal");
    timing.finishLoading();
    env.run(timing);
    timing.finish();

    // Report time
    System.out.format("loading: %.2fms, finish: %.2fms\n",
                      timing.getFinishedLoading() / 1000000f,
                      timing.getFinished() / 1000000f);
  }
}
