package fr.inria.hopla.parser.processors

import fr.inria.hopla.ast.{ASTTrait, ASTComponentImpl}
import org.scalatest.{Matchers, FlatSpec}
import fr.inria.hopla.parser.XSDParser

import scala.io.Source
import scala.xml.pull.XMLEventReader

/**
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
class ChoiceProcessorTest extends FlatSpec with Matchers with ASTComponentMock {
  "ChoiceProcessor" should "create a trait which inherits a class" in {
    val xsd = new XMLEventReader(Source.fromFile("src/test/resources/xsd/choice.xsd"))
    val parser = new XSDParser with ASTComponentMock
    val ast = parser.parse(xsd)

    assert( ast.contains("group") &&
            ast.contains("element"))

    val subClass = ast.get("element").get
    val superTrait = ast.get("group").get.asInstanceOf[ASTTrait]
    assert(subClass.getTraits contains superTrait)
  }
}
