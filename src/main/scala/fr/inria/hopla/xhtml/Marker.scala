package fr.inria.hopla.xhtml

/**
 * <p>Trait implemented by all XHtml markers</p>
 * <p>
 *  Define methods which ables to generate html page from any scala Marker instance<br/>
 *  A Marker implements helpers in order to retrieve html marker information:
 *  <ul>
 *    <li>getName retrieves the html marker's name</li>
 *    <li>getText return the marker's content</li>
 *    <li>getChildren return all nested Markers of this one</li>
 *  </ul>
 * </p>
 *
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
trait Marker {

  /**
   * Generate html page by writing this Marker, its content,
   * and then recursively generate each child of this Marker by calling <code>child.toHtml</code>.
   * @return the html page for this Marker and its childs
   */
  def toHtml : String = {
    val builder : StringBuilder = new StringBuilder

    // Open the Marker and add these attributes
    builder.append(s"<$getName$htmlAttributes>")

    // Write Marker content
    val text = getText
    if(text != null) {
      builder.append(getText)
    }

    // Write each Marker's child
    for (child <- getChildren) {
      if(child != null) {
        builder.append(child.toHtml)
      }
    }

    // Close the marker
    builder.append(s"</$getName>")
    builder.toString()
  }

  /**
   * Get the name of the html Marker
   * @return the name of the html Marker
   */
  def getName : String = {
    getClass.getName
  }

  /**
   * Get all nested markers to generate
   * @return a sequence which contains all children
   */
  def getChildren : Seq[Marker]

  /**
   * Get the content of this Marker
   * @return the content of this Marker
   */
  def getText: String = ""

  /**
   * Generate a grooveString which defines all attributes of this Marker
   * @return a String which defines all attributes of this Marker
   */
  def htmlAttributes : String
}
