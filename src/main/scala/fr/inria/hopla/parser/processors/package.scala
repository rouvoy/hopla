package fr.inria.hopla.parser

import scala.xml.pull.EvElemStart

/**
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
package object processors {
  /**
   * Find the name of the file, by looking at "name", "ref", and "base" attributes successively.<br>
   * Throw an error if none of these attributes exist.
   * @param event the event where to find the name
   * @return the name of the ASTFile
   */
  def markerNameField(event: EvElemStart): String = {
    event.attrs.get("name") match {
      case Some(name) => name.mkString
      case None => event.attrs.get("ref") match {
        case Some(ref) => ref.mkString
        case None => event.attrs.get("base") match {
          case Some(base) => base.mkString
          case None => throw new Error("No name nor ref nor base attribute : " + event.label)
        }
      }
    }
  }
}
