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
}
