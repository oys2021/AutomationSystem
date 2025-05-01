package org.example.automationsystem;



import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import org.example.automationsystem.Automation.*;
import java.util.logging.Logger;
import java.util.logging.Level;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

public class RegexToolController {

    private static final Logger LOGGER = Logger.getLogger(RegexToolController.class.getName());


    @FXML private TextField regexField;
    @FXML private TextArea inputTextArea;
    @FXML private TextArea resultTextArea;
    @FXML private TextField replacementField;
    @FXML private CheckBox replaceCheckbox;
    @FXML private Text matchSummary;

    private final DataManager dataManager = new DataManager();

    private Stage primaryStage;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }



    private String readTextFile(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }



    public void onBrowseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Text File");
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                String content = readTextFile(file);
                inputTextArea.setText(content);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "IOException while reading file", e);
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("File Read Error");
                alert.setHeaderText("Unable to read file");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void onClear() {
        regexField.clear();
        replacementField.clear();
        inputTextArea.clear();
        resultTextArea.clear();
        matchSummary.setText("Matches: None yet");
    }

    @FXML
    private void onClearResults() {
        resultTextArea.clear();
        matchSummary.setText("Matches: None yet");
    }

    private void analyzeWordFrequency() {
        String text = inputTextArea.getText();

        Map<String, Long> wordFreq = Arrays.stream(text.split("\\W+"))
                .filter(word -> !word.isBlank())
                .map(String::toLowerCase)
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()));

        dataManager.storeWordFrequencies(wordFreq);

        StringBuilder result = new StringBuilder("Word Frequency:\n");
        wordFreq.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
                .limit(20)  // Limit to top 20 words
                .forEach(entry -> result.append(String.format("%s: %d\n", entry.getKey(), entry.getValue())));

        resultTextArea.setText(result.toString());
        matchSummary.setText("Top words counted: " + wordFreq.size());
    }



    private void recognizePatterns(String keyword) {
        String text = inputTextArea.getText();

        List<String> linesWithKeyword = Arrays.stream(text.split("\\R"))
                .filter(line -> line.contains(keyword))
                .collect(Collectors.toList());

        resultTextArea.setText("Lines containing \"" + keyword + "\":\n\n" +
                String.join("\n", linesWithKeyword));
        matchSummary.setText("Matches found: " + linesWithKeyword.size());
    }

    @FXML
    private void onWordFrequency() {
        analyzeWordFrequency();
    }

    @FXML
    private void onShowStoredResults() {
        List<MatchEntry> storedMatches = dataManager.getMatchedLines();
        Map<String, Long> storedFreqs = dataManager.getWordFrequencies();

        StringBuilder summary = new StringBuilder("Stored Data:\n\n");

        summary.append("Matches:\n");
        storedMatches.forEach(m -> summary.append(m).append("\n"));

        summary.append("\nWord Frequencies:\n");
        storedFreqs.forEach((word, freq) -> summary.append(word).append(": ").append(freq).append("\n"));

        resultTextArea.setText(summary.toString());
        matchSummary.setText("Showing stored results.");
    }


    @FXML
    private void onDeleteLastMatch() {
        List<MatchEntry> matches = dataManager.getMatchedLines();
        if (!matches.isEmpty()) {
            MatchEntry last = matches.get(matches.size() - 1);
            dataManager.removeEntry(last);
            matchSummary.setText("Last match removed.");
        } else {
            matchSummary.setText("No match to remove.");
        }
    }

    @FXML
    private void onAddCustomMatch() {
        String customText = inputTextArea.getText().trim();
        if (!customText.isEmpty()) {
            MatchEntry custom = new MatchEntry(customText, 0);
            dataManager.addCustomEntry(custom);
            matchSummary.setText("Custom entry added.");
        } else {
            matchSummary.setText("No custom text to add.");
        }
    }



    @FXML
    public void onRun() {
        String text = inputTextArea.getText();
        String patternText = regexField.getText();
        String replacement = replacementField.getText();
        boolean isReplace = replaceCheckbox.isSelected();

        if (patternText.isEmpty()) {
            matchSummary.setText("Please enter a regex pattern.");
            return;
        }

        try {
            TextProcessing pattern = new TextProcessing(patternText);
            Matcher matcher = pattern.matchSetup(text);

            StringBuilder matches = new StringBuilder();
            List<MatchEntry> matchList = new ArrayList<>();
            int count = 0;

            while (matcher.find()) {
                String matchedText = matcher.group();
                int index = matcher.start();
                MatchEntry entry = new MatchEntry(matchedText, index);

                matchList.add(entry);
                matches.append("Match ").append(++count).append(": ").append(entry).append("\n");
            }

            dataManager.storeMatches(matchList);

            if (isReplace) {
                String replacedText = pattern.findAndreplace(text, replacement);
                resultTextArea.setText(replacedText);
            } else {
                resultTextArea.setText("Matches shown below.\n\n" + text);
            }

            matchSummary.setText(count > 0 ? "Matches found: " + count : "No matches found.");
            if (count > 0 && !isReplace) {
                resultTextArea.appendText("\n\nMatch Details:\n" + matches);
            }

        } catch (PatternSyntaxException e) {
            LOGGER.log(Level.SEVERE, "Invalid regex pattern", e);  // ✅ log the exception
            matchSummary.setText("Invalid regex pattern: " + e.getDescription());
        }
    }


    @FXML
    private TextField keywordField;

    @FXML
    private void onKeywordMatch() {
        String keyword = keywordField.getText();
        if (keyword == null || keyword.trim().isEmpty()) {
            matchSummary.setText("Please enter a keyword.");
            return;
        }
        recognizePatterns(keyword.trim());
    }




}
