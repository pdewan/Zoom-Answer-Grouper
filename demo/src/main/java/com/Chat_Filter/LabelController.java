package com.Chat_Filter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.Chat_Filter.ZoomChatSegmenter.SingleChat;
import com.Chat_Group.StudentGroup;

public class LabelController {
    public static void labelCsvFileGenerator(Hashtable<StudentGroup, List<SingleChat>> groupedAnswersTable) {
        // Get the user's home directory and append the Downloads/label path
        String userHome = System.getProperty("user.home");
        Path labelDirectoryPath = Paths.get(userHome, "Downloads", "label");
        
        try {
            // Check if the directory exists, create it if it doesn't
            if (Files.notExists(labelDirectoryPath)) {
                Files.createDirectories(labelDirectoryPath);
            }
            
            // Format the current time to create a unique filename
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
            String formattedDateTime = LocalDateTime.now().format(formatter);
            String fileName = formattedDateTime + ".csv";
            Path filePath = labelDirectoryPath.resolve(fileName);
            
            try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
                Enumeration<StudentGroup> groups = groupedAnswersTable.keys();
                int groupCounter = 1; // To keep track of the group number
                
                while (groups.hasMoreElements()) {
                    StudentGroup group = groups.nextElement();
                    List<SingleChat> chats = groupedAnswersTable.get(group);
                    
                    // Write the group separator as "GroupX"
                    writer.write(String.format("Group%d\n", groupCounter++));
                    
                    // Write each chat in the group
                    for (int i = 0; i < chats.size(); i++) {
                        // Assuming SingleChat's toString method outputs the chat content
                        String message = chats.get(i).getMessage();
                        // Escape double quotes in the message
                        message = message.replace("\"", "\"\"");
                        // Enclose the message in double quotes and prepend it with a comma for CSV formatting
                        writer.write(",\"" + message + "\"\n");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
