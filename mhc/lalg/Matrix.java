package mhc.lalg;

import java.io.StringWriter;

import mhc.lalg.CMat.SubMatrix;

/**
 * Class: Matrix
 */
public abstract class Matrix {
  
  /**
   * Add to the target row in this {@link Matrix} a multiple of the other source row. The
   * updated Matrix is returned.
   * <p>
   * <b>Note:</b>The target row is not changed anywhere to source row has a zero element as
   * adding <code>0 * multiplier)</code> does nothing.
   * 
   * @param sourceRowIndex the index of the source row.
   * @param multiplier the multiplier to use.
   * @param targetRowIndex the index of the target row. This will be the only row changed in
   *          the updated {@link Matrix}.
   * @return The updated Matrix is returned.
   */
  public Matrix addRowsWithMult(int sourceRowIndex, double multiplier, int targetRowIndex) {
    int len = getNumCols();
    for (int colIndex = 0; colIndex < len; colIndex++ ) {
      double targetRowValue = get(targetRowIndex, colIndex);
      double sourceRowValue = get(sourceRowIndex, colIndex);
      double delta = sourceRowValue * multiplier;
      double newValue = targetRowValue + delta;
      set(targetRowIndex, colIndex, newValue);
    }
    return this;
  }
  
  /**
   * Return the value in this {@link Matrix} at the specified coordinates.
   * 
   * @param rowIndex the row
   * @param colIndex the column
   * @return the value in this {@link Matrix} at the specified coordinates.
   */
  public abstract double get(int rowIndex, int colIndex);
  
  /**
   * Returns a view on the contents of the specified column as a {@link Vector}.
   * 
   * @param colIndex the column
   * @return a view on the contents of the specified column as a {@link Vector}.
   */
  public Vector getCol(int colIndex) {
    return getSubCol(colIndex, 0, getNumRows());
  }
  
  /**
   * Returns the leading entry value in the indicated row. If there is not leading entry (the
   * row is all zeros) then 0 is returned.
   * 
   * @param rowIndex the index of the row to be examined.
   * @return the leading entry value in the indicated row. If there is not leading entry (the
   *         row is all zeros) then 0 is returned.
   */
  public double getLeadingEntry(int rowIndex) {
    int leColIndex = getLeadingEntryCol(rowIndex);
    if (leColIndex == -1)
      return 0.0; // No leading entry
    double leadingEntry = get(rowIndex, leColIndex);
    return leadingEntry;
  }
  
  /**
   * Returns the column index of the first value in the indicated row that is not 0, if there
   * is no non-zero value in the row, -1 is returned.
   * 
   * @param rowIndex the index of the row to be examined
   * @return the column index of the first value in the indicated row that is not 0, if there
   *         is no non-zero value in the row, -1 is returned.
   */
  public int getLeadingEntryCol(int rowIndex) {
    int numCols = getNumCols();
    for (int cI = 0; cI < numCols; cI++ ) {
      double value = get(rowIndex, cI);
      if (value != 0)
        return cI;
    }
    /*
     * No non-zero entries found.
     */
    return -1;
  }
  
  /**
   * Returns the number of columns in this {@link Matrix}
   * 
   * @return the number of columns in this {@link Matrix}
   */
  public abstract int getNumCols();
  
  /**
   * Returns the number of rows in this {@link Matrix}
   * 
   * @return the number of rows in this {@link Matrix}
   */
  public abstract int getNumRows();
  
  /**
   * Returns a view on the contents of the specified row as a {@link Vector}.
   * 
   * @param rowIndex the row
   * @return a view on the contents of the specified row as a {@link Vector}.
   */
  public Vector getRow(int rowIndex) {
    return getSubRow(rowIndex, 0, getNumCols());
  }
  
  /**
   * Returns a view on the contents of the specified part of a column as a {@link Vector}.
   * 
   * @param colIndex the column
   * @param firstRow the starting point in the column
   * @param numRows the number of elements to be in the vector.
   * @return a view on the contents of the specified part of a column as a {@link Vector}.
   */
  public abstract Vector getSubCol(int colIndex, int firstRow, int numRows);
  
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
  public abstract Matrix getSubMatrix(int firstRow, int numRows, int firstCol, int numCols);
  
  /**
   * Returns a view on the contents of the specified part of a row as a {@link Vector}.
   * 
   * @param rowIndex the row
   * @param firstCol the starting point in the row
   * @param numCols the number of elements to be in the vector.
   * @return a view on the contents of the specified part of a row as a {@link Vector}.
   */
  public abstract Vector getSubRow(int rowIndex, int firstCol, int numCols);
  
  /**
   * Return true if the number of rows and columns are equal.
   * 
   * @return true if the number of rows and columns are equal.
   */
  public boolean isSquare() {
    return getNumRows() == getNumCols();
  }
  
  /**
   * Transforms this {@link Matrix} by row scaling operations so that the leading value in each
   * row is 1.
   */
  public void normalize() {
    int numRows = getNumRows();
    for (int rI = 0; rI < numRows; rI++ ) {
      normalize(rI);
    }
  }
  
  /**
   * Scales the indicated row so that its leading value is 1. If the row is all zeros nothing
   * is done.
   * 
   * @param rowIndex the index of the row to be normalized.
   */
  public void normalize(int rowIndex) {
    int numCols = getNumCols();
    int leCol = getLeadingEntryCol(rowIndex);
    if (leCol == -1)
      return; // all zeros, so nothing to do
    double scaleFactor = 1.0 / get(rowIndex, leCol);
    set(rowIndex, leCol, 1.0); // don't need to do the scaling multiplication.
    for (int cI = leCol + 1; cI < numCols; cI++ ) {
      double value = get(rowIndex, cI);
      double scaledValue = scaleFactor * value;
      set(rowIndex, cI, scaledValue);
    }
    return;
  }
  
  /**
   * Scale the indicated row in this {@link Matrix} with the indicated scale factor. This
   * {@link Matrix} is returned with scaled row.
   * 
   * @param rowIndex the index of the row to be scaled
   * @param scaleFactor the factor to scale the row by
   * @return this {@link Matrix} after the change.
   */
  public Matrix scaleRow(int rowIndex, double scaleFactor) {
    for (int colIndex = 0; colIndex < getNumCols(); colIndex++ ) {
      set(rowIndex, colIndex, scaleFactor * get(rowIndex, colIndex));
    }
    return this;
  }
  
  /**
   * Replaces the value in this {@link Matrix} at the specified coordinates with the specified
   * value.
   * 
   * @param rowIndex the row
   * @param colIndex the column
   * @param value the new value
   */
  public abstract void set(int rowIndex, int colIndex, double value);
  
  /**
   * Sets the contents of the specified column with the values in the specified {@link Vector}.
   * 
   * @param colIndex the column
   * @param vec the {@link Vector}
   */
  public void setCol(int colIndex, Vector vec) {
    assert vec.dimension() <= getNumRows();
    setSubCol(colIndex, 0, vec);
  }
  
  /**
   * Sets the contents of the specified row with the values in the specified {@link Vector}.
   * 
   * @param rowIndex the row
   * @param vec the {@link Vector}
   */
  public void setRow(int rowIndex, Vector vec) {
    assert vec.dimension() == getNumCols();
    setSubRow(rowIndex, 0, vec);
  }
  
  /**
   * Set the context of the specified portion of a column with the specified {@link Vector}.
   * 
   * @param colIndex the column
   * @param firstRow the first element in the column to set
   * @param vec the {@link Vector}
   */
  public void setSubCol(int colIndex, int firstRow, Vector vec) {
    int len = vec.dimension();
    for (int i = 0; i < len; i++ ) {
      set(firstRow + i, colIndex, vec.get(i));
    }
  }
  
  /**
   * Set the context of the specified portion of a row with the specified {@link Vector}.
   * 
   * @param rowIndex the row
   * @param firstCol the first element in the row to set
   * @param vec the {@link Vector}
   */
  public void setSubRow(int rowIndex, int firstCol, Vector vec) {
    int len = vec.dimension();
    for (int i = 0; i < len; i++ ) {
      set(rowIndex, firstCol + i, vec.get(i));
    }
  }
  
  /**
   * Swap the contents of the two specified rows.
   * 
   * @param row1Index the index of the first row
   * @param row2Index the index of the second row
   * @return this {@link Matrix} after the swap
   */
  public Matrix swapRows(int row1Index, int row2Index) {
    if (row1Index == row2Index)
      /*
       * There is nothing to do as the two rows are the same.
       */
      return this;
    for (int colIndex = 0; colIndex < getNumCols(); colIndex++ ) {
      double temp = get(row1Index, colIndex);
      set(row1Index, colIndex, get(row2Index, colIndex));
      set(row2Index, colIndex, temp);
    }
    return this;
  }
  
  @Override
  public String toString() {
    StringWriter out = new StringWriter();
    for (int i = 0; i < getNumRows(); i++ ) {
      boolean commaNeeded = false;
      out.write("|");
      for (int j = 0; j < getNumCols(); j++ ) {
        double value = get(i, j);
        if (commaNeeded) {
          out.write(", ");
        } else {
          commaNeeded = true;
        }
        out.write("%6.2f".formatted(value));
      }
      out.write("|\n");
    }
    return out.toString();
  }
}
