package edu.utexas.cs.ml.ace2004parse;


public final class Dependency {

  private String type;
  private TokenLocation governor;
  private TokenLocation dependent;

  public Dependency(String type, TokenLocation governor,
                    TokenLocation dependent) {
    this.type = type;
    this.governor = governor;
    this.dependent = dependent;
  }

  public boolean equals(Object rhs) {
    if (rhs instanceof TokenInfo) {
      Dependency rhsDep = (Dependency) rhs;
      return this.type.equals(rhsDep.type) &&
             this.governor.equals(rhsDep.governor) &&
             this.dependent.equals(rhsDep.dependent);
    }
    return false;
  }

  public String getType() {
    return this.type;
  }

  public TokenLocation getGovernor() {
    return this.governor;
  }

  public TokenLocation getDependent() {
    return this.dependent;
  }

  public String toString() {
    return "([" + type + "]" + governor + ";" + dependent + ")";
  }
}

