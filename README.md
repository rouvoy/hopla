Hopla
=====

## Overview ##

Hopla parses XSD documents in order to generate a Domain Specific Language in Scala.
In particular, this process can create a Scala library for XHTML.

## Run Project ##

Launch fr.inria.hopla.XHtmlParsing.scala

## Project Structure ##

    hopla/
        src/
        	main/
        		scala/
		            fr.inria.hopla/
		            	ast 	# Abstract Syntax Tree representing a scala project
		            	parser	# Parser for XSD files
		            		processors	# Processors for each XSD marker
		    test/
		    	scala/ 
		    		fr.inria.hopla/
		            	ast
		            	parser.processors


