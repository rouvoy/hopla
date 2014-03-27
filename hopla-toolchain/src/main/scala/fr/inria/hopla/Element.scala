package fr.inria.hopla

import scala.xml.Node
import scala.collection.mutable.ListBuffer

/**
 * Created by JIN Benli on 26/03/14.
 *
 * Element is the first class container for storing all possible relation presented in
 * the original file, called by Parser and can be accessed by Any Type of visitor
 *
 */
class Element(element: Node) {
  private val _parent: ListBuffer[Element] = new ListBuffer[Element]();
  private val _childs: ListBuffer[Element] = new ListBuffer[Element]();
  private var _level = 0
  private var _root = false

  /**
   * Generate child element from the parent, so their relation can be defined
   *
   * @return
   */
  def generate = {
    for (e <- element.child \\ "element") yield _childs += new Element(e)
  }

  /**
   * Suite function of generate, fix the relation and set the level from current
   *
   */
  def setParentAndLevel = {
    if (!_childs.isEmpty) {
      for (e <- _childs) {
        e.parent_=(this)
        e.level_=(this.level + 1)
      }
    }
  }

  // Setter
  def level_=(value: Int): Unit = _level = value

  // Getter
  def level = _level

  def root_=(value: Boolean): Unit = _root = value

  def root = _root

  def parent_=(value: Element): Unit = _parent += value

  def parent = _parent

  def childs_=(value: Element): Unit = _childs += value

  def childs = _childs

  /**
   * Get all attributes as type of MetaData
   * @return
   */
  def getAttributes = element.attributes

  /**
   * Get attribute by name and return its string as result
   * @param s
   * @return
   */
  def getAttributeString(s: String) = {
    element.attribute(s) match {
      case Some(s) => s.toString()
      case None => null
    }
  }
}
