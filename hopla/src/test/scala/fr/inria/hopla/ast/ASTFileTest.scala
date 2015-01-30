package fr.inria.hopla.ast

import org.scalatest.{FlatSpec, Matchers}

/**
 * @author Jérémy Bossut, Jonathan Geoffroy
 */
class ASTFileTest extends FlatSpec with Matchers {
  "A Trait" should "equals to another which have the same name" in {
    val first = new ASTTrait("theName")
    val second = new ASTTrait("theName")

    assert(first equals second)
  }

  it should " not equals to another which have a different name" in {
    val first = new ASTTrait("firstName")
    val second = new ASTTrait("secondName")

    assert(!(first equals second))
  }

  "A Class" should "equals to another which have the same name" in {
    val first = new ASTClass("theName")
    val second = new ASTClass("theName")

    assert(first equals second)
  }

  it should "not equals to another which have a different name" in {
    val first = new ASTClass("firstName")
    val second = new ASTClass("secondName")

    assert(!(first equals second))
  }
}
