package fr.inria.hopla.xhtml

import org.scalatest.{Matchers, FlatSpec}

/**
* @author Jérémy Bossut, Jonathan Geoffroy
*/

class MarkerTest extends FlatSpec with Matchers {
  "Nested Scala markers" should "generate nested html markers" in {
    val scalaHtml =
      html(
        body(
          div(
            p("some text")
          )
        )
      )

    val stringHtml = "<html><body><div><p>some text</p></div></body></html>"
    val toHtml = scalaHtml.toHtml
    assert(scalaHtml.toHtml equals stringHtml)
  }

  "Consecutive Scala markers" should "generate consecutive html markers" in {
    val scalaHtml =
      div(
        p("first paragraph"),
        p("second paragraph")
      )

    val stringHtml = "<div><p>first paragraph</p><p>second paragraph</p></div>"
    assert(scalaHtml.toHtml equals stringHtml)
  }

  "Scala fields" should "generate one html attribute" in {
    val scalaHtml = fr.inria.hopla.xhtml.a("google link") href "http://www.google.com"

    val stringHtml = """<a href = "http://www.google.com">google link</a>"""
    assert(scalaHtml.toHtml equals stringHtml)
  }

  "Scala fields" should "generate two html attributes" in {
    val scalaHtml = fr.inria.hopla.xhtml.a("google link") href "http://www.google.com" _type "theType"

    val stringHtml = """<a type = "theType" href = "http://www.google.com">google link</a>"""
    assert(scalaHtml.toHtml equals stringHtml)
  }
}
