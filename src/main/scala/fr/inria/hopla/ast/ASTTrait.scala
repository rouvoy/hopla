package fr.inria.hopla.ast

import fr.inria.hopla.generator.{ASTTraitGenerator, ASTFileGenerator}

/**
 * Representation of a scala trait in AST
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
class ASTTrait(name : String = null) extends ASTFile(name) {
  override def accept(ast: AST): ASTFileGenerator = new ASTTraitGenerator(ast, this)
}
