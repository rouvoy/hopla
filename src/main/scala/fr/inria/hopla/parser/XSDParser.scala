package fr.inria.hopla.parser

import fr.inria.hopla.ast.{ASTImpl, AST, ASTComponent}
import fr.inria.hopla.parser.processors._

import scala.collection.immutable.Stack
import scala.xml.pull.{EvElemEnd, EvElemStart, XMLEventReader}

/**
 *
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
class XSDParser {
  this: ASTComponent =>

  var processors = Stack[XSDProcessor]()

  /**
   * parse the entire XSD file, and add each Compilation Units to the AST<br>
   * For each marker, just delegates the process to a [MarkerName]processor, and process the marker<br>
   * The processor just process the marker by adding data into AST, and return the current parent,
   * depending on the type of the marker
   *
   * @param xsd the XSD to parse
   */
  def parse(xsd: XMLEventReader): AST = {
    while (xsd.hasNext) {
      xsd.next() match {
        case event@EvElemStart(_, label, _, _) =>
          label match {
            case "schema" => processors = processors.push(new SchemaProcessor(ast).process(event))
            case "element"
                 | "attributeGroup" => process(new ASTClassProcessor(ast), event)
            case "group" => process(new ASTTraitProcessor(ast), event)
            case "extension" => process(new ExtensionProcessor(ast), event)
            case "complexType" => process(new ComplexTypeProcessor(ast), event)
            case "choice" => process(new ChoiceProcessor(), event)
            case "sequence" => process(new ASTFieldProcessor(), event)
            case "attribute" => process(new PrimitiveTypeProcessor(), event)
            case "import" | "annotation" | "documentation" | "complexContent" => processors = processors.push(processors.top) // skip these markers
            case _ =>
              println(label + " skipped")
              processors = processors.push(processors.top) // duplicates the top of the stack
          }
        case event@EvElemEnd(_, _) => processors = processors.pop // remove the last parent
        case _ => // ignore
      }
    }
    ast simplify()
    ast
  }

  private def process(processor : AbstractXSDProcessor, event: EvElemStart): Unit = {
    processors = processors.push(processor.process(event, processors.top))
  }
}
