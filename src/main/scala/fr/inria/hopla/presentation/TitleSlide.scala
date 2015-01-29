package fr.inria.hopla.presentation

import fr.inria.hopla.xhtml.{h1, h2}

/**
* Created by geoffroy on 23/01/15.
*/
class TitleSlide(title: String, subtitle: String) extends Slide(h1(title), h2(subtitle))

object TitleSlide {
  def apply(title: String, subtitle: String) : TitleSlide = new TitleSlide(title, subtitle)
}
