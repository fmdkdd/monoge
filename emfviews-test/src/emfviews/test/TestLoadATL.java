package emfviews.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.engine.parser.AtlParser;

public class TestLoadATL {

  static String here = new File(".").getAbsolutePath();

  public static void main(String[] args) throws IOException, ATLCoreException {
    try (InputStream in = new FileInputStream("resources/MEL2VirtualLinks.atl")) {

      EObject ast = AtlParser.getDefault().parse(in);

      System.out.println(ast);
    }
  }

}
