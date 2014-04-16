package fr.inria.hopla.visitorCase.HtmlTag

import fr.inria.hopla.{Parser, VisitorMode}
import scala.collection.{SortedMap, mutable}

/**
 * Created by JIN Benli on 07/04/14.
 */
class TagVisitor(val filename: String) extends VisitorMode {
  val parser: Parser = new Parser(filename)
  parser.parse()

  val tagBuffer: mutable.HashMap[String, HtmlTag] = new mutable.HashMap[String, HtmlTag]()
  val s: mutable.StringBuilder = new mutable.StringBuilder()

  def createTagsFromFile() {
    for (p <- parser.elementList) {
      val name = p.getAttributeString("name")
      //      println(name)
      val temp = p.getAttributeString("name").toString.tag
      if (!tagBuffer.contains(name)) {
        tagBuffer.put(name, temp)
      }
    }
    tagBuffer.foreach {
      case (key, value) =>
        s ++= "val " ++= key ++= " = \"" ++= key ++= "\".tag\n  "
    }
  }

  def printAllTags() {
    println(s.toString())
  }
}
