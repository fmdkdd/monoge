package com.packtpub.e4.minimark.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class MinimarkTranslator {
  public static void convert(Reader reader, Writer writer) throws IOException {
    BufferedReader lines = new BufferedReader(reader);
    String line;
    String title = String.valueOf(lines.readLine());
    if (title == null) {
      title = "";
    }
    writer.write("<html><head><title>");
    writer.write(title);
    writer.write("</title></head><body><h1>");
    writer.write(title);
    writer.write("</h1><p>");
    while ((line = lines.readLine()) != null) {
      if (line.equals("")) {
        writer.write("</p><p>");
      } else {
        writer.write(line);
        writer.write('\n');
      }
    }
    writer.write("</p></body></html>");
    writer.flush();
  }
}
