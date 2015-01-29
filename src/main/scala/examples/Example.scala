package examples

import fr.inria.hopla.presentation._
import fr.inria.hopla.presentation.title
import fr.inria.hopla.xhtml._

/**
* @author Jérémy Bossut, Jonathan Geoffroy
*/
object Example {
  val presentation = Presentation(
    TitleSlide("Hopla","Reveal.js Example"),
    Slide(
      Slide(
        title("Presentation"),
        p(bolditalic("Some text about the presentation "), url("and a link", "http://www.google.com"))
      ),
      Slide(
        title("Presentation2"),
        p("Some text about the presentation"),
        image("https://images.duckduckgo.com/iu/?u=http%3A%2F%2Fwww.wired.com%2Fimages_blogs%2Fbusiness%2F2012%2F12%2Fgoogle.jpg&f=1") width "400" height "300"
      )
    )
  )
}
