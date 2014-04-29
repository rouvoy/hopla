package fr.inria.hopla

import scala.collection.mutable.ListBuffer
import scala.collection.mutable
import scala.xml.Node
import javax.xml.validation.Schema

/**
 * Created by JIN Benli on 26/03/14.
 *
 * Simple Parser creating Element from xsd file
 *
 */
class Parser(val fileName: String) {

  // save all sequence element when traverse the xsd file
  lazy val xsdFile = scala.xml.XML.loadFile(fileName)
  // find all element node sequence
  val element = xsdFile
  val elementMap: mutable.HashMap[String, Element] = new mutable.HashMap[String, Element]
  val elementList: ListBuffer[Element] = new ListBuffer[Element]

  val simpleTypeMap: mutable.HashMap[String, SimpleType] = new mutable.HashMap[String, SimpleType]()
  val complexTypesMap: mutable.HashMap[String, ComplexType] = new mutable.HashMap[String, ComplexType]()
  val attributesGroupMap: mutable.HashMap[String, AttributesGroup] = new mutable.HashMap[String, AttributesGroup]()
  val groupMap: mutable.HashMap[String, Group] = new mutable.HashMap[String, Group]()
  val attributeMap: mutable.HashMap[String, Attribute] = new mutable.HashMap[String, Attribute]()

  /**
  /**
   * Set the first element as root, creating all element from the root, using
   * ListBuffer temp for storing child list for every iteration of generation,
   * Initially the temp should be empty so we can figure out where is the root
   *
   */
  def parse() {
    //      println(e)
    val elem = new Element(element)
    elementMap ++= elem.generate
    elementMap.foreach {
      case (key, value) => {
//        println(key + " " + value)
        elementList += value
      }
    }
  }
    */

  def parse(node: Node) {
    val label: String = node.label
    label match {
      case "schema" =>
        for (child <- node.child) {
          parse(child)
        }
      case "attributeGroup" =>
        val attrsG = new AttributesGroup(node)
        attributesGroupMap.put(attrsG.getName, attrsG)
        val attrsList = attrsG.getAttributeGroup
        attrsList.foreach(att => attributeMap.put(att.getName, att.asInstanceOf))
      case "group" =>
        val group = new Group(node)
        groupMap.put(group.getName, group)
        val elemList = group.getGroup
        elemList.foreach(elem => elementMap.put(elem.getName, elem.asInstanceOf))
      case "simpleType" =>
        val s = new SimpleType(node)
        simpleTypeMap.put(s.getName, s)
      case "complexType" =>
        val c = new ComplexType(node)
        complexTypesMap.put(c.getName, c)
      case "element" =>
        val elem = new Element(node)
        elementMap.put(elem.getName, elem)
      case "attribute" =>
        val att = new Attribute(node)
        attributeMap.put(att.getName, att)
      case _ =>

    }

  }
}
