package fr.inria.hopla.parser.processors

import fr.inria.hopla.ast._

import scala.xml.pull.EvElemStart

/**
 * Specific processor to process <i>extension</i> markers<br>
 *
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
class ExtensionProcessor(ast: AST) extends ASTFieldProcessor {

  /**
   * Create a new ASTTrait from "base" attribute<br>
   * Let the parent implement this new ASTTrait
   * @param event the event of the marker to process
   * @param parent the parent processor
   * @return <code>this</code>
   */
  override def process(event: EvElemStart, parent: XSDProcessor): XSDProcessor = {
    super.process(event, parent)
    val fieldName = markerNameField(event)
    parent.getAstFile.withField(new ASTField(fieldName, fieldName))
    this
  }
}
