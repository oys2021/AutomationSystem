package org.example.automationsystem.Automation;


import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TextProcessingTest {

    @Test
    public void testHasMatch_true() {
        TextProcessing processor = new TextProcessing("\\d+");
        assertTrue(processor.hasMatch("There are 42 apples"));
    }

    @Test
    public void testHasMatch_false() {
        TextProcessing processor = new TextProcessing("\\d+");
        assertFalse(processor.hasMatch("No numbers here"));
    }

    @Test
    public void testReplaceFirst() {
        TextProcessing processor = new TextProcessing("\\d+");
        String result = processor.replaceFirst("Numbers: 123 and 456", "X");
        assertEquals("Numbers: X and 456", result);
    }

//    @Test
//    public void testFindAllMatches() {
//        TextProcessing processor = new TextProcessing("\\b[a-zA-Z]{4}\\b");
//        List<String> matches = processor.findAllMatches("This test finds four word list items like word or test");
//        assertEquals(List.of("This", "test", "find", "word", "like", "word", "test"), matches);
//    }

    @Test
    public void testCountMatches() {
        TextProcessing processor = new TextProcessing("\\d+");
        int count = processor.countMatches("1 fish, 2 fish, 3 fish");
        assertEquals(3, count);
    }

    @Test
    public void testMatchSetupReturnsMatcher() {
        TextProcessing processor = new TextProcessing("\\w+");
        assertNotNull(processor.matchSetup("Testing matchSetup"));
    }
}
