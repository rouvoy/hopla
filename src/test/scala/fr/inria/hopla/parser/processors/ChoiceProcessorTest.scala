package fr.inria.hopla.parser.processors

import fr.inria.hopla.ast.{ASTTrait, AST}
import org.scalatest.{Matchers, FlatSpec}
import fr.inria.hopla.parser.XSDParser

import scala.io.Source
import scala.xml.pull.XMLEventReader

/**
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
class ChoiceProcessorTest extends FlatSpec with Matchers {
  "ChoiceProcessor" should "create a trait which inherits a class" in {
    val xsd = new XMLEventReader(Source.fromFile("src/test/resources/xsd/choice.xsd"))
    val ast = new AST()
    new XSDParser(ast, xsd).parse()

    assert( ast.files.contains("group") &&
            ast.files.contains("element"))

    val subClass = ast.files.get("element").get
    val superTrait = ast.files.get("group").get.asInstanceOf[ASTTrait]
    assert(subClass.getTraits contains superTrait)
  }
}
