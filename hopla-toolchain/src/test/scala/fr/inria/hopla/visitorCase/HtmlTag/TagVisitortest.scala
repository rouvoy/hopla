package fr.inria.hopla.visitorCase.HtmlTag

/**
 * Created by JIN Benli on 15/04/14.
 */
object TagVisitorTest {
  def main(args: Array[String]) {
    val t = new TagVisitor("address.xsd")
    t.createTagsFromFile()
    t.passTagsToMemory()
  }
}
