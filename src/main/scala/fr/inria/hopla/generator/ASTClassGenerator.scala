package fr.inria.hopla.generator

import fr.inria.hopla.ast._

/**
 * Generate a class which contains fields and attributes. Each field can be a sequence of elements or a simple object<br/>
 * This class extends <i>Marker</i> trait, so it generates :
 * <ul>
 *   <li><i>getName</i> method</li>
 *   <li><i>getText</i> method</li>
 *   <li><i>getChildren</i> method</li>
 * </ul>
 * <br/>
 * Also generate a companion object which contains apply methods in order to easily create instances of this class
 *
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
class ASTClassGenerator(ast: AST, astClass: ASTClass) extends ASTFileGenerator(astClass) {
  /**
   * The list of fields to generate
   */
  val fields = new ASTField("text", "String") :: astClass.fields.filterNot(f => isPrimitiveType(f)).toList

  /**
   * The list of attributes to generate
   */
  val attributes = astClass.fields.filter(f => isPrimitiveType(f)).toList

  /**
   * Check if the field is a sequence of elements
   * @param field the field to check
   * @return true if the field is a sequence of elements
   */
  def isListOfElements(field: ASTField): Boolean = {
    ast.get(field.fieldType) match {
      case Some(astFile) => astFile.isListOfElements
      case None => false
    }
  }

  /**
   * Check if the field is a primitive type
   * @param field the field to check
   * @return true if the field is a primitive type
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
  def generateApplyDeclaration(generator: PrintGenerator, field: ASTField): Unit = {
    val astClassName = scalaFileName(astClass.name)
    val fieldType = if (isListOfElements(field)) {
      scalaFileName(field.fieldType) + "*"
    }
    else {
      scalaFileName(field.fieldType)
    }
    generator.startDef("apply", scalaFileName(field.name), fieldType, astClassName)
  }

  /**
   * Give the default value of the provided <code>field</code>,
   * @param field the field to have the default value
   * @return the default value of the field: Seq[fieldType] if the field is a sequence of elements, null otherwise
   */
  def defaultValue(field: ASTField): String = {
    if (isListOfElements(field)) {
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
    /** each field of this class
      * Generate the inheritance part: extends [inheritance1] with [inheritance2] ...
      *
      * @param generator the generator where to add file content
      */
    protected def generateInheritance(generator: PrintGenerator) = {
      var fileExtends: List[ASTFile] = new ASTTrait("Marker") :: astClass.getTraits.toList
      // If file inherits from another class, prepend fileExtends list by this inherited class
      astClass.getInheritsFrom match {
        case Some(astClass) => fileExtends = List(astClass) ::: fileExtends
        case None =>
      }

      if (fileExtends.nonEmpty) {
        // For the first element of inheritance list, prepend class name by "extends"
        generator.write(s" extends ${scalaFileName(fileExtends(0).name)}")

        // For all others, prepend class name by a comma
        for (fileTrait <- fileExtends.drop(1)) {
          generator.write(s" with  ${scalaFileName(fileTrait.name)}")
        }
      }
    }

    /**
     * Generate each field of this class
     * @param generator the generator where to add content
     */
    def generateFields(generator: PrintGenerator): Unit = {
      if (fields.nonEmpty) {
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
     * @param field the field to convert into a String
     * @return the field converted into a String
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
     * Generate each attribute of this class
     * @param generator the generator where to add content
     */
    def generateAttributes(generator: PrintGenerator): Unit = {
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
     * Generate each apply method of this class
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
     * Generate a method which returns the name of the marker to generate
     * @param generator the generator where to add content
     */
    def generateGetNameMethod(generator: PrintGenerator): Unit = {
      generator.writeLine("override def getName () : String = \"" + astClass.name + "\"")
    }

    /**
     * Generate a method which returns the content of the marker to generate
     * @param generator the generator where to add content
     */
    def generateGetTextMethod(generator: PrintGenerator) = {
      generator.writeLine("override def getText () : String = text")
    }

    /**
     * Generate a child from <code>field</code>
     * @see generateGetChildrenMethod
     * @param field the field to transform into a child
     * @return the transformed child
     */
    private def generateChild(field: ASTField): String = {
      if (isListOfElements(field)) {
        scalaFileName(field.name)
      }
      else {
        s"Seq[Marker] (${scalaFileName(field.name)})"
      }
    }

    /**
     * Generate a <i>getChildrenMethod</i> which returns a sequence of children markers.<br/>
     * Add each field which is not a primitive type into a Seq[Marker], then return the created sequence.
     * @param generator the generator where to add content
     */
    def generateGetChildrenMethod(generator: PrintGenerator): Unit = {
      generator.startDef("getChildren", "Seq[Marker]")
      val fields = astClass.fields.filterNot(f => isPrimitiveType(f)).toList
      if (fields.nonEmpty) {
        generator.write(generateChild(fields(0)))
        for (field <- fields.drop(1)) {
          generator.write(s" ++ ${generateChild(field)}")
        }
      } else {
        generator.writeLine("Seq[Marker]()")
      }
      generator.endDef()
    }

    def generateHtmlAttributesMethod(generator: PrintGenerator) = {
      generator.startDef("htmlAttributes", "String")
      generator.writeLine("val builder : StringBuilder = new StringBuilder")

      // Map each scala attribute with the html attribute if the latter isn't null
      for(attribute <- attributes) {
        val attributeName = scalaFileName(attribute.name)
        generator.startBlock(s"if($attributeName != null)")
        // builder.append("htmlAttributeName = \"" + scalaAttributeName + "\"")
        generator.writeLine(s"""builder.append(" ${attribute.name} = \\"" + $attributeName + "\\"")""")
        generator.endBlock()
      }
      generator.writeLine("builder.toString")
      generator.endDef()
    }

    /**
     * Generate a <i>toHtml</i> method which returns an HTML code corresponding to this Marker<br/>
     * Generate:
     * <ul>
     *   <li><i>getName</i> method</li>
     *   <li><i>getText</i> method</li>
     *   <li><i>getChildren</i> method</li>
     * </ul>
     * add each field which isn't a primitive type into a Seq[Marker], then return the created sequence.
     * @param generator the generator where to add content
     */
    def generateToHtmlMethods(generator: PrintGenerator) = {
      generateGetNameMethod(generator)
      generateGetTextMethod(generator)
      generateGetChildrenMethod(generator)
      generateHtmlAttributesMethod(generator)
    }

    /**
     * Generate the class, its fields, attributes and apply methods
     * @param generator the generator where to add content
     */
    def generate(generator: PrintGenerator) = {
      generator.write(s"class ${scalaFileName(astClass.name)}")
      generateFields(generator)
      generateInheritance(generator)
      generator.startBlock("")
      generateAttributes(generator)
      generateApplyMethods(generator)
      generateToHtmlMethods(generator)
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
     * Generate a parameter for <i>apply</i> returned object, which represents the provided <code>currentField</code>
     * If this <code>currentField</code> equals <code>applyField</code>, return the applyField's name,
     * return <code>defaultValue</code> of <code>currentField</code> otherwise
     * @param applyField the parameter of apply
     * @param currentField the field to insert into object returned by apply
     * @return a String which represents the field to insert into object returned by apply
     */
    private def generateApplyParameter(applyField : ASTField, currentField : ASTField) : String = {
      if(currentField equals applyField) {
        scalaFileName(currentField.name)
      }
      else {
        defaultValue(currentField)
      }
    }

    /**
     * Generate apply method for the field given as parameter
     * @param applyField the parameter of apply
     * @param generator the generator where to add content
     */
    def generateApply(generator: PrintGenerator, applyField: ASTField) = {
      val astClassName = scalaFileName(astClass.name)
      generateApplyDeclaration(generator, applyField)

      // Generate new instance to return
      generator.write(s"new $astClassName( ")

      // generate each parameter
      if (fields.nonEmpty) {
        generator.write(generateApplyParameter(applyField, fields(0)))
        for (currentField <- fields.drop(1)) {
          generator.write(s", ${generateApplyParameter(applyField, currentField)}")
        }
      }

      // Close the returned instance
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
