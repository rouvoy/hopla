package fr.inria.hopla.parser.processors

import fr.inria.hopla.ast.{ASTTrait, AST}
import org.scalatest.{Matchers, FlatSpec}
import fr.inria.hopla.parser.XSDParser

import scala.io.Source
import scala.xml.pull.XMLEventReader

/**
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
class ASTTraitProcessorTest extends FlatSpec with Matchers {

  "ASTTraitProcessor" should "create a trait when it process a group" in {
    val xsd = new XMLEventReader(Source.fromFile("src/test/resources/xsd/simpleGroup.xsd"))
    val ast = new AST()
    new XSDParser(ast, xsd).parse()

    assert(ast.files.contains("group"))
  }

  it should "create a trait which inherits another" in {
    val xsd = new XMLEventReader(Source.fromFile("src/test/resources/xsd/inheritedGroup.xsd"))
    val ast = new AST()
    new XSDParser(ast, xsd).parse()

    assert( ast.files.contains("subTrait") &&
      ast.files.contains("superTrait"))

    val subClass = ast.files.get("subTrait").get
    val superClass = ast.files.get("superTrait").get.asInstanceOf[ASTTrait]
    assert(subClass.getTraits contains superClass)
  }
}
