package org.example.automationsystem.Automation;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FileProcessorTest {

    private TextProcessing processor;
    private FileProcessor fileProcessor;

    private Path tempInputFile;
    private Path tempOutputFile;
    private Path tempInputDir;
    private Path tempOutputDir;

    @BeforeAll
    public void setup() throws IOException {
        processor = new TextProcessing("cat");
        fileProcessor = new FileProcessor(processor);


        tempInputFile = Files.createTempFile("input", ".txt");
        tempOutputFile = Files.createTempFile("output", ".txt");
        Files.writeString(tempInputFile, "The cat sat.\nAnother cat.\nNo match here.");

        // Setup directory
        tempInputDir = Files.createTempDirectory("inputDir");
        tempOutputDir = Files.createTempDirectory("outputDir");

        Files.writeString(Files.createFile(tempInputDir.resolve("file1.txt")), "The cat is here.");
        Files.writeString(Files.createFile(tempInputDir.resolve("file2.txt")), "Another cat is there.");
    }

    @Test
    public void testProcessFile_replacesTextCorrectly() throws IOException {
        fileProcessor.processFile(tempInputFile.toString(), tempOutputFile.toString(), "dog");

        List<String> lines = Files.readAllLines(tempOutputFile);
        assertEquals("The dog sat.", lines.get(0));
        assertEquals("Another dog.", lines.get(1));
        assertEquals("No match here.", lines.get(2));
    }

    @Test
    public void testBatchProcessDirectory_createsProcessedFiles() throws IOException {
        fileProcessor.batchProcessDirectory(tempInputDir.toString(), tempOutputDir.toString(), "dog");

        Path outFile1 = tempOutputDir.resolve("file1.txt");
        Path outFile2 = tempOutputDir.resolve("file2.txt");

        assertTrue(Files.exists(outFile1));
        assertTrue(Files.exists(outFile2));

        assertEquals("The dog is here.", Files.readString(outFile1).trim());
        assertEquals("Another dog is there.", Files.readString(outFile2).trim());
    }

    @Test
    public void testExtractMatchingLines_returnsOnlyMatchingLines() throws IOException {
        List<String> matches = fileProcessor.extractMatchingLines(tempInputFile.toString());

        assertEquals(2, matches.size());
        assertTrue(matches.get(0).contains("cat"));
    }

    @AfterAll
    public void cleanup() throws IOException {
        Files.deleteIfExists(tempInputFile);
        Files.deleteIfExists(tempOutputFile);
        Files.walk(tempInputDir)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        Files.walk(tempOutputDir)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }
}
