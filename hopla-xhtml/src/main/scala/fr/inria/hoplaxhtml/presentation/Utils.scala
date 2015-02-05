package fr.inria.hoplaxhtml.presentation

import fr.inria.hoplaxhtml.xhtml._

/**
* @author Jérémy Bossut, Jonathan Geoffroy
*/
case class title(title: String) extends h1(title, Seq[Inline]())
case class subtitle(subtitle: String) extends h2(subtitle, Seq[Inline]())
case class italic(italicText: String = "", markers: Seq[Inline]) extends em(italicText, markers)
case class bold(boldText: String = "", markers: Seq[Inline]) extends strong(boldText, markers)
case class bolditalic(bolditalicText: String) extends strong("", Seq[Inline](em(bolditalicText)))
case class url(linkText : String, linkSrc : String, markers : Seq[a_content]) extends a(linkText, markers)
case class image() extends img("")

object image {
  def apply(link : String): img = new img("") src link
}

object url {
  def apply(linkText : String, linkSrc : String) : a = new a(linkText, Seq[a_content]()) href linkSrc
}