Hopla
=====

Hopla project allows to write HTML pages in scala.

## Overview ##

Hopla parses XSD documents in order to generate a Domain Specific Language in Scala.
In particular, this process can create a Scala library for XHTML.

## How to execute ##
### Requirements ###

 * [Scala](http://www.scala-lang.org/),
 * [sbt](http://www.scala-sbt.org/),
 * [Play](https://www.playframework.com/)

### Compile & Run ###
The following command compiles and runs the entire application:

    sbt "project hopla" compile run "project generatedxhtml" compile "project hoplaxhtml" compile run

This command allows to automatically run each task to do :

 * compile _hopla_ module,
 * run _hopla_ module, i.e. generate scala code for each html tag,
 * compile _generatedxhtml_ module, i.e. generated code,
 * compile _hoplaxhtml_ module,
 * run _hoplaxhtml module.

## Project Structure ##
### Project Tree ###

    .
    ├── hopla           # Parse XSD file and generate a corresponding scala source code
    │   └── src
    │       ├── main
    │       │   ├── resources
    │       │   └── scala
    │       │       └── fr
    │       │           └── inria
    │       │               └── hopla
    │       │                   ├── ast             # Abstract Syntax Tree representing a scala project
    │       │                   ├── generator       # Generate scala source code from AST instance
    │       │                   ├── parser          # Parser for XSD files
    │       │                   │   └── processors  # Processors for each XSD marker
    │       │                   └── xhtml           # Define methods implementation for each HTML tag
    │       └── test
    │           ├── resources
    │           │   └── xsd
    │           └── scala
    │               └── fr
    │                   └── inria
    │                       └── hopla
    │                           ├── ast
    │                           ├── parser
    │                           │   └── processors
    │                           └── xhtml
    ├── hopla-xhtml     # Generate HTML page from scala object
    │   ├── app         # Play server application
    │   │   ├── controllers
    │   │   │   └── fr
    │   │   │       └── inria
    │   │   │           └── hoplaxhtml
    │   │   │               ├── examples        # Examples of scala presentations using Reveal.js framework
    │   │   │               └── presentation    # Helpers to quickly write Reveal.js presentations
    │   │   └── views
    │   ├── conf
    │   ├── project
    │   ├── public
    │   │   └── images
    │   └── src
    │       └── main
    │           └── resources
    └── project

### Modules ###
The _hopla_ project is splitted in three different sbt modules:

 * _hopla_ module parses [xhtml-strict.xsd](http://www.w3.org/2002/08/xhtml/xhtml1-strict.xsd) and generates scala source code which corresponds to the XSD declaration,
 * _generatedxhtml_ is the result of _hopla_ module generation,
 * _hopla-xhtml_ contains a way to transform a HTML scala object into an html page.

 #### hopla module ####
_hopla_ module first parses any XSD file, before generating the corresponding scala source code.  
In order to make the scala generation easier, the Parser creates an [AST](hopla/src/main/scala/fr/inria/hopla/ast/Ast.md) and inserts all information into this one.  
This [AST](hopla/src/main/scala/fr/inria/hopla/ast/Ast.md) is then given to the [Generator](hopla/src/main/scala/fr/inria/hopla/generator/Generator.md), which generates each compilation unit contained by the provided AST. In particular, each generated compilation unit implements the __[Marker](hopla/src/main/scala/fr/inria/hopla/xhtml/Marker.scala)__ trait, which defines some useful methods for HTML page generation.

#### hopla-xhtml module ####
The _hopla-hxtml_ module generates an HTML page from a provided scala Object which implements __[Marker](hopla/src/main/scala/fr/inria/hopla/xhtml/Marker.scala)__ trait. It uses __Marker__ helpers to generate the HTML page by recursively calling the _toHtml_ method.  
_hopla-hxtml_ also offers some examples of [Reveal.js](http://lab.hakim.se/reveal-js/#/) presentation. Please see _[Example.scala](hopla-xhtml/app/controllers/fr/inria/hoplaxhtml/examples/Example.scala)_

