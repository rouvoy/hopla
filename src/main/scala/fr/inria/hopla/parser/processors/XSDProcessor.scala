package fr.inria.hopla.parser.processors

import fr.inria.hopla.ast.ASTFile

import scala.xml.pull.EvElemStart

/**
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
trait XSDProcessor extends AbstractXSDProcessor {
  /**
   * @return the ASTFile of this Processor
   */
  def getAstFile: ASTFile

  /**
   * Process a child marker<br>
   * This method is useful to process a child, depending on its parent
   * @param childEvent the event of the child marker
   * @param childFile the file created by the child processor
   */
  def processChild(childEvent: EvElemStart, childFile : ASTFile): Unit
}
