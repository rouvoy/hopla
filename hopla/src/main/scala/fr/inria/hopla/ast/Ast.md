# AST #

## Overview ##

This package describes the Scala [AST](https://en.wikipedia.org/wiki/Abstract_syntax_tree "Abstract Syntax Tree") used by the parser. This AST is simplified to a set of Scala elements which are required to provide an *Object Oriented* XHTML definition:

 * Files:
 	* Classes,
 	* Traits
 * Fields

Note that this AST implementation follows the [Cake Pattern](http://www.warski.org/blog/2014/02/using-scala-traits-as-modules-or-the-thin-cake-pattern/)

## AST ##

An **AST** instance contains all compilation units (more or less a file) of a parsed XSD file. This class provides some helpers such as:

 * *get*, which retrieves a file representation by giving its filename,
 * *getOrAdd*, which adds the given ASTFile into AST if it hasn't been added yet and just returns the already added version otherwise.

 This last helper is very useful to reference an **ASTFile** which is potentially not yet parsed. In this case, the AST will create an empty **ASTFile** which can serve as reference. When the real content of this *ASTFile* is then parsed, the AST will retrieve and return the previously added reference without adding a duplicate of the **ASTFile**.

 Moreover, note that this method doesn't return an **ASTFile** abstract object, but a specific object, depending on the type of the given parameter. For example, if you call `getOrAdd(ASTClass)`, it returns an **ASTClass**, so users don't have to cast the returned type:

	astClass = ast.getOrAddFile(new ASTClass("TheClassName"))

## File Representation ##

### Compilation Unit generic representation ###
In order to represent a Scala file efficiently, the **ASTFile** class always keeps in memory:

 * inherited class, if any,
 * a set of implemented traits,

In particular, **ASTFile** makes a two-way set, in such a way that if an ASTFile implements another, the latter also knows that it is implemented by the first one:

	/**
   	* Add an implemented trait to the list <code>traits</code>
   	* @param astTrait the trait implemented by this ASTFile
   	*/
  	def withTrait(astTrait: ASTTrait): Unit = {
    	traits += astTrait
      	astTrait.subFiles += this
  	}

### Class & Trait specific representation ###

**ASTClass** and **ASTTrait** classes can be seen as markers in order to distinguish classes and traits representation.  

Note that, as XHTML doesn't define any *singleton*, there is no *object* representation in the AST.

## Field representation ##

As mentioned before, an **ASTFile** can also contain a set of fields. A field is represented in the AST by an instance of **ASTField** class. This class represents a field by :

 * a name,
 * a type, i.e. a reference to an existing **ASTFile**.
