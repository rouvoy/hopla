package fr.inria.hopla.generator

import fr.inria.hopla.ast.{ASTTrait, ASTField, ASTFile}

import scala.tools.nsc.io.File

/**
 * Scala generator which provides helpers in order to generate scala source code<br>
 *
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
abstract class ScalaGenerator(astFile: ASTFile) {

  /**
   * Create a File
   * @param outputDirectory the folder to put the created file
   * @return the generator where to add file content
   */
  protected def createFile(outputDirectory: String) : PrintGenerator = {
    val file = File(s"$outputDirectory/${scalaFileName(astFile.name)}.scala")
    // Create parent directories if they don't already exist
    file.parent.createDirectory()

    val writer = file.printWriter(false)
    new PrintGenerator(writer)
  }

  /**
   * Generate the inheritance part: extends [inheritance1], [inheritance2] ...
   * @param generator the generator where to add file content
   */
  protected def generateInheritance(generator: PrintGenerator) = {
    var fileExtends : List[ASTFile] = new ASTTrait("Marker") :: astFile.getTraits.toList
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
   * @param field the field to convert into a String
   * @return the field converted into a String
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
   * @param packageName the name of the package
   */
  def generatePackage(generator: PrintGenerator, packageName : String) : Unit = {
    generator.writeLine(s"package $packageName")
    generator.writeLine("import fr.inria.hopla.xhtml.Marker")
  }
}
