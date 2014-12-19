package fr.inria.hopla.parser.processors

import fr.inria.hopla.parser.XSDParser
import org.scalatest.{Matchers, FlatSpec}

import scala.io.Source
import scala.xml.pull.XMLEventReader

/**
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
class PrimitiveTypeProcessorTest extends FlatSpec with Matchers {

  "PrimitiveTypeProcessorTest" should "add a field to parent class" in {
    val xsd = new XMLEventReader(Source.fromFile("src/test/resources/xsd/attribute.xsd"))
    val parser = new XSDParser with ASTComponentMock

    val ast = parser.parse(xsd)
    val elementTrait = ast.get("element").get
    val fields = elementTrait.getFields
    assert(fields.nonEmpty && fields(0).name.equals("attribute") && fields(0).fieldType.equals("String"))
  }
}
