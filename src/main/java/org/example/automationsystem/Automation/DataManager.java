package org.example.automationsystem.Automation;

import java.util.*;

public class DataManager {

    private final List<MatchEntry> matchedLines = new ArrayList<>();
    private final Map<String, Long> wordFrequencies = new HashMap<>();


    public void storeMatches(List<MatchEntry> matches) {
        matchedLines.clear();
        matchedLines.addAll(matches);
    }

    public List<MatchEntry> getMatchedLines() {
        return new ArrayList<>(matchedLines);
    }

    public void storeWordFrequencies(Map<String, Long> frequencies) {
        wordFrequencies.clear();
        wordFrequencies.putAll(frequencies);
    }

    public Map<String, Long> getWordFrequencies() {
        return new HashMap<>(wordFrequencies);
    }

    public void addCustomEntry(MatchEntry entry) {
        matchedLines.add(entry);
    }

    public boolean removeEntry(MatchEntry entry) {
        return matchedLines.remove(entry);
    }

    public boolean updateEntry(MatchEntry oldEntry, MatchEntry newEntry) {
        int index = matchedLines.indexOf(oldEntry);
        if (index != -1) {
            matchedLines.set(index, newEntry);
            return true;
        }
        return false;
    }

    public void clearAll() {
        matchedLines.clear();
        wordFrequencies.clear();
    }

    public void printSummary() {
        System.out.println("Matched Lines:");
        matchedLines.forEach(System.out::println);
        System.out.println("\nWord Frequencies:");
        wordFrequencies.forEach((word, count) -> System.out.println(word + ": " + count));
    }
}
