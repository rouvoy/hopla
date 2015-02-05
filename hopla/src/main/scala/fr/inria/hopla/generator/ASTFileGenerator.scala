package fr.inria.hopla.generator

import fr.inria.hopla.ast.{ASTField, ASTFile}

import scala.tools.nsc.io.File

/**
 * Generate a scala file depending on the ASTFile data
 *
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
abstract class ASTFileGenerator(astFile: ASTFile) extends ScalaGenerator(astFile) {
 /**
   * Create a new file and generate the file content depending on the ASTFile data
   * @param outputDirectory the path to the directory where to create scala files
   */
  def generate(outputDirectory: String, packageName: String): Unit
}
