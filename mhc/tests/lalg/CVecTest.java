package mhc.tests.lalg;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mhc.lalg.CVec;
import mhc.lalg.Vector;

/**
 * Class: ConcreteVectorTest
 */
class CVecTest {
  
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
  }
  
  /**
   * @throws java.lang.Exception
   */
  @AfterEach
  void tearDown() throws Exception {
  }
  
  /**
   * Test method for {@link mhc.lalg.CVec#ConcreteVector(double[])}.
   */
  @Test
  final void testConcreteVectorDoubleArray() {
    Vector vec = CVec.vec(0.0, 1.1, 2.2, 3.3, 4.4, 5.5);
    assertEquals(3.3, vec.get(3));
  }
  
  /**
   * Test method for {@link mhc.lalg.CVec#ConcreteVector(int)}.
   */
  @Test
  final void testConcreteVectorInt() {
    CVec vec = new CVec(6);
    assertEquals(6, vec.dimension());
  }
  
  /**
   * Test method for {@link mhc.lalg.CVec#get(int)}.
   */
  @Test
  final void testGetSet() {
    Vector vec = CVec.vec(0.0, 1.1, 2.2, 3.3, 4.4, 5.5);
    assertEquals(3.3, vec.get(3));
    vec.set(3, 33.3);
    assertEquals(33.3, vec.get(3));
  }
}
