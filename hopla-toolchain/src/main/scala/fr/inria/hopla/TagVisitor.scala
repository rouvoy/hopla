//package fr.inria.hopla
//
//import scala.collection.mutable
//import scala.collection.mutable.ListBuffer
//
///**
// * Created by JIN Benli on 07/04/14.
// */
//class TagVisitor(val filename: String) extends VisitorMode {
//  val parser: Parser = new Parser(filename)
//  parser.parse()
//
//  val tagBuffer: ListBuffer[String] = new ListBuffer[String]()
//  val s: mutable.StringBuilder = new mutable.StringBuilder()
//
//  def createTagsFromFile() {
//    for (p <- parser.elementList) {
//      val name = p.getAttributeString("name")
//
//      //      println(name)
//      if (!tagBuffer.contains(name)) {
//        tagBuffer += name
//      }
//    }
//    tagBuffer.foreach {
//      case (value) =>
//        s ++= "val " ++= value ++= " = \"" ++= value ++= "\".tag\n  "
//    }
//  }
//
//  def visitAttributes(e: Xsd) {
//    val attributes = e.getAttributes
//
////    val buffer: StringBuilder = new StringBuilder()
//    for(attr <- attributes) attr.key match {
//      case "name" => println("name: " + attr.value)
//      case "type" => println("type: " + attr.value)
//      case "minOccurs" => println("minOcccurs: " + attr.value)
//      case _ =>
//    }
//  }
//
//  def printAllTags() {
//    println(s.toString())
//  }
//}
