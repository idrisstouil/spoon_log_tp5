package processor;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtType;

/**
 * Processor for analyzing and modifying Java constructors.
 */
public class ExceptionProcessor extends AbstractProcessor<CtConstructor> {

   
    public void process(CtConstructor element) {
        // Create a code snippet to be inserted into the method body
        CtCodeSnippetStatement snippet = getFactory().Core().createCodeSnippetStatement();

        // Build the string value for the snippet
        StringBuilder sb = new StringBuilder();
        String value = "";
        CtType declaringType = element.getDeclaringType();
        String className = declaringType.getSimpleName();
        sb.append("\"");
        sb.append("MethodeName : " + className + "");
        sb.append("\"+\";\"+");
        sb.append("UserService.getUser().toString()");
        sb.append("+ \";\" +");
        sb.append("\"Constructor\"");

        // Check if the class name ends with "Exception" and set the snippet value accordingly
        if (className.endsWith("Exception")) {
            value = String.format("LOGGER.severe(%s)", sb.toString());
            snippet.setValue(value);
            // Insert the snippet at the beginning of the method body, if it exists
            if (element.getBody() != null) {
                element.getBody().insertBegin(snippet);
            }
        } 
    }
}
