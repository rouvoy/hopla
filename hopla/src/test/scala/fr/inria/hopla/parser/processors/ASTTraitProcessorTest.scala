package fr.inria.hopla.parser.processors

import fr.inria.hopla.ast.{ASTTrait, ASTComponentImpl}
import org.scalatest.{Matchers, FlatSpec}
import fr.inria.hopla.parser.XSDParser

import scala.io.Source
import scala.xml.pull.XMLEventReader

/**
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
class ASTTraitProcessorTest extends FlatSpec with Matchers with ASTComponentMock {

  "ASTTraitProcessor" should "create a trait when it process a group" in {
    val xsd = new XMLEventReader(Source.fromFile("hopla/src/test/resources/xsd/simpleGroup.xsd"))
    val parser = new XSDParser with ASTComponentMock
    val ast = parser.parse(xsd)

    assert(ast.contains("group"))
  }

  it should "create a trait which inherits another" in {
    val xsd = new XMLEventReader(Source.fromFile("hopla/src/test/resources/xsd/inheritedGroup.xsd"))
    val parser = new XSDParser with ASTComponentMock
    val ast = parser.parse(xsd)

    assert( ast.contains("subTrait") &&
      ast.contains("superTrait"))

    val subClass = ast.get("subTrait").get
    val superClass = ast.get("superTrait").get.asInstanceOf[ASTTrait]
    assert(subClass.getTraits contains superClass)
  }
}
