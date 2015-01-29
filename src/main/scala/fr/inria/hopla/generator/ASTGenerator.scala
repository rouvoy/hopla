package fr.inria.hopla.generator

import fr.inria.hopla.ast.{ASTFile, ASTTrait, ASTClass, AST}

/**
 * Generate each file of an AST
 *
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
class ASTGenerator(ast: AST) {
  /**
   * Generate each file of an AST by delegating file generation to FileGenerators
   * @param outputDirectory the path to the directory where to create scala files
   */
  def generate(outputDirectory : String, packageName: String): Unit = {
    for (file <- ast.getFiles) {
      file.accept(ast).generate(outputDirectory, packageName)
    }
    new ASTTraitGenerator(ast, new ASTTrait("schema")).generate(outputDirectory, packageName)
  }
}
