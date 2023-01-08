package processor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtParameter;
import spoon.support.reflect.declaration.CtConstructorImpl;
public class LoggingProcessor extends AbstractProcessor<CtExecutable> {
    // Set of method names that should not be processed by this processor
    private static final Set<String> METHOD_NAMES_TO_AVOID = new HashSet<String>(Arrays.asList(
        "getUser", "initProducts", "toString", "main", "createUser", "generateRandomID"
    ));
 
    @Override
    public boolean isToBeProcessed(CtExecutable candidate) {
        // Don't process Constructor methods
        if (candidate.getClass().getSimpleName().equals(CtConstructorImpl.class.getSimpleName())) {
            return false;
        }
        return true;
    } 

    public void process(CtExecutable element) {
        if (isToBeProcessed(element)) {
            // Create a code snippet that will be inserted at the beginning of the method body
            CtCodeSnippetStatement snippet = getFactory().Core().createCodeSnippetStatement();

            // Build the snippet which contains the log message
            StringBuilder sb = new StringBuilder();
            List params = element.getParameters();

            if (avoidMethodes(element)) {
                // Append the method name and user information to the log message
                sb.append("\"MethodeName : " + element.getSimpleName().toString()+ "");
                sb.append("\"+\";\"+");
                sb.append("UserService.getUser().toString()");
                sb.append("+ \";\" +");
            
                if (params.size() == 0) {
                    // If there are no parameters, append a message indicating this
                    sb.append("\"no parameters given\"");
                } else {
                    // Append the string representation of each parameter to the log message
                    boolean isBeforePenultimate = true; // penultimate means "before the last"
                    for (int i = 0; i < params.size(); i++) {
                        CtParameter param = (CtParameter) params.get(i);
                        // Make the assumption that the parameters have a toString() method defined
                        sb.append(param.getSimpleName() + ".toString()");
                        isBeforePenultimate = i < (params.size() - 1);
                        if (isBeforePenultimate) {
                            // Separate each parameter with a semicolon
                            sb.append("+ \";\" +");
                        }
                    }
                }

                // Create the final log message using the snippet
                final String value = String.format("LOGGER.info(%s)", sb.toString());
                System.out.println(value);
                snippet.setValue(value);
                if (element.getBody() != null) {
                    // Insert the snippet at the beginning of the method body
                    element.getBody().insertBegin(snippet);
                }
            }
        }
    }

    // Returns true if the given executable element should be processed, false if it should be avoided
    private boolean avoidMethodes(CtExecutable element) {
        return !METHOD_NAMES_TO_AVOID.contains(element.getSimpleName());
    }
}
