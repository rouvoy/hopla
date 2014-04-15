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

  override def toString = "class " + value(0)
}

case class ScVariable(value: List[Any]) extends ScClass(value: List[Any]) {
  override var pos = 2

  override def toString = ""
}

case class ScParam(value: List[Any]) extends ScClass(value: List[Any]) {
  override var pos = 3
  val typeName = value(0) match {
    case "xs:string" => "s: String"
    // possible on adding new type
  }

  override def toString = typeName
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

  override def toString = s.toString()
}

object ScFunction extends ScMemory {
  def apply(kind: String, value: List[Any]) = kind match {
    case "name" => new ScName(value)
    case "param" => new ScParam(value)
    case "body" => new ScBody(value)
    case "var" => new ScVariable(value)
  }
}


class ScTags(val buffer: StringBuilder) {
  def write = buffer.toString()

  override def toString: String = "Exporting Tags\n"
}

class ScPackage(val name: String) {
  def write: String = {
    "package object " + name + "{\n\n  /**\n   * Allows you to modify a HtmlTag by adding a String to its list of children\n   */\n  implicit def stringNode(v: String) = new StringNode(v)\n\n  /**\n   * Providing string extension to fit into the ScalaTag fragments.\n   * @param s\n   */\n  implicit class StringExtension(s: String) {\n    def tag = {\n      if (!Escaping.validTag(s))\n        throw new IllegalArgumentException(\n          s\"Illegal tag name: $s is not a valid XML tag name\"\n        )\n      ScalaTag(s, Nil, SortedMap.empty)\n    }\n  }\n\n}"
  }

  override def toString: String = "Exporting Package Object\n"
}

class ScTagFunction() {
  def write: String = {
    "class ScalaTag(tag: String = \"\",\n                   children: List[Node],\n                   attrs: SortedMap[String, String],\n                   void: Boolean = false) {\n\n  def apply(xs: Modifier*) = {\n    var newTag = this\n\n    var i = 0\n    while (i < xs.length) {\n      newTag = xs(i).transform(newTag)\n      i += 1\n    }\n    newTag\n  }\n\n  def writeTo(strb: StringBuilder): Unit = {\n    // tag\n    strb ++= \"<\" ++= tag\n\n    // attributes\n    for ((attr, value) <- attrs) {\n      strb ++= \" \" ++= attr ++= \"=\\\"\"\n      Escaping.escape(value, strb)\n      strb ++= \"\\\"\"\n    }\n    if (children.isEmpty && void)\n    // No children - close tag\n      strb ++= \" />\"\n    else {\n      strb ++= \">\"\n      // Childrens\n      var x = children.reverse\n      while (!x.isEmpty) {\n        val child :: newX = x\n        x = newX\n        child.writeTo(strb)\n      }\n\n      // Closing tag\n      strb ++= \"</\" ++= tag ++= \">\"\n    }\n  }\n}"
  }

  override def toString: String = "Exporting ScalaTag class definition"
}

class ScDefTag {
  def write: String = {
    ""
  }
}
