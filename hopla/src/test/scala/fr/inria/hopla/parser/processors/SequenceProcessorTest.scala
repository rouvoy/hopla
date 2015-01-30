package fr.inria.hopla.parser.processors

import fr.inria.hopla.ast.ASTComponentImpl
import org.scalatest.{Matchers, FlatSpec}
import fr.inria.hopla.parser.XSDParser

import scala.io.Source
import scala.xml.pull.XMLEventReader

/**
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
class SequenceProcessorTest extends FlatSpec with Matchers with ASTComponentMock {

  "SequenceProcessorTest" should "add fields into element" in {
    val xsd = new XMLEventReader(Source.fromFile("hopla/src/test/resources/xsd/sequence.xsd"))
    val parser = new XSDParser with ASTComponentMock
    val ast = parser.parse(xsd)

    assert(ast.contains("element"))

    val elementTrait = ast.get("element").get
    val fields = elementTrait.getFields
    assert(fields.nonEmpty && fields(0).name.equals("attr1") && fields(1).name.equals("attr2"))
  }
}
