package fr.inria.hopla.parser.processors

import fr.inria.hopla.ast.ASTComponentImpl
import org.scalatest.{Matchers, FlatSpec}
import fr.inria.hopla.parser.XSDParser

import scala.io.Source
import scala.xml.pull.XMLEventReader

/**
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
class ComplexTypeProcessorTest extends FlatSpec with Matchers with ASTComponentMock {
  "ComplexTypeProcessor" should "process a named marker by creating a trait" in {
    val xsd = new XMLEventReader(Source.fromFile("hopla/src/test/resources/xsd/namedComplexType.xsd"))
    val parser = new XSDParser with ASTComponentMock
    val ast = parser.parse(xsd)

    assert(ast.contains("complexType"))
  }
}
