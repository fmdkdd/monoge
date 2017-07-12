package emfviews.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Before;
import org.junit.Test;

import fr.inria.atlanmod.emfviews.virtuallinks.ConcreteElement;
import fr.inria.atlanmod.emfviews.virtuallinks.ContributingModel;
import fr.inria.atlanmod.emfviews.virtuallinks.VirtualLinksFactory;
import fr.inria.atlanmod.emfviews.virtuallinks.WeavingModel;

public class PackageComposition {
  EcoreFactory f = EcoreFactory.eINSTANCE;
  EPackage PA;
  EPackage PB;

  @Before
  public void setupPackages() {
    // A
    EClass A = f.createEClass();
    A.setName("A");

    EAttribute A_a = f.createEAttribute();
    A_a.setName("a");
    A_a.setEType(EcorePackage.Literals.EINT);
    A.getEStructuralFeatures().add(A_a);

    EAttribute A_b = f.createEAttribute();
    A_b.setName("b");
    A_b.setEType(EcorePackage.Literals.EINT);
    A.getEStructuralFeatures().add(A_b);

    PA = f.createEPackage();
    PA.setName("pa");
    PA.getEClassifiers().add(A);

    // B
    EClass B = f.createEClass();
    B.setName("B");

    EAttribute B_b = f.createEAttribute();
    B_b.setName("b");
    B_b.setEType(EcorePackage.Literals.EINT);
    B.getEStructuralFeatures().add(B_b);

    EAttribute B_c = f.createEAttribute();
    B_c.setName("c");
    B_c.setEType(EcorePackage.Literals.EINT);
    B.getEStructuralFeatures().add(B_c);

    PB = f.createEPackage();
    PB.setName("pb");
    PB.getEClassifiers().add(B);
  }

  @Test
  public void testMovingClassifiers() {
    // Adding the EClassifiers to another package does not work, because
    // they are contained by exactly one package.

    EPackage V = f.createEPackage();
    V.setName("V");
    V.getEClassifiers().addAll(PA.getEClassifiers());
    V.getEClassifiers().addAll(PB.getEClassifiers());

    // Features are in the view
    assertTrue(((EClass) V.getEClassifier("A")).getEStructuralFeature("a") != null);

    // But not in the original package anymore.
    assertTrue(PA.getEClassifier("A") == null);
  }

  @Test
  public void testCopyingPackages() {
    // By copying the packages, we should not affect the originals

    // EcoreUtil.copy uses a Copier and copies all references for us
    EPackage PA_ = EcoreUtil.copy(PA);
    EPackage PB_ = EcoreUtil.copy(PB);

    EPackage V = f.createEPackage();
    V.setName("V");

    V.getEClassifiers().addAll(PA_.getEClassifiers());
    V.getEClassifiers().addAll(PB_.getEClassifiers());

    // Features are in the view
    assertTrue(((EClass) V.getEClassifier("A")).getEStructuralFeature("a") != null);

    // And we haven't affected the originals
    assertTrue(PA.getEClassifier("A") != null);
  }

  @Test
  public void usingSuperPackage() {
    // Instead of stealing the classifiers from the copied packages, why not
    // move the packages themselves to another super package?

    EPackage PA_ = EcoreUtil.copy(PA);
    // But how to set the super package?
    // PA_.getESuperPackage().getESubpackages().add(PA);
  }

  @Test
  public void testEcoreRemovingOppositeReference() {
    // When we remove a part of a bidirectional reference in an Ecore metamodel,
    // is the opposite removed?

    Resource r = new ResourceSetImpl()
        .getResource(URI.createURI("resources/metamodels/minimalref.ecore", true), true);
    EPackage p = (EPackage) r.getContents().get(0);
    EClass A = (EClass) p.getEClassifiers().get(0);
    EClass B = (EClass) p.getEClassifiers().get(1);
    EReference manyB = (EReference) A.getEStructuralFeature("manyB");
    EReference parentA = (EReference) B.getEStructuralFeature("parentA");

    assertNotNull(manyB);
    assertEquals(parentA, manyB.getEOpposite());
    assertNotNull(parentA);
    assertEquals(manyB, parentA.getEOpposite());

    EcoreUtil.delete(manyB);

    assertNull(A.getEStructuralFeature("manyB"));
    assertNull(parentA.getEOpposite());

    // Yes, the opposite is removed if we use EcoreUtil.delete, but not if we
    // use Ecore.remove.
  }

  @Test
  public void testEcoreAddingDuplicateAttributes() {
    // When we add twice the same attribute to an EClass, or another one with
    // the same name, does Ecore react immediately or do we need to call some
    // validation?

    EClass c = EcoreFactory.eINSTANCE.createEClass();
    c.setName("C");
    EAttribute att1 = EcoreFactory.eINSTANCE.createEAttribute();
    att1.setName("a");
    att1.setEType(EcorePackage.Literals.ESTRING);

    c.getEStructuralFeatures().add(att1);
    c.getEStructuralFeatures().add(att1);

    EAttribute att2 = EcoreFactory.eINSTANCE.createEAttribute();
    att2.setName("a");
    att2.setEType(EcorePackage.Literals.ESTRING);
    c.getEStructuralFeatures().add(att2);

    // Looks like... it doesn't say anything at this point. Might be because of
    // the lack of builder pattern: you cannot signal to EMF when you are 'done'
    // creating the metamodel here, so it doesn't complain prematurely.

    // Calling the Diagnostician is like calling the Validation in the Ecore
    // editor?
    Diagnostic diag = Diagnostician.INSTANCE.validate(c);
    System.out.printf("%d -- %s\n", diag.getSeverity(), diag.getMessage());
    for (Diagnostic d : diag.getChildren()) {
      System.out.println(d.getMessage());
    }

    EValidator.Registry r = EValidator.Registry.INSTANCE;

    // Adding the same attribute twice has no effect: EMF silently does add the
    // duplicate element. However, adding another attribute with a different
    // name is caught by the diagnostician.
  }

  @Test
  public void directObjectLink() throws IOException {
    // Trying to include EObjects from an Ecore metamodel into a VirtualLinks instance

    {
      ResourceSet rs = new ResourceSetImpl();
      Resource r = rs.getResource(URI.createURI("resources/metamodels/minimalA.ecore", true), true);
      EPackage minA = (EPackage) r.getContents().get(0);
      EClass A = (EClass) minA.getEClassifiers().get(0);

      EPackage togaf = EPackage.Registry.INSTANCE
          .getEPackage("http://www.obeonetwork.org/dsl/togaf/contentfwk/9.0.0");
      EClassifier k = togaf.getEClassifiers().get(0);

      VirtualLinksFactory f = VirtualLinksFactory.eINSTANCE;
      WeavingModel wm = f.createWeavingModel();
      ContributingModel cm = f.createContributingModel();
      cm.setURI("foo");
      ConcreteElement ce = f.createConcreteElement();
      ce.setPath("foo");
      ce.setModel(cm);
      // ce.setObject(A);

      ConcreteElement ce2 = f.createConcreteElement();
      ce2.setPath("foo");
      ce2.setModel(cm);
      // ce2.setObject(k);

      wm.getContributingModels().add(cm);

      Resource wmr = rs.createResource(URI.createURI("resources/viewpoints/test/test.xmi"));
      wmr.getContents().add(wm);
      wmr.save(null);
    }

    {
      Resource r = new ResourceSetImpl()
          .getResource(URI.createURI("resources/viewpoints/test/test.xmi"), true);
      WeavingModel wm = (WeavingModel) r.getContents().get(0);
      // EObject o = wm.getContributingModels().get(0).getConcreteElements().get(0).getObject();
      // System.out.println(o);

      // EObject o2 = wm.getContributingModels().get(0).getConcreteElements().get(1).getObject();
      // System.out.println(o2);
    }

  }
}
