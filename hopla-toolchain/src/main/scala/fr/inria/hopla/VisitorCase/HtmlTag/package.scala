package fr.inria.hopla.VisitorCase

import scala.collection.SortedMap

/**
 * Created by JIN Benli on 26/03/14.
 */
package object HtmlTag {

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
      HtmlTag(s, Nil, SortedMap.empty)
    }
  }

}