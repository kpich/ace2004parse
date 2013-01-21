package edu.utexas.cs.ml.ace2004parse;

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
    ACE2004Parse parse = new ACE2004Parse("NYT20001127.2214.0372");
  }

}
