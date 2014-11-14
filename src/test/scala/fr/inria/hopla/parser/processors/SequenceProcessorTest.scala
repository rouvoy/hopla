package fr.inria.hopla.parser.processors

import fr.inria.hopla.ast.AST
import org.scalatest.{Matchers, FlatSpec}
import fr.inria.hopla.parser.XSDParser

import scala.io.Source
import scala.xml.pull.XMLEventReader

/**
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
class SequenceProcessorTest extends FlatSpec with Matchers {

  "SequenceProcessorTest" should "add fields into element" in {
    val xsd = new XMLEventReader(Source.fromFile("src/test/resources/xsd/sequence.xsd"))
    val ast = new AST()
    new XSDParser(ast, xsd).parse()

    assert(ast.files.contains("element"))

    val elementTrait = ast.files.get("element").get
    val fields = elementTrait.getFields
    assert(fields.nonEmpty && fields(0).getName.equals("attr1") && fields(1).getName.equals("attr2"))
  }
}
