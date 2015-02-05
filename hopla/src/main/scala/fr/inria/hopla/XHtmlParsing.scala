import fr.inria.hopla.ast.{ASTImpl, ASTComponentImpl, AST, ASTComponent}
import fr.inria.hopla.generator.ASTGenerator
import fr.inria.hopla.parser.XSDParser

import scala.io.Source
import scala.xml.pull.XMLEventReader

/**
 * Run the application<br>
 * Parse xhtml1-strict.xsd and
 *
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
object XHtmlParsing extends App {
  override def main (args: Array[String]) {
    val xml = new XMLEventReader(Source.fromFile("hopla/src/main/resources/xhtml_sample.xsd"))
    val parser = new XSDParser with ASTComponentImpl
    val ast = parser.parse(xml).asInstanceOf[ASTImpl]
    new ASTGenerator(ast).generate("target/generated/src/main/scala/fr/inria/hopla/xhtml", "fr.inria.hoplaxhtml.xhtml")
  }
}
