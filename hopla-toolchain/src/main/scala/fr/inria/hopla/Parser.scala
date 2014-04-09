package fr.inria.hopla

import scala.collection.mutable.ListBuffer
import scala.collection.mutable

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

  /**
   * Set the first element as root, creating all element from the root, using
   * ListBuffer temp for storing child list for every iteration of generation,
   * Initially the temp should be empty so we can figure out where is the root
   *
   */
  def parse() {
    val level = 1
    for (e <- element) {
//      println(e)
      val elem = new Element(e)
      if(elem.getAttributeString("name") == null) {
        println("Namespace: " + elem.getNameSpace)
        elem.generate
      }
      else {
        if (!elementMap.contains(elem.getAttributeString("name"))) {
          elem.level_=(level)
          elem.root_=(true)
          val tempNameList = elem.generate
          tempNameList.foreach {
            case (key, value) => elementMap.put(key, value)
          }
          elementMap.put(elem.getAttributeString("name"), elem)
          println(elem.getAttributeString("name") + " " + elem.level)
        }
      }
    }

    elementMap.foreach {
      case(key, value) => elementList += value
    }
  }
}
