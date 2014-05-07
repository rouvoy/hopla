package fr.inria.hopla

import java.io.PrintWriter

/**
 * Created by JIN Benli on 05/03/14.
 *
 * Test function for creating element scala class, version naif
 */

class ScGenerator {

  def export(filename: String) {

    val p = new Parser(filename)
    p.parse()

    val vis = new TagVisitor(p)
    vis.createTagsFromFile()
    val tag = new ScTags(vis.s)
    val rel = new ScRelation(vis.getRelationMap)
    var existMap = true
    if(vis.getRelationMap.isEmpty)
      existMap = false
    val tagFun = new ScTagFunction(existMap)
    val tagOb = new ScDefTag(tag)
    val escape = new ScEscapeFun


    val list = List(rel.asInstanceOf[ScMemory], tagFun.asInstanceOf[ScMemory], tagOb.asInstanceOf[ScMemory], escape.asInstanceOf[ScMemory])

    //TODO name handling
    val lv = new LevelVisitor(p)
    lv.visitByLevel()
    val name = lv.getMaxName.toLowerCase
    val imp = new ScImport(name)
    val pac = new ScPackage(name, list)

//    println(imp.write + pac.write)

    val out = {
      new PrintWriter("hopla-toolchain/out/" + name + ".scala")
    }
    out.println(imp.write + pac.write)
    out.close()
  }
}