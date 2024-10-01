package com.Chat_Filter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ZoomChatSegmenter {

    public static class SingleChat {
        public LocalTime time;
        public String personName;
        public String message;

        public SingleChat(LocalTime time, String personName, String message) {
            this.time = time;
            this.personName = personName;
            this.message = message;
        }

        public LocalTime getTime() {
            return time;
        }

        public void setTime(LocalTime time) {
            this.time = time;
        }

        public String getPersonName() {
            return personName;
        }

        public void setPersonName(String personName) {
            this.personName = personName;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return time + " From " + personName + ": " + message;
        }
    }

    public static List<List<SingleChat>> segmentChatLog(String filePath, long thresholdMinutes) {
        List<List<SingleChat>> segments = new ArrayList<>();
        List<SingleChat> currentSegment = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            LocalTime lastTime = null;
            String lastPersonName = "";
            StringBuilder messageBuilder = new StringBuilder();
            boolean isMessageStart = true; // Flag to indicate if we're at the start of a new message

            while ((line = br.readLine()) != null) {
                if (line.matches("\\d{2}:\\d{2}:\\d{2} From .* [Tt]o Everyone:")) {
                    if (messageBuilder.length() > 0) { // If there's a message, add it before processing a new header
                        SingleChat chat = new SingleChat(lastTime, lastPersonName, messageBuilder.toString().trim());
                        if (lastTime != null && !currentSegment.isEmpty()
                                && ChronoUnit.MINUTES.between(currentSegment.get(currentSegment.size() - 1).time,
                                        chat.time) > thresholdMinutes) {
                            segments.add(new ArrayList<>(currentSegment));
                            currentSegment.clear();
                        }
                        currentSegment.add(chat);
                        messageBuilder = new StringBuilder(); // Reset the message builder
                    }
                    String[] headerParts = line.split(" From | [Tt]o Everyone:");
                    lastTime = LocalTime.parse(headerParts[0].trim());
                    lastPersonName = headerParts[1].trim();
                    isMessageStart = false; // After setting the header, the next lines are part of the message
                } else if (!isMessageStart) {
                    // If the line is part of a message, append it
                    messageBuilder.append(line).append("\n");
                }
            }
            // Add the last chat message if exists
            if (messageBuilder.length() > 0) {
                SingleChat chat = new SingleChat(lastTime, lastPersonName, messageBuilder.toString().trim());
                currentSegment.add(chat);
            }
            // Don't forget to add the last segment
            if (!currentSegment.isEmpty()) {
                segments.add(currentSegment);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return segments;
    }

    public static void main(String[] args) {
        long thresholdMinutes = 2; // Threshold in minutes for segmenting chats

        Path startPath = Paths.get("/Users/shanw25/College/Research/DEWAN_Research/Code-Clustering/data/Zoom-Chats");
        try (Stream<Path> stream = Files.walk(startPath)) {
            stream.filter(Files::isDirectory) // Filter to include only directories
                  .forEach(dirPath -> {
                    String filePath = dirPath + File.separator + "meeting_saved_chat.txt";
                    List<List<SingleChat>> segments = segmentChatLog(filePath, thresholdMinutes);
                    saveSegmentationToDir(filePath, segments);
                  });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // String filePath = "/Users/shanw25/College/Research/DEWAN_Research/Code-Clustering/data/Zoom-Chats/2023-09-19 12.55.01 Comp 524 Lectures Fall 2023/meeting_saved_chat.txt";

        // List<List<SingleChat>> segments = segmentChatLog(filePath, thresholdMinutes);
        // saveSegmentationToDir(filePath, segments);
    }

    public static void segment(String filePath, long thresholdMinutes) {
        List<List<SingleChat>> segments = segmentChatLog(filePath, thresholdMinutes);
        saveSegmentationToDir(filePath, segments);
    }

    private static void saveSegmentationToDir(String filePath, List<List<SingleChat>> segments) {
        // Get the directory of the file path and create a new directory named
        // "segmentation"
        String directoryPath = Paths.get(filePath).getParent().toString();
        String segmentationDirPath = directoryPath + File.separator + "segmentation";
        File segmentationDir = new File(segmentationDirPath);
        if (!segmentationDir.exists()) {
            segmentationDir.mkdirs(); // Create the directory if it doesn't exist
        }

        try{
            boolean hasFile = Files.list(Paths.get(segmentationDirPath)).findAny().isPresent();
            if(hasFile){
                System.out.println(directoryPath + " already has a segmentation files");
                return;
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        // Save each segment to a separate text file
        for (int i = 0; i < segments.size(); i++) {
            String segmentFileName = segmentationDirPath + File.separator + "segment" + (i + 1) + ".txt";
            try (FileWriter writer = new FileWriter(segmentFileName)) {
                for (SingleChat chat : segments.get(i)) {
                    writer.write(chat.toString() + "\n\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Chat segments have been saved to " + segmentationDirPath);
    }
}