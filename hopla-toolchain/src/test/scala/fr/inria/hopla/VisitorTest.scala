package fr.inria.hopla

import org.junit._
import Assert._
/**
* Created by JIN Benli on 26/03/14.
*
* Visitor Test
*/

@Test
class VisitorTest {
  @Test
  def lanceTest() {
    val filename1 = "address.xsd"
    leveVisitorTest(filename1)
    println()
    val filename2 = "fixml-schema-4-4-20040109rev1/Schema/fixml-allocation-base-4-4.xsd"
    leveVisitorTest(filename2)
  }

  def leveVisitorTest(filename: String) {
    val lv = new LevelVisitor(filename)
    assertTrue(lv != null)
    lv.visitByLevel()
  }
}
