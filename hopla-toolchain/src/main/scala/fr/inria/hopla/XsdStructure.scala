package fr.inria.hopla

import scala.xml.{MetaData, Node}
import scala.collection.mutable._
import scala.Some

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

trait Elem extends Schema {
  override def toString = "Element Implement: [" + this.getClass + "]: " + this.getName
}

trait Attr extends Schema {
  override def toString = "Attribute Implement: [" + this.getClass + "]: " + this.getName
}

trait Type extends Schema {
  override def toString = "Type Implement: [" + this.getClass + "]: " + this.getName
}

case class Attribute(attr: Node) extends Attr {
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

case class AttributesGroup(node: Node) extends Attr with Schema {
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

  def getAttributeGroup = {
    val attributesList: ListBuffer[Attribute] = new ListBuffer[Attribute]
    for (child <- node.child) {
      attributesList += new Attribute(child)
    }
    attributesList.toList
  }
}

case class Group(node: Node) extends Schema {
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

  def getGroup = {
    val firstChild = node.child(0)
    val label = firstChild.label
    val elemBuffer: ListBuffer[Schema] = new ListBuffer[Schema]
    label match {
      case "sequence" =>
        val seq = new Sequence(firstChild)
        elemBuffer ++= seq.childs
      case "choice" =>
        val choice = new Choice(firstChild)
        elemBuffer ++= choice.childs.toList
      case "#PCDATA" =>
      case _ =>
        println("Group " + label)
        null
    }
    elemBuffer.toList
  }
}

case class SimpleType(node: Node) extends Type {

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
      case _ =>
        println("SimpleType " + label)
        null
    }
  }
}


case class ComplexType(node: Node) extends Type {

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
    for (c <- node.child) {
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
          println("ComplexType " + label)
      }
    }
    childs.toList
  }
}

case class Element(element: Node) extends Elem {
  private var _level = 0
  private var _root = false

  private var _ref = false
  private var _refHandled = true
  private var _externalType = false
  private var _typeHandled = true

  private val interComplexTypes: ListBuffer[InternalComplexType] = new ListBuffer[InternalComplexType]
  private val interSimpleType: ListBuffer[InternalSimpleType] = new ListBuffer[InternalSimpleType]
  private val interAttributes: ListBuffer[Attribute] = new ListBuffer[Attribute]

  private var _referenceElement: Elem = null
  private var _externalTypeDeclaration: Type = null
  private var _externalTypeName: String = null


  override def getName = {
    if (ref == true)
      throw new IllegalArgumentException("This is not a element declaration, but a reference")
    else
      getAttributeString("name")
  }

  def getRefName = getAttributeString("ref")

  // Setter
  def level_=(value: Int): Unit = _level = value

  // Getter
  def level = _level

  def root_=(value: Boolean): Unit = _root = value

  def root = _root

  def ref_=(value: Boolean): Unit = _ref = value

  def ref = _ref

  def refHandled_=(value: Boolean): Unit = _refHandled = value

  def refHandled = _refHandled

  def externalType_=(value: Boolean): Unit = _externalType = value

  def externalType = _externalType

  def typeHandled_=(value: Boolean): Unit = _typeHandled = value

  def typeHandled = _typeHandled

  def getNameSpace: String = element.label

  def referenceElement_=(value: Elem): Unit = _referenceElement = value

  def referenceElement = _referenceElement

  def externalTypeDeclaration_=(value: Type): Unit = _externalTypeDeclaration = value

  def externalTypeDeclaration = _externalTypeDeclaration

  def externalTypeName_=(value: String): Unit = _externalTypeName = value

  def externalTypeName = _externalTypeName

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

  def handle() {
    if (getAttributeString("ref") != null) {
      ref_=(true)
      refHandled_=(false)
      println("Wait for reference declaration: " + getAttributeString("ref"))
    }

    if(!getAttributeString("type").contains("xs")) {
      externalType_=(true)
      typeHandled_=(false)
      externalTypeName_=(getAttributeString("type"))
      println("Wait for external Type declaration: " + getAttributeString("type"))
    }

    //TODO type is an external declaration, how to handle

    for (child <- element.child) {
      val label = child.label
      label match {
        case "complexType" =>
          val ict = new InternalComplexType(child)
          interComplexTypes += ict
        case "simpleType" =>
          val ist = new InternalSimpleType(child)
          interSimpleType += ist
        case "attribute" =>
          val ia = new Attribute(child)
          interAttributes += ia
        case "#PCDATA" =>
        case _ => println("Element " + label)
      }
    }
  }

  def referenceHandle(elem: Elem) {
    if(!refHandled) {
      referenceElement_=(elem)
      level_=(value = level + elem.asInstanceOf[Element].level)
      refHandled_=(true)
    }
  }

  def typeHandle(value: Type) {
    if(!typeHandled) {
      externalTypeDeclaration_=(value)
      //TODO level handle

    }
  }

  def hasChild = element.nonEmpty

  def getNode = element

  class InternalComplexType(node: Node) extends Element(node: Node) {
    override def getName = "Internal ComplexType"

    private val _child: ListBuffer[Schema] = new ListBuffer[Schema]()

    def childs_=(value: Schema): Unit = _child += value

    def childs = _child

    def getType = {
      for (c <- node.child) {
        val label = c.label
        label match {
          case "sequence" =>
            val seq = new Sequence(c)
            level_=(value = level.+(seq.getElementNumber))
            childs_=(seq)
          case "choice" =>
            val choice = new Choice(c)
            level_=(value = level.+(choice.getElementNumber))
            childs_=(choice)
          case "attribute" =>
            val attr = new Attribute(c)
            childs_=(attr)
          case "element" =>
            val elem = new Element(c)
            level_=(value = level.+(1))
            childs_=(elem)
          case _ => println("Element InternalComplexType " + label)
        }
      }
      childs.toList
    }
  }

  class InternalSimpleType(node: Node) extends Element(node: Node) {
    override def getName = "InternalSimpleType"

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
        case _ =>
          println("Element InternalSimpleType " + label)
          null
      }
    }
  }

}


class Restriction(res: Node) extends SimpleType(res: Node) {
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
        case _ => println("Restriction " + label)

        //TODO more cases
      }
    }


  }
}

class Enumeration(enum: Node) extends Restriction(enum: Node) {
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

class List(list: Node) extends SimpleType(list: Node) {
  def getItemType = getAttributeString("itemType")
}

class Union(union: Node) extends SimpleType(union: Node) {
  def getMemberTypes = getAttributeString("memberTypes").split(" ")
}

class Sequence(seq: Node) extends ComplexType(seq: Node) {
  def getElementNumber = childs.size

  def generate() {
    for (c <- seq.child) {
      val e = new Element(c)
      e.handle()
      childs_=(e)
    }
  }
}

class Choice(choice: Node) extends ComplexType(choice: Node) {
  def getElementNumber = childs.size

  def generate() {
    for (c <- choice.child) {
      val e = new Element(c)
      e.handle()
      childs_=(e)
    }
  }
}
