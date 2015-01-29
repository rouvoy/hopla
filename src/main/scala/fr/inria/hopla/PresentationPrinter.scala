package fr.inria.hopla

import examples.Example

/**
* @author Jérémy Bossut, Jonathan Geoffroy
*/
object PresentationPrinter extends App {
  override def main (args: Array[String]): Unit = {
    System.out.println(Example.presentation.toHtml)
  }
}
