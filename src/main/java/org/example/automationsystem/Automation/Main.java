package org.example.automationsystem.Automation;


import org.example.automationsystem.Automation.FileProcessor;
import org.example.automationsystem.Automation.TextProcessing;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

import static org.example.automationsystem.Automation.StreamTextProcessor.*;

public class Main {
    public static void main(String[] args) {
        String regex = "foo";
        String replacement = "bar";

        // Load input.txt from resources folder
        URL resource = Main.class.getResource("/input.txt");
        if (resource == null) {
            System.err.println("ERROR: input.txt not found in resources folder!");
            return;
        }

        String inputFilePath;
        try {
            inputFilePath = Paths.get(resource.toURI()).toString();
        } catch (Exception e) {
            System.err.println("ERROR: Could not convert resource to file path.");
            return;
        }

        String outputFilePath = "output.txt";  // Will be created in the working directory

        TextProcessing processor = new TextProcessing(regex);
        FileProcessor fileProcessor = new FileProcessor(processor);

        try {
            fileProcessor.processFile(inputFilePath, outputFilePath, replacement);
            System.out.println("File processed successfully. Output saved to: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Error during file processing: " + e.getMessage());
        }

        try {
            System.out.println("=== Word Frequency Analysis ===");
            analyzeWordFrequency(inputFilePath);

            System.out.println("\n=== Pattern Recognition: 'error' ===");
            recognizePattern(inputFilePath, "\\berror\\b");  // looks for whole word 'error'

            System.out.println("\n=== Text Summarization (Top 5 Lines) ===");
            summarizeText(inputFilePath, 5);

        } catch (IOException e) {
            System.err.println("File error: " + e.getMessage());
        }
    }

}
