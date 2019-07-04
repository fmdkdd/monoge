package another;

public class TestInstanceOf {
  interface I {}
  static class A implements I {}
  static class B {}

  public static void main(String[] args) {
    I a = new A();
    if (a instanceof B) {
      System.out.println("impossible!");
    }
  }
}
