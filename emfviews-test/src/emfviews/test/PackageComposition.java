package emfviews.test;

import static org.junit.Assert.assertTrue;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Before;
import org.junit.Test;

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
}
