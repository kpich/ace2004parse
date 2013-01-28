package edu.utexas.cs.ml.ace2004parse;

import java.io.*;
import java.util.*;

import org.junit.*;
import static org.junit.Assert.*;

public class ACE2004ParseTest {

// The input SGM was:
//    <DOC>
//    <TEXT>
//    Dogs are fun.
//    <XMLTAG>
//    Cats are dumb.
//    </TEXT>
//    </DOC>


private static final int OFFSET = 13;
private static final String OFFSET_STRING = "13";

private static final String XML_PARSE_STRING =
  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
  "<?xml-stylesheet href=\"CoreNLP-to-HTML.xsl\" type=\"text/xsl\"?>\n" +
  "<root>\n" +
    "<document>\n" +
      "<sentences>\n" +
        "<sentence id=\"1\">\n" +
          "<tokens>\n" +
            "<token id=\"1\">\n" +
              "<word>Dogs</word>\n" +
              "<lemma>dog</lemma>\n" +
              "<CharacterOffsetBegin>0</CharacterOffsetBegin>\n" +
              "<CharacterOffsetEnd>4</CharacterOffsetEnd>\n" +
              "<POS>NNS</POS>\n" +
            "</token>\n" +
            "<token id=\"2\">\n" +
              "<word>are</word>\n" +
              "<lemma>be</lemma>\n" +
              "<CharacterOffsetBegin>5</CharacterOffsetBegin>\n" +
              "<CharacterOffsetEnd>8</CharacterOffsetEnd>\n" +
              "<POS>VBP</POS>\n" +
            "</token>\n" +
            "<token id=\"3\">\n" +
              "<word>fun</word>\n" +
              "<lemma>fun</lemma>\n" +
              "<CharacterOffsetBegin>9</CharacterOffsetBegin>\n" +
              "<CharacterOffsetEnd>12</CharacterOffsetEnd>\n" +
              "<POS>NN</POS>\n" +
            "</token>\n" +
            "<token id=\"4\">\n" +
              "<word>.</word>\n" +
              "<lemma>.</lemma>\n" +
              "<CharacterOffsetBegin>12</CharacterOffsetBegin>\n" +
              "<CharacterOffsetEnd>13</CharacterOffsetEnd>\n" +
              "<POS>.</POS>\n" +
            "</token>\n" +
          "</tokens>\n" +
          "<parse>(ROOT (S (NP (NNS Dogs)) (VP (VBP are) (NP (NN fun))) (. .))) </parse>\n" +
          "<basic-dependencies>\n" +
            "<dep type=\"nsubj\">\n" +
              "<governor idx=\"3\">fun</governor>\n" +
              "<dependent idx=\"1\">Dogs</dependent>\n" +
            "</dep>\n" +
            "<dep type=\"cop\">\n" +
              "<governor idx=\"3\">fun</governor>\n" +
              "<dependent idx=\"2\">are</dependent>\n" +
            "</dep>\n" +
          "</basic-dependencies>\n" +
          "<collapsed-dependencies>\n" +
            "<dep type=\"nsubj\">\n" +
              "<governor idx=\"3\">fun</governor>\n" +
              "<dependent idx=\"1\">Dogs</dependent>\n" +
            "</dep>\n" +
            "<dep type=\"cop\">\n" +
              "<governor idx=\"3\">fun</governor>\n" +
              "<dependent idx=\"2\">are</dependent>\n" +
            "</dep>\n" +
          "</collapsed-dependencies>\n" +
          "<collapsed-ccprocessed-dependencies>\n" +
            "<dep type=\"nsubj\">\n" +
              "<governor idx=\"3\">fun</governor>\n" +
              "<dependent idx=\"1\">Dogs</dependent>\n" +
            "</dep>\n" +
            "<dep type=\"cop\">\n" +
              "<governor idx=\"3\">fun</governor>\n" +
              "<dependent idx=\"2\">are</dependent>\n" +
            "</dep>\n" +
          "</collapsed-ccprocessed-dependencies>\n" +
        "</sentence>\n" +
        "<sentence id=\"2\">\n" +
          "<tokens>\n" +
            "<token id=\"1\">\n" +
              "<word>Cats</word>\n" +
              "<lemma>cat</lemma>\n" +
              "<CharacterOffsetBegin>15</CharacterOffsetBegin>\n" +
              "<CharacterOffsetEnd>19</CharacterOffsetEnd>\n" +
              "<POS>NNS</POS>\n" +
            "</token>\n" +
            "<token id=\"2\">\n" +
              "<word>are</word>\n" +
              "<lemma>be</lemma>\n" +
              "<CharacterOffsetBegin>20</CharacterOffsetBegin>\n" +
              "<CharacterOffsetEnd>23</CharacterOffsetEnd>\n" +
              "<POS>VBP</POS>\n" +
            "</token>\n" +
            "<token id=\"3\">\n" +
              "<word>dumb</word>\n" +
              "<lemma>dumb</lemma>\n" +
              "<CharacterOffsetBegin>24</CharacterOffsetBegin>\n" +
              "<CharacterOffsetEnd>28</CharacterOffsetEnd>\n" +
              "<POS>JJ</POS>\n" +
            "</token>\n" +
            "<token id=\"4\">\n" +
              "<word>.</word>\n" +
              "<lemma>.</lemma>\n" +
              "<CharacterOffsetBegin>28</CharacterOffsetBegin>\n" +
              "<CharacterOffsetEnd>29</CharacterOffsetEnd>\n" +
              "<POS>.</POS>\n" +
            "</token>\n" +
          "</tokens>\n" +
          "<parse>(ROOT (S (NP (NNS Cats)) (VP (VBP are) (ADJP (JJ dumb))) (. .))) </parse>\n" +
          "<basic-dependencies>\n" +
            "<dep type=\"nsubj\">\n" +
              "<governor idx=\"3\">dumb</governor>\n" +
              "<dependent idx=\"1\">Cats</dependent>\n" +
            "</dep>\n" +
            "<dep type=\"cop\">\n" +
              "<governor idx=\"3\">dumb</governor>\n" +
              "<dependent idx=\"2\">are</dependent>\n" +
            "</dep>\n" +
          "</basic-dependencies>\n" +
          "<collapsed-dependencies>\n" +
            "<dep type=\"nsubj\">\n" +
              "<governor idx=\"3\">dumb</governor>\n" +
              "<dependent idx=\"1\">Cats</dependent>\n" +
            "</dep>\n" +
            "<dep type=\"cop\">\n" +
              "<governor idx=\"3\">dumb</governor>\n" +
              "<dependent idx=\"2\">are</dependent>\n" +
            "</dep>\n" +
          "</collapsed-dependencies>\n" +
          "<collapsed-ccprocessed-dependencies>\n" +
            "<dep type=\"nsubj\">\n" +
              "<governor idx=\"3\">dumb</governor>\n" +
              "<dependent idx=\"1\">Cats</dependent>\n" +
            "</dep>\n" +
            "<dep type=\"cop\">\n" +
              "<governor idx=\"3\">dumb</governor>\n" +
              "<dependent idx=\"2\">are</dependent>\n" +
            "</dep>\n" +
          "</collapsed-ccprocessed-dependencies>\n" +
        "</sentence>\n" +
      "</sentences>\n" +
    "</document>\n" +
  "</root>\n";

  @BeforeClass
  public static void testSetup(){
  }

  @AfterClass
  public static void testCleanup(){
  }

  @Test
  public void testObjectCreationWorks() {
    //ACE2004Parse parse = new ACE2004Parse("NYT20001127.2214.0372");
    ACE2004Parse parse =
        new ACE2004Parse(new ByteArrayInputStream(XML_PARSE_STRING.getBytes()),
                         new ByteArrayInputStream(OFFSET_STRING.getBytes()));
  }

  @Test
  public void testTokenLocationsReturned() {
    //ACE2004Parse parse = new ACE2004Parse("NYT20001127.2214.0372");
    ACE2004Parse parse =
        new ACE2004Parse(new ByteArrayInputStream(XML_PARSE_STRING.getBytes()),
                         new ByteArrayInputStream(OFFSET_STRING.getBytes()));
  TokenInfo firstInfo = new TokenInfo(1, "Dogs", "dog", 0, 4, "NNS");
  assertEquals(firstInfo, parse.getTokenAtLocation(new TokenLocation(1, 1)));

  TokenInfo secondInfo = new TokenInfo(2, "are", "be", 5, 8, "VBP");
  assertEquals(secondInfo, parse.getTokenAtLocation(new TokenLocation(1, 2)));

  TokenInfo thirdInfo = new TokenInfo(1, "Cats", "cat", 15, 19, "NNS");
  assertEquals(thirdInfo, parse.getTokenAtLocation(new TokenLocation(2, 1)));
  }

  @Test
  public void testDependenciesInDependencylessSpan() {
    ACE2004Parse parse =
        new ACE2004Parse(new ByteArrayInputStream(XML_PARSE_STRING.getBytes()),
                         new ByteArrayInputStream(OFFSET_STRING.getBytes()));

    Set<Dependency> deps = parse.getDependenciesInSpan(OFFSET + 28,
                                                        OFFSET + 29);
    assertEquals(new HashSet<Dependency>(), deps);
  }

  @Test
  public void testDependenciesInOutOfRangeSpan() {
    ACE2004Parse parse =
        new ACE2004Parse(new ByteArrayInputStream(XML_PARSE_STRING.getBytes()),
                         new ByteArrayInputStream(OFFSET_STRING.getBytes()));

    Set<Dependency> deps = parse.getDependenciesInSpan(100000, 100001);
    assertEquals(new HashSet<Dependency>(), deps);

    deps = parse.getDependenciesInSpan(0, 0);
    assertEquals(new HashSet<Dependency>(), deps);
  }

  @Test
  public void testDependenciesInPerfectlySizedSpan() {
    ACE2004Parse parse =
        new ACE2004Parse(new ByteArrayInputStream(XML_PARSE_STRING.getBytes()),
                         new ByteArrayInputStream(OFFSET_STRING.getBytes()));

    // "Dogs" to "fun", inclusive, sent 1:
    Set<Dependency> deps = parse.getDependenciesInSpan(OFFSET + 0,
                                                       OFFSET + 11);

    // (nsubj, fun, Dogs):
    Dependency dep1 = new Dependency("nsubj", new TokenLocation(1, 3),
                                     new TokenLocation(1, 1));
    // (cop, fun, are):
    Dependency dep2 = new Dependency("cop", new TokenLocation(1, 3),
                                     new TokenLocation(1, 2));

    HashSet<Dependency> expected = new HashSet<Dependency>();
    expected.add(dep1);
    expected.add(dep2);
    assertEquals(expected, deps);
  }

  @Test
  public void testDependenciesInSubspan() {
    ACE2004Parse parse =
        new ACE2004Parse(new ByteArrayInputStream(XML_PARSE_STRING.getBytes()),
                         new ByteArrayInputStream(OFFSET_STRING.getBytes()));

    // "ogs" to "fu", inclusive, sent 1:
    Set<Dependency> deps = parse.getDependenciesInSpan(OFFSET + 1, OFFSET + 10);

    // (nsubj, fun, Dogs):
    Dependency dep1 = new Dependency("nsubj", new TokenLocation(1, 3),
                                     new TokenLocation(1, 1));
    // (cop, fun, are):
    Dependency dep2 = new Dependency("cop", new TokenLocation(1, 3),
                                     new TokenLocation(1, 2));

    HashSet<Dependency> expected = new HashSet<Dependency>();
    expected.add(dep1);
    expected.add(dep2);
    assertEquals(expected, deps);
  }

  @Test
  public void testDependenciesInSuperspan() {
    ACE2004Parse parse =
        new ACE2004Parse(new ByteArrayInputStream(XML_PARSE_STRING.getBytes()),
                         new ByteArrayInputStream(OFFSET_STRING.getBytes()));

    // "Dogs" to ".", inclusive, sent 1:
    Set<Dependency> deps = parse.getDependenciesInSpan(OFFSET + 0, OFFSET + 12);

    // (nsubj, fun, Dogs):
    Dependency dep1 = new Dependency("nsubj", new TokenLocation(1, 3),
                                     new TokenLocation(1, 1));
    // (cop, fun, are):
    Dependency dep2 = new Dependency("cop", new TokenLocation(1, 3),
                                     new TokenLocation(1, 2));

    HashSet<Dependency> expected = new HashSet<Dependency>();
    expected.add(dep1);
    expected.add(dep2);
    assertEquals(expected, deps);
  }

  @Test
  public void testDependenciesInEntireDocument() {
    ACE2004Parse parse =
        new ACE2004Parse(new ByteArrayInputStream(XML_PARSE_STRING.getBytes()),
                         new ByteArrayInputStream(OFFSET_STRING.getBytes()));

    // "Dogs" to "dumb.", inclusive:
    Set<Dependency> deps = parse.getDependenciesInSpan(OFFSET + 0, OFFSET + 28);

    // (nsubj, fun, Dogs):
    Dependency dep1 = new Dependency("nsubj", new TokenLocation(1, 3),
                                     new TokenLocation(1, 1));
    // (cop, fun, are):
    Dependency dep2 = new Dependency("cop", new TokenLocation(1, 3),
                                     new TokenLocation(1, 2));
    // (nsubj, dumb, Cats):
    Dependency dep3 = new Dependency("nsubj", new TokenLocation(2, 3),
                                     new TokenLocation(2, 1));
    // (cop, dumb, are):
    Dependency dep4 = new Dependency("cop", new TokenLocation(2, 3),
                                     new TokenLocation(2, 2));
    HashSet<Dependency> expected = new HashSet<Dependency>();
    expected.add(dep1);
    expected.add(dep2);
    expected.add(dep3);
    expected.add(dep4);
    assertEquals(expected, deps);
  }

  @Test
  public void testEmptyPOSGivesNoDependencies() {
    ACE2004Parse parse =
        new ACE2004Parse(new ByteArrayInputStream(XML_PARSE_STRING.getBytes()),
                         new ByteArrayInputStream(OFFSET_STRING.getBytes()));

    // "Dogs" to "fun", inclusive, sent 1:
    Set<Dependency> deps =
        parse.getDependenciesInSpan(OFFSET + 0, OFFSET + 11,
                                    new ArrayList<String>());
    assertEquals(new HashSet<String>(), deps);
  }

  @Test
  public void testSinglePOSFilterWorks() {
    ACE2004Parse parse =
        new ACE2004Parse(new ByteArrayInputStream(XML_PARSE_STRING.getBytes()),
                         new ByteArrayInputStream(OFFSET_STRING.getBytes()));

    List<String> posList = new ArrayList<String>();
    posList.add("NNS");

    // "Dogs" to "fun", inclusive, sent 1:
    Set<Dependency> deps = parse.getDependenciesInSpan(OFFSET + 0,
                                                        OFFSET + 11, posList);

    // (nsubj, fun, Dogs):
    Dependency dep1 = new Dependency("nsubj", new TokenLocation(1, 3),
                                     new TokenLocation(1, 1));

    HashSet<Dependency> expected = new HashSet<Dependency>();
    expected.add(dep1);
    assertEquals(expected, deps);

    posList = new ArrayList<String>();
    posList.add("VBP");

    // "Dogs" to "fun", inclusive, sent 1:
    deps = parse.getDependenciesInSpan(OFFSET + 0, OFFSET + 11, posList);

    // (cop, fun, are):
    Dependency dep2 = new Dependency("cop", new TokenLocation(1, 3),
                                     new TokenLocation(1, 2));

    expected = new HashSet<Dependency>();
    expected.add(dep1);
    expected.add(dep2);
    assertEquals(expected, deps);
  }

  @Test
  public void testTwoPOSFilterWorks() {
    ACE2004Parse parse =
        new ACE2004Parse(new ByteArrayInputStream(XML_PARSE_STRING.getBytes()),
                         new ByteArrayInputStream(OFFSET_STRING.getBytes()));

    List<String> posList = new ArrayList<String>();
    posList.add("NNS");
    posList.add("NN");

    // "Dogs" to "fun", inclusive, sent 1:
    Set<Dependency> deps = parse.getDependenciesInSpan(OFFSET + 0, OFFSET + 11,
                                                       posList);

    // (nsubj, fun, Dogs):
    Dependency dep1 = new Dependency("nsubj", new TokenLocation(1, 3),
                                     new TokenLocation(1, 1));
    // (cop, fun, are):
    Dependency dep2 = new Dependency("cop", new TokenLocation(1, 3),
                                     new TokenLocation(1, 2));

    HashSet<Dependency> expected = new HashSet<Dependency>();
    expected.add(dep1);
    expected.add(dep2);
    assertEquals(expected, deps);
  }

  @Test
  public void testSingleDepTypeFilterWorks() {
    ACE2004Parse parse =
        new ACE2004Parse(new ByteArrayInputStream(XML_PARSE_STRING.getBytes()),
                         new ByteArrayInputStream(OFFSET_STRING.getBytes()));

    List<String> depList = new ArrayList<String>();
    depList.add("nsubj");

    // "Dogs" to "fun", inclusive, sent 1:
    Set<Dependency> deps = parse.getDependenciesInSpan(OFFSET + 0,
                                                       OFFSET + 11, null,
                                                       depList);

    // (nsubj, fun, Dogs):
    Dependency dep = new Dependency("nsubj", new TokenLocation(1, 3),
                                     new TokenLocation(1, 1));

    HashSet<Dependency> expected = new HashSet<Dependency>();
    expected.add(dep);
    assertEquals(expected, deps);

    depList = new ArrayList<String>();
    depList.add("cop");

    // "Dogs" to "fun", inclusive, sent 1:
    deps = parse.getDependenciesInSpan(OFFSET + 0, OFFSET + 11, null, depList);

    // (cop, fun, are):
    dep = new Dependency("cop", new TokenLocation(1, 3),
                         new TokenLocation(1, 2));

    expected = new HashSet<Dependency>();
    expected.add(dep);
    assertEquals(expected, deps);
  }

  @Test
  public void testTwoDepTypeFilterWorks() {
    ACE2004Parse parse =
        new ACE2004Parse(new ByteArrayInputStream(XML_PARSE_STRING.getBytes()),
                         new ByteArrayInputStream(OFFSET_STRING.getBytes()));

    List<String> depList = new ArrayList<String>();
    depList.add("nsubj");
    depList.add("cop");

    // "Dogs" to "fun", inclusive, sent 1:
    Set<Dependency> deps = parse.getDependenciesInSpan(OFFSET + 0,
                                                       OFFSET + 11, null,
                                                       depList);

    // (nsubj, fun, Dogs):
    Dependency dep1 = new Dependency("nsubj", new TokenLocation(1, 3),
                                     new TokenLocation(1, 1));
    // (cop, fun, are):
    Dependency dep2 = new Dependency("cop", new TokenLocation(1, 3),
                                     new TokenLocation(1, 2));

    HashSet<Dependency> expected = new HashSet<Dependency>();
    expected.add(dep1);
    expected.add(dep2);
    assertEquals(expected, deps);
  }

  @Test
  public void testSingleRelationFilterWorks() {
    ACE2004Parse parse =
        new ACE2004Parse(new ByteArrayInputStream(XML_PARSE_STRING.getBytes()),
                         new ByteArrayInputStream(OFFSET_STRING.getBytes()));

    List<String> relList = new ArrayList<String>();
    relList.add("dependent");

    // "Dogs", inclusive, sent 1:
    Set<Dependency> deps = parse.getDependenciesInSpan(OFFSET + 0, OFFSET + 3,
                                                       null, null, relList);

    // (nsubj, fun, Dogs):
    Dependency dep = new Dependency("nsubj", new TokenLocation(1, 3),
                                     new TokenLocation(1, 1));

    HashSet<Dependency> expected = new HashSet<Dependency>();
    expected.add(dep);
    assertEquals(expected, deps);

    relList = new ArrayList<String>();
    relList.add("governor");

    // "Dogs", inclusive, sent 1:
    deps = parse.getDependenciesInSpan(OFFSET + 0, OFFSET + 3, null, null,
                                       relList);

    assertEquals(new HashSet<Dependency>(), deps);
  }

  @Test
  public void testTwoRelationFilterWorks() {
    ACE2004Parse parse =
        new ACE2004Parse(new ByteArrayInputStream(XML_PARSE_STRING.getBytes()),
                         new ByteArrayInputStream(OFFSET_STRING.getBytes()));

    List<String> relList = new ArrayList<String>();
    relList.add("governor");
    relList.add("dependent");

    // "are" to "fun", inclusive, sent 1:
    Set<Dependency> deps = parse.getDependenciesInSpan(OFFSET + 5,
                                                       OFFSET + 11, null, null,
                                                       relList);

    // (nsubj, fun, Dogs):
    Dependency dep1 = new Dependency("nsubj", new TokenLocation(1, 3),
                                     new TokenLocation(1, 1));
    // (cop, fun, are):
    Dependency dep2 = new Dependency("cop", new TokenLocation(1, 3),
                                     new TokenLocation(1, 2));

    HashSet<Dependency> expected = new HashSet<Dependency>();
    expected.add(dep1);
    expected.add(dep2);
    assertEquals(expected, deps);
  }

}
