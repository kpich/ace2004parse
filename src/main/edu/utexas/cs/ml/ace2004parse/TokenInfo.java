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
      this.id = id;
      this.word = word;
      this.lemma = lemma;
      this.charOffsetBegin = charOffsetBegin;
      this.charOffsetEnd = charOffsetEnd;
      this.pos = pos;
    }

    public boolean equals(Object rhs) {
      if (rhs instanceof TokenInfo) {
        TokenInfo rhsTok = (TokenInfo) rhs;
        return this.id == rhsTok.id &&
               this.word.equals(rhsTok.word) &&
               this.lemma.equals(rhsTok.lemma) &&
               this.charOffsetBegin == rhsTok.charOffsetBegin &&
               this.charOffsetEnd == rhsTok.charOffsetEnd &&
               this.pos.equals(rhsTok.pos);
      }
      return false;
    }

    public String toString() {
      return "[" + id + ", " + word + ", " + lemma + ", " + charOffsetBegin +
             ", " + charOffsetEnd + ", " + pos + "]";
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

