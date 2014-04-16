import scala.collection.SortedMap

package object personne {

  trait Modifier {
    def transform(tag: ScalaTag): ScalaTag
  }

  trait Node extends Modifier {
    override def toString = {
      val strb = new StringBuilder
      writeTo(strb)
      strb.toString()
    }

    def writeTo(strb: StringBuilder): Unit


    def transform(tag: ScalaTag) = {
      tag.copy(children = this :: tag.children)
    }
  }

  /**
   * A tag which contain a String
   * @param v
   */
  case class StringNode(v: String) extends Node {
    def writeTo(strb: StringBuilder): Unit = Escaping.escape(v, strb)
  }

  case class ScalaTag(tag: String = "",
                      children: List[Node],
                      attrs: SortedMap[String, String],
                      void: Boolean = false) extends Node {

    def apply(xs: Modifier*) = {
      var newTag = this

      var i = 0
      while (i < xs.length) {
        newTag = xs(i).transform(newTag)
        i += 1
      }
      newTag
    }

    def writeTo(strb: StringBuilder): Unit = {
      // tag
      strb ++= "<" ++= tag

      // attributes
      for ((attr, value) <- attrs) {
        strb ++= " " ++= attr ++= "=\""
        Escaping.escape(value, strb)
        strb ++= "\""
      }
      if (children.isEmpty && void)
      // No children - close tag
        strb ++= " />"
      else {
        strb ++= ">"
        // Childrens
        var x = children.reverse
        while (!x.isEmpty) {
          val child :: newX = x
          x = newX
          child.writeTo(strb)
        }

        // Closing tag
        strb ++= "</" ++= tag ++= ">"
      }
    }
  }

  object Tags extends Tags

  trait Tags {
    val etablissement = "etablissement".tag
    val date_naissance = "date_naissance".tag
    val nom = "nom".tag
    val num_tel = "num_tel".tag
    val prenom = "prenom".tag
    val personne = "personne".tag

  }

  object Escaping {

    private[this] val tagRegex = "^[a-z][\\w0-9-]*$".r

    /**
     * Uses a regex to check if something is a valid tag name.
     */
    def validTag(s: String) = true //tagRegex.unapplySeq(s).isDefined

    private[this] val attrNameRegex = "^[a-zA-Z_:][-a-zA-Z0-9_:.]*$".r

    /**
     * Uses a regex to check if something is a valid attribute name.
     */
    def validAttrName(s: String) = attrNameRegex.unapplySeq(s).isDefined

    /**
     * Code to escape text HTML nodes. Taken from scala.xml
     */
    def escape(text: String, s: StringBuilder) = {
      // Implemented per XML spec:
      // http://www.w3.org/International/questions/qa-controls
      // imperative code 3x-4x faster than current implementation
      // dpp (David Pollak) 2010/02/03
      val len = text.length
      var pos = 0
      while (pos < len) {
        text.charAt(pos) match {
          case '<' => s.append("&lt;")
          case '>' => s.append("&gt;")
          case '&' => s.append("&amp;")
          case '"' => s.append("&quot;")
          case '\n' => s.append('\n')
          case '\r' => s.append('\r')
          case '\t' => s.append('\t')
          case c => if (c >= ' ') s.append(c)
        }

        pos += 1
      }
    }
  }

  /**
   * Allows you to modify a ScalaTag by adding a String to its list of children
   */
  implicit def stringNode(v: String) = new StringNode(v)

  /**
   * Providing string extension to fit into the ScalaTag fragments.
   * @param s
   */
  implicit class StringExtension(s: String) {
    def tag = {
      if (!Escaping.validTag(s))
        throw new IllegalArgumentException(
          s"Illegal tag name: $s is not a valid XML tag name"
        )
      ScalaTag(s, Nil, SortedMap.empty)
    }
  }

}
