package main;

import java.io.File;

import processor.ClassProcessor;
import processor.ExceptionProcessor;
import processor.LoggingProcessor;
import spoon.Launcher;


public class App {
    /**
     * Processor for analyzing and modifying Java classes.
     */
    private static ClassProcessor classProcessor;
    
    /**
     * Processor for adding logging statements to Java code.
     */
    private static LoggingProcessor loggingProcessor;
    
    /**
     * Processor for adding exception handling to Java code.
     */
    private static ExceptionProcessor exceptionProcessor;

    private static String argSource = "";
    private static String argOutput = "";

    public static void main(String[] args) {
        processArguments(args);
        Launcher spoon = configure();
        spoon.run();
    }

    /**
     * Processes command line arguments and sets the input and output directories.
     * @param args command line arguments for the application
     */
    private static void processArguments(String[] args) {
        argSource = args[0];
        argOutput = args[1];
		argOutput += File.separator + "generatedClassesBySpoon" + File.separator + "src";
    }

    /** 
     * Configures the Spoon library for code transformation.
     * @return configured Launcher object
     */
    private static Launcher configure() {
        Launcher spoon = new Launcher();
        spoon.addInputResource(argSource);
        spoon.setSourceOutputDirectory(argOutput);
        spoon.getEnvironment().setNoClasspath(true);
        spoon.getEnvironment().setAutoImports(true);
        classProcessor = new ClassProcessor();
        exceptionProcessor = new ExceptionProcessor();
        loggingProcessor = new LoggingProcessor();
        spoon.addProcessor(exceptionProcessor);
        spoon.addProcessor(classProcessor);
        spoon.addProcessor(loggingProcessor);
        return spoon;
    }

}