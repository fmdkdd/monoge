package another;

import java.util.HashMap;
import java.util.Map;

// Problem: how to define a map in Java that has the following invariant:
//
// Given classes A, B, C, where B <: A (B subtypes A), C <: A, the map can
// contain any subtype of A as key, and any subtype A as value, (Map<A,A>) with
// the following restriction: if a key has type K, then its value has type K as
// well.
//
// The restriction should be enforced at compile-time.
//
// E.g., map.put(b, b) should compile, but map.put(b, c) should not.  However,
// map.put(c, c) should also compile for the same map, since the map should
// accept any subtype of A.

public class MyMap {
  static class A {}
  static class B extends A {}
  static class C extends A {}

  B b = new B();
  C c = new C();

  // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Attempt 1: FAIL

  Map<E extends A, E> map1 = new HashMap<>(); // <- does not compile, syntax error

  // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Attempt 1b: FAIL
  //
  // Using wildcards in bounds

  // Actually,
  private Map<? extends A, ? extends A> map1b = new HashMap<>();

  void testAttempt1b() {
    map1b.put(b, b); // FAIL: does not compile

    // Actually, it seems we can't put anything into this map.
    // Would be interested to know why.

    map1b.put((A)b, (A)b); // FAIL: does not compile

    // Only valid arguments seems to be `null`:
    map1b.put(null, null);

    // Less useful map ever?
  }


  // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Attemp 2: FAIL
  // We cannot bound an attribute, but we can bound the type on a class

  static class MyMap2<E extends A> {
    Map<E, E> map = new HashMap<>();

    void put(E key, E value) {
      map.put(key, value);
    }
  }

  void testAttempt2() {
    MyMap2<A> map = new MyMap2<>();

    map.put(b, b); // compiles
    map.put(b, c); // FAIL: compiles, but should not

    // both b and c are subtypes of A, so can be put into a MyMap2<A>
  }

  // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Attempt 3: FAIL
  // Bound the put method instead of the attribute

  static class MyMap3<E> {
    Map<E, E> map = new HashMap<>();

    <K extends E> void put(K key, K value) {
      map.put(key, value);
    }
  }

  void testAttempt3() {
    MyMap3<A> map = new MyMap3<>();

    map.put(b, b); // compiles
    map.put(b, c); // FAIL: compiles, but should not

    // b and c are subtypes of A, so K resolves to A and put(A, A) compiles
  }

  // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Attempt 4: FAIL
  // Bound the put method instead of the attribute

  static class MyMap4<E> {
    Map<E, E> map = new HashMap<>();

    // Try to force the resolution of the `K extends E` bound to the first
    // parameter, and use that value for the second parameter.
    <K extends E, V extends K> void put(K key, V value) {
      map.put(key, value);
    }
  }

  void testAttempt4() {
    MyMap4<A> map = new MyMap4<>();

    map.put(b, b); // compiles
    map.put(b, c); // FAIL: compiles, but should not

    // C does not subtype B, so the only to unify the bounds is
    // to have K: A and, then V can be A or C
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Attempt 5: FAIL
  //
  // Trying to pass the map as argument to `put`

  <U, E extends U> void put(E key, E value, Map<U, U> map) {
    map.put(key, value);
  }

  void testAttempt5() {
    Map<A, A> map = new HashMap<>();

    map.put(b, b); // compiles
    map.put(b, c); // FAIL: compiles, but should not

    // Similar to attempt 3, without the MyMap wrapper
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Attempt 6: SUCCESS, but could be better
  //
  // It seems the bounds will always escape to the supertype A if we give
  // the key and value together.  What if we split the `put` method in two
  // steps?

  static class MyMap6<E> {
    Map<E, E> map = new HashMap<>();

    static interface PleaseInsertValue<V> {
      void putValue(V value);
    }

    <K extends E> PleaseInsertValue<K> put(K key) {
      // Return a class that will ultimately put the value in the map,
      // provided the value is the same type as the key.
      // Sort of a closure, suspension, or lambda.
      return new PleaseInsertValue<K>() {
        @Override
        public void putValue(K value) {
          map.put(key, value);
        }
      };
    }
  }

  void testAttempt6() {
    MyMap6<A> map = new MyMap6<>();

    map.put(b).putValue(b); // compiles
    map.put(b).putValue(c); // SUCCESS: does not compile

    // Two downsides: 1) more awkward to use 2) may create an unncessary closure
    // at runtime (but maybe the compiler optimizes it away)
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Attempt 7: SUCCESS, still awkward
  //
  // Less verbose definition with lambdas, still awkward to use

  static class MyMap7<E> {
    Map<E, E> map = new HashMap<>();

    <K extends E> java.util.function.Consumer<K> put(K key) {
      return value -> map.put(key, value);
    }
  }

  void testAttempt7() {
    MyMap7<A> map = new MyMap7<>();

    map.put(b).accept(b); // compiles
    map.put(b).accept(c); // SUCCESS: does not compile
  }

  // Still unsolved: is there a way to define the `put` function to take the key and value
  // at the same time while enforcing the invariant that they both have the same type?

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Attempt 8: FAIL
  //

  static class MyMap8<E> {
    Map<E, E> map = new HashMap<>();

    <K extends E> java.util.function.Consumer<K> put(K key) {
      return value -> map.put(key, value);
    }

    <K extends E, V extends K> void put(K key, V value) {
      put(key).accept(value);
    }
  }

  void testAttempt8() {
    MyMap8<A> map = new MyMap8<>();

    map.put(b, b); // compiles
    map.put(b, c); // FAIL: compiles, but should not

    // All bounds are satisfied, because we come back to the initial problem of
    // pinning the type of the first parameter before unifying V.

    // I want macros.
  }
}
