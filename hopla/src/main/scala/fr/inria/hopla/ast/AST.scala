package fr.inria.hopla.ast

/**
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
trait AST {

  /**
   * Add a file into AST if it doesn't already exist, return the existing file otherwise.
   * @param file the file to get or add
   * @tparam T type of the given file.
   * @return a file of type T.
   */
  def getOrAddFile[T <: ASTFile](file: T): T

  /**
   * Get the file named <code>filename</code> if it exists
   * @param filename the name of the file to find
   * @return the file if found, None otherwise.
   */
  def get(filename: String): Option[ASTFile]

  /**
   * Simplify the AST by removing redundant implementations
   */
  def simplify(): Unit

  /**
   * Compute ast size
   * @return the number of ASTFile contained by the AST
   */
  def size : Int

  /**
   * Check if the AST contains an ASTFile named <code>filename</code>
   * @param filename the name of the ASTFile to check
   * @return true if ast contains an ASTFile named <code>filename</code>
   */
  def contains(filename : String) : Boolean

  /**
   * Check whether ast is empty or not
   * @return true if ast doesn't contain any ASTFile
   */
  def isEmpty: Boolean
  def getFiles: List[ASTFile]
}