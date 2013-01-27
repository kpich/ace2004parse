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
