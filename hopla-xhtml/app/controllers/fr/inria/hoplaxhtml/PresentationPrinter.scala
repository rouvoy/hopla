package fr.inria.hoplaxhtml

import fr.inria.hoplaxhtml.examples.Example

/**
* @author Jérémy Bossut, Jonathan Geoffroy
*/
object PresentationPrinter extends App {
  override def main (args: Array[String]): Unit = {
    println(Example.presentation.toHtml)
  }
}
