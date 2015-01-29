package fr.inria.hopla.presentation

import fr.inria.hopla.xhtml._

import scala.io.Source

/**
* @author Jérémy Bossut, Jonathan Geoffroy
*/
case class Presentation(slides : Slide*) extends html("", head(""), new body("", slides)) {
  override def toHtml : String = {
    val builder = new StringBuilder(Source.fromFile("src/main/resources/reveal_top.html").mkString)

    // Add each slide
    for(child <- slides) {
      builder.append(child.toHtml)
    }

    builder.append(Source.fromFile("src/main/resources/reveal_bottom.html").mkString)

    builder.toString
  }
}
