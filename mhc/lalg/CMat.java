package mhc.lalg;

/**
 * A concrete implementation of {@link Matrix}.
 */
public class CMat extends Matrix {
  
  /**
   * Implements a {@link Vector} view of a col in this matrix.
   */
  public class ColVec extends Vector {
    
    private final int colIndex;
    
    private final int firstRow;
    
    private final int cntRows;
    
    public ColVec(int colIndex, int firstRow, int numRows) {
      this.colIndex = colIndex;
      this.firstRow = firstRow;
      cntRows = numRows;
    }
    
    @Override
    public int dimension() {
      return cntRows;
    }
    
    @Override
    public double get(int index) {
      return CMat.this.get(firstRow + index, colIndex);
    }
    
    @Override
    public void set(int index, double value) {
      CMat.this.set(firstRow + index, colIndex, value);
    }
  }
  
  /**
   * Implements a {@link Vector} view of a row in this matrix.
   */
  public class RowVec extends Vector {
    
    private final int rowIndex;
    
    private final int firstCol;
    
    private final int cntCols;
    
    public RowVec(int rowIndex, int firstCol, int numCols) {
      this.rowIndex = rowIndex;
      this.firstCol = firstCol;
      cntCols = numCols;
    }
    
    @Override
    public int dimension() {
      return cntCols;
    }
    
    @Override
    public double get(int index) {
      return CMat.this.get(rowIndex, firstCol + index);
    }
    
    @Override
    public void set(int index, double value) {
      CMat.this.set(rowIndex, firstCol + index, value);
    }
  }
  
  /**
   * Implements a {@link Matrix} view of a portion of its containing {@link Matrix}.
   */
  public class SubMatrix extends Matrix {
    
    final int firstRow;
    
    final int cntCols;
    
    final int firstCol;
    
    final int cntRows;
    
    /**
     * Creates a new {@link SubMatrix} view of its containing {@link Matrix}. This sub-matrix
     * just a view on the containing {@link Matrix} so updates to the {@link SubMatrix} will be
     * updates to the containing {@link Matrix}.
     * 
     * @param firstRow the index of the first row in the {@link SubMatrix}.
     * @param numRows the number of rows in the {@link SubMatrix}.
     * @param firstCol the index of the first column in the {@link SubMatrix}.
     * @param numCols the number of columns in the {@link SubMatrix}.
     */
    public SubMatrix(int firstRow, int numRows, int firstCol, int numCols) {
      this.firstRow = firstRow;
      cntRows = numRows;
      this.firstCol = firstCol;
      cntCols = numCols;
    }
    
    @Override
    public double get(int rowIndex, int colIndex) {
      return CMat.this.get(toOuterRowIndex(rowIndex), toOuterColIndex(colIndex));
    }
    
    @Override
    public int getNumCols() {
      return cntCols;
    }
    
    @Override
    public int getNumRows() {
      return cntRows;
    }
    
    /**
     * Overrides: getSubCol
     * 
     * @see mhc.lalg.Matrix#getSubCol(int, int, int)
     */
    @Override
    public Vector getSubCol(int colIndex, int firstRow, int numRows) {
      return CMat.this.getSubCol(toOuterColIndex(colIndex), toOuterRowIndex(firstRow), numRows);
    }
    
    @Override
    public Matrix getSubMatrix(int rowIndex, int numRows, int colIndex, int numCols) {
      SubMatrix subMat = new SubMatrix(toOuterRowIndex(rowIndex), numRows,
              toOuterColIndex(colIndex), numCols);
      return subMat;
    }
    
    /**
     * Overrides: getRow
     * 
     * @see mhc.lalg.Matrix#getSubRow(int, int, int)
     */
    @Override
    public Vector getSubRow(int rowIndex, int firstCol, int numCols) {
      return CMat.this.getSubRow(toOuterRowIndex(rowIndex), toOuterColIndex(firstCol), numCols);
    }
    
    @Override
    public void set(int rowIndex, int colIndex, double value) {
      CMat.this.set(toOuterRowIndex(rowIndex), toOuterColIndex(colIndex), value);
    }
    
    /**
     * Returns the column index in the containing matrix that is equivalent to the specified
     * column index in this {@link SubMatrix}.
     * 
     * @param colIndex the column index in the {@link SubMatrix}.
     * @return the column index in the containing matrix that is equivalent to the specified
     *         column index in this {@link SubMatrix}.
     */
    public final int toOuterColIndex(int colIndex) {
      return colIndex + firstCol;
    }
    
    /**
     * Returns the row index in the containing matrix that is equivalent to the specified row
     * index in this {@link SubMatrix}.
     * 
     * @param rowIndex the row index in the {@link SubMatrix}.
     * @return the row index in the containing matrix that is equivalent to the specified row
     *         index in this {@link SubMatrix}.
     */
    public final int toOuterRowIndex(int rowIndex) {
      return rowIndex + firstRow;
    }
  }
  
  private final int numRows;
  
  private final int numCols;
  
  private final int size;
  
  /**
   * The array that holds the elements of this {@link CMat} in row major order.
   */
  private final double[] values;
  
  /**
   * 
   */
  public CMat(int rowDimension, int colDimension) {
    numRows = rowDimension;
    numCols = colDimension;
    size = rowDimension * colDimension;
    values = new double[size];
  }
  
  public CMat(Vector... rows) {
    this(rows.length, rows[0].dimension());
  }
  
  public static CMat mat(Vector... rows) {
    int numRows = rows.length;
    int numCols = rows[0].dimension();
    CMat newMat = new CMat(numRows, numCols);
    for (int ri = 0; ri < numRows; ri++ ) {
      newMat.setRow(ri, rows[ri]);
    }
    return newMat;
  }
  
  /**
   * Overrides: get
   * 
   * @see mhc.lalg.Matrix#get(int, int)
   */
  @Override
  public double get(int rowIndex, int colIndex) {
    return values[pos(rowIndex, colIndex)];
  }
  
  /**
   * Overrides: getNumCols
   * 
   * @see mhc.lalg.Matrix#getNumCols()
   */
  @Override
  public int getNumCols() {
    return numCols;
  }
  
  /**
   * Overrides: getNumRows
   * 
   * @see mhc.lalg.Matrix#getNumRows()
   */
  @Override
  public int getNumRows() {
    return numRows;
  }
  
  /**
   * Overrides: getSubCol
   * 
   * @see mhc.lalg.Matrix#getSubCol(int, int, int)
   */
  @Override
  public Vector getSubCol(int colIndex, int firstRow, int numRows) {
    return new ColVec(colIndex, firstRow, numRows);
  }
  
  /**
   * Returns a new {@link SubMatrix} view of its containing {@link Matrix}. This sub-matrix is
   * just a view on the containing {@link Matrix} so updates to the {@link SubMatrix} will be
   * updates to the containing {@link Matrix}.
   * 
   * @param firstRow the index of the first row in the {@link SubMatrix}.
   * @param numRows the number of rows in the {@link SubMatrix}.
   * @param firstCol the index of the first column in the {@link SubMatrix}.
   * @param numCols the number of columns in the {@link SubMatrix}.
   * @return the specified {@link SubMatrix}.
   */
  @Override
  public Matrix getSubMatrix(int firstRow, int numRows, int firstCol, int numCols) {
    SubMatrix subM = new SubMatrix(firstRow, numRows, firstCol, numCols);
    return subM;
  }
  
  @Override
  public Vector getSubRow(int rowIndex, int colIndex, int numCols) {
    return new RowVec(rowIndex, colIndex, numCols);
  }
  
  /**
   * Returns the index in the {@link #values} array that corresponds the the indicated row and
   * col indices.
   * 
   * @param rowIndex
   * @param colIndex
   * @return the index in the {@link #values} array that corresponds the the indicated row and
   *         col indices.
   */
  private final int pos(int rowIndex, int colIndex) {
    return (rowIndex * numCols) + colIndex;
  }
  
  /**
   * Overrides: set
   * 
   * @see mhc.lalg.Matrix#set(int, int, double)
   */
  @Override
  public void set(int rowIndex, int colIndex, double value) {
    values[pos(rowIndex, colIndex)] = value;
  }
}
