package fr.inria.hopla

import scala.collection.mutable.ListBuffer

/**
 * Created by JIN benli on 19/02/14.
 *
 * Scala Visitor for providing a scala class generation interface
 */

class ScalaVisitor(val elem: Element) {

  // noting all class created by factory method and return itself for chain of responsibility generation
  val _elemBuffer: ListBuffer[ScClass] = new ListBuffer[ScClass]()

  _elemBuffer += ScFunction("body", List("getLevel", elem.level))

  _elemBuffer += ScFunction("body", List("getChildList", elem.childs.toList))

  _elemBuffer += ScFunction("body", List("getParent", elem.parent.toList))


  for (att <- elem.getAttributes) att.key match {
    case "name" => _elemBuffer += ScFunction("name", List(att.value.toString()))
    case "type" => _elemBuffer += ScFunction("param", List(att.value.toString()))
    case _ =>
  }

  _elemBuffer += ScFunction("body", List("writeTo"))

  def elemBuffer = _elemBuffer
}


