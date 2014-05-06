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

    val res1 = generate(parser1)
    assert(res1 == "    val map = new mutable.HashMap[String, ListBuffer[String]]\n    " +
      "val temp1= new ListBuffer[String]\n    " +
      "temp1 += \"Nom\"\n    " +
      "temp1 += \"Note\"\n    " +
      "map.put(\"Town\", temp1)\n    " +
      "val temp2= new ListBuffer[String]\n    " +
      "temp2 += \"House\"\n    " +
      "temp2 += \"Town\"\n    " +
      "temp2 += \"Recipient\"\n    " +
      "temp2 += \"PostCode\"\n    " +
      "temp2 += \"Country\"\n    " +
      "temp2 += \"Street\"\n    " +
      "temp2 += \"County\"\n    " +
      "map.put(\"Address\", temp2)\n")
    val res2 = generate(parser2)
    assert(res2 == "")

  }
  def generate(p: Parser) = {
    //    val vis = new TagVisitor(p)
    //    vis.createTagsFromFile()
    //    val tag = new ScTags(vis.s)
    //    val tagFun = new ScTagFunction
    //    val tagOb = new ScDefTag(tag)
    //    val escape = new ScEscapeFun
    //
    //    val list = List(tagFun.asInstanceOf[ScMemory], tagOb.asInstanceOf[ScMemory], escape.asInstanceOf[ScMemory])
    //
    //    val imp = new ScImport("personne")
    //    val pac = new ScPackage("personne", list)
    //
    //    println(imp.write + pac.write)
    //
    //    val out = new PrintWriter("hopla-toolchain/out/personne.scala")
    //    out.println(imp.write + pac.write)
    //    out.close()

    val vis = new TagVisitor(p)
    val rel = new ScRelation(vis.getRelationMap)
    println(rel.write)
    rel.write
  }
}
