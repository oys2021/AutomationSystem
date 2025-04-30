package org.example.automationsystem.Automation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextProcessing {

    private String regex;
    private final Pattern pattern;

    public TextProcessing(String regex) {
        this.regex = regex;
        this.pattern = Pattern.compile(regex);
    }

    public Matcher matchSetup(String textInput){
        return  pattern.matcher(textInput);
    }

    public String findAndreplace(String textInput , String replacement ){
        Matcher matcher = this.matchSetup(textInput);
        return matcher.replaceAll(replacement);
    }

    public boolean hasMatch(String textInput) {
        Matcher matcher = pattern.matcher(textInput);
        return matcher.find();
    }

    public String replaceFirst(String textInput, String replacement) {
        Matcher matcher = pattern.matcher(textInput);
        return matcher.replaceFirst(replacement);
    }

    public List<String> findAllMatches(String textInput) {
        Matcher matcher = pattern.matcher(textInput);
        List<String> matches = new ArrayList<>();
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }

    public int countMatches(String textInput) {
        Matcher matcher = pattern.matcher(textInput);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }



}