//package fr.inria.hopla
//
//import scala.collection.mutable.ListBuffer
//
///**
//* Created by JIN Benli on 26/03/14.
//*/
//trait VisitorMode {
//  val parser: Parser
//}
//
//class Visitor(val fileName: String) extends VisitorMode {
//
//  val parser: Parser = new Parser(fileName)
//  parser.parse()
//
//  def visitByLevel(level: Int) {
//    println("\n**Begin to visit level " + level + " parents\n")
//    for (p <- parser.elementList) {
//      if (p.level == level) {
//        println("Visiting " + p.getAttributeString("name") + " Level = " + p.level + "\n")
//        if (p.childs.nonEmpty)
//          visitChild(p.childs)
//      }
//    }
//  }
//
//  def visitChild(childList: ListBuffer[Element]) {
//    for (c <- childList) {
//      println("Visiting " + c.getAttributeString("name") + " Children level = " + c.level)
//      print(c.getAttributeString("name") + "'s parent is/are ")
//      for (p <- c.parent) {
//        println(p.getAttributeString("name"))
//      }
//
//      //      println()
//      if (c.childs.nonEmpty) {
//        visitChild(c.childs)
//      }
//    }
//  }
//}
//
