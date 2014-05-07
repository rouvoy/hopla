package fr.inria.hopla

/**
 * Created by JIN Benli on 07/05/14.
 */

import org.junit._
import Assert._

class ScGeneratorTest {
  @Test
  def lanceTest() {
    val scg = new ScGenerator
    scg.export("address.xsd")
  }

  @Test
  def lanceTestFixml() {
    val scg = new ScGenerator
    scg.export("hopla-toolchain/fixml-schema-4-4-20040109rev1/Schema/fixml-allocation-base-4-4.xsd")
  }
}
