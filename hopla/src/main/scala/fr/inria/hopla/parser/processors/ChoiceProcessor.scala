package fr.inria.hopla.parser.processors

import scala.xml.Node
import scala.xml.pull.EvElemStart

/**
 * Specific processor to process <i>choice</i> markers<br>
 *
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
class ChoiceProcessor extends AbstractXSDProcessor {
  var parent : XSDProcessor = null

  /**
   * Process the marker by setting parentFile.isListOfElements, and delegate these children to its parent
   * @param event the event of the marker to process
   * @param parent the parent processor
   * @return <code>parent</code>
   */
  override def process(event: EvElemStart, parent: XSDProcessor): XSDProcessor = {
    val parentFile = parent.getAstFile

    // is this choice a list of choices ?
    event.attrs.get("maxOccurs") match {
      case Some(value) => value match {
        case Node("0", _, _) | Node("1", _ , _) => parentFile.setListOfElements(isListOfElements = false)
        case _ => parentFile.setListOfElements(isListOfElements = true)
      }
      case None => parentFile.setListOfElements(isListOfElements = false)
    }

    parent
  }
}
