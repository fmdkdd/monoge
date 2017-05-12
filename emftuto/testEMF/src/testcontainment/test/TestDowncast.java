package testcontainment.test;

import org.junit.Test;

public class TestDowncast {
	@Test
	public void testDowncast() {
		{
			A a = new AImpl();		
			B b = (B) a;
		}
		
		{
			AImpl a = new AImpl();
			BImpl b = (BImpl) a;
		}
		
	}
}

interface A {
	void foo();
}

interface B extends A {
	void bar();
}

class AImpl implements A {
	public void foo() {}
}

class BImpl extends AImpl implements B {
	public void foo() {}
	public void bar() {}
}