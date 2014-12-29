package fr.inria.hopla.generator

import fr.inria.hopla.ast.{ASTField, ASTFile}

import scala.tools.nsc.io._

/**
 * Scala generator which provides helpers in order to generate scala source code<br>
 *
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
abstract class ScalaGenerator(astFile: ASTFile) {

  protected def createFile(outputDirectory: String) : PrintGenerator = {
    val writer = File(s"$outputDirectory/${scalaFileName(astFile.name)}.scala").printWriter(append = false)
    new PrintGenerator(writer)
  }

  /**
   * Generate the inheritance part: extends [inheritance1], [inheritance2] ...
   *
   * @param generator the generator where to add file content
   */
  protected def generateInheritance(generator: PrintGenerator) = {
    var fileExtends : List[ASTFile] = astFile.getTraits.toList
    // If file inherits from another class, prepend fileExtends list by this inherited class
    astFile.getInheritsFrom match {
      case Some(astClass) => fileExtends = List(astClass) ::: fileExtends
      case None =>
    }

    if(fileExtends.nonEmpty) {
      // For the first element of inheritance list, prepend class name by "extends"
      generator.write(s"extends ${scalaFileName(fileExtends(0).name)}")
      // For all others, prepend class name by a comma
      for(fileTrait <- fileExtends.drop(1)) {
        generator.write(s" with  ${scalaFileName(fileTrait.name)}")
      }
    }
  }

  /**
   * Generate a field
   * @param field the field to stringify
   * @return the stringified field
   */
  protected def generateField(field: ASTField, isListOfElements : Boolean) : String = {
    val stringField = s"var ${scalaFileName(field.name)}: ${scalaFileName(field.fieldType)}"
    if(isListOfElements) {
      stringField + "*"
    }
    else {
      stringField + " = null"
    }
  }

  /**
   * Generate package
   * @param generator the generator which writes the content
   * @param packageName the name of he package
   */
  def generatePackage(generator: PrintGenerator, packageName : String) : Unit = {
    generator.writeLine(s"package $packageName")
  }
}
