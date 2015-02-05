package fr.inria.hopla.parser.processors

import fr.inria.hopla.ast.ASTComponentImpl
import org.scalatest.{Matchers, FlatSpec}
import fr.inria.hopla.parser.XSDParser

import scala.io.Source
import scala.xml.pull.XMLEventReader

/**
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
class SchemaProcessorTest  extends FlatSpec with Matchers with ASTComponentMock {
  "SchemaProcessor" should "create a trait when it process a group" in {
    val xsd = new XMLEventReader(Source.fromFile("hopla/src/test/resources/xsd/schema.xsd"))
    val parser = new XSDParser with ASTComponentMock
    val ast = parser.parse(xsd)

    assert(ast.contains("schema"))
  }
}
