package fr.inria.hoplaxhtml.presentation

import fr.inria.hoplaxhtml.xhtml.{head, body, html}

import scala.io.Source

/**
* @author Jérémy Bossut, Jonathan Geoffroy
*/
case class Presentation(slides : Slide*) extends html("", head(""), new body("", slides)) {
  override def toHtml : String = {
    val builder = new StringBuilder(Source.fromFile("hopla-xhtml/src/main/resources/reveal_top.html").mkString)

    // Add each slide
    for(child <- slides) {
      builder.append(child.toHtml)
    }

    builder.append(Source.fromFile("hopla-xhtml/src/main/resources/reveal_bottom.html").mkString)

    builder.toString
  }
}
