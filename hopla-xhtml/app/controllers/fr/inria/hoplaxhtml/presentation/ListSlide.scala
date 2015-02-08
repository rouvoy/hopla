package controllers.fr.inria.hoplaxhtml.presentation

import fr.inria.hoplaxhtml.presentation.{TitleSlide, Slide}
import fr.inria.hoplaxhtml.xhtml._

/**
 * Created by geoffroy on 06/02/15.
 */
class ListSlide(title: String, listItems : Seq[li]) extends Slide(h1(title), new ul("", listItems))

object ListSlide {
  def apply(title: String, items: String*): ListSlide = new ListSlide(title, items.map(item => li(item)))
}
