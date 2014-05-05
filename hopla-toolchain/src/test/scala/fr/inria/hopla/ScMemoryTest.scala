package fr.inria.hopla

import java.io.PrintWriter
import org.junit._
import Assert._

/**
* Created by JIN Benli on 15/04/14.
*/
@Test
class ScMemoryTest {
  @Test
  def lanceTest() {
    val filename1 = "address.xsd"
    val filename2 = "fixml-schema-4-4-20040109rev1/Schema/fixml-allocation-base-4-4.xsd"

    val parser1 = new Parser(filename1)
    parser1.parse()
    val parser2 = new Parser(filename2)
    parser2.parse()


  }

  def generate(p: Parser) {
    val vis = new TagVisitor(p)
    vis.createTagsFromFile()
    val tag = new ScTags(vis.s)
    val tagFun = new ScTagFunction
    val tagOb = new ScDefTag(tag)
    val escape = new ScEscapeFun

    val list = List(tagFun.asInstanceOf[ScMemory], tagOb.asInstanceOf[ScMemory], escape.asInstanceOf[ScMemory])

    val imp = new ScImport("personne")
    val pac = new ScPackage("personne", list)

    println(imp.write + pac.write)

    val out = new PrintWriter("hopla-toolchain/out/personne.scala")
    out.println(imp.write + pac.write)
    out.close()
  }
}
