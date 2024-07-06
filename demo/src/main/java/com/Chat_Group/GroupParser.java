package com.Chat_Group;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GroupParser {
    private static final Pattern GROUP_PATTERN = Pattern.compile("\\[group (\\d+)\\]: \\[(.+)\\]");

    public Map<Integer, StudentGroup> parse(String filename) throws IOException {
        Map<Integer, StudentGroup> groups = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;

        while ((line = reader.readLine()) != null) {
            Matcher matcher = GROUP_PATTERN.matcher(line);
            if (matcher.find()) {
                int groupNumber = Integer.parseInt(matcher.group(1));
                String[] studentNames = matcher.group(2).split(", ");
                String description = matcher.group(3);
                boolean isCorrect = line.contains("Correct");

                StudentGroup group = new StudentGroup(description, isCorrect);
                for (String name : studentNames) {
                    group.addStudent(name.trim());
                }
                groups.put(groupNumber, group);
            }
        }
        reader.close();
        return groups;
    }

    public Map<Integer, StudentGroup> representationViewParse(String filename) throws IOException {
        Map<Integer, StudentGroup> groups = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;

        while ((line = reader.readLine()) != null) {
            Matcher matcher = GROUP_PATTERN.matcher(line);
            if (matcher.find()) {
                int groupNumber = Integer.parseInt(matcher.group(1));
                String[] studentNames = matcher.group(2).split(", ");

                StudentGroup group = new StudentGroup();
                for (String name : studentNames) {
                    group.addStudent(name.trim());
                }
                groups.put(groupNumber, group);
            }
        }
        reader.close();
        return groups;
    }
}