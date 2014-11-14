import fr.inria.hopla.ast.AST
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
    val xml = new XMLEventReader(Source.fromURL("http://www.w3.org/2002/08/xhtml/xhtml1-strict.xsd"))
    val ast = new AST()
    new XSDParser(ast, xml).parse()
    ast simplify()
    // ast generate("./results/xhtml/")
  }
}
