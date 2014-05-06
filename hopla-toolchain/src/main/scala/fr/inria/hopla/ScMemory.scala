package fr.inria.hopla

import scala.collection.immutable
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * Created by JIN benli on 05/03/14.
 *
 * Factory method for memorise different part of a representation to a element class
 */
trait ScMemory {
  def write: String
}

class ScImport(val name: String) extends ScMemory {
  def write = "import " + name + ".Tags\n"
    "import scala.collection.SortedMap\n" +
    "import scala.collection.mutable\n" +
    "import scala.collection.mutable.ListBuffer\n" +
    "import scala.collection.SortedMap\n"

  override def toString: String = "Exporting Import"
}

class ScTags(val buffer: mutable.StringBuilder) extends ScMemory {
  def write = buffer.toString()

  override def toString: String = "Exporting Tags\n"
}

class ScPackage(val name: String, val scList: immutable.List[ScMemory]) extends ScMemory {
  def write: String = {
    val buffer: mutable.StringBuilder = new mutable.StringBuilder()
    buffer ++= "package object " ++= name ++= "{\n\n  "
    scList.foreach {
      case (value) => buffer ++= value.write ++= "\n"
    }
    buffer ++= "/**\n   " +
      "* Allows you to modify a ScalaTag by adding a String to its list of children\n   " +
      "*/\n  implicit def stringNode(v: String) = new StringNode(v)\n\n  " +
      "/**\n   * Providing string extension to fit into the ScalaTag fragments.\n   " +
      "* @param s\n   */\n  implicit class StringExtension(s: String) " +
      "{\n    " +
      "def tag = {\n      " +
      "if (!Escaping.validTag(s))\n        " +
      "throw new IllegalArgumentException(\n          " +
      "s\"Illegal tag name: $s is not a valid XML tag name\"\n        " +
      ")\n      ScalaTag(s, Nil, SortedMap.empty)\n    }\n  }\n\n}"
    buffer.toString()
  }

  override def toString: String = "Exporting Package Object\n"
}

class ScTagFunction() extends ScMemory {
  def write: String = {
    "trait Modifier {\n  " +
      "def transform(tag: ScalaTag): ScalaTag\n    " +
      "def getName: String\n}\n\n" +
      "trait Node extends Modifier {\n  " +
      "override def toString = {\n    " +
      "val strb = new StringBuilder\n    " +
      "writeTo(strb)\n    " +
      "strb.toString()\n  }\n\n  " +
      "def writeTo(strb: StringBuilder): Unit\n\n\n  " +
      "def transform(tag: ScalaTag) = {\n    " +
      "tag.copy(children = this :: tag.children)\n  }\n}\n\n" +
      "/**\n * A tag which contain a String\n * @param v\n */\n" +
      "case class StringNode(v: String) extends Node {\n  " +
      "override def getName = null\n  " +
      "def writeTo(strb : StringBuilder): Unit = Escaping.escape(v, strb)\n}\n\n" +
      "case class ScalaTag(tag: String = \"\",\n                   " +
      "children: List[Node],\n                   " +
      "attrs: SortedMap[String, String],\n                   " +
      "void: Boolean = false) extends Node {\n\n  " +
      "override def getName = tag\n  " +
      "def apply(xs: Modifier*) = {\n      " +
      "var newTag = this\n      " +
      "val buffer: ListBuffer[String] = new ListBuffer[String]\n      " +
      "println(newTag.getName + \"***\")\n      " +
      "if(map.contains(newTag.getName))\n        " +
      "buffer ++= map.get(newTag.getName).get\n\n      " +
      "println(buffer)\n\n      " +
      "var i = 0\n      " +
      "while (i < xs.length) {\n        " +
      "xs(i) match {\n          " +
      "case x: ScalaTag => {\n            " +
      "println(xs(i).getName + \" \" + xs(i).getClass)\n            " +
      "if(buffer.contains(xs(i).getName)) {\n              " +
      "println(\"transform: \" + xs(i).getName)\n              " +
      "newTag = xs(i).transform(newTag)\n            }\n            " +
      "else if(buffer != null)\n              " +
      "throw new IllegalArgumentException(\"illegal child: \" + xs(i).getName)\n            " +
      "else\n              println(\"case not handled\")\n          }\n          " +
      "case x: StringNode => {\n            " +
      "println(\"String node \" + xs(i).getClass)\n            " +
      "newTag = xs(i).transform(newTag)\n          }\n          " +
      "case _ =>\n        }\n        i += 1\n      }\n      newTag\n    }" +
      "def writeTo(strb: StringBuilder): Unit = {\n    // tag\n    " +
      "strb ++= \"<\" ++= tag\n\n    // attributes\n    " +
      "for ((attr, value) <- attrs) {\n      " +
      "strb ++= \" \" ++= attr ++= \"=\\\"\"\n      " +
      "Escaping.escape(value, strb)\n      " +
      "strb ++= \"\\\"\"\n    }\n    " +
      "if (children.isEmpty && void)\n    // No children - close tag\n      " +
      "strb ++= \" />\"\n    else {\n      " +
      "strb ++= \">\"\n      // Childrens\n      " +
      "var x = children.reverse\n      " +
      "while (!x.isEmpty) {\n        " +
      "val child :: newX = x\n        " +
      "x = newX\n        " +
      "child.writeTo(strb)\n      }\n\n      // Closing tag\n      " +
      "strb ++= \"</\" ++= tag ++= \">\"\n    }\n  }\n}"
  }

  override def toString: String = "Exporting ScalaTag class definition\n"
}

class ScDefTag(val tags: ScTags) extends ScMemory {
  def write: String = {
    "object Tags extends Tags\n\ntrait Tags {\n  " + tags.write + "\n}"
  }

  override def toString: String = "Exporting Tags Object and Class definition\n"
}

class ScEscapeFun() extends ScMemory {
  def write: String = {
    "object Escaping {\n\n  private[this] val tagRegex = \"^[a-z][\\\\w0-9-]*$\".r\n\n" +
      "  /**\n   * Uses a regex to check if something is a valid tag name.\n   */\n  " +
      "def validTag(s: String) = true //tagRegex.unapplySeq(s).isDefined\n\n  " +
      "private[this] val attrNameRegex = \"^[a-zA-Z_:][-a-zA-Z0-9_:.]*$\".r\n\n  " +
      "/**\n   * Uses a regex to check if something is a valid attribute name.\n   */\n  " +
      "def validAttrName(s: String) = attrNameRegex.unapplySeq(s).isDefined\n\n  /**\n   " +
      "* Code to escape text HTML nodes. Taken from scala.xml\n   */\n  " +
      "def escape(text: String, s: StringBuilder) = {\n    " +
      "// Implemented per XML spec:\n    " +
      "// http://www.w3.org/International/questions/qa-controls\n    " +
      "// imperative code 3x-4x faster than current implementation\n    " +
      "// dpp (David Pollak) 2010/02/03\n    " +
      "val len = text.length\n    " +
      "var pos = 0\n    " +
      "while (pos < len) {\n      " +
      "text.charAt(pos) match {\n        " +
      "case '<' => s.append(\"&lt;\")\n        " +
      "case '>' => s.append(\"&gt;\")\n        " +
      "case '&' => s.append(\"&amp;\")\n        " +
      "case '\"' => s.append(\"&quot;\")\n        " +
      "case '\\n' => s.append('\\n')\n        " +
      "case '\\r' => s.append('\\r')\n        " +
      "case '\\t' => s.append('\\t')\n        " +
      "case c => if (c >= ' ') s.append(c)\n      }\n\n      " +
      "pos += 1\n    }\n  }\n}"
  }

  override def toString: String = "Exporting Escape function definition\n"
}

class ScRelation(val rel: mutable.HashMap[String, ListBuffer[String]]) extends ScMemory {
  private var noRel = true

  def write: String = {
    val buffer: mutable.StringBuilder = new mutable.StringBuilder()
    buffer ++= "    val map = new mutable.HashMap[String, ListBuffer[String]]\n"
    rel.foreach {
      case (key, value) =>
        if(value != null && value.nonEmpty)
          noRel = false
    }
    var count = 1
    rel.foreach {
      case (key, value) =>
        if(value != null && value.nonEmpty) {
          buffer ++= "    val temp" ++= count.toString ++= "= new ListBuffer[String]\n"
          value.foreach(s => buffer ++= "    temp" ++= count.toString ++= " += \"" ++= s ++= "\"\n")
          buffer ++= "    map.put(" ++= "\"" ++= key ++= "\", "
          buffer ++= "temp" ++= count.toString ++= ")\n"
          count += 1
        }
      case _ =>
    }
    if(noRel)
      ""
    else {
      buffer.toString()
    }
  }

  override def toString: String = "Exporting Relation\n"

}