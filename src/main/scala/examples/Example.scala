package examples

import examples.xhtml._

/**
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
object Example {
  html(
    body(
      div(
        h1("A Title"),
        div(
          h2("A subtitle"),
          p("some text"),
          p(a("a link") href "http://www.google.com")
        ),

        div(
          h2("A second subtitle"),
          p("some text")
        )
      )
    )
  )
}
