package fr.inria.hoplaxhtml.presentation

import fr.inria.hopla.xhtml.Marker
import fr.inria.hoplaxhtml.xhtml.{Block, head, body, html}

import scala.io.Source

/**
* @author Jérémy Bossut, Jonathan Geoffroy
*/
case class Presentation(markers : Seq[Block]) extends html("", head(""), new body("", markers)) {
  override def toHtml : String = {
    val builder = new StringBuilder()

    // Add each slide
    for(child <- markers) {
      builder.append(child.toHtml)
    }

    builder.toString
  }
}
