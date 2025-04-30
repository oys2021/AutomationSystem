package org.example.automationsystem.Automation;


import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

public class StreamTextProcessor {


    public static void analyzeWordFrequency(String filePath) throws IOException {
        Map<String, Long> wordCounts = Files.lines(Paths.get(filePath))
                .flatMap(line -> Arrays.stream(line.trim().split("\\W+")))
                .filter(word -> !word.isEmpty())
                .map(String::toLowerCase)
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()));

        System.out.println("Top 10 Most Frequent Words:");
        wordCounts.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));
    }

    public static void recognizePattern(String filePath, String regex) throws IOException {
        Pattern pattern = Pattern.compile(regex);
        System.out.println("Lines matching pattern '" + regex + "':");

        Files.lines(Paths.get(filePath))
                .filter(line -> pattern.matcher(line).find())
                .forEach(System.out::println);
    }

    public static void summarizeText(String filePath, int maxLines) throws IOException {
        List<String> summary = Files.lines(Paths.get(filePath))
                .filter(line -> !line.trim().isEmpty())
                .limit(maxLines)
                .collect(Collectors.toList());

        System.out.println("Text Summary (first " + maxLines + " lines):");
        summary.forEach(System.out::println);
    }


    public static void main(String[] args) throws IOException {
        String file = "sample.txt";

        analyzeWordFrequency(file);
        System.out.println("-----");
        recognizePattern(file, "\\berror\\b");
        System.out.println("-----");
        summarizeText(file, 5);
    }
}
