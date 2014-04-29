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

trait Schema {
  def getName = getAttributeString("name")

  def getAttributes: MetaData

  def getAttributeString(s: String): String
}

case class Attribute(attr: Node) extends Schema {
  /**
   * Get all attributes as type of MetaData
   * @return
   */
  override def getAttributes = attr.attributes

  /**
   * Get attribute by name and return its string as result
   * @param s given argument
   * @return
   */
  override def getAttributeString(s: String): String = {
    attr.attribute(s) match {
      case Some(nodes) => nodes.toString()
      case None => null
    }
  }
}

case class SimpleType(node: Node) extends Schema {

  /**
   * Get all attributes as type of MetaData
   * @return
   */
  override def getAttributes = node.attributes

  /**
   * Get attribute by name and return its string as result
   * @param s given argument
   * @return
   */
  override def getAttributeString(s: String): String = {
    node.attribute(s) match {
      case Some(nodes) => nodes.toString()
      case None => null
    }
  }

  def getType: SimpleType = {
    val firstChild = node.child(0)
    val label = firstChild.label
    label match {
      case "restriction" =>
        val res = new Restriction(firstChild)
        res.generate()
        res
      case "list" =>
        val list = new List(firstChild)
        list
      case "Union" =>
        val union = new Union(firstChild)
        union
      case _ => null
    }
  }
}


case class ComplexType(node: Node) extends Schema {

  private val _child: ListBuffer[Schema] = new ListBuffer[Schema]()

  def childs_=(value: Schema): Unit = _child += value

  def childs = _child


  /**
   * Get all attributes as type of MetaData
   * @return
   */
  override def getAttributes = node.attributes

  /**
   * Get attribute by name and return its string as result
   * @param s given argument
   * @return
   */
  override def getAttributeString(s: String): String = {
    node.attribute(s) match {
      case Some(nodes) => nodes.toString()
      case None => null
    }
  }

  def getType = {
    for(c <- node.child) {
      val label = c.label
      label match {
        case "sequence" =>
          val seq = new Sequence(c)
          childs_=(seq)
        case "choice" =>
          val choice = new Choice(c)
          childs_=(choice)
        case "attribute" =>
          val attr = new Attribute(c)
          childs_=(attr)
        case "element" =>
          val elem = new Element(c)
          childs_=(elem)
        case _ =>
      }
    }
    childs.toList
  }
}

case class Element(element: Node) extends Schema {
  private var _level = 0
  private var _root = false

  // Setter
  def level_=(value: Int): Unit = _level = value

  // Getter
  def level = _level

  def root_=(value: Boolean): Unit = _root = value

  def root = _root

  def getNameSpace: String = element.label

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

  def generate() {

  }
}


case class Restriction(res: Node) extends SimpleType(res: Node) {
  def getBase = getAttributeString("base")

  private val _child: ListBuffer[Schema] = new ListBuffer[Schema]()

  def childs_=(value: Schema): Unit = _child += value

  def childs = _child

  def generate() {
    val child = res.child
    for (c <- child) {
      val label = c.label
      label match {
        case "minInclusive" =>
          if (getBase != "xs:integer" && getBase != "xs:decimal") throw new IllegalArgumentException("Restriction base isn't numeric!")
          else childs_=(new NumericRestriction(c))
        case "minExclusive" =>
          if (getBase != "xs:integer" && getBase != "xs:decimal") throw new IllegalArgumentException("Restriction base isn't numeric!")
          else childs_=(new NumericRestriction(c))
        case "maxInclusive" =>
          if (getBase != "xs:integer" && getBase != "xs:decimal") throw new IllegalArgumentException("Restriction base isn't numeric!")
          else childs_=(new NumericRestriction(c))
        case "maxExclusive" =>
          if (getBase != "xs:integer" && getBase != "xs:decimal") throw new IllegalArgumentException("Restriction base isn't numeric!")
          else childs_=(new NumericRestriction(c))
        case "enumeration" =>
          childs_=(new Enumeration(c))

        case "pattern" =>
        //TODO
      }
    }


  }
}

case class Enumeration(enum: Node) extends Restriction(enum: Node) {
  /**
   * Get attribute by name and return its string as result
   * @return
   */
  def getValueString: String = {
    enum.attribute("value") match {
      case Some(s) => s.toString()
      case None => null
    }
  }
}

class NumericRestriction(nr: Node) extends Restriction(nr: Node) {


  def apply(num: BigDecimal): Boolean = {

    val operator = nr.label
    val number = BigDecimal(getAttributeString("value"))
    operator match {
      case "maxExclusive" => num < number
      case "maxInclusive" => num <= number
      case "minExclusive" => num > number
      case "minInclusive" => num >= number
      case _ => false
    }
  }
}

case class List(list: Node) extends SimpleType(list: Node) {
  def getItemType = getAttributeString("itemType")
}

case class Union(union: Node) extends SimpleType(union: Node) {
  def getMemberTypes = getAttributeString("memberTypes").split(" ")
}

case class Sequence(seq: Node) extends ComplexType(seq: Node) {

  def generate() {
    for(c <- seq.child) {
      childs_=(new Element(c))
    }
  }
}

case class Choice(choice: Node) extends ComplexType(choice: Node) {

  def generate() {
    for(c <- choice.child) {
      childs_=(new Element(c))
    }
  }
}
