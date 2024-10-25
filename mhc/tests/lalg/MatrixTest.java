package mhc.tests.lalg;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
 * Class: MatrixTest
 */
class MatrixTest {
  
  private static boolean BaseDataStructuresShown = false;
  
  private CMat tMat;
  
  private Matrix tMatSub;
  
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
    tMatSub = tMat.getSubMatrix(2, 3, 1, 4);
    if ( !BaseDataStructuresShown) {
      BaseDataStructuresShown = true;
      Out.trace(true, "Base matrix:%n%s%n", tMat);
      Out.trace(true, "Sub matrix:%n%s%n", tMatSub);
    }
  }
  
  /**
   * @throws java.lang.Exception
   */
  @AfterEach
  void tearDown() throws Exception {
  }
  
  /**
   * Test method for {@link mhc.lalg.Matrix#addRowsWithMult(int, double, int)}.
   */
  @Test
  final void testAddRowsWithMult() {
    tMatSub.addRowsWithMult(1, -1.0, 2);
    Out.trace(false, "Base matrix after add with mult:%n%s%n", tMat);
    assertEquals(10.0, tMat.get(4, 2));
    assertEquals(30.0, tMat.get(3, 0));
  }
  
  /**
   * Test method for {@link mhc.lalg.Matrix#getSubCol(int, int, int)}.
   */
  @Test
  final void testGetSubCol() {
    Vector vec = tMatSub.getSubCol(1, 0, 2);
    assertEquals(2, vec.dimension());
    assertEquals(32.0, vec.get(1));
  }
  
  /**
   * Test method for {@link mhc.lalg.Matrix#getSubMatrix(int, int, int, int)}.
   */
  @Test
  final void testGetSubMatrix() {
    assertEquals(3, tMatSub.getNumRows());
    assertEquals(42.0, tMatSub.get(2, 1));
  }
  
  /**
   * Test method for {@link mhc.lalg.Matrix#getSubRow(int, int, int)}.
   */
  @Test
  final void testGetSubRow() {
    Vector vec = tMatSub.getSubRow(1, 0, 2);
    assertEquals(2, vec.dimension());
    assertEquals(32.0, vec.get(1));
  }
  
  /**
   * Test method for {@link Matrix#scaleRow(int, double)}.
   */
  @Test
  public void testScaleRow() {
    tMatSub.scaleRow(1, 3.0);
    assertEquals(96.0, tMat.get(3, 2));
    assertEquals(30.0, tMat.get(3, 0));
  }
  
  /**
   * Test method for {@link mhc.lalg.Matrix#setSubCol(int, int, mhc.lalg.Vector)}.
   */
  @Test
  final void testSetSubCol() {
    Vector vec = tMatSub.getSubCol(1, 0, 2);
    assertEquals(2, vec.dimension());
    assertEquals(32.0, vec.get(1));
    vec.set(1, 99.0);
    tMatSub.setSubRow(1, 0, vec);
    assertEquals(99.0, tMatSub.get(1, 1));
  }
  
  /**
   * Test method for {@link mhc.lalg.Matrix#setSubRow(int, int, mhc.lalg.Vector)}.
   */
  @Test
  final void testSetSubRow() {
    Vector vec = tMatSub.getSubRow(1, 0, 2);
    assertEquals(2, vec.dimension());
    assertEquals(32.0, vec.get(1));
    vec.set(1, 99.0);
    tMatSub.setSubRow(1, 0, vec);
    assertEquals(99.0, tMatSub.get(1, 1));
  }
  
  /**
   * Test method for {@link mhc.lalg.Matrix#swapRows(int, int)}.
   */
  @Test
  final void testSwapRows() {
    tMatSub.swapRows(0, 2);
    assertEquals(42.0, tMat.get(2, 2));
    assertEquals(22.0, tMat.get(4, 2));
    assertEquals(25.0, tMat.get(2, 5));
    assertEquals(40.0, tMat.get(4, 0));
  }
}
