package fr.inria.hopla

import scala.xml.{MetaData, Node}
import scala.collection.mutable.ListBuffer
import scala.collection.mutable

/**
 * Created by JIN Benli on 26/03/14.
 *
 * Element is the first class container for storing all possible relation presented in
 * the original file, called by Parser and can be accessed by Any Type of visitor
 *
 */

trait Xsd {
  def getAttributes: MetaData
  def getAttributeString(s: String): String
}

class Element(element: Node) extends Xsd{
  private val _parent: ListBuffer[Element] = new ListBuffer[Element]()
  private val _child: ListBuffer[Element] = new ListBuffer[Element]()
  private var _level = 0
  private var _root = false
  // important name list who remember all created element during the recursive process generate
  // preventing double generation using dynamic programming
  private val nameList: mutable.HashMap[String, Element] = new mutable.HashMap[String, Element]
  private val _xsdAttributes: ListBuffer[String] = new ListBuffer[String]()
  private val _enum: ListBuffer[Enumeration] = new ListBuffer[Enumeration]()

  /**
   * Generate child element from the parent, so their relation can be defined
   *
   * @return
   */
  def generate: mutable.HashMap[String, Element] = {
    val namespace: String = element.label
    namespace match {
      case "schema" =>
        for (child <- element.child) {
          val elementChild = new Element(child)
          elementChild.root_=(value = true)
          val tempList = elementChild.generate
          tempList.foreach {
            case (key, value) =>
              if (!nameList.contains(key)) {
                nameList.put(key, value)
              }
          }
        }
      case "element" =>
        if (this.root) {
          this.level_=(1)
          this.parent_=(null)
          if (!nameList.contains(this.getAttributeString("name"))) {
            nameList.put(this.getAttributeString("name"), this)
          }
        }
        else {
          this.level_=(this.parent.last.level + 1)
          for (p <- this.parent) {
            p.childs_=(this)
          }
          if (!nameList.contains(this.getAttributeString("name"))) {
            nameList.put(this.getAttributeString("name"), this)
          }
        }
        for (child <- element.child) {
          val elementChild = new Element(child)
          elementChild.parent_=(this)
          val tempList = elementChild.generate
          tempList.foreach {
            case (key, value) =>
              if (!nameList.contains(key)) nameList.put(key, value)
          }
        }
      case "complexType" =>
        for (p <- this.parent) {
          p.xsdAttributes_=("complexType")
        }
        for (child <- element.child) {
          val namespace = child.label
          namespace match {
            case "sequence" =>
              for (p <- this.parent) {
                p.xsdAttributes_=("sequence")
              }
              for (grandChild <- child.child) {
                val elementGrandChild: Element = new Element(grandChild)
                elementGrandChild.parent ++= this.parent
                elementGrandChild.level_=(elementGrandChild.parent.last.level)
                val tempList = elementGrandChild.generate
                tempList.foreach {
                  case (key, value) =>
                    if (!nameList.contains(key)) {
                      nameList.put(key, value)
                    }
                }
              }
            case "attribute" => // TODO
            case "all" => // TODO
            case "#PCDATA" =>
          }
        }
      case "simpleType" =>
        for (p <- this.parent) {
          p.xsdAttributes_=("simpleType")
        }
        for (child <- element.child) {
          val namespace = child.label
          namespace match {
            case "restriction" =>
              for (p <- this.parent) {
                p.xsdAttributes_=("restriction")
              }
              for (grandChild <- child.child) {
                val enumGrandChild = new Enumeration(grandChild)
                for (p <- this.parent) {
                  p.enum_=(enumGrandChild)
                }
              }
            case "#PCDATA" =>
          }
        }
      case "#PCDATA" =>

      case "annotation" =>

      case "documentation" =>

      case "unique" =>

      case "selector" =>

      case "field" =>

      case "key" =>

      case "keyref" =>

      case "attribute" =>

      case _ =>

    }
    nameList
  }

  // Setter
  def level_=(value: Int): Unit = _level = value

  // Getter
  def level = _level

  def root_=(value: Boolean): Unit = _root = value

  def root = _root

  def parent_=(value: Element): Unit = _parent += value

  def parent = _parent

  def childs_=(value: Element): Unit = _child += value

  def childs = _child

  def xsdAttributes_=(value: String): Unit = _xsdAttributes += value

  def xsdAttributes = _xsdAttributes

  def enum_=(value: Enumeration): Unit = _enum += value

  def enum = _enum

  /**
   * Get all attributes as type of MetaData
   * @return
   */
  override def getAttributes = element.attributes

  /**
   * Get attribute by name and return its string as result
   * @param s given argument
   * @return
   */
  override def getAttributeString(s: String): String = {
    element.attribute(s) match {
      case Some(nodes) => nodes.toString()
      case None => null
    }
  }

  def getNameSpace: String = element.label
}

class Enumeration(element: Node) extends Xsd{
  /**
   * Get attribute by name and return its string as result
   * @return
   */
  def getValueString: String = {
    element.attribute("value") match {
      case Some(s) => s.toString()
      case None => null
    }
  }

  override def getAttributes: MetaData = element.attributes

  override def getAttributeString(s: String): String = {
    element.attribute(s) match {
      case Some(nodes) => nodes.toString()
      case None => null
    }
  }
}