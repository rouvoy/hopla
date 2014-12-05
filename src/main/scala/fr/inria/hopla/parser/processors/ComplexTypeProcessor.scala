package fr.inria.hopla.parser.processors

import fr.inria.hopla.ast.{AST, ASTComponent}

import scala.xml.pull.EvElemStart

/**
 * Specific processor to process <i>complexType</i> markers<br>
 *
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
class ComplexTypeProcessor(ast: AST) extends ASTTraitProcessor(ast) {

  /**
   * Skip markers without name, and create a Trait when there is a name
   * @param event the event of the marker to process
   * @param parent the parent processor.
   * @return <code>parent</code> for markers without name, <code>this</code> otherwise
   */
  override def process(event: EvElemStart, parent: XSDProcessor): XSDProcessor = {
    try {
      super.process(event, parent)
    } catch {
      case error : Throwable => parent // skip the complexType and return the parent for the next marker
    }
  }
}
