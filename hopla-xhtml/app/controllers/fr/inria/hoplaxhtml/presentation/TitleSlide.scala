package fr.inria.hoplaxhtml.presentation

import fr.inria.hoplaxhtml.xhtml.{h2, h1}

/**
* Created by geoffroy on 23/01/15.
*/
class TitleSlide(title: String, subtitle: String) extends Slide(h1(title), h2(subtitle))

object TitleSlide {
  def apply(title: String, subtitle: String) : TitleSlide = new TitleSlide(title, subtitle)
}
