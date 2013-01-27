package edu.utexas.cs.ml.ace2004parse;


public class Event {

  public enum DependencyType {
    SUBJ, OBJ, PREP
  }

  private String lemma;
  private DependencyType dep;
  private TokenInfo tokenInfo;

  /**
   * Ctor allowing you to load a parse for a specific document
   */
  public Event(String lemma, DependencyType dep, TokenInfo tokenInfo) {
    this.lemma = lemma;
    this.dep = dep;
    this.tokenInfo = tokenInfo;
  }

  public boolean equals(Object rhs) {
    if (rhs instanceof Event) {
      Event rhsEv = (Event) rhs;
      return this.lemma.equals(rhsEv.lemma) &&
             this.dep == rhsEv.dep &&
             this.tokenInfo.equals(rhsEv.tokenInfo);
    }
    return false;
  }

  public int hashCode() {
    int result = 17;
    result = 31 * result + lemma.hashCode();
    result = 31 * result + dep.hashCode();
    result = 31 * result + tokenInfo.hashCode();
    return result;
  }

  public String getLemma() {
    return this.lemma;
  }

  public DependencyType getDep() {
    return this.dep;
  }

  public TokenInfo getTokenInfo() {
    return this.tokenInfo;
  }
}
