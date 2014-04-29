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
  def getAttributes: MetaData

  def getAttributeString(s: String): String
}


trait SimpleType extends Schema {
  def getName = getAttributeString("name")
}


trait ComplexType extends Schema {
  def getName = getAttributeString("name")

  private val _child: ListBuffer[Schema] = new ListBuffer[Schema]()

  def childs_=(value: Schema): Unit = _child += value

  def childs = _child
}

case class Element(element: Node) extends SimpleType with ComplexType {
  private val _parent: ListBuffer[Element] = new ListBuffer[Element]()
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

  def xsdAttributes_=(value: String): Unit = _xsdAttributes += value

  def xsdAttributes = _xsdAttributes

  def enum_=(value: Enumeration): Unit = _enum += value

  def enum = _enum

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
}

case class Attribute(attr: Node) extends SimpleType {
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

abstract case class Restriction(res: Node) extends SimpleType {
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

  /**
   * Get all attributes as type of MetaData
   * @return
   */
  override def getAttributes = res.attributes

  /**
   * Get attribute by name and return its string as result
   * @param s given argument
   * @return
   */
  override def getAttributeString(s: String): String = {
    res.attribute(s) match {
      case Some(nodes) => nodes.toString()
      case None => null
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

case class List(list: Node) extends SimpleType {
  def getItemType = getAttributeString("itemType")

  /**
   * Get all attributes as type of MetaData
   * @return
   */
  override def getAttributes = list.attributes

  /**
   * Get attribute by name and return its string as result
   * @param s given argument
   * @return
   */
  override def getAttributeString(s: String): String = {
    list.attribute(s) match {
      case Some(nodes) => nodes.toString()
      case None => null
    }
  }
}

case class Union(union: Node) extends SimpleType {
  def getMemberTypes = getAttributeString("memberTypes").split(" ")

  /**
   * Get all attributes as type of MetaData
   * @return
   */
  override def getAttributes = union.attributes

  /**
   * Get attribute by name and return its string as result
   * @param s given argument
   * @return
   */
  override def getAttributeString(s: String): String = {
    union.attribute(s) match {
      case Some(nodes) => nodes.toString()
      case None => null
    }
  }
}

case class Sequence(seq: Node) extends ComplexType {

  def generate() {
    for(c <- seq.child) {
      childs_=(new Element(c))
    }
  }

  /**
   * Get all attributes as type of MetaData
   * @return
   */
  override def getAttributes = seq.attributes

  /**
   * Get attribute by name and return its string as result
   * @param s given argument
   * @return
   */
  override def getAttributeString(s: String): String = {
    seq.attribute(s) match {
      case Some(nodes) => nodes.toString()
      case None => null
    }
  }
}

case class Choice(choice: Node) extends ComplexType {

  def generate() {
    for(c <- choice.child) {
      childs_=(new Element(c))
    }
  }

  /**
   * Get all attributes as type of MetaData
   * @return
   */
  override def getAttributes = choice.attributes

  /**
   * Get attribute by name and return its string as result
   * @param s given argument
   * @return
   */
  override def getAttributeString(s: String): String = {
    choice.attribute(s) match {
      case Some(nodes) => nodes.toString()
      case None => null
    }
  }
}
