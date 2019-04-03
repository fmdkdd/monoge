package another;

import org.eclipse.tracecompass.ctf.core.CTFException;
import org.eclipse.tracecompass.ctf.core.event.IEventDeclaration;
import org.eclipse.tracecompass.ctf.core.event.IEventDefinition;
import org.eclipse.tracecompass.ctf.core.trace.CTFResponse;
import org.eclipse.tracecompass.ctf.core.trace.CTFStreamInput;
import org.eclipse.tracecompass.ctf.core.trace.CTFStreamInputReader;
import org.eclipse.tracecompass.ctf.core.trace.CTFTrace;
import org.eclipse.tracecompass.ctf.core.trace.ICTFStream;

public class TestCTF {
  public static void main(String[] args) throws CTFException {
    CTFTrace trace = new CTFTrace("/home/fmdkdd/megamart/ctf/tmpnl2pnllx");
    System.out.println("dumb");
    for (ICTFStream s : trace.getStreams()) {
      for (CTFStreamInput e : s.getStreamInputs()) {
        CTFStreamInputReader r = new CTFStreamInputReader(e);
        while (r.readNextEvent() != CTFResponse.ERROR) {
          IEventDefinition d = r.getCurrentEvent();
          if (d != null) {
            System.out.printf("%s: %s\n", d.getTimestamp(), d.getFields());
          }
        }
      }
    }
  }
}
