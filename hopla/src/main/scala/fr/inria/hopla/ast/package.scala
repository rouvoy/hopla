/**
 * Helpers
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
package object ast {
  /**
   * List of all scala keywords
   */
  lazy val keywords = List("abstract","case","catch","class","def ","do","else","extends","false ","final ","finally","for ","forSome","if ","implicit","import","lazy","match","new","null ","object ","override","package","private","protected ","return","sealed","","super ","this","throw ","trait","try","true ","type","val (value)","var  (variable)","while","with","yield ")

  /**
   * Transform <code>name</code> into an available name for a real scala file replacing each '.' by '_'<br>
   * If this name is a scala keyword, prefix it by '_'
   *
   * @return an available name for a scala file
   */
  def scalaFileName(name: String) : String = {
    // replace each '.' by '_' in the class name
    var fileName = name.replace('.', '_')

    // If this name is a scala keyword, prefix it by '_'
    if(keywords contains fileName) {
      fileName = "_" + fileName
    }

    fileName
  }
}
