package fr.inria.hopla.visitorCase.HtmlTag

import fr.inria.hopla.{Parser, VisitorMode}
import scala.collection.mutable

/**
 * Created by JIN Benli on 07/04/14.
 */
class HtmlTagVisitor(val filename: String) extends VisitorMode {
  val parser: Parser = new Parser(filename)
  parser.parse()

  val tagBuffer: mutable.HashMap[String, HtmlTag] = new mutable.HashMap[String, HtmlTag]()

  def createTagsFromFile() {
    for(p <- parser.elementList) {
      val name = p.getAttributeString("name").toString
      val temp = p.getAttributeString("name").toString.tag
      tagBuffer.put(name, temp)
    }
  }
}
