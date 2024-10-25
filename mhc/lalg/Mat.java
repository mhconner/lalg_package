package mhc.lalg;

import mhc.lalg.util.Out;

/**
 * Provides static methods for basic {@link Matrix} operations
 */
public class Mat {
  
  private Mat() {
  }
  
  /**
   * Returns a concrete copy ({@link CMat}) of the contents of the specified {@link Matrix}.
   * 
   * @param mat the {@link Matrix} to be copied.
   * @return a concrete copy ({@link CMat}) of the contents of the specified {@link Matrix}. If
   *         the
   */
  public static CMat copy(Matrix mat) {
    int numRows = mat.getNumRows();
    int numCols = mat.getNumCols();
    CMat copyMat = new CMat(numRows, numCols);
    for (int rI = 0; rI < numRows; rI++ ) {
      for (int cI = 0; cI < numCols; cI++ ) {
        copyMat.set(rI, cI, mat.get(rI, cI));
      }
    }
    return copyMat;
  }
  
  /**
   * Returns the index of the left-most column in the specified {@link Matrix} that has a
   * non-zero element or -1 if no column in the {@link Matrix} has a non-zero element.
   * 
   * @param mat the Matrix to be examined.
   * @return the index of the left-most column in the specified {@link Matrix} that has a
   *         non-zero element or -1 if no column in the {@link Matrix} has a non-zero element.
   */
  public static int findPivotColumn(Matrix mat) {
    int numCols = mat.getNumCols();
    int numRows = mat.getNumRows();
    for (int cI = 0; cI < numCols; cI++ ) {
      for (int rI = 0; rI < numRows; rI++ ) {
        if (mat.get(rI, cI) != 0.0)
          return cI;
      }
    }
    /*
     * No non-zero element found.
     */
    return -1;
  }
  
  /**
   * Returns the index of the row in the specified {@link Matrix} the has the largest absolute
   * value in column 0. It is an error if no row has a non-zero value in column 0.
   * 
   * @param mat The {@link Matrix} to be examined.
   * @return the index of the row in the specified {@link Matrix} the has the largest value in
   *         column 0.
   */
  public static int findPivotRow(Matrix mat) {
    int numRows = mat.getNumRows();
    double maxValue = Math.abs(mat.get(0, 0));
    int maxRowIndex = 0;
    for (int rI = 1; rI < numRows; rI++ ) {
      double nextVal = Math.abs(mat.get(rI, 0));
      if (nextVal > maxValue) {
        maxValue = nextVal;
        maxRowIndex = rI;
      }
    }
    if (maxValue == 0.0)
      throw new RuntimeException("No non-zero pivot value");
    return maxRowIndex;
  }
  
  /**
   * Takes a matrix that is already in echelon form and reduces it to be in reduced echelon
   * form.
   * <p>
   * Uses primitive row operators to put the specified {@link Matrix} into reduced echelon
   * form.
   * <p>
   * The primitive row operations are:
   * <ol>
   * <li>{@link Matrix#addRowsWithMult(int, double, int)}
   * <li>{@link Matrix#scaleRow(int, double)}
   * <li>{@link Matrix#swapRows(int, int)}
   * </ol>
   * <p>
   * A rectangular matrix is in echelon form (or row echelon form) if it has the following
   * three properties:
   * <ol>
   * <li>All nonzero rows are above any rows of all zeros.
   * <li>Each leading entry of a row is in a column to the right of the leading entry of the
   * row above it.
   * <li>All entries in a column below a leading entry are zeros.
   * </ol>
   * <p>
   * If a matrix in echelon form satisfies the following additional conditions, then it is in
   * reduced echelon form (or reduced row echelon form):
   * <ol>
   * <li>The leading entry in each nonzero row is 1.
   * <li>Each leading 1 is the only nonzero entry in its column.
   * </ol>
   * <p>
   * <b>Note:</b> The algorithm works from the bottom up
   * 
   * @param mat the {@link Matrix} to be transformed.
   */
  public static void reduceEchelonForm(Matrix mat) {
    int numRows = mat.getNumRows();
    for (int rI = numRows - 1; rI >= 0; rI-- ) {
      int leadingEntryColIndex = mat.getLeadingEntryCol(rI);
      if (leadingEntryColIndex == -1) {
        continue; // row is all zeros
      }
      mat.normalize(rI);
      if (rI == 0) {
        continue; // at the top of the matrix
      }
      for (int reduceRI = 0; reduceRI < rI; reduceRI++ ) {
        double valueToZeroOut = mat.get(reduceRI, leadingEntryColIndex);
        if (valueToZeroOut == 0.0) {
          continue; // already zero
        }
        double multiplier = -valueToZeroOut; // really -1.0 * (valueToZeroOut / 1.0)
        mat.addRowsWithMult(rI, multiplier, reduceRI);
      }
    }
  }
  
  /**
   * Uses primitive row operators to put the specified {@link Matrix} into echelon form.
   * <p>
   * The primitive row operations are:
   * <ol>
   * <li>{@link Matrix#addRowsWithMult(int, double, int)}
   * <li>{@link Matrix#scaleRow(int, double)}
   * <li>{@link Matrix#swapRows(int, int)}
   * </ol>
   * <p>
   * A rectangular matrix is in echelon form (or row echelon form) if it has the following
   * three properties:
   * <ol>
   * <li>All nonzero rows are above any rows of all zeros.
   * <li>Each leading entry of a row is in a column to the right of the leading entry of the
   * row above it.
   * <li>All entries in a column below a leading entry are zeros.
   * </ol>
   * <p>
   * <b>Note:</b> This method uses a recursive algorithm the that starts by transforming the
   * largest possible sub-matrix and the recursing on the next largest sub-matrix.
   * 
   * @param mat the {@link Matrix} to be transformed.
   */
  public static void toEchelonForm(Matrix mat) {
    Out.trace(false, "Mat at entry %n%s%n", mat);
    int numRows = mat.getNumRows();
    int numCols = mat.getNumCols();
    if (numRows <= 1)
      return; // there is nothing left to do
    int pivotCol = findPivotColumn(mat);
    if (pivotCol == -1)
      return; // there are no non-zero entries left
    if (pivotCol > 0) {
      /*
       * some left-most columns are all zeros so reduce the matrix to ignore them.
       */
      mat = mat.getSubMatrix(0, numRows, pivotCol, numCols - pivotCol);
      numCols = mat.getNumCols(); // in the sub-matrix, not needed but this keeps it accurate
      pivotCol = 0; // in the sub-matrix, not needed but this keeps it accurate
    }
    /*
     * the pivot column is now column zero and it must have a non-zero entry.
     */
    int pivotRow = findPivotRow(mat);
    mat.swapRows(0, pivotRow);
    pivotRow = 0; // not needed, but this keeps it accurate
    /*
     * The non-zero pivot element is now at [0,0]. So zero all the elements below it.
     */
    double pivotValue = mat.get(0, 0);
    for (int rI = 1; rI < numRows; rI++ ) {
      double leadingValue = mat.get(rI, 0);
      if (leadingValue == 0) {
        continue; // there is nothing to do
      }
      double multFactor = -1 * (leadingValue / pivotValue);
      /*
       * Make the leading value 0 by subtracting the appropriate multiple of the pivot row from
       * the current row.
       */
      mat.addRowsWithMult(0, multFactor, rI);
    }
    /*
     * The pivot column is now all zeros except for the pivot value [0,0]. So recurse to
     * complete the process on the sub matrix below the pivot row (0) and the column to the
     * left of the pivot column (0).
     */
    Out.trace(false, "Mat after zero reduction %n%s%n", mat);
    mat = mat.getSubMatrix(1, numRows - 1, 1, numCols - 1);
    toEchelonForm(mat);
  }
  
  /**
   * Uses primitive row operators to put the specified {@link Matrix} into reduced echelon
   * form.
   * <p>
   * The primitive row operations are:
   * <ol>
   * <li>{@link Matrix#addRowsWithMult(int, double, int)}
   * <li>{@link Matrix#scaleRow(int, double)}
   * <li>{@link Matrix#swapRows(int, int)}
   * </ol>
   * <p>
   * A rectangular matrix is in echelon form (or row echelon form) if it has the following
   * three properties:
   * <ol>
   * <li>All nonzero rows are above any rows of all zeros.
   * <li>Each leading entry of a row is in a column to the right of the leading entry of the
   * row above it.
   * <li>All entries in a column below a leading entry are zeros.
   * </ol>
   * <p>
   * If a matrix in echelon form satisfies the following additional conditions, then it is in
   * reduced echelon form (or reduced row echelon form):
   * <ol>
   * <li>The leading entry in each nonzero row is 1.
   * <li>Each leading 1 is the only nonzero entry in its column.
   * </ol>
   * 
   * @param mat the {@link Matrix} to be transformed.
   */
  public static void toReducedEchelonForm(Matrix mat) {
    toEchelonForm(mat);
    reduceEchelonForm(mat);
  }
}
