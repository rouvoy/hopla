package fr.inria.hopla.ast

/**
  * Abstract Syntax Tree which represents a scala project<br>
  * Each Compilation Unit, represented by an <code>ASTFile</code>, can be generated
  *
  * @author Jérémy Bossut, Jonathan Geoffroy
  */
case class AST() {
  var files:Map[String, ASTFile] = Map()

  /**
   * Add a file into AST
   * @param file
   */
  private def addFile(file: ASTFile): Unit = {
    files = files + (file.name -> file)
  }

  /**
   * Add a file into AST if it doesn't already exist, return the existing file otherwise.
   * @param file
   * @tparam T type of the given file.
   * @return a file of type T.
   */
  def getOrAddFile[T <: ASTFile](file : T) : T = {
    files.get(file.name) match {
      case None => {
    	  addFile(file)
    	  file
      }
      case f => f.get.asInstanceOf[T]
    }
  }

  /**
   * Simplify the AST by removing redundant implementations
   */
  def simplify(): Unit = {
    for(file <- files.values) {
      file.simplify()
    }
  }
}
