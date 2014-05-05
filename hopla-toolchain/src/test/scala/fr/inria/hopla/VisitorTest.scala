package fr.inria.hopla

import org.junit._
import Assert._

/**
 * Created by JIN Benli on 26/03/14.
 *
 * Visitor Test
 */

@Test
class VisitorTest {
  @Test
  def lanceTest() {
    val filename1 = "address.xsd"
    val filename2 = "fixml-schema-4-4-20040109rev1/Schema/fixml-allocation-base-4-4.xsd"

    val parser1 = new Parser(filename1)
    parser1.parse()
    val parser2 = new Parser(filename2)
    parser2.parse()

    levelVisitorTest(parser1)
    println()
    levelVisitorTest(parser2)
    println()
    childVisitorTest(parser1)
    println()
    childVisitorTest(parser2)
    println()
    tagVisitorTest(parser1)
    println()
    tagVisitorTest(parser2)
  }

  def levelVisitorTest(p: Parser) {
    val lv = new LevelVisitor(p)
    assertTrue(lv != null)
    lv.visitByLevel()
  }

  def childVisitorTest(p: Parser) {
    val cv = new ChildVisitor(p)
    assertTrue(cv != null)
    cv.visitChild()
  }

  def tagVisitorTest(p: Parser) {
    val t = new TagVisitor(p)
    t.createTagsFromFile()
    t.printAllTags()
    t.getRelationMap.foreach {
      case (key, value) =>
        print(key + ": ")
        value.foreach(s => print(s + " "))
        println()
    }
  }
}
