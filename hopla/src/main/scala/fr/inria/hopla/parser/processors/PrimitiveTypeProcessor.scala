package fr.inria.hopla.parser.processors

import fr.inria.hopla.ast.{ASTField, ASTFile}

import scala.xml.pull.EvElemStart

/**
 * Special parser for primitive types.
 * A primitive type is cast to scala String in order to be used as a simple String by user.
 *
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
class PrimitiveTypeProcessor extends ASTFieldProcessor {
  /**
   * Create a new field from "base" attribute, and add it to parent<br>
   * @param event the event of the marker to process
   * @param parent the parent processor
   * @return <code>this</code>
   */
  override def process(event: EvElemStart, parent: XSDProcessor): XSDProcessor = {
    parent.getAstFile.withField(new ASTField(markerNameField(event), "String"))
    this
  }
}
