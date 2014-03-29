package fr.inria.hopla

/**
 * Created by JIN benli on 05/03/14.
 *
 * Factory method for memorise different part of a representation to a element class
 */
trait ScMemory

abstract class ScClass(value: List[Any]) {
  var pos: Int

  def write = this.toString

  def getPos = pos
}

case class ScName(value: List[Any]) extends ScClass(value: List[Any]) {
  override var pos = 1

  override def toString(): String = "class " + value(0)
}

case class ScVariable(value: List[Any]) extends ScClass(value: List[Any]) {
  override var pos = 2

  override def toString(): String = ""
}

case class ScParam(value: List[Any]) extends ScClass(value: List[Any]) {
  override var pos = 3
  val typeName = value(0) match {
    case "xs:string" => "s: String"
    // possible on adding new type
  }

  override def toString(): String = typeName
}

case class ScBody(value: List[Any]) extends ScClass(value: List[Any]) {
  override var pos = 4
  var s: StringBuilder = new StringBuilder()
  val funName = value(0).toString
  s ++= "def " ++= funName ++= " = "
  for (i <- 1 to value.length - 1) {
    value(i) match {
      case num: Int => s ++= value(i).toString
      case list: List[Element] =>
        if (!list.isEmpty) {
          s ++= "["
          for (l <- list if !l.equals(list.last)) {
            s ++= "\"" ++= l.getAttributeString("name") ++= "\","
          }
          s ++= "\"" ++= list.last.getAttributeString("name") ++= "\""
          s ++= "]"
        }
        else {
          s ++= "null"
        }
    }
  }
  override def toString(): String = s.toString()
}

object ScFunction extends ScMemory {
  def apply(kind: String, value: List[Any]) = kind match {
    case "name" => new ScName(value)
    case "param" => new ScParam(value)
    case "body" => new ScBody(value)
    case "var" => new ScVariable(value)
  }
}