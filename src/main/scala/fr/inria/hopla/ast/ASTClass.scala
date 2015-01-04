package fr.inria.hopla.ast

import fr.inria.hopla.generator.{ASTFileGenerator, ASTClassGenerator}

/**
 * Representation of a scala class in AST
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
class ASTClass(name: String) extends ASTFile(name) {
  override def accept(ast: AST): ASTFileGenerator = new ASTClassGenerator(ast, this)
}
