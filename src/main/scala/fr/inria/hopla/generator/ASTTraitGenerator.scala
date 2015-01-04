package fr.inria.hopla.generator

import fr.inria.hopla.ast.{AST, ASTTrait, ASTFile}

/**
 * Generate a trait
 *
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
class ASTTraitGenerator(ast : AST, astTrait : ASTTrait) extends ASTFileGenerator(astTrait) {

  /**
   * Create a new file and generate the file content depending on the ASTFile data.
   *
   * @param outputDirectory the path to the directory where to create scala files.
   */
  override def generate(outputDirectory: String, packageName: String): Unit = {
    val generator = createFile(outputDirectory)
    generatePackage(generator, packageName)
    generator.write(s"trait ${scalaFileName(astTrait.name)} ")
    generateInheritance(generator)
    generator.startBlock("")
    generateFields(generator)
    generator.close
  }

  /**
   * Generates Fields
   * @param generator the generator where to add file content
   */
  private def generateFields(generator: PrintGenerator): Unit = {
    for (field <- astTrait.getFields) {
      generator.writeLine(s"${generateField(field, ast.get(field.fieldType).get.isListOfElements)}")
    }
  }
}
