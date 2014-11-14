package fr.inria.hopla.parser.processors

import fr.inria.hopla.ast.AST
import org.scalatest.{Matchers, FlatSpec}
import fr.inria.hopla.parser.XSDParser

import scala.io.Source
import scala.xml.pull.{XMLEventReader}

/**
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
class ASTClassProcessorTest extends FlatSpec with Matchers {

  "ASTClassProcessor" should "create a class when it process an element" in {
    val xsd = new XMLEventReader(Source.fromFile("src/test/resources/xsd/simpleElement.xsd"))
    val ast = new AST()
    new XSDParser(ast, xsd).parse()

    assert(ast.files.contains("element"))
  }

  it should "create a class when it process an attributeGroup" in {
    val xsd = new XMLEventReader(Source.fromFile("src/test/resources/xsd/simpleAttributeGroup.xsd"))
    val ast = new AST()
    new XSDParser(ast, xsd).parse()

    assert(ast.files.contains("attributeGroup"))
  }

  it should "create a class which inherits another" in {
    val xsd = new XMLEventReader(Source.fromFile("src/test/resources/xsd/inheritedElement.xsd"))
    val ast = new AST()
    new XSDParser(ast, xsd).parse()

    assert( ast.files.contains("subClass") &&
            ast.files.contains("superClass"))

    val subClass = ast.files.get("subClass").get
    val superClass = ast.files.get("superClass").get
    assert(subClass.getInheritsFrom equals Some(superClass))
  }
}
