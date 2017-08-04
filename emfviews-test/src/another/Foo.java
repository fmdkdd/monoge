package another;

import emfviews.test.TestVisibility;

public class Foo extends TestVisibility {

  static class MyA extends A {}

  public void foo() {
    A a = new MyA();
  }

}
