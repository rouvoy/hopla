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
    val parser = new Parser("fixml-schema-4-4-20040109rev1/Schema/fixml-allocation-base-4-4.xsd")
    parser.parse()

    assertTrue(!parser.elementMap.isEmpty)
    println("- Elements: ")
    parser.elementMap.foreach {
      case (key, value) => println(key)
    }

    println("\n- Attributes: ")
    parser.attributeMap.foreach {
      case (key, value) => println(key)
    }

    println("\n- ComplexTypes: ")
    parser.complexTypesMap.foreach {
      case (key, value) => println(key)
    }

    println("\n- SimpleTypes:")
    parser.simpleTypeMap.foreach {
      case (key, value) => println(key)
    }

    println("\n- Group:")
    parser.groupMap.foreach {
      case(key, value) => println(key)
    }

    println("\n- Attributes Group:")
    parser.attributesGroupMap.foreach {
      case(key, value) => println(key)
    }
  }
}
