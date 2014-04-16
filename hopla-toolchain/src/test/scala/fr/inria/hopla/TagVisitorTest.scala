package fr.inria.hopla

/**
 * Created by JIN Benli on 15/04/14.
 */
object TagVisitorTest {
  def main(args: Array[String]) {
    val t = new TagVisitor("address.xsd")
    t.createTagsFromFile()
    t.printAllTags()

    val parser: Parser = new Parser("address.xsd")
    parser.parse()
    val e = parser.elementList(2)
    t.visitAttributes(e)
  }
}
