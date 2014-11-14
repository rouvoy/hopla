package fr.inria.hopla.parser.processors

import fr.inria.hopla.ast.{ASTFile, ASTClass, AST}

import scala.xml.pull.EvElemStart

/**
 * Specific processor to process <i>element</i> and <i>attributeGroup</i> markers<br>
 * Create a new ASTClass in <code>ast</code> (if it doesn't exist)
 *
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
class ASTClassProcessor(ast : AST) extends ASTFileProcessor {
  private var astClass : ASTClass = null

  /**
   * Process the marker by creating a new ASTClass in the AST
   * @param event the event of the marker to process
   * @param parent the parent processor.
   * @return <code>this</code>
   */
  override def process(event: EvElemStart, parent: XSDProcessor): XSDProcessor = {
    astClass = ast.getOrAddFile(new ASTClass(markerNameField(event)))
    parent.processChild(event, astClass)
    this
  }

  /**
   * Process child marker by inheriting <code>childFile</code> from <code>this.getASTFile()</code>
   * @param childEvent the event of the child marker
   * @param childFile the file created by the child processor
   */
  override def processChild(childEvent: EvElemStart, childFile : ASTFile): Unit = {
    childFile.inheritsFrom(astClass)
  }

  override def getAstFile(): ASTFile = astClass
}
