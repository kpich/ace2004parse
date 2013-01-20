package edu.utexas.cs.ml.ace2004parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

/**
 * Main Class, provides interface to get at Dependency Parse of ACE 2004
 * sentence by span extent.
 *
 * @TODO investigate performance hit of using XPath as terribly as done here.
 */
public class ACE2004Parse {

  /**
   * The directory containing the DOCID.parse.xml.gz and DOCID.offset files.
   */
  public static final String PARSE_DIR =
      "/u/pichotta/deft/coref/corpus/ace-parse/ace_parsed";

  private int offset = -1;

  /**
   * as long as the number of characters in the document; for each
   * character, gives its word number.
   */
  private TokenLocation[] tokenLocations;

  /**
   * this is a map from TokenLocations (representing instances of tokens) to
   * the dependencies that somehow involve them.
   */
  private Map<TokenLocation, List<Dependency>> locationToDeps;

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
      populateTokenLocations(doc);
      populateLocationToDepMap(doc);
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

  /**
   * This method returns dependencies (according to the collapsed dependency
   * parse output by stanford corenlp) that mention a word in the span
   * between lhsChar and rhsChar (inclusive).
   */
  public List<Dependency> getDependenciesInSpan(int lhsChar, int rhsChar) {
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

  private void populateTokenLocations(Document doc) {
    try {
      NodeList sentNodes = getSentenceNodes(doc);
      this.tokenLocations = new TokenLocation[getNumChars(sentNodes)];
      for (int i = 0; i < sentNodes.getLength(); i++){
        NodeList wordNodes = getTokenNodesForSent(sentNodes.item(i));
        for (int j = 0; j < wordNodes.getLength(); j++) {
          int lb = getCharacterOffsetBegin(wordNodes.item(j));
          int ub = getCharacterOffsetEnd(wordNodes.item(j));
          // add 1 because the CoreNLP offsets are 1-indexed
          TokenLocation tokLocation = new TokenLocation(i + 1, j + 1);
          for (int k = lb; k < ub; k++) {
            this.tokenLocations[k] = tokLocation;
          }
        }
      }
    } catch (XPathExpressionException e) {
      e.printStackTrace();
    }
  }

  private void populateLocationToDepMap(Document doc) {
    this.locationToDeps = new HashMap<TokenLocation, List<Dependency>>();
    try {
      NodeList sentNodes = getSentenceNodes(doc);
      for (int i = 0; i < sentNodes.getLength(); i++) {
        NodeList depNodes = getCollapsedDependencyNodes(sentNodes.item(i));
        for (int j = 0; j < depNodes.getLength(); j++) {
          Dependency dep = depNodeToDependency(depNodes.item(j));
        }
      }
    } catch (XPathExpressionException e) {
      e.printStackTrace();
    }
  }

  private Dependency depNodeToDependency(Node depNode) {
    Dependency dep = null;
    try {
      String type = getDepType(depNode);
      int governorIndex = getGovernorIndex(depNode);
      int dependentIndex = getDependentIndex(depNode);
      System.out.println(governorIndex);
      System.out.println(dependentIndex);
    } catch (XPathExpressionException e) {
      e.printStackTrace();
    }
    assert dep != null;
    return dep;
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

  private String getDepType(Node depNode) {
    return depNode.getAttributes().getNamedItem("type").getNodeValue();
  }

  private int getGovernorIndex(Node depNode)
          throws XPathExpressionException {
    Node gov = (Node) XPathFactory.newInstance().newXPath()
                                  .compile("governor")
                                  .evaluate(depNode, XPathConstants.NODE);
    return Integer.parseInt(gov.getAttributes().getNamedItem("idx")
                                               .getNodeValue());
  }

  private int getDependentIndex(Node depNode)
          throws XPathExpressionException {
    Node gov = (Node) XPathFactory.newInstance().newXPath()
                                  .compile("dependent")
                                  .evaluate(depNode, XPathConstants.NODE);
    return Integer.parseInt(gov.getAttributes().getNamedItem("idx")
                                               .getNodeValue());
  }

  private NodeList getSentenceNodes(Document doc)
          throws XPathExpressionException {
    return (NodeList) XPathFactory.newInstance().newXPath()
                                  .compile("//sentence")
                                  .evaluate(doc, XPathConstants.NODESET);
  }

  private NodeList getCollapsedDependencyNodes(Node sent)
          throws XPathExpressionException {
    return (NodeList) XPathFactory.newInstance().newXPath()
                                  .compile("collapsed-dependencies/dep")
                                  .evaluate(sent, XPathConstants.NODESET);
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
