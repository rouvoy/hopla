package fr.inria.hopla.parser.processors

import fr.inria.hopla.ast.{ASTTrait, AST}

import scala.xml.pull.EvElemStart

/**
 * Specific processor to process <i>schema</i> markers<br>
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
class SchemaProcessor(ast: AST) extends ASTTraitProcessor(ast) {

  /**
   * Create a new ASTTrait named "schema"
   * @param event the event of the marker to process
   * @param parent the parent processor.
   * @return <code>this</code>
   */
  override def process(event: EvElemStart, parent: XSDProcessor = null): XSDProcessor = {
    astTrait = ast.getOrAddFile(new ASTTrait("schema"))
    this
  }
}
