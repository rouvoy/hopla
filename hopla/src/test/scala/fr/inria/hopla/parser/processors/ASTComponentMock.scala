package fr.inria.hopla.parser.processors

import fr.inria.hopla.ast.{ASTImpl, ASTComponent}

/**
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
trait ASTComponentMock extends ASTComponent {
  override val ast = new ASTImpl
}
