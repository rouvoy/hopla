package fr.inria.hoplaxhtml.examples

import controllers.fr.inria.hoplaxhtml.presentation.ListSlide
import fr.inria.hoplaxhtml.presentation._
import fr.inria.hoplaxhtml.presentation.title
import fr.inria.hoplaxhtml.xhtml._

/**
* @author Jérémy Bossut, Jonathan Geoffroy
*/
object Example {
  val hoplaHoverView = Seq[Block](
    TitleSlide("Hopla","The Scala Presentation Framework"),
    Slide(
      Slide(
        title("Overview"),
        p("Hopla enables you to create slide decks using a Scala DSL (Domain Specific Langage) instead of HTML."),
        image("https://s3.amazonaws.com/hakim-static/reveal-js/arrow.png")
      ),
      Slide(
        p(span("So you can create beautiful HTML5 presentations using "), italic("Javascript frameworks"), span(", with Scala convenience."))
      ),
      ListSlide(
        "Which means:",
        "less code to write",
        "more human-readable",
        "and all Scala features"
      )
    ),
    Slide(
      Slide(
        title("Possibilities"),
        p("Hopla knows almost every HTML tag,"),
        p("so you can easily write web pages.")
      ),
      Slide(
        title("Such as images"),
        div(
          image("http://www.w3.org/html/logo/downloads/HTML5_Badge_512.png") width("128px") height("128px"),
          image("http://fruzenshtein.com/wp-content/uploads/2013/09/scala-logo.png") width("128px") height("128px"),
          image("https://www.playframework.com/assets/images/logos/play_icon_full_color.svg") width("128px") height("128px")
        )
      ),
      Slide(
        h1("Titles"),
        h2("Subtitles"),
        h3("And so on!")
      ),
      Slide (
        p(bold("But also "), italic("text"), bolditalic(" decoration")),
        p(span("and "), url("links", "https://github.com/rouvoy/hopla"), span("."))
      )
    )
  )

  val howItWorks = Seq[Block](
    Slide(
      TitleSlide("How it works", "Hopla Black Magic"),
      Slide(
        title("Main steps"),
        ol(
          li("DSL generation from XSD definition"),
          li("HTML generation from your Hopla code"),
          li("Run the presentation on the web")
        )
      ),
      Slide(
        title("DSL generation"),
        p("In order to generate a DSL, Hopla:"),
        ul(
          li("Reads the XSD file"),
          li("Transforms it into Scala files"),
          li("Compiles them automatically")
        )
      ),
      Slide(
        title("DSL improvement"),
        p("You can improve this DSL by extending any tag class!")
      ),
      Slide(
        title("HTML generation"),
        p("Hopla generates HTML content"),
        p("and displays it on the web using Play server.")
      )
    )
  )

  val finalWord = Seq[Block](
    Slide(
      title("So Hopla is"),
      p("Based on HTML5 frameworks..."),
      p("but really more convenient!")
    ),
    TitleSlide("And of course...", "This presentation is entirely written in Hopla!"),
    Slide(
      h2(span("Fork Hopla on "), url("GitHub", "https://github.com/rouvoy/hopla")),
      h3("Jérémy Bossut - Jonathan Geoffroy"),
      a(image("http://willnielsen.com/image/1/350/0/0/secure/images/github-mark.png") width ("128px") height ("128px")) href("https://github.com/rouvoy/hopla")
    )
  )

  val presentation = Presentation(hoplaHoverView  ++ howItWorks ++ finalWord)

}
