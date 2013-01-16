package edu.utexas.cs.ml.ace2004parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class ACE2004Parse {

  /**
   * The directory containing the DOCID.parse.xml.gz and DOCID.offset files.
   */
  public static final String PARSE_DIR =
      "/u/pichotta/deft/coref/corpus/ace-parse/ace_parsed";

  private int offset = -1;

  /**
   * Ctor allowing you to load a parse for a specific document
   */
  public ACE2004Parse(String docID) {
    String parseFilename = docID + ".parse.xml.gz";
    String offsetFilename = docID + ".offset";
    readOffset(new File(PARSE_DIR, offsetFilename));
    System.out.println(this.offset);
    try {
      InputStream in = new GZIPInputStream(new FileInputStream(
          new File(PARSE_DIR, parseFilename)));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method returns any events with relations to any NPs inside the
   * span between lhsChar and rhsChar (inclusive), according to the Stanford
   * CoreNLP Dependency Parse.
   */
  public List<Event> getEventsInSpan(int lhsChar, int rhsChar) {
    return null;
  }

  private void readOffset(File offsetFile) {
    try {
      BufferedReader b = new BufferedReader(new FileReader(offsetFile));
      String line;
      while ((line = b.readLine()) != null) {
        if (line.trim().length() > 0) {
          try {
            this.offset = Integer.parseInt(line.trim());
          } catch (NumberFormatException e) {
            e.printStackTrace();
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    assert this.offset != -1;
  }


  public static void main(String[] args) {
    ACE2004Parse parse = new ACE2004Parse("NYT20001127.2214.0372");
  }
}
