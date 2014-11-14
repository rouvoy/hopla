package fr.inria.hopla.parser.processors

import fr.inria.hopla.ast.{ASTField, ASTFile}

import scala.xml.pull.EvElemStart

/**
 * Abstract Processor able to add fields into parent ASTFile
 *
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
class ASTFieldProcessor extends XSDProcessor {
  var parent: XSDProcessor = null

  /**
   * Create a new ASTTrait from "base" attribute<br>
   * Let the parent implements this new ASTTrait
   * @param event the event of the marker to process
   * @param parent the parent processor.
   * @return <code>this</code>
   */
  def process(event: EvElemStart, parent: XSDProcessor): XSDProcessor = {
    this.parent = parent
    this
  }

  /**
   * Add the child as a field of <code>parentFile</code>
   * @param childEvent the event of the child marker
   * @param childFile the file created by the child processor
   */
  def processChild(childEvent: EvElemStart, childFile: ASTFile): Unit = {
    parent.getAstFile.withField(new ASTField(childFile.name, childFile.name))
  }

  override def getAstFile(): ASTFile = parent.getAstFile
}
