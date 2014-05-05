package fr.inria.hopla

/**
 * Created by JIN Benli on 26/03/14.
 */
trait Visitor {
  val parser: Parser
}

class LevelVisitor(val fileName: String) extends Visitor {

  val parser: Parser = new Parser(fileName)
  parser.parse()

  def visitByLevel() {
    for (pa <- parser.elementList) {
      val p = pa.asInstanceOf[Element]
      if (p.ref) {
        print("Visiting reference element to element: " + p.getName + " level: " + p.level)
        if (p.parent != null)
          println(" parent: " + p.parent.getName)
        else
          println()
      }
      else {
        print("Visiting element: " + p.getName + " level: " + p.level)
        if (p.parent != null)
          println(" parent: " + p.parent.getName)
        else
          println()
      }
    }
  }
}

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

