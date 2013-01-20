package edu.utexas.cs.ml.ace2004parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ACE2004Parse {

  /**
   * The directory containing the DOCID.parse.xml.gz and DOCID.offset files.
   */
  public static final String PARSE_DIR =
      "/u/pichotta/deft/coref/corpus/ace-parse/ace_parsed";

  private int offset = -1;

  /**
   * Array as long as the number of characters in the document; for each
   * character, gives its word number.
   */
  private int[] wordArray;








  /**
   * Ctor allowing you to load a parse for a specific document
   */
  public ACE2004Parse(String docID) {
    String parseFilename = docID + ".parse.xml.gz";
    String offsetFilename = docID + ".offset";
    readOffset(new File(PARSE_DIR, offsetFilename));
    try {
      InputStream in = new GZIPInputStream(new FileInputStream(
          new File(PARSE_DIR, parseFilename)));

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.parse(in);
      populateWordArray(doc);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (SAXException e) {
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

  private void populateWordArray(Document doc) {
    // TODO investigate performance hit of creating XPath objects over and over
    try {
      XPathFactory xFactory = XPathFactory.newInstance();
      XPath xpath = xFactory.newXPath();
      XPathExpression expr = xpath.compile("//sentence");
      NodeList sentNodes =
          (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
      this.wordArray = new int[getNumChars(sentNodes)];
      System.out.println(this.wordArray.length);
      for (int i=0; i < sentNodes.getLength(); i++){
        NodeList wordNodes = getTokenNodesForSent(sentNodes.item(i));
      }
    } catch (XPathExpressionException e) {
      e.printStackTrace();
    }
  }

  private int getNumChars(NodeList sentNodes) {
    assert sentNodes.getLength() > 0;
    int length = -1;
    Node lastSent = sentNodes.item(sentNodes.getLength() - 1);
    try {
      NodeList tokenNodes = getTokenNodesForSent(lastSent);
      Node lastToken = tokenNodes.item(tokenNodes.getLength() - 1);
      // each token is of the form
      // <token id="N">
      //   <word>to</word>
      //   ...
      //   <CharacterOffsetBegin>12</CharacterOffsetBegin>
      //   <CharacterOffsetEnd>14</CharacterOffsetEnd>
      // </token>
      length = getCharacterOffsetEnd(lastToken);
    } catch (XPathExpressionException e) {
      e.printStackTrace();
    }
    assert length != -1;
    return length;
  }


  private int getCharacterOffsetBegin(Node token)
          throws XPathExpressionException {
    return getIntegerTextProperty(token, "CharacterOffsetBegin");
  }

  private int getCharacterOffsetEnd(Node token)
          throws XPathExpressionException {
    return getIntegerTextProperty(token, "CharacterOffsetEnd");
  }

  /**
   * Takes a node that has <prop>[number]</prop> as a child, returns
   * parsed num.
   */
  private int getIntegerTextProperty(Node n, String prop)
          throws XPathExpressionException {
    Node intNode = (Node) XPathFactory.newInstance().newXPath()
                                      .compile(prop)
                                      .evaluate(n, XPathConstants.NODE);
    return Integer.parseInt(intNode.getFirstChild().getNodeValue());
  }

  private NodeList getTokenNodesForSent(Node sent)
          throws XPathExpressionException {
    return (NodeList) XPathFactory.newInstance().newXPath()
                      .compile("tokens/token")
                      .evaluate(sent, XPathConstants.NODESET);
  }


  public static void main(String[] args) {
    ACE2004Parse parse = new ACE2004Parse("NYT20001127.2214.0372");
  }
}
