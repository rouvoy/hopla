package fr.inria.hopla

/**
 * Created by JIN benli on 05/03/14.
 *
 * Factory method for memorise different part of a representation to a element class
 */
trait ScMemory

abstract class ScClass(value: List[Any]) {
  var toString: String
  var pos: Int

  def write = toString

  def getPos = pos
}

class ScName(value: List[Any]) extends ScClass(value: List[Any]) {
  override var pos = 1
  override var toString = "class " + value(0)
}

class ScVariable(value: List[Any]) extends ScClass(value: List[Any]) {
  override var pos = 2
  override var toString = ""
}

class ScParam(value: List[Any]) extends ScClass(value: List[Any]) {
  override var pos = 3
  val typeName = value(0) match {
    case "xs:string" => "s: String"
    // possible on adding new type
  }
  override var toString = typeName
}

class ScBody(value: List[Any]) extends ScClass(value: List[Any]) {
  override var pos = 4
  val funName = value(0).toString
  override var toString = "def " + funName + " = "
  for (i <- 1 to value.length - 1)
    value(i) match {
      case num: Int => toString += value(i)
      case list: List[Element] =>
        if (!list.isEmpty) {
          toString += "["
          for (l <- list if !l.equals(list.last)) {
            toString += "\"" + l.getAttributeString("name") + "\","
          }
          toString += "\"" + list.last.getAttributeString("name") + "\""
          toString += "]"
        }
        else {
          toString += "null"
        }
    }

}

object ScFunction extends ScMemory {
  def apply(kind: String, value: List[Any]) = kind match {
    case "name" => new ScName(value)
    case "param" => new ScParam(value)
    case "body" => new ScBody(value)
    case "var" => new ScVariable(value)
  }
}