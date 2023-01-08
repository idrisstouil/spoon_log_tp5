package processor;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtCodeSnippetExpression;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;

import java.util.logging.Logger;

/**
 * Processor for analyzing and modifying Java classes.
 */
public class ClassProcessor extends AbstractProcessor<CtClass> {
	
    public void process(CtClass element) {
        // Create reference to the Logger class
        final CtTypeReference<Logger> loggerRef = getFactory().Code().createCtTypeReference(Logger.class);

        // Create static field for the logger
        final CtField<Logger> loggerField = getFactory().Core().<Logger>createField();
        loggerField.setType(loggerRef);
        loggerField.addModifier(ModifierKind.STATIC);
        loggerField.addModifier(ModifierKind.PRIVATE);
        loggerField.setSimpleName("LOGGER");
 
        // Initialize the logger with the current class name
        String expression = "Logger.getLogger(" + element.getSimpleName() + ".class.getName())";
        final CtCodeSnippetExpression loggerExpression = getFactory().Code().createCodeSnippetExpression(expression);
        loggerField.setDefaultExpression(loggerExpression);

        // Add the logger field to the class
        element.addFieldAtTop(loggerField);
    }
}


