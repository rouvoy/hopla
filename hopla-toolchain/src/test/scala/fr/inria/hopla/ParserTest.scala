package fr.inria.hopla

import org.junit._
import Assert._

/**
 * Created by JIN Benli on 29/04/14.
 */
@Test
class ParserTest {
  @Test
  def parser() {
    val parser = new Parser("personal.xsd")
    parser.parse()

    assertTrue(!parser.elementMap.isEmpty)
    parser.elementMap.foreach {
      case (key, value) => println(key)
    }

    parser.attributeMap.foreach {
      case (key, value) => println(key)
    }

    parser.complexTypesMap.foreach {
      case (key, value) => println(key)
    }
  }
}
