package mhc.lalg;

import java.io.StringWriter;

/**
 * Class: Vector
 */
public abstract class Vector implements Comparable<Vector> {
  
  /**
   * This does an element by element comparison in index order. If one {@link Vector} is
   * shorter than the other but they are equal up to its length then it is considered less than
   * the longer {@link Vector}. Overrides: compareTo
   * 
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(Vector oVec) {
    int len1 = dimension();
    int len2 = oVec.dimension();
    for (int i = 0; i < len1; i++ ) {
      if (i >= len2)
        /*
         * this vector is longer than the other vector and they are equal up to this point so
         * this this vector is greater
         */
        return 1;
      double thisVal = get(i);
      double oVal = oVec.get(i);
      int result = Double.compare(thisVal, oVal);
      if (result != 0)
        return result;
    }
    if (len1 < len2)
      /*
       * They are equal through all of this Vector's values and this vector is shorter so it is
       * less than the other.
       */
      return -1;
    return 0;
  }
  
  public abstract int dimension();
  
  @Override
  public boolean equals(Object o) {
    Vector oVec = (Vector) o;
    if (compareTo(oVec) == 0)
      return true;
    else
      return false;
  }
  
  public abstract double get(int index);
  
  public abstract void set(int index, double value);
  
  @Override
  public String toString() {
    StringWriter out = new StringWriter();
    boolean commaNeeded = false;
    out.write("<");
    for (int i = 0; i < dimension(); i++ ) {
      double value = get(i);
      if (commaNeeded) {
        out.write(", ");
      } else {
        commaNeeded = true;
      }
      out.write("%f".formatted(value));
    }
    out.write(">");
    return out.toString();
  }
}
