package com.packtub.e4.hello.util;

public class Utility {
  public static boolean breakpoint(boolean stop, String format, Object... objs) {
    System.out.println(String.format(format, objs));
    return stop;
  }
}
