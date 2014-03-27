package fr.inria.hopla

import scala.collection.mutable.ListBuffer

/**
 * Created by JIN Benli on 26/03/14.
 */
class Visitor(val fileName: String) {

  val parser: Parser = new Parser(fileName)
  parser.parse()

  def visitByLevel(level: Int) {
    println("\n**Begin to visit level " + level + " parents")
    for (p <- parser.elementList) {
      if (p.level == level) {
        println("Visiting " + p.getAttributeString("name") + " Level = " + p.level)
        if (p.childs.nonEmpty)
          visitChilds(p.childs)
      }
    }
  }

  def visitChilds(childList: ListBuffer[Element]) {
    for (c <- childList) {
      println("Visiting " + c.getAttributeString("name") + " Children level = " + c.level)
      print(c.getAttributeString("name") + "'s parent is/are ");
      for (p <- c.parent) {
        println(p.getAttributeString("name"))
      }
      println()
    }
  }
}

