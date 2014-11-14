package fr.inria.hopla.ast

import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, FlatSpec}

/**
 * @author Jérémy Bossut, Jonathan Geoffroy
 */

class ASTTest extends FlatSpec with Matchers with MockFactory {
  "An empty AST" should "empty" in {
    assert(new AST().files.isEmpty)
  }

  "An empty AST" should "add a Trait" in {
    val traitMock = new ASTTrait("TestTrait")
    val ast : AST = new AST()
    ast.getOrAddFile(traitMock)
    assert(ast.files.size == 1 && ast.files.contains("TestTrait"))
  }

  "An AST" should "add two different Traits" in {
    val firstTraitMock = new ASTTrait("FirstTraitMock")
    val secondTraitMock = new ASTTrait("SecondTraitMock")

    val ast : AST = new AST()
    ast.getOrAddFile(firstTraitMock)
    ast.getOrAddFile(secondTraitMock)
    assert( ast.files.size == 2 &&
            ast.files.contains("FirstTraitMock") &&
            ast.files.contains("SecondTraitMock"))
  }

  "An AST" should "add the same Trait only once" in {
    val firstTraitMock = new ASTTrait("SameTraitName")
    val secondTraitMock = new ASTTrait("SameTraitName")

    val ast : AST = new AST()
    ast.getOrAddFile(firstTraitMock)
    val returnedTrait = ast.getOrAddFile(secondTraitMock)
    assert( ast.files.size == 1 &&
            ast.files.contains("SameTraitName") &&
            returnedTrait.equals(firstTraitMock))
  }
}
