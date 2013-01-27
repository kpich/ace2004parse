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
             (this.word != null && this.word.equals(rhsTok.word)) &&
             (this.lemma != null && this.lemma.equals(rhsTok.lemma)) &&
             this.charOffsetBegin == rhsTok.charOffsetBegin &&
             this.charOffsetEnd == rhsTok.charOffsetEnd &&
             (this.pos != null && this.pos.equals(rhsTok.pos));
    }
    return false;
  }

  public int hashCode() {
    int result = 17;
    result = 31 * result + (id < 0 ? 0 : id);
    result = 31 * result + (word == null ? 0 : word.hashCode());
    result = 31 * result + (word == null ? 0 : lemma.hashCode());
    result = 31 * result + (charOffsetBegin < 0 ? 0 : charOffsetBegin);
    result = 31 * result + (charOffsetEnd < 0 ? 0 : charOffsetEnd);
    result = 31 * result + (pos == null ? 0 : pos.hashCode());
    return result;
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

