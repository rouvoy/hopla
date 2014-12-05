package fr.inria.hopla.ast

import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, FlatSpec}

/**
 * @author Jérémy Bossut, Jonathan Geoffroy
 */

class ASTTest extends FlatSpec with Matchers with MockFactory {
  "An empty AST" should "empty" in {
    assert(true)
    new ASTImpl().isEmpty
  }

  "An empty AST" should "add a Trait" in {
    val traitMock = new ASTTrait("TestTrait")
    val ast : ASTImpl = new ASTImpl()
    ast.getOrAddFile(traitMock)
    assert(ast.contains("TestTrait"))
  }

  "An AST" should "add two different Traits" in {
    val firstTraitMock = new ASTTrait("FirstTraitMock")
    val secondTraitMock = new ASTTrait("SecondTraitMock")

    val ast : ASTImpl = new ASTImpl()
    ast.getOrAddFile(firstTraitMock)
    ast.getOrAddFile(secondTraitMock)
    assert( ast.size == 2 &&
            ast.contains("FirstTraitMock") &&
            ast.contains("SecondTraitMock"))
  }

  "An AST" should "add the same Trait only once" in {
    val firstTraitMock = new ASTTrait("SameTraitName")
    val secondTraitMock = new ASTTrait("SameTraitName")

    val ast : ASTImpl = new ASTImpl()
    ast.getOrAddFile(firstTraitMock)
    val returnedTrait = ast.getOrAddFile(secondTraitMock)
    assert( ast.size == 1 &&
            ast.contains("SameTraitName") &&
            returnedTrait.equals(firstTraitMock))
  }
}
