package fr.inria.hopla.parser.processors

import scala.xml.pull.EvElemStart

/**
 * Represent a Processor able to process a xsd marker
 *
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
trait AbstractXSDProcessor {

  /**
   * Process a marker, represented by <code>event</code>
   * @param event the event of the marker to process
   * @param parent the parent processor.
   * @return the current processor. should be <code>this</code> if this processor is used by these children, or <code>parent</code> otherwise
   */
  def process(event: EvElemStart, parent: XSDProcessor = null): XSDProcessor
}
