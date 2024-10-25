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
import mhc.lalg.Vec;
import mhc.lalg.Vector;
import mhc.lalg.util.Out;

/**
 * Class: VecTest
 */
class VecTest {
  
  public static boolean BaseDataStructuresShown = false;
  
  public CVec vec1 = null;
  
  public CMat tMat = null;
  
  public Matrix tMatSub = null;
  
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
   * Test method for
   * {@link mhc.lalg.Vec#add(mhc.lalg.Vector, mhc.lalg.Vector, mhc.lalg.Vector)}.
   */
  @Test
  final void testAddVec() {
    Vector vec1 = tMatSub.getRow(1);
    Out.trace(true, "Base vec:%n%s%n", vec1);
    Vec.add(vec1, vec1, vec1);
    Out.trace(true, "Base vec doubled:%n%s%n", vec1);
    assertEquals(66.0, tMat.get(3, 3));
  }
  
  /**
   * Test method for {@link mhc.lalg.Vec#dotProduct(mhc.lalg.Vector, mhc.lalg.Vector)}.
   */
  @Test
  final void testDotProduct() {
    Vector vec1 = CVec.vec(1, 2, 3);
    Vector vec2 = CVec.vec(4, 5, 6);
    Out.trace(true, "Vec1:%n%s%n", vec1);
    Out.trace(true, "Vec2:%n%s%n", vec2);
    double dotProd = Vec.dotProduct(vec1, vec2);
    assertEquals(32, dotProd);
  }
  
  /**
   * Test method for {@link mhc.lalg.Vec#mult(double, mhc.lalg.Vector, mhc.lalg.Vector)}.
   */
  @Test
  final void testMultVec() {
    Vector vec1 = tMatSub.getRow(1);
    Out.trace(true, "Base vec:%n%s%n", vec1);
    Vec.mult(2.0, vec1, vec1);
    Out.trace(true, "Base vec times 2:%n%s%n", vec1);
    assertEquals(66.0, tMat.get(3, 3));
  }
}
