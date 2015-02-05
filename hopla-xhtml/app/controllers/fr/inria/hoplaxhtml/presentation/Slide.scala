package fr.inria.hoplaxhtml.presentation

import fr.inria.hoplaxhtml.xhtml.{div, Flow}

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
