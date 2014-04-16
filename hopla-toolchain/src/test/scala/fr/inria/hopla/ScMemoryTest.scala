package fr.inria.hopla

import fr.inria.hopla.visitorCase.HtmlTag.TagVisitor
import java.io.PrintWriter

/**
 * Created by JIN Benli on 15/04/14.
 */
object ScMemoryTest {
  def main(args: Array[String]) {
    val vis = new TagVisitor("personne.xsd")
    vis.createTagsFromFile()
    val tag = new ScTags(vis.s)
    val tagFun = new ScTagFunction
    val tagOb = new ScDefTag(tag)
    val escape = new ScEscapeFun

    val list = List(tagFun.asInstanceOf[ScMemory], tagOb.asInstanceOf[ScMemory], escape.asInstanceOf[ScMemory])

    val imp = new ScImport("personne")
    val pac = new ScPackage("personne", list)
    val test = new ScTestCase

    println(imp.write + pac.write + test.write)

    val out = new PrintWriter("hopla-toolchain/out/personne.scala")
    out.println(imp.write + pac.write + test.write)
    out.close()
  }
}
