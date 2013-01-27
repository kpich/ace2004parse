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
    if (rhs instanceof Dependency) {
      Dependency rhsDep = (Dependency) rhs;
      return this.type.equals(rhsDep.type) &&
             this.governor.equals(rhsDep.governor) &&
             this.dependent.equals(rhsDep.dependent);
    }
    return false;
  }

  public int hashCode() {
    int result = 17;
    result = 31 * result + (type == null ? 0 : type.hashCode());
    result = 31 * result + (governor == null ? 0 : governor.hashCode());
    result = 31 * result + (dependent == null ? 0 : dependent.hashCode());
    return result;
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

