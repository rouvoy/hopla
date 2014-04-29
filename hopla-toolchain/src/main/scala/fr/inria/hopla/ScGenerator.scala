//package fr.inria.hopla
//
///**
//* Created by JIN Benli on 05/03/14.
//*
//* Test function for creating element scala class, version naif
//*/
//
//object ScGenerator {
//  def main(args: Array[String]) {
//    val parser = new Parser("address.xsd")
//    parser.parse()
//    for (c <- parser.elementList) {
//      //      println(c.getAttributeString("name"))
//      val scVisitor = new ScalaVisitor(c)
//      val bufferList = scVisitor.elemBuffer.toList
//      var buffer = ""
//      for (b <- bufferList) {
//        if (b.getPos == 1) {
//          buffer += b.write + " ("
//        }
//      }
//      for (b <- bufferList) {
//        if (b.getPos == 3) {
//          buffer += b.write + ") {"
//        }
//      }
//
//      for (b <- bufferList) {
//        if (b.getPos == 4) {
//          buffer += "\n\t" + b.write + "\n"
//        }
//      }
//      buffer += "}"
//      println(buffer)
//    }
//  }
//}