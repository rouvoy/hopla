package fr.inria.hopla.generator

import fr.inria.hopla.ast.{ASTFile, ASTField, AST, ASTClass}

/**
 * Generate a class which contains fields and attributes.Each field can be a sequence of elements or a simple object<br>
 * Also generate a companion object which contains apply methods in order to create instances of this class easily
 *
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
class ASTClassGenerator(ast : AST, astClass : ASTClass) extends ASTFileGenerator(astClass) {
  /**
   * The list of fields to generate
   */
  val fields = new ASTField("text", "String") :: astClass.fields.filterNot(f => isPrimitiveType(f)).toList

  /**
   * The list of attributes to generate
   */
  val attributes =  astClass.fields.filter(f => isPrimitiveType(f)).toList

  /**
   *
   * @param field the field to check
   * @return true if the field is a sequence of elements
   */
  def isListOfElements(field: ASTField) : Boolean = {
    ast.get(field.fieldType) match {
      case Some(astFile)  => astFile.isListOfElements
      case None           => false
    }
  }

  /**
   *
   * @param field the field to check
   * @return true if the field has a primitive type
   */
  def isPrimitiveType(field: ASTField): Boolean = field.fieldType == "String"

  /**
   * Create a new file and generate the file content depending on the ASTFile data.
   * Generate a class and a companion object, which contains apply methods
   * @param outputDirectory the path to the directory where to create scala files.
   */
  override def generate(outputDirectory: String, packageName: String): Unit = {
    val generator = createFile(outputDirectory)
    generatePackage(generator, packageName)
    ClassGenerator.generate(generator)
    CompanionGenerator.generate(generator)
    generator.close()
  }

  /**
   * Generate the declaration of apply method
   * @param generator the generator where to write generated code
   * @param field the field of the generated apply method
   */
  def generateApplyDeclaration(generator: PrintGenerator, field:ASTField): Unit = {
    val astClassName = scalaFileName(astClass.name)
    val fieldType = if(isListOfElements(field)) {
      scalaFileName(field.fieldType) + "*"
    }
    else {
      scalaFileName(field.fieldType)
    }
    generator.startDef("apply", scalaFileName(field.name), fieldType, astClassName)
  }

  /**
   *
   * @param field
   * @return the default value of the field: Seq[fieldType] if the field is a sequence of element, null otherwise
   */
  def defaultValue(field: ASTField): String = {
    if(isListOfElements(field)) {
      s"Seq[${scalaFileName(field.fieldType)}]()"
    }
    else {
      "null"
    }
  }

  /**
   * Generate a class and its fields and attributes
   */
  object ClassGenerator {
    /**each field of this class
     * Generate the inheritance part: extends [inheritance1] with [inheritance2] ...
     *
     * @param generator the generator where to add file content
     */
    protected def generateInheritance(generator: PrintGenerator) = {
      var fileExtends : List[ASTFile] = astClass.getTraits.toList
      // If file inherits from another class, prepend fileExtends list by this inherited class
      astClass.getInheritsFrom match {
        case Some(astClass) => fileExtends = List(astClass) ::: fileExtends
        case None =>
      }

      if(fileExtends.nonEmpty) {
        // For the first element of inheritance list, prepend class name by "extends"
        generator.write(s" extends ${scalaFileName(fileExtends(0).name)}")

        // For all others, prepend class name by a comma
        for(fileTrait <- fileExtends.drop(1)) {
          generator.write(s" with  ${scalaFileName(fileTrait.name)}")
        }
      }
    }

    /**
     * generate each field of this class
     * @param generator the generator where to add content
     */
    def generateFields(generator: PrintGenerator):Unit = {
      if(fields.nonEmpty) {
        generator.write("(")
        generator.write(generateField(fields(0)))
        for (field <- fields.drop(1)) {
          generator.write(s", ${generateField(field)}")
        }
        generator.write(")")
      }
    }

    /**
     * Generate a field
     * @param field the field to stringify
     * @return the stringified field
     */
    def generateField(field: ASTField): String = {
      val stringField = s"var ${scalaFileName(field.name)}: "
      if (isListOfElements(field)) {
        stringField + s"Seq[${scalaFileName(field.fieldType)}]"
      }
      else {
        stringField + scalaFileName(field.fieldType)
      }
    }

    /**
     * generate each attribute of this class
     * @param generator the generator where to add content
     */
    def generateAttributes(generator: PrintGenerator):Unit = {
      for (attribute <- attributes) {
        // Field attribute
        generator.writeLine(s"var ${scalaFileName(attribute.name)} : ${scalaFileName(attribute.fieldType)} = null")

        // Builder method
        val attributeName = scalaFileName(attribute.name)
        val attributeType = scalaFileName(attribute.fieldType)
        generator.startDef(attributeName, attributeName, attributeType, scalaFileName(astClass.name))
        generator.writeLine(s"this.$attributeName = $attributeName")
        generator.writeLine("this")
        generator.endDef()
      }
    }

    /**
     * generate each apply method of this class
     * @param generator the generator where to add content
     */
    def generateApplyMethods(generator: PrintGenerator) = {
      var fieldName: String = null
      var fieldType: String = null
      for (field <- fields) {
        fieldName = scalaFileName(field.name)
        fieldType = scalaFileName(field.fieldType)
        generateApplyDeclaration(generator, field)
        generator.writeLine(s"this.$fieldName = $fieldName")
        generator.writeLine("this")
        generator.endDef()
      }
    }

    /**
     * generate the class, its fields, attributes and apply methods
     * @param generator the generator where to add content
     */
    def generate(generator: PrintGenerator) = {
      generator.write(s"class ${scalaFileName(astClass.name)}")
      generateFields(generator)
      generateInheritance(generator)
      generator.startBlock("")
      generateAttributes(generator)
      generateApplyMethods(generator)
      generator.endBlock()
    }
  }

  /**
   * Generate a companion object and its apply methods
   */
  object CompanionGenerator extends ScalaGenerator(astClass) {

    /**
     * Generate all necessary apply methods
     * @param generator the generator where to add content
     */
    def generateApplyMethods(generator: PrintGenerator) = {
      for (field <- fields) {
        generateApply(generator, field)
      }
    }

    /**
     * Generate apply method for the field given as parameter
     * @param generator the generator where to add content
     */
    def generateApply(generator: PrintGenerator, field: ASTField) = {
      val astClassName = scalaFileName(astClass.name)
      generateApplyDeclaration(generator, field)
      generator.write(s"new $astClassName( ")
      if(fields.nonEmpty) {
        generator.write(defaultValue(fields(0)))
        for (field <- fields.drop(1)) {
          generator.write(s", ${defaultValue(field)}")
        }
      }
      generator.writeLine(")")
      generator.endDef()
    }

    /**
     * Generate the entire companion object
     * @param generator the generator where to add content
     */
    def generate(generator: PrintGenerator): Unit = {
      generator.startBlock(s"object  ${scalaFileName(astClass.name)}")
      generateApplyMethods(generator)
      generator.endBlock()
    }
  }
}
