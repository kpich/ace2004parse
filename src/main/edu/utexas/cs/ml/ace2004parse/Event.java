package edu.utexas.cs.ml.ace2004parse;


public class Event {

  public enum DependencyType {
    SUBJ, OBJ, PREP
  }

  private String lemma;
  private DependencyType depType;
  private TokenInfo tokenInfo;
  private Dependency dep;

  /**
   * Ctor allowing you to load a parse for a specific document
   */
  public Event(String lemma, DependencyType depType, TokenInfo tokenInfo,
               Dependency dep) {
    this.lemma = lemma;
    this.depType = depType;
    this.tokenInfo = tokenInfo;
    this.dep = dep;
  }

  public boolean equals(Object rhs) {
    if (rhs instanceof Event) {
      Event rhsEv = (Event) rhs;
      return this.lemma.equals(rhsEv.lemma) &&
             this.depType == rhsEv.depType &&
             this.tokenInfo.equals(rhsEv.tokenInfo) &&
             this.dep.equals(rhsEv.dep);
    }
    return false;
  }

  public int hashCode() {
    int result = 17;
    result = 31 * result + (lemma == null ? 0 : lemma.hashCode());
    result = 31 * result + (depType == null ? 0 : depType.hashCode());
    result = 31 * result + (tokenInfo == null ? 0 : tokenInfo.hashCode());
    result = 31 * result + (dep == null ? 0 : dep.hashCode());
    return result;
  }

  public String getLemma() {
    return this.lemma;
  }

  public DependencyType getDepType() {
    return this.depType;
  }

  public Dependency getDep() {
    return this.dep;
  }

  public TokenInfo getTokenInfo() {
    return this.tokenInfo;
  }
}
