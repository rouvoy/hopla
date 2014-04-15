package fr.inria.hopla

import fr.inria.hopla.visitorCase.HtmlTag.TagVisitor

/**
 * Created by JIN Benli on 15/04/14.
 */
object ScMemoryTest {
  def main(args: Array[String]) {
    val vis = new TagVisitor("address.xsd")
    vis.createTagsFromFile()
    val tag = new ScTags(vis.s)
    val tagFun = new ScTagFunction
    val pac = new ScPackage("address")

    println(tag.write)
    println(tagFun.write)
    println(pac.write)
  }
}
