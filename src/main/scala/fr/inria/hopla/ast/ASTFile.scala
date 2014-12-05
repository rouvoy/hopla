package fr.inria.hopla.ast

import java.io.{File, PrintWriter}

/**
 * Abstraction for a scala file (Compilation Unit)<br>
 * Can contain all fields & methods for this ASTFile.<br>
 * An ASTFile can be an ASTClass or an ASTTrait
 *
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
abstract case class ASTFile(name: String) {

  /**
   * Fields to generate into this ASTFile
   */
  private var fields: List[ASTField] = List()

  /**
   * All traits implemented by this ASTFile
   */
  private var traits: Set[ASTTrait] = Set()

  /**
   * Inherited class
   */
  private var inheritsFrom : Option[ASTClass] = None

  /**
   * All files which extend this ASTFile
   */
  private var subFiles : Set[ASTFile] = Set()

  private var isListOfElements = false

  /**
   * Add an implemented trait to the list <code>traits</code>
   * @param astTrait the trait implemented by this ASTFile
   */
  def withTrait(astTrait: ASTTrait): Unit = {
      traits = traits + astTrait
      astTrait.subFiles = astTrait.subFiles + this
  }

  /**
   * Add a field to the list <code>fields</code>
   * @param field the field to add into this ASTFile
   */
  def withField(field: ASTField) : Unit = {
    fields = fields :+ field
  }

  /**
   * Set the class inherited by this file
   * @param inheritsFrom the inherited class
   */
  def inheritsFrom(inheritsFrom: ASTClass): Unit = {
    this.inheritsFrom = Some(inheritsFrom)
  }
  
  def setListOfElements(isListOfElements: Boolean) {
    this.isListOfElements = isListOfElements
  }

  /**
   * Simplify the ASTFile by removing unnecessary implemented traits
   */
  def simplify(): Unit = {
    // remove the schema trait implementation when it isn't necessary
    val schemaTrait = new ASTTrait("schema")
    if(traits.contains(schemaTrait) && (traits.size > 1 || !inheritsFrom.equals(None))) {
      traits = traits - schemaTrait
    }
  }

  def getInheritsFrom = inheritsFrom
  def getTraits = traits
  def getFields = fields
}
