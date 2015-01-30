package fr.inria.hopla.generator

import java.io.PrintWriter

/**
 * Scala generator which provides some helper methods in order to make the generated source code cleaner<br>
 * In particular, this generator can write blocks and methods, and automatically compute indentation.
 *
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
class PrintGenerator(writer : PrintWriter) {
  /**
   * Indentation to add to each line
   */
  var tabs: String = ""

  /**
   * Number of inner blocks, i.e. current depth.
   */
  var blocks = 0

  /**
   * Write the content to <code>writer</code>
   * @param content the content to write
   * @return this
   */
  def write(content: String): PrintGenerator = {
    writer.write(s"$tabs$content")
    this
  }

  /**
   * Write the content followed by a new line to <code>writer</code>
   * @param content the content to write
   * @return this
   */
  def writeLine(content: String): PrintGenerator = write(s"$content\n")

  /**
   * Add an indentation to the current line
   * @return this
   */
  def indent: PrintGenerator = {
    tabs += "\t"
    this
  }

  /**
   * Remove an indentation to the current line
   * @return this
   */
  def dedent(): PrintGenerator = {
    tabs = tabs.dropRight(1)
    this
  }

  /**
   * Start a block, prefixed by <code>prefixContent</code><br>.
   * Write <code>prefixContent</code> followed by `{` and a new line
   * @param prefixContent the prefix of the new block
   * @return this
   */
  def startBlock(prefixContent: String): PrintGenerator = {
    blocks += 1
    writeLine(s"$prefixContent {").indent
  }

  /**
   * Close the block
   * @return this
   */
  def endBlock() : PrintGenerator = {
    blocks -= 1
    dedent().writeLine("}").writeLine("")
  }

  /**
   * Start a method definition, and open a new block
   * @param name the name of the method
   * @param parameterName the name of the parameter for this method
   * @param parameterType the type of the parameter for this method
   * @param returnType the type of the returned value
   * @return this
   */
  def startDef(name: String, parameterName : String, parameterType : String, returnType : String) =
    startBlock(s"def ${scalaFileName(name)} (${scalaFileName(parameterName)} : ${scalaFileName(parameterType)}) : ${scalaFileName(returnType)} = ")

  def startDef(name: String, returnType: String) =
    startBlock(s"def ${scalaFileName(name)} () : ${scalaFileName(returnType)} = ")

  /**
   * Close the method definition<br>
   * Same as endBlock
   * @return this
   */
  def endDef() = endBlock()

  /**
   * Close all opened blocks and close the <code>writer</code>
   */
  def close(): Unit = {
    while(blocks > 0) {
      endBlock()
    }
    writer.close()
  }
}
