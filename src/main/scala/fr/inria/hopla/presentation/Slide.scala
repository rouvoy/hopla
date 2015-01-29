package fr.inria.hopla.presentation

import fr.inria.hopla.xhtml.{Flow, div}

/**
* @author Jérémy Bossut, Jonathan Geoffroy
*/
case class Slide (flow: Flow*) extends div("", flow) {
  override def toHtml: String = {
    val builder = new StringBuilder("<section>")

    for(child <- flow) {
      builder.append(child.toHtml)
    }

    builder.append("</section>")
    builder.toString()
  }
}
