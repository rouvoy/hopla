package fr.inria.hopla.visitorCase.HtmlTag

/**
 * Created by JIN Benli on 26/03/14.
 */

object HtmlTagTest extends Tags {
  def main(args: Array[String]) {
    val res = html(
      head(),
      body(
        h1("Hello Test")
      )
    )
    println(res)
  }
}