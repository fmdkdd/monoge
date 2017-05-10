package testcontainment.test;

import testcontainment.A;
import testcontainment.B;
import testcontainment.TestContainmentFactory;

import static org.junit.Assert.*;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.junit.Test;

public class TestContainment {

	@Test
	public void testContainment() {
		TestContainmentFactory factory = TestContainmentFactory.eINSTANCE;
		A a1 = factory.createA();
		A a2 = factory.createA();
		B b = factory.createB();
		
		a1.getContainedBs().add(b);
		assertEquals(b.eContainer(), a1);
		
		a2.getContainedBs().add(b);
		assertTrue(a1.getContainedBs().size() == 0);
		assertEquals(b.eContainer(), a2);
		
		a1.getBs().add(b);
		assertEquals(b.eContainer(), a2);
		assertEquals(b.getA(), a1);
		assertTrue(a1.getBs().size() == 1);
		assertTrue(a2.getBs().size() == 0);
		
		a2.getBs().add(b);
		assertEquals(b.eContainer(), a2);
		assertEquals(b.getA(), a2);		
		assertTrue(a1.getBs().size() == 0);
		assertTrue(a2.getBs().size() == 1);
	}
	
	public static void main(String[] args) throws IOException {
		TestContainmentFactory factory = TestContainmentFactory.eINSTANCE;
		
		A a = factory.createA();
		B b1 = factory.createB();
		B b2 = factory.createB();
		
		a.getContainedBs().add(b1);
		a.getBs().add(b2);
		
		ResourceSet rset = new ResourceSetImpl();
		Resource.Factory rfact = new XMIResourceFactoryImpl();
		rset.getResourceFactoryRegistry().getExtensionToFactoryMap().put("a", rfact);
		rset.getResourceFactoryRegistry().getExtensionToFactoryMap().put("b", rfact);
		URI uri = URI.createURI("file://tmp/out.a");
		Resource r = rset.createResource(uri);
		r.getContents().add(a);
		
		Resource rb = rset.createResource(URI.createURI("file://tmp/out.b"));
		rb.getContents().add(b2);
		
		r.save(null);
		rb.save(null);
	}
}
