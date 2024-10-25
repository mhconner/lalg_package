package mhc.lalg;

/**
 * A concrete implementation of {@link Vector}.
 */
public class CVec extends Vector {
  
  protected int dimension;
  
  protected final double[] values;
  
  /**
   * 
   */
  public CVec(int dimension) {
    this.dimension = dimension;
    values = new double[dimension];
  }
  
  /**
   * Returns a new {@link Vector} with the specified values.
   * 
   * @param values the values for the new vector
   * @return a new {@link Vector} with the specified values.
   */
  public static Vector vec(double... values) {
    CVec vec = new CVec(values.length);
    for (int i = 0; i < vec.dimension; i++ ) {
      vec.values[i] = values[i];
    }
    return vec;
  }
  
  /**
   * Overrides: dimension
   * 
   * @see mhc.lalg.Vector#dimension()
   */
  @Override
  public int dimension() {
    return dimension;
  }
  
  /**
   * Overrides: get
   * 
   * @see mhc.lalg.Vector#get(int)
   */
  @Override
  public double get(int index) {
    return values[index];
  }
  
  /**
   * Overrides: set
   * 
   * @see mhc.lalg.Vector#set(int, double)
   */
  @Override
  public void set(int index, double value) {
    values[index] = value;
  }
}
