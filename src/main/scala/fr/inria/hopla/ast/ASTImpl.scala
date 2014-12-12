package fr.inria.hopla.ast

/**
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
case class ASTImpl(files: scala.collection.mutable.Map[String, ASTFile]) extends AST {

  def this() = this(scala.collection.mutable.Map[String, ASTFile]())
  /**
   * Add a file into AST
   * @param file
   */
  private def addFile(file: ASTFile): Unit = {
    files += (file.name -> file)
  }

  override def getOrAddFile[T <: ASTFile](file: T): T = {
    files.get(file.name) match {
      case None => {
        addFile(file)
        file
      }
      case f => f.get.asInstanceOf[T]
    }
  }

  override def simplify(): Unit = {
    for (file <- files.values) {
      file.simplify()
    }
  }

  override def contains(filename : String) : Boolean = files.contains(filename)
  override def isEmpty: Boolean = files.isEmpty
  override def size : Int = files.size
  override def get(filename: String) : Option[ASTFile] = files.get(filename)
}
