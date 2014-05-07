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
  def write = "import " + name + ".Tags\n" +
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
    buffer ++= "package object " ++= name ++= "{\n"
    scList.foreach {
      case (value) => buffer ++= value.write ++= "\n"
    }
    buffer ++= "  /**\n   " +
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

class ScTagFunction(map: Boolean) extends ScMemory {
  def write: String = {
    val buffer = new mutable.StringBuilder()
    buffer ++=
    "  trait Modifier {\n\t" +
      "def transform(tag: ScalaTag): ScalaTag\n\t" +
      "def getName: String\n  }\n\n  " +
      "trait Node extends Modifier {\n\t" +
      "override def toString = {\n\t  " +
      "val strb = new StringBuilder\n\t  " +
      "writeTo(strb)\n\t  " +
      "strb.toString()\n\t  }\n\n\t" +
      "def writeTo(strb: StringBuilder): Unit\n\n\t" +
      "def transform(tag: ScalaTag) = {\n\t  " +
      "tag.copy(children = this :: tag.children)\n\t}\n  }\n\n  " +
      "/**\n   * A tag which contain a String\n   * @param v\n   */\n  " +
      "case class StringNode(v: String) extends Node {\n\t" +
      "override def getName = null\n\t" +
      "def writeTo(strb : StringBuilder): Unit = Escaping.escape(v, strb)\n  }\n\n  " +
      "case class ScalaTag(tag: String = \"\",\n\t\t\t\t\t  " +
      "children: List[Node],\n\t\t\t\t\t  " +
      "attrs: SortedMap[String, String],\n\t\t\t\t\t  " +
      "void: Boolean = false) extends Node {\n\t" +
      "override def getName = tag\n\t"
    if(map) {
      buffer ++= "def apply(xs: Modifier*) = {\n\t  " +
        "var newTag = this\n\t  " +
        "val buffer: ListBuffer[String] = new ListBuffer[String]\n\t  " +
        "println(newTag.getName + \"***\")\n\t  " +
        "if(map.contains(newTag.getName))\n\t\t" +
        "buffer ++= map.get(newTag.getName).get\n\t  " +
        "println(buffer)\n\t  " +
        "var i = 0\n\t  " +
        "while (i < xs.length) {\n\t\t" +
        "xs(i) match {\n\t\t  " +
        "case x: ScalaTag => {\n\t\t\t" +
        "println(xs(i).getName + \" \" + xs(i).getClass)\n\t\t\t" +
        "if(buffer.contains(xs(i).getName)) {\n\t\t\t  " +
        "println(\"transform: \" + xs(i).getName)\n\t\t\t  " +
        "newTag = xs(i).transform(newTag)\n\t\t\t}\n\t\t\t" +
        "else if(buffer != null)\n\t\t\t  " +
        "throw new IllegalArgumentException(\"illegal child: \" + xs(i).getName)\n            " +
        "else\n              println(\"case not handled\")\n          }\n          " +
        "case x: StringNode => {\n            " +
        "println(\"String node \" + xs(i).getClass)\n            " +
        "newTag = xs(i).transform(newTag)\n          }\n          " +
        "case _ =>\n        }\n        i += 1\n      }\n      newTag\n\t}\n\n\t"
    }
    else {
      buffer ++=
        "    def apply(xs: Modifier*) = {\n" +
          "      var newTag = this\n" +
          "      println(newTag.getName + \"***\")\n" +
          "      var i = 0\n" +
          "      while (i < xs.length) {\n" +
          "        xs(i) match {\n" +
          "          case x: ScalaTag => {\n" +
          "            println(xs(i).getName + \" \" + xs(i).getClass)\n" +
          "            println(\"transform: \" + xs(i).getName)\n" +
          "            newTag = xs(i).transform(newTag)\n" +
          "          }\n" +
          "          case x: StringNode => {\n" +
          "            println(\"String node \" + xs(i).getClass)\n" +
          "            newTag = xs(i).transform(newTag)\n" +
          "          }\n" +
          "          case _ =>\n" +
          "        }\n" +
          "        i += 1\n" +
          "      }\n" +
          "      newTag\n" +
          "    }"
    }

    buffer ++= "def writeTo(strb: StringBuilder): Unit = {\n\t  // tag\n\t  " +
      "strb ++= \"<\" ++= tag\n\n\t  // attributes\n\t  " +
      "for ((attr, value) <- attrs) {\n\t\t" +
      "strb ++= \" \" ++= attr ++= \"=\\\"\"\n\t\t" +
      "Escaping.escape(value, strb)\n\t\t" +
      "strb ++= \"\\\"\"\n\t  }\n\t  " +
      "if (children.isEmpty && void)\n\t\t// No children - close tag\n\t\t" +
      "strb ++= \" />\"\n\t  else {\n\t\t" +
      "strb ++= \">\"\n\t\t// Childrens\n\t\t" +
      "var x = children.reverse\n\t\t" +
      "while (!x.isEmpty) {\n\t\t  " +
      "val child :: newX = x\n\t\t  " +
      "x = newX\n\t\t  " +
      "child.writeTo(strb)\n\t\t}\n\n\t\t// Closing tag\n\t\t" +
      "strb ++= \"</\" ++= tag ++= \">\"\n\t  }\n\t}\n  }\n"

    buffer.toString()
  }

  override def toString: String = "Exporting ScalaTag class definition\n"
}

class ScDefTag(val tags: ScTags) extends ScMemory {
  def write: String = {
    "  object Tags extends Tags\n\n  trait Tags {\n\t" + tags.write + "\n  }"
  }

  override def toString: String = "Exporting Tags Object and Class definition\n"
}

class ScEscapeFun() extends ScMemory {
  def write: String = {
    "\n  object Escaping {\n\n\tprivate[this] val tagRegex = \"^[a-z][\\\\w0-9-]*$\".r\n\n\t" +
      "/**\n\t  * Uses a regex to check if something is a valid tag name.\n\t  */\n\t" +
      "def validTag(s: String) = true //tagRegex.unapplySeq(s).isDefined\n\n\t" +
      "private[this] val attrNameRegex = \"^[a-zA-Z_:][-a-zA-Z0-9_:.]*$\".r\n\n\t" +
      "/**\n\t  * Uses a regex to check if something is a valid attribute name.\n\t  */\n\t" +
      "def validAttrName(s: String) = attrNameRegex.unapplySeq(s).isDefined\n\n\t/**\n\t  " +
      "* Code to escape text HTML nodes. Taken from scala.xml\n\t  */\n\t" +
      "def escape(text: String, s: StringBuilder) = {\n\t  " +
      "// Implemented per XML spec:\n\t  " +
      "// http://www.w3.org/International/questions/qa-controls\n\t  " +
      "// imperative code 3x-4x faster than current implementation\n\t  " +
      "// dpp (David Pollak) 2010/02/03\n\t  " +
      "val len = text.length\n\t  " +
      "var pos = 0\n\t  " +
      "while (pos < len) {\n\t\t" +
      "text.charAt(pos) match {\n\t\t  " +
      "case '<' => s.append(\"&lt;\")\n\t\t  " +
      "case '>' => s.append(\"&gt;\")\n\t\t  " +
      "case '&' => s.append(\"&amp;\")\n\t\t  " +
      "case '\"' => s.append(\"&quot;\")\n\t\t  " +
      "case '\\n' => s.append('\\n')\n\t\t  " +
      "case '\\r' => s.append('\\r')\n\t\t  " +
      "case '\\t' => s.append('\\t')\n\t\t  " +
      "case c => if (c >= ' ') s.append(c)\n\t\t}\n\n\t\t" +
      "pos += 1\n\t  }\n\t}\n  }"
  }

  override def toString: String = "Exporting Escape function definition\n"
}

class ScRelation(val rel: mutable.HashMap[String, ListBuffer[String]]) extends ScMemory {
  private var noRel = true

  def write: String = {
    val buffer: mutable.StringBuilder = new mutable.StringBuilder()
    buffer ++= "  val map = new mutable.HashMap[String, ListBuffer[String]]\n"
    rel.foreach {
      case (key, value) =>
        if(value != null && value.nonEmpty)
          noRel = false
    }
    var count = 1
    rel.foreach {
      case (key, value) =>
        if(value != null && value.nonEmpty) {
          buffer ++= "  val temp" ++= count.toString ++= " = new ListBuffer[String]\n"
          value.foreach(s => buffer ++= "  temp" ++= count.toString ++= " += \"" ++= s ++= "\"\n")
          buffer ++= "  map.put(" ++= "\"" ++= key ++= "\", "
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