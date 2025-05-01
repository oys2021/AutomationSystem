package org.example.automationsystem.Automation;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

public class StreamTextProcessorTest {

    private Path tempFile;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private PrintStream originalOut;

    @BeforeEach
    public void setUp() throws IOException {
        tempFile = Files.createTempFile("streamTest", ".txt");

        Files.write(tempFile, String.join("\n",
                "This is a test file.",
                "Another line with ERROR message.",
                "Test line two.",
                "Error: something went wrong.",
                "The test is successful.",
                "",
                "Another test line."
        ).getBytes());

        // Capture System.out
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void tearDown() throws IOException {

        System.setOut(originalOut);
        outContent.reset();

        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testAnalyzeWordFrequency() throws IOException {
        StreamTextProcessor.analyzeWordFrequency(tempFile.toString());
        String output = outContent.toString();

        assertTrue(output.contains("test:"));
        assertTrue(output.contains("another:"));
        assertTrue(output.contains("error:"));
    }

//    @Test
//    public void testRecognizePattern() throws IOException {
//        StreamTextProcessor.recognizePattern(tempFile.toString(), "\\berror\\b");
//        String output = outContent.toString().toLowerCase();
//
//        assertTrue(output.contains("error message"));
//        assertTrue(output.contains("something went wrong"));
//    }

    @Test
    public void testSummarizeText() throws IOException {
        StreamTextProcessor.summarizeText(tempFile.toString(), 3);
        String output = outContent.toString();

        assertTrue(output.contains("This is a test file."));
        assertTrue(output.contains("Another line with ERROR message."));
        assertTrue(output.contains("Test line two."));
        assertFalse(output.contains("Another test line."));
    }
}
