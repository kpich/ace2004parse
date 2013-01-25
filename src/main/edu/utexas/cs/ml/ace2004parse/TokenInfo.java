/**
 * contains the info about a single token present in CoreNLP parse, which is
 *   <token id=\"1\">
 *     <word>Dogs</word>
 *     <lemma>dog</lemma>
 *     <CharacterOffsetBegin>0</CharacterOffsetBegin>
 *     <CharacterOffsetEnd>4</CharacterOffsetEnd>
 *     <POS>NNS</POS>
 *   </token>
 */

package edu.utexas.cs.ml.ace2004parse;

public final class TokenInfo {
    private int id;
    private String word;
    private String lemma;
    private int charOffsetBegin;
    private int charOffsetEnd;
    private String pos;

    public TokenInfo(int id, String word, String lemma, int charOffsetBegin,
                     int charOffsetEnd, String pos) {
    }

    public int getId() {
      return id;
    }

    public String getWord() {
      return word;
    }

    public String getLemma() {
      return lemma;
    }

    public int getCharOffsetBegin() {
      return charOffsetBegin;
    }

    public int getCharOffsetEnd() {
      return charOffsetEnd;
    }

    public String getPOS() {
      return pos;
    }

}

