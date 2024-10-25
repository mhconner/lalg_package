package mhc.tests.lalg;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import mhc.lalg.CMat;
import mhc.lalg.CVec;
import mhc.lalg.Matrix;
import mhc.lalg.util.Out;

/**
 * Class: LinearAlgTest
 */
class LATest {
  
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
}
