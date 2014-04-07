package fr.inria.hopla.VisitorCase.HtmlTag

import scala.collection.SortedMap

/**
 * Created by JIN Benli on 26/03/14.
 */
trait Modifier {
  def transform(tag: HtmlTag): HtmlTag
}

trait Node extends Modifier {
  override def toString = {
    val strb = new StringBuilder
    writeTo(strb)
    strb.toString()
  }

  def writeTo(strb: StringBuilder): Unit


  def transform(tag: HtmlTag) = {
    tag.copy(children = this :: tag.children)
  }
}

case class HtmlTag(tag: String = "",
                   children: List[Node],
                   attrs: SortedMap[String, String],
                   void: Boolean = false) extends Node {

  def apply(xs: Modifier*) = {
    var newTag = this

    var i = 0
    while (i < xs.length) {
      newTag = xs(i).transform(newTag)
      i += 1
    }
    newTag
  }

  def writeTo(strb: StringBuilder): Unit = {
    // tag
    strb ++= "<" ++= tag

    // attributes
    for ((attr, value) <- attrs) {
      strb ++= " " ++= attr ++= "=\""
      Escaping.escape(value, strb)
      strb ++= "\""
    }
    if (children.isEmpty && void)
    // No children - close tag
      strb ++= " />"
    else {
      strb ++= ">"
      // Childrens
      var x = children.reverse
      while (!x.isEmpty) {
        val child :: newX = x
        x = newX
        child.writeTo(strb)
      }

      // Closing tag
      strb ++= "</" ++= tag ++= ">"
    }
  }
}