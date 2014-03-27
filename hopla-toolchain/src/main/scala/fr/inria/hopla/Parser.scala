package fr.inria.hopla

import scala.collection.mutable.ListBuffer

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
  val element = xsdFile \\ "element"
  var temp: ListBuffer[Element] = new ListBuffer[Element]
  val elementList: ListBuffer[Element] = new ListBuffer[Element]

  /**
   * Set the first element as root, creating all element from the root, using
   * ListBuffer temp for storing child list for every iteration of generation,
   * Initially the temp should be empty so we can figure out where is the root
   *
   */
  def parse() {
    var level = 1
    for (e <- element) {
      //      println(e)
      val elem = new Element(e)
      var exist = false
      //      println(elem.getAttributeString("name"))
      if (!temp.isEmpty) {
        for (t <- temp) {
          //          println(t.getAttributeString("name"))
          if (t.getAttributeString("name") == elem.getAttributeString("name") && !elementList.contains(t)) {
            elementList += t
            //            println(elem.getAttributeString("name"))
            exist = true
          }
        }
        if (!exist) {
          elem.level_=(level)
          elem.generate
          elem.setParentAndLevel
          temp = elem.childs
          level += 1
          elementList += elem
          //          println(elem.getAttributeString("name"))
        }
      }
      else {
        elem.level_=(level)
        elem.generate
        elem.setParentAndLevel
        elem.root_=(true)
        temp = elem.childs
        level += 1
        elementList += elem
        //        println(elem.getAttributeString("name"))
      }
    }
  }
}
