package org.example.automationsystem.Automation;


import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileProcessor {

    private final TextProcessing textProcessor;

    public FileProcessor(TextProcessing textProcessor) {
        this.textProcessor = textProcessor;
    }


    public void processFile(String inputFilePath, String outputFilePath, String replacement) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String processedLine = textProcessor.findAndreplace(line, replacement);
                writer.write(processedLine);
                writer.newLine();
            }
        }
    }


    public void batchProcessDirectory(String inputDirPath, String outputDirPath, String replacement) throws IOException {
        Files.createDirectories(Paths.get(outputDirPath));

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(inputDirPath), "*.txt")) {
            for (Path inputPath : stream) {
                String outputFileName = inputPath.getFileName().toString();
                Path outputPath = Paths.get(outputDirPath, outputFileName);

                processFile(inputPath.toString(), outputPath.toString(), replacement);
            }
        }
    }


    public List<String> extractMatchingLines(String filePath) throws IOException {
        List<String> matchedLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (textProcessor.hasMatch(line)) {
                    matchedLines.add(line);
                }
            }
        }
        return matchedLines;
    }
}
