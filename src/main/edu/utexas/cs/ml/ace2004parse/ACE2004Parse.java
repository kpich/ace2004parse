package edu.utexas.cs.ml.ace2004parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
   * In some sense the reverse of tokenLocations: given a (sentencneum, wordnum)
   * pair, this gives us the information about the token contained in the parse
   */
  private Map<TokenLocation, TokenInfo> tokenLocationToInfoMap;

  /**
   * this is a map from TokenLocations (representing instances of tokens) to
   * the dependencies that somehow involve them.
   */
  private Map<TokenLocation, List<Dependency>> locationToDeps;

  /**
   * Takes a docID, finds the file according to PARSE_DIR.
   */
  public ACE2004Parse(String docID) {
    this(getXMLStream(docID), getOffsetStream(docID));
  }

  /**
   * Takes an InputStream pointing to the XML and the offset file.
   */
  public ACE2004Parse(InputStream xmlStream, InputStream offsetStream) {
    readOffset(offsetStream);
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.parse(xmlStream);
      readTokens(doc);
      populateLocationToDepMap(doc);
      //DBG_printLocToDepMap();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    }
  }

  /**
   * returns null if there's no Token there, otherwise returns the
   * token at a given (sentence, tokennum) pair, with sentence and tokennum
   * numbered as output in the Stanford CoreNLP parse.
   */
  public TokenInfo getTokenAtLocation(TokenLocation loc) {
    return this.tokenLocationToInfoMap.get(loc);
  }

  /**
   * This method returns any events with relations to any NPs inside the
   * span between lhsChar and rhsChar (inclusive), according to the Stanford
   * CoreNLP Dependency Parse.
   *
   * lhsChar and rhsChar should be offset (that is, should be indexed like
   * the annotated ACE 2004 corpus).
   */
  public List<Event> getEventsInSpan(int lhsChar, int rhsChar) {
    return null;
  }

  /**
   * This method returns dependencies (according to the collapsed dependency
   * parse output by stanford corenlp) that mention a word in the span
   * between lhsChar and rhsChar (inclusive).
   *
   * lhsChar and rhsChar should be offset (that is, should be indexed like
   * the annotated ACE 2004 corpus).
   */
  public List<Dependency> getDependenciesInSpan(int lhsChar, int rhsChar) {
    List<Dependency> res = new ArrayList<Dependency>();
    int unOffsetLhsChar = Math.max(lhsChar - this.offset, 0);
    int unOffsetRhsChar = Math.min(rhsChar - this.offset,
                                   this.tokenLocations.length - 1);
    for (int i = unOffsetLhsChar; i <= unOffsetRhsChar; i++) {
      TokenLocation loc = this.tokenLocations[i];
      if (this.locationToDeps.containsKey(loc)) {
        res.addAll(this.locationToDeps.get(loc));
      }
    }
    return res;
  }

  private static InputStream getXMLStream(String docID) {
    String parseFilename = docID + ".parse.xml.gz";
    InputStream is = null;
    try {
      is = new GZIPInputStream(new FileInputStream(
          new File(PARSE_DIR, parseFilename)));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return is;
  }

  private static InputStream getOffsetStream(String docID) {
    String offsetFilename = docID + ".offset";
    InputStream is = null;
    try {
      is = new FileInputStream(new File(PARSE_DIR, offsetFilename));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return is;
  }

  private void readOffset(InputStream offsetStream) {
    try {
      BufferedReader b = new BufferedReader(new InputStreamReader(offsetStream,
                                                                  "UTF-8"));
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

  /**
   * This populates both this.TokenLocations and this.tokenLocationToInfoMap.
   */
  private void readTokens(Document doc) {
    try {
      NodeList sentNodes = getSentenceNodes(doc);
      this.tokenLocations = new TokenLocation[getNumChars(sentNodes)];
      this.tokenLocationToInfoMap = new HashMap<TokenLocation, TokenInfo>();
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
          this.tokenLocationToInfoMap.put(tokLocation,
                                          getTokenInfo(wordNodes.item(j)));
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
          Dependency dep = depNodeToDependency(depNodes.item(j), i + 1);
          appendDepToMap(dep.getGovernor(), dep);
          appendDepToMap(dep.getDependent(), dep);
        }
      }
    } catch (XPathExpressionException e) {
      e.printStackTrace();
    }
  }

  private void appendDepToMap(TokenLocation loc, Dependency dep) {
    if (!this.locationToDeps.containsKey(loc)) {
      this.locationToDeps.put(loc, new ArrayList<Dependency>());
    }
    List<Dependency> deps = this.locationToDeps.get(loc);
    deps.add(dep);
  }

  private Dependency depNodeToDependency(Node depNode, int sentID) {
    Dependency dep = null;
    try {
      String type = getDepType(depNode);
      int governorIndex = getGovernorIndex(depNode);
      int dependentIndex = getDependentIndex(depNode);
      dep = new Dependency(type,
                           new TokenLocation(sentID, governorIndex),
                           new TokenLocation(sentID, dependentIndex));
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

  private TokenInfo getTokenInfo(Node token)
          throws XPathExpressionException {
    int id = Integer.parseInt(token.getAttributes().getNamedItem("id")
                              .getNodeValue());
    return new TokenInfo(id, getStringTextProperty(token, "word"),
                         getStringTextProperty(token, "lemma"),
                         getCharacterOffsetBegin(token),
                         getCharacterOffsetEnd(token),
                         getStringTextProperty(token, "POS"));
  }

  /**
   * Takes a node that has <prop>[number]</prop> as a child, returns
   * parsed num.
   */
  private int getIntegerTextProperty(Node n, String prop)
          throws XPathExpressionException {
    return Integer.parseInt(getStringTextProperty(n, prop));
  }

  /**
   * Takes a node that has <prop>[string]</prop> as a child, returns
   * parsed num.
   */
  private String getStringTextProperty(Node n, String prop)
          throws XPathExpressionException {
    Node strNode = (Node) XPathFactory.newInstance().newXPath()
                                      .compile(prop)
                                      .evaluate(n, XPathConstants.NODE);
    return strNode.getFirstChild().getNodeValue();
  }

  private NodeList getTokenNodesForSent(Node sent)
          throws XPathExpressionException {
    return (NodeList) XPathFactory.newInstance().newXPath()
                      .compile("tokens/token")
                      .evaluate(sent, XPathConstants.NODESET);
  }

  private void DBG_printLocToDepMap() {
    Iterator it = this.locationToDeps.entrySet().iterator();
    while (it.hasNext()) {
        Map.Entry pairs = (Map.Entry)it.next();
        System.out.println(pairs.getKey() + " = " + pairs.getValue());
    }
  }


  public static void main(String[] args) {
    ACE2004Parse parse = new ACE2004Parse("NYT20001127.2214.0372");
  }
}
