package fr.inria.hopla.parser.processors

import fr.inria.hopla.ast.AST
import org.scalatest.{Matchers, FlatSpec}
import fr.inria.hopla.parser.XSDParser

import scala.io.Source
import scala.xml.pull.XMLEventReader

/**
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
class SchemaProcessorTest  extends FlatSpec with Matchers {
  "SchemaProcessor" should "create a trait when it process a group" in {
    val xsd = new XMLEventReader(Source.fromFile("src/test/resources/xsd/schema.xsd"))
    val ast = new AST()
    new XSDParser(ast, xsd).parse()

    assert(ast.files.contains("schema"))
  }
}
