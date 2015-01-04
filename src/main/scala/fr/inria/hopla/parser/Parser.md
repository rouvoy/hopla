# XSD Parser #

## Overview ##
This package describes some **XSDProcessor**s which can add data into an AST. Depending on the marker type, *XSDParser* calls the XSDProcessor which has to parse the marker.

## XSDParser ##

*XSDParser* class chooses which **XSDProcessor** will parse each marker. So, *XSDParser* parses the entire XSD content by delegating the AST creation to **XSDProcessor**s, and then returns the AST.

Moreover, it keeps a stack of nested markers, in order to provide a XSDProcessor with the parent of the currently processed marker.

## XSDProcessor ##

### Global Behavior ###

As explained in the previous part, a **XSDProcessor** parses at least one type of marker, in order to add information into AST. Each **XSDProcessor** has a specific behavior:

<table>
	<tr>
		<th>XSDProcessor</th>
		<th>Parsed markers</th>
		<th>Behavior</th>
	</tr>
	<tr>
		<td>ASTClassProcessor</td>
		<td>element</td>
		<td>Create a new ASTClass, and add it to the AST. Each child marker of the currently processed one will inherit from this created ASTClass</td>
	</tr>
	<tr>
		<td>ASTFieldProcessor</td>
		<td>sequence</td>
		<td>Create a new ASTField, and add it to the parent ASTFile (i.e. the file created by a parent marker)</td>
	</tr>
	<tr>
		<td>ASTTraitProcessor</td>
		<td>group</td>
		<td>Create a new ASTTrait, and add it to the AST. Each child marker of the currently processed one will implement this created ASTTrait</td>
	</tr>
	<tr>
		<td>ChoiceProcessor</td>
		<td>choice</td>
		<td>Same behavior than AstTraitProcessor, but also indicate if the field is a unique value or a list of elements</td>
	</tr>
	<tr>
		<td>ComplexTypeProcessor</td>
		<td>complexType</td>
		<td>Same behavior than AstTraitProcessor, but only if the marker contains a *name* or *ref* attribute</td>
	</tr>
	<tr>
		<td>ExtensionProcessor</td>
		<td>extension</td>
		<td>Add the referenced marker as a field of the parent marker</td>
	</tr>
</table>

### Specific parent/child behaviors ###

We can note that some processors have two behaviors:
 * a first one for the currently processed marker,
 * another one for these children.

This is why a `processChild` method is defined, and the parent processor is always given to each child processor. Then, each processor should call `parentProcessor.processChild(markerCurrentlyProcessed)`. This example shows how an ASTClass is created, then an inheritance reference will be added for each file.

	class ASTClassProcessor(ast : AST) extends ASTFileProcessor {
		private var astClass : ASTClass = null
		
		// Call for each element we have to create an ASTClass
		override def process(event: EvElemStart, parent: XSDProcessor): XSDProcessor = {
			astClass = ast.getOrAddFile(new ASTClass(markerNameField(event)))
		}

		// For each child marker, add astClass as super class.
		override def processChild(childEvent: EvElemStart, childFile : ASTFile): Unit = {
			childFile.inheritsFrom(astClass)
		}
	}

The processor which parses the child marker then just has to call *processChild*:

	override def process(event: EvElemStart, parent: XSDProcessor): XSDProcessor = {
	    /*
	     * ...
	     * Some treatment for the currently processed marker
	     * ...
	     */
	    parent.processChild(event, astFile)
  	}
