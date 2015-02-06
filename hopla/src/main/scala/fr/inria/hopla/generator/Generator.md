# Generator #

## Overview ##
A Generator transforms any AST in source code. We implement here a generator of scala code from AST.
 
## AstGenerator ##
ASTGenerator generates each *ASTFile* by delegating the generation part to a specific Generator for each kind of compilation unit.
It uses the Visitor pattern to find the right Generator to call:

* **ASTClassGenerator** to generate source code from **ASTClass**,
* **ASTTraitGenerator** to generate source code from **ASTTrait**

## Generate scala files ##

### PrintGenerator  ###
The **PrintGenerator** class offers some helpers in order to easily write code into a file. In particular, it can :

* Automatically add indentation,
* open and close blocks and methods.

So scala code writing can be expressed in terms of *startBlock* and *endBlock*, which is a little closer to scala code development:

    /* 
     * Will generate:
     *  if(fields.nonEmpty) {
     *      fields.toList
     *  }
     */
    generator.startBlock("if(fields.nonEmpty)")
        generator.writeLine("fields.toList")
    generator.endBlock()

Note that there are also helpers which start and end method definitions:
 
    /* 
     * Will generate:
     *  def getName() : String = {
     *    name
     *  }
     */
     generator.startDef("getName", "String")
         generator.writeLine("name")
     generator.endDef()
     
### ScalaGenerator ###
ScalaGenerator not only offers to create a new *PrintGenerator* from ASTFile's name, but also provides some helpers for each kind of scala code:

|     Method Name     |                          Generation                         |
|:-------------------:|:-----------------------------------------------------------:|
| generateInheritance | [class|trait] extends A with B with C                       |
| generateField       | var fieldName : fieldType* var fieldname : fieldType = null |
| generatePackage     | package thePackage.path.name                                |

### ASTTraitGenerator ###
This Generator writes each field of an ASTTrait into a file.
 
### ASTClassGenerator ###
This Generator writes in a same file both a class, which contains fields, and a companion object containing an *apply* method for each field.