package fr.inria.hopla.parser.processors

import fr.inria.hopla.ast.{AST, ASTComponent, ASTFile, ASTTrait}

import scala.xml.pull.EvElemStart


/**
 * Specific processor to process <i>group</i> markers<br>
 * Create a new ASTTrait in <code>ast</code> (if it doesn't exist)
 *
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
class ASTTraitProcessor(ast : AST) extends ASTFileProcessor {
  /**
   * The created ASTTrait
   */
  protected var astTrait : ASTTrait = null

  /**
   * Process the marker by creating a new ASTTrait in the AST
   * @param event the event of the marker to process
   * @param parent the parent processor
   * @return <code>this</code>
   */
  override def process(event: EvElemStart, parent: XSDProcessor): XSDProcessor = {
    astTrait = ast.getOrAddFile(new ASTTrait(markerNameField(event)))
    parent.processChild(event, astTrait)
    this
  }

  /**
   * Process child marker by implementing <code>childFile</code> from <code>this.getASTFile()</code>
   * @param childEvent the event of the child marker
   * @param childFile the file created by the child processor
   */
  override def processChild(childEvent: EvElemStart, childFile : ASTFile): Unit = {
    childFile.withTrait(astTrait)
  }

  override def getAstFile(): ASTFile = astTrait
}
