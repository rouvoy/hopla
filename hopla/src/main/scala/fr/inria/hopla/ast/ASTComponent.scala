package fr.inria.hopla.ast

/**
 * Abstract Syntax Tree which represents a scala project<br>
 * Each Compilation Unit, represented by an <code>ASTFile</code>, can be generated
 *
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
trait ASTComponent {
  val ast : AST
}
