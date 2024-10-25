package mhc.lalg;

/**
 * Provides static methods for basic operations on {@link Vector}s.
 */
public class Vec {
  
  private Vec() {
  }
  
  /**
   * Adds the first two {@link Vector}s together and saves the result in the third
   * {@link Vector} which could be one of the original {@link Vector}s. That is:
   * <code>vSum.set(i, v1.get(i) + v2.get(i))</code>.
   * 
   * @param v1 the first vector
   * @param v2 the second vector
   * @param vSum the sum of v1 and v2 is saved here
   * @return vSum after it has been updated.
   */
  public static Vector add(Vector v1, Vector v2, Vector vSum) {
    int dim = v1.dimension();
    assert (dim == v2.dimension()) && (dim == vSum.dimension());
    for (int i = 0; i < dim; i++ ) {
      vSum.set(i, v1.get(i) + v2.get(i));
    }
    return vSum;
  }
  
  public static CVec copy(Vector vec) {
    int len = vec.dimension();
    CVec copyVec = new CVec(len);
    for (int i = 0; i < len; i++ ) {
      copyVec.set(i, vec.get(i));
    }
    return copyVec;
  }
  
  /**
   * Returns the dot product of the two specified {@link Vector}s.
   * 
   * @param v1 {@link Vector} 1
   * @param v2 {@link Vector} 2
   * @return the dot product of the two specified {@link Vector}s.
   */
  public static double dotProduct(Vector v1, Vector v2) {
    assert v1.dimension() == v2.dimension();
    double dp = 0.0;
    for (int i = 0; i < v1.dimension(); i++ ) {
      dp += v1.get(i) * v2.get(i);
    }
    return dp;
  }
  
  /**
   * Multiples each of the elements in the target {@link Vector} by the specified factor and
   * puts the results in the result {@link Vector} which may be the same at the target
   * {@link Vector}. The result {@link Vector} is returned.
   * 
   * @param factor the multiplication factor
   * @param targetV the target {@link Vector}
   * @param resultV the result {@link Vector}
   * @return The result {@link Vector}.
   */
  public static Vector mult(double factor, Vector targetV, Vector resultV) {
    int dim = targetV.dimension();
    assert dim == resultV.dimension();
    for (int i = 0; i < dim; i++ ) {
      resultV.set(i, factor * targetV.get(i));
    }
    return resultV;
  }
}
