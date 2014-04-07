package fr.inria.hopla

import scala.xml.Node
import scala.collection.mutable.ListBuffer
import scala.collection.mutable

/**
 * Created by JIN Benli on 26/03/14.
 *
 * Element is the first class container for storing all possible relation presented in
 * the original file, called by Parser and can be accessed by Any Type of visitor
 *
 */
class Element(element: Node) {
  private val _parent: ListBuffer[Element] = new ListBuffer[Element]()
  private val _child: ListBuffer[Element] = new ListBuffer[Element]()
  private var _level = 0
  private var _root = false
  // important name list who remember all created element during the recursive process generate
  // preventing double generation using dynamic programming
  private val nameList: mutable.HashMap[String, Element] = new mutable.HashMap[String, Element]

  /**
   * Generate child element from the parent, so their relation can be defined
   *
   * @return
   */
  def generate: mutable.HashMap[String, Element] = {
    val child = element.child \\ "element"
    if (!child.equals(null)) {
      for (e <- child) yield {
        val temp: Element = new Element(e)
        if (!nameList.contains(temp.getAttributeString("name"))) {
          temp.parent_=(this)
          temp.level_=(value = level + 1)
          val tempNameList = temp.generate
          tempNameList.foreach {
            case (key, value) => nameList.put(key, value)
          }
          nameList.put(temp.getAttributeString("name"), temp)
          _child += temp
          println("Trait %s %d %s".format(temp.getAttributeString("name"), temp.level, temp.parent(0).getAttributeString("name")))
          //          println(e)
        }
      }
    }
    nameList
  }

  // Setter
  def level_=(value: Int): Unit = _level = value

  // Getter
  def level = _level

  def root_=(value: Boolean): Unit = _root = value

  def root = _root

  def parent_=(value: Element): Unit = _parent += value

  def parent = _parent

  def childs_=(value: Element): Unit = _child += value

  def childs = _child

  /**
   * Get all attributes as type of MetaData
   * @return
   */
  def getAttributes = element.attributes

  /**
   * Get attribute by name and return its string as result
   * @param s given argument
   * @return
   */
  def getAttributeString(s: String): String = {
    element.attribute(s) match {
      case Some(s) => s.toString()
      case None => null
    }
  }
}
