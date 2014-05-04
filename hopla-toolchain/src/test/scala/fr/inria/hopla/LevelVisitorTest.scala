package fr.inria.hopla

import org.junit._
import Assert._
/**
* Created by JIN Benli on 26/03/14.
*
* Visitor Test
*/

@Test
class LevelVisitorTest {
  @Test
  def leveVisitorTest {
    val lv = new LevelVisitor("address.xsd")
    assertTrue(lv != null)
    lv.visitByLevel()
  }
}
