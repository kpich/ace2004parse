package edu.utexas.cs.ml.ace2004parse;


public class Event {

  public enum DependencyType {
    SUBJ, OBJ, PREP
  }

  private String lemma;
  private DependencyType dep;

  /**
   * Ctor allowing you to load a parse for a specific document
   */
  public Event(String lemma, DependencyType dep) {
    this.lemma = lemma;
    this.dep = dep;
  }

  public String getLemma() {
    return this.lemma;
  }
  public DependencyType getDep() {
    return this.dep;
  }
}
