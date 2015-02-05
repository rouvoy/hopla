package fr.inria.hopla.ast

/**
 * Real implementation of ASTComponent
 * @see{ASTComponent}
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
trait ASTComponentImpl extends ASTComponent {

  override val ast = new ASTImpl

}
