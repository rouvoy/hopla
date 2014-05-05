package fr.inria.hopla

import scala.collection.immutable

/**
 * Created by JIN benli on 05/03/14.
 *
 * Factory method for memorise different part of a representation to a element class
 */
trait ScMemory {
  def write: String
}

class ScImport(val name: String) extends ScMemory {
  def write = "import scala.collection.SortedMap\n"

  override def toString: String = "Exporting Import"
}

class ScTags(val buffer: StringBuilder) extends ScMemory{
  def write = buffer.toString()

  override def toString: String = "Exporting Tags\n"
}

class ScPackage(val name: String, val scList: immutable.List[ScMemory]) extends ScMemory {
  def write: String = {
    val buffer: StringBuilder = new StringBuilder
    buffer ++= "package object " ++= name ++= "{\n\n  "
    scList.foreach {
      case(value) => buffer ++= value.write ++= "\n"
    }
    buffer ++= "/**\n   * Allows you to modify a ScalaTag by adding a String to its list of children\n   */\n  implicit def stringNode(v: String) = new StringNode(v)\n\n  /**\n   * Providing string extension to fit into the ScalaTag fragments.\n   * @param s\n   */\n  implicit class StringExtension(s: String) {\n    def tag = {\n      if (!Escaping.validTag(s))\n        throw new IllegalArgumentException(\n          s\"Illegal tag name: $s is not a valid XML tag name\"\n        )\n      ScalaTag(s, Nil, SortedMap.empty)\n    }\n  }\n\n}"
    buffer.toString()
  }

  override def toString: String = "Exporting Package Object\n"
}

class ScTagFunction() extends ScMemory {
  def write: String = {
    "trait Modifier {\n  def transform(tag: ScalaTag): ScalaTag\n}\n\ntrait Node extends Modifier {\n  override def toString = {\n    val strb = new StringBuilder\n    writeTo(strb)\n    strb.toString()\n  }\n\n  def writeTo(strb: StringBuilder): Unit\n\n\n  def transform(tag: ScalaTag) = {\n    tag.copy(children = this :: tag.children)\n  }\n}\n\n/**\n * A tag which contain a String\n * @param v\n */\ncase class StringNode(v: String) extends Node {\n  def writeTo(strb : StringBuilder): Unit = Escaping.escape(v, strb)\n}\n\ncase class ScalaTag(tag: String = \"\",\n                   children: List[Node],\n                   attrs: SortedMap[String, String],\n                   void: Boolean = false) extends Node {\n\n  def apply(xs: Modifier*) = {\n    var newTag = this\n\n    var i = 0\n    while (i < xs.length) {\n      newTag = xs(i).transform(newTag)\n      i += 1\n    }\n    newTag\n  }\n\n  def writeTo(strb: StringBuilder): Unit = {\n    // tag\n    strb ++= \"<\" ++= tag\n\n    // attributes\n    for ((attr, value) <- attrs) {\n      strb ++= \" \" ++= attr ++= \"=\\\"\"\n      Escaping.escape(value, strb)\n      strb ++= \"\\\"\"\n    }\n    if (children.isEmpty && void)\n    // No children - close tag\n      strb ++= \" />\"\n    else {\n      strb ++= \">\"\n      // Childrens\n      var x = children.reverse\n      while (!x.isEmpty) {\n        val child :: newX = x\n        x = newX\n        child.writeTo(strb)\n      }\n\n      // Closing tag\n      strb ++= \"</\" ++= tag ++= \">\"\n    }\n  }\n}"
  }

  override def toString: String = "Exporting ScalaTag class definition\n"
}

class ScDefTag(val tags: ScTags) extends ScMemory{
  def write: String = {
    "object Tags extends Tags\n\ntrait Tags {\n  " + tags.write +  "\n}"
  }

  override def toString: String = "Exporting Tags Object and Class definition\n"
}

class ScEscapeFun() extends ScMemory{
  def write: String = {
    "object Escaping {\n\n  private[this] val tagRegex = \"^[a-z][\\\\w0-9-]*$\".r\n\n  /**\n   * Uses a regex to check if something is a valid tag name.\n   */\n  def validTag(s: String) = true //tagRegex.unapplySeq(s).isDefined\n\n  private[this] val attrNameRegex = \"^[a-zA-Z_:][-a-zA-Z0-9_:.]*$\".r\n\n  /**\n   * Uses a regex to check if something is a valid attribute name.\n   */\n  def validAttrName(s: String) = attrNameRegex.unapplySeq(s).isDefined\n\n  /**\n   * Code to escape text HTML nodes. Taken from scala.xml\n   */\n  def escape(text: String, s: StringBuilder) = {\n    // Implemented per XML spec:\n    // http://www.w3.org/International/questions/qa-controls\n    // imperative code 3x-4x faster than current implementation\n    // dpp (David Pollak) 2010/02/03\n    val len = text.length\n    var pos = 0\n    while (pos < len) {\n      text.charAt(pos) match {\n        case '<' => s.append(\"&lt;\")\n        case '>' => s.append(\"&gt;\")\n        case '&' => s.append(\"&amp;\")\n        case '\"' => s.append(\"&quot;\")\n        case '\\n' => s.append('\\n')\n        case '\\r' => s.append('\\r')\n        case '\\t' => s.append('\\t')\n        case c => if (c >= ' ') s.append(c)\n      }\n\n      pos += 1\n    }\n  }\n}"
  }

  override def toString: String = "Exporting Escape Funtion definition\n"
}