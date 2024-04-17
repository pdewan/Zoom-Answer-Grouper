package com.Chat_Cluster;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnswerParser {
    private static final Pattern ANSWER_PATTERN = Pattern.compile("^(\\d{2}:\\d{2}:\\d{2}) From (.+?): (.+)$");

    public Map<String, String> parse(String filename) throws IOException {
        Map<String, StringBuilder> answers = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        String currentSpeaker = null;
        StringBuilder currentMessage = null;

        while ((line = reader.readLine()) != null) {
            Matcher matcher = ANSWER_PATTERN.matcher(line);
            if (matcher.find()) {
                // New message starts
                String time = matcher.group(1).trim();
                currentSpeaker = matcher.group(2).trim();
                String messageStart = matcher.group(3).trim();

                if (!answers.containsKey(currentSpeaker)) {
                    answers.put(currentSpeaker, new StringBuilder(messageStart));
                } else {
                    answers.get(currentSpeaker).append(System.lineSeparator()).append(messageStart);
                }
                currentMessage = answers.get(currentSpeaker);
            } else {
                // Continue accumulating message for current speaker
                if (currentMessage != null) {
                    currentMessage.append(System.lineSeparator()).append(line);
                }
            }
        }
        reader.close();

        // Convert StringBuilder values to String for final map
        Map<String, String> finalAnswers = new HashMap<>();
        answers.forEach((speaker, messageBuilder) -> finalAnswers.put(speaker, messageBuilder.toString()));

        return finalAnswers;
    }
}
