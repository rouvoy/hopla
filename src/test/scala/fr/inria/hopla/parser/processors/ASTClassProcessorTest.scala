package fr.inria.hopla.parser.processors

import org.scalatest.{Matchers, FlatSpec}
import fr.inria.hopla.parser.XSDParser

import scala.io.Source
import scala.xml.pull.{XMLEventReader}

/**
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
class ASTClassProcessorTest extends FlatSpec with Matchers {

  "ASTClassProcessor" should "create a class when it process an element" in {
    val xsd = new XMLEventReader(Source.fromFile("src/test/resources/xsd/simpleElement.xsd"))
    val parser = new XSDParser with ASTComponentMock

    val ast = parser.parse(xsd)
    assert(ast.contains("element"))
  }

  it should "create a class when it process an attributeGroup" in {
    val xsd = new XMLEventReader(Source.fromFile("src/test/resources/xsd/simpleAttributeGroup.xsd"))
    val parser = new XSDParser with ASTComponentMock
    val ast = parser.parse(xsd)
    assert(ast.contains("attributeGroup"))
  }

  it should "create a class which inherits another" in {
    val xsd = new XMLEventReader(Source.fromFile("src/test/resources/xsd/inheritedElement.xsd"))
    val parser = new XSDParser with ASTComponentMock
    val ast = parser.parse(xsd)

    assert( ast.contains("subClass") &&
            ast.contains("superClass"))

    val subClass = ast.get("subClass").get
    val superClass = ast.get("superClass").get
    assert(subClass.getInheritsFrom equals Some(superClass))
  }
}
