package mhc.tests.lalg;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mhc.lalg.CMat;
import mhc.lalg.CVec;
import mhc.lalg.Mat;
import mhc.lalg.Matrix;
import mhc.lalg.util.Out;

/**
 * Class: MatTest
 */
class MatTest {
  
  public static boolean BaseDataStructuresShown = false;
  
  public CMat tMat;
  
  public Matrix tMatSub;
  
  /**
   * Used for testing {@link Mat#toEchelonForm(Matrix)}, and
   * {@link Mat#toReducedEchelonForm(Matrix)}.
   */
  public CMat exp3Mat;
  
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
    exp3Mat = CMat.mat( //
            CVec.vec(0, 3, -6, 6, 4, -5), //
            CVec.vec(3, -9, 12, -9, 6, 15), //
            CVec.vec(3, -7, 8, -5, 8, 9) //
    );
    tMatSub = tMat.getSubMatrix(2, 3, 1, 4);
    if ( !BaseDataStructuresShown) {
      BaseDataStructuresShown = true;
      Out.trace(false, "Base matrix:%n%s%n", tMat);
      Out.trace(false, "Sub matrix:%n%s%n", tMatSub);
    }
  }
  
  /**
   * @throws java.lang.Exception
   */
  @AfterEach
  void tearDown() throws Exception {
  }
  
  /**
   * Test method for {@link mhc.lalg.Mat#toEchelonForm(Matrix)}.
   */
  // @Test
  final void testToEchelonForm() {
    Matrix mat = Mat.copy(exp3Mat);
    Out.trace(true, "Example 3 matrix befor transformation to echelon form:%n%s%n", exp3Mat);
    Mat.toEchelonForm(mat);
    Out.trace(true, "Example 3 matrix after transformation to echelon form:%n%s%n", mat);
  }
  
  /**
   * Test method for {@link mhc.lalg.Mat#toReducedEchelonForm(Matrix)}.
   */
  @Test
  final void testToReducedEchelonForm() {
    Matrix mat = Mat.copy(exp3Mat);
    Out.trace(true, "Example 3 matrix befor transformation to echelon form:%n%s%n", exp3Mat);
    Mat.toEchelonForm(mat);
    Out.trace(true, "Example 3 matrix after transformation to echelon form:%n%s%n", mat);
    Mat.reduceEchelonForm(mat);
    Out.trace(true, "Example 3 matrix after reduction:%n%s%n", mat);
    assertEquals( -24.0, mat.get(0, 5), 0.000001);
    assertEquals( -7.0, mat.get(1, 5), 0.000001);
    assertEquals(4.0, mat.get(2, 5), 0.000001);
  }
}
