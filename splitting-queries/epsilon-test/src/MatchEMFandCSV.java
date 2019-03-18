import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.ecl.EclModule;
import org.eclipse.epsilon.ecl.execute.EclOperationFactory;
import org.eclipse.epsilon.ecl.trace.MatchTrace;
import org.eclipse.epsilon.emc.csv.CsvModel;
import org.eclipse.epsilon.emc.emf.InMemoryEmfModel;

public class MatchEMFandCSV {
  static String readFile(String path, Charset encoding) throws IOException {
    byte[] encoded = Files.readAllBytes(Paths.get(path));
    return new String(encoded, encoding);
  }

  static String here = new File(".").getAbsolutePath();

  static URI resourceURI(String relativePath) {
    return URI.createFileURI(here + relativePath);
  }

  public static void main(String[] args) throws Exception {
    // Init EMF
    {
      Map<String, Object> map = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap();
      map.put("ecore", new EcoreResourceFactoryImpl());
    }

    var module = new EclModule();
    module.parse(readFile("resources/matching.ecl", StandardCharsets.UTF_8));
    if (module.getParseProblems().size() > 0) {
      System.err.println("Parse errors occured...");
      for (ParseProblem problem : module.getParseProblems()) {
        System.err.println(problem.toString());
      }
      throw new Exception("Error in parsing ECL file.  See stderr for details");
    }
    EclOperationFactory operationFactory = new EclOperationFactory();
    module.getContext().setOperationFactory(operationFactory);

    // Add EMF model
    Resource r = (new ResourceSetImpl()).getResource(resourceURI("/resources/VirtualLinks.ecore"), true);

    var emfModel = new InMemoryEmfModel("EMF", r);
    module.getContext().getModelRepository().addModel(emfModel);

    // Add CSV
    var csvModel = new CsvModel();
    var csvGetter = csvModel.getPropertyGetter();
    csvModel.setName("CSV");
    try (var br = new BufferedReader(new FileReader(new File("resources/names.csv")))) {
      csvModel.setReader(br);
      csvModel.setKnownHeaders(true);
      csvModel.load();
      module.getContext().getModelRepository().addModel(csvModel);

      // Execute the module
      var mt = (MatchTrace) module.execute();

      for (var match : mt.getMatches()) {
        System.out.printf("%s %s\n", match.getRight(), csvGetter.invoke(match.getRight(), "name"), csvModel.getTypeOf(match.getRight()));
      }
    }
  }
}
