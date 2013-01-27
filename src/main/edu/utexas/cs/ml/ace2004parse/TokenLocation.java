/**
 * Essentially an immutable (sent, word) pair.
 *
 * Adapted from http://stackoverflow.com/a/677248
 */

package edu.utexas.cs.ml.ace2004parse;

public final class TokenLocation {
  private int sent = -1;
  private int word = -1;

  public TokenLocation(int sent, int word) {
    this.sent = sent;
    this.word = word;
  }

  /**
   * Basically doing what Josh Bloch says to do in Item 9 of Effective Java
   */
  public int hashCode() {
    int result = 17;
    result = 31 * result + (sent == -1 ? 0 : sent);
    result = 31 * result + (word == -1 ? 0 : word);
    return result;
  }

  public boolean equals(Object rhs) {
    if (rhs instanceof TokenLocation) {
      TokenLocation rhsTok = (TokenLocation) rhs;
      return this.sent == rhsTok.sent && this.word == rhsTok.word;
    }
    return false;
  }

  public String toString()
  {
    return "(" + sent + ", " + word + ")";
  }

  public int getSent() {
    return sent;
  }

  public int getWord() {
    return word;
  }
}
