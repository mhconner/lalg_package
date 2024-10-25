package mhc.tests.lalg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mhc.lalg.CMat;
import mhc.lalg.CVec;
import mhc.lalg.Matrix;
import mhc.lalg.Vector;
import mhc.lalg.util.Out;

/**
 * Class: CMatTest
 */
class CMatTest {
  
  public CMat tMat = null;
  
  /**
   * @throws java.lang.Exception
   */
  @BeforeAll
  static void setUpBeforeClass() throws Exception {
  }
  
  /**
   * @throws java.lang.Exception
   */
  @AfterAll
  static void tearDownAfterClass() throws Exception {
  }
  
  /**
   * @throws java.lang.Exception
   */
  @BeforeEach
  void setUp() throws Exception {
    Out.setDevelopmentMode(true);
    tMat = CMat.mat( //
            CVec.vec(0, 1, 2, 3, 4, 5), //
            CVec.vec(10, 11, 12, 13, 14, 15),//
            CVec.vec(20, 21, 22, 23, 24, 25),//
            CVec.vec(30, 31, 32, 33, 34, 35),//
            CVec.vec(40, 41, 42, 43, 44, 45),//
            CVec.vec(50, 51, 52, 53, 54, 55)//
    );
  }
  
  /**
   * @throws java.lang.Exception
   */
  @AfterEach
  void tearDown() throws Exception {
  }
  
  /**
   * Test method for most CMat operations
   */
  @Test
  final void testGeneral() {
    testMat(tMat, 0);
    Vector tVec = tMat.getRow(2);
    testVec(tVec, 20, 1);
    Vector tVec2 = tMat.getCol(1);
    testVec(tVec2, 1, 10);
    Matrix tMat2 = tMat.getSubMatrix(2, 3, 3, 2);
    testMat(tMat2, 23);
    tVec = tMat2.getRow(1);
    testVec(tVec, 33, 1);
    tVec2 = tMat2.getCol(1);
    testVec(tVec2, 24, 10);
    assertTrue(tMat.isSquare());
    assertTrue( !tMat2.isSquare());
    tVec.set(1, 99.0);
    Out.trace(true, "%s%n", tMat);
    assertEquals(99.0, tMat.get(3, 4));
    tVec2.set(2, 101.0);
    Out.trace(true, "%s%n", tMat);
    assertEquals(101.0, tMat.get(4, 4));
  }
  
  /**
   * Test method for {@link mhc.lalg.CMat#getSubCol(int, int, int)}.
   */
  @Test
  final void testGetSubCol() {
  }
  
  /**
   * Test method for {@link mhc.lalg.CMat#getSubMatrix(int, int, int, int)}.
   */
  @Test
  final void testGetSubMatrix() {
  }
  
  /**
   * Test method for {@link mhc.lalg.CMat#getSubRow(int, int, int)}.
   */
  @Test
  final void testGetSubRow() {
  }
  
  /**
   * Test the specified {@link Matrix} to see that its values are as expected, the matrix is
   * assume to have values increase by 1 on rows and by 10 on columns.
   * 
   * @param mat
   * @param firstVal this the value of the matrix element at [0,0]
   */
  public void testMat(Matrix mat, double firstVal) {
    Out.trace(true, "%s%n", mat);
    int numRows = mat.getNumRows();
    int numCols = mat.getNumCols();
    for (int rowI = 0; rowI < numRows; rowI++ ) {
      for (int colI = 0; colI < numCols; colI++ ) {
        assertEquals((firstVal + (rowI * 10)) + colI, mat.get(rowI, colI));
      }
    }
  }
  
  /**
   * Test the specifed vector to see that it values are as expected, the vector must have
   * values that increase by factor starting with firstVal.
   * 
   * @param vec
   * @param firstVal this the is value for vec[0].
   * @param factor
   */
  public void testVec(Vector vec, double firstVal, double factor) {
    Out.trace(true, "%s%n", vec);
    for (int i = 0; i < vec.dimension(); i++ ) {
      assertEquals(firstVal + (i * factor), vec.get(i));
    }
  }
}
