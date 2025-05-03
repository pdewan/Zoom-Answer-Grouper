package com.chat.analyze;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OutputDirectoryProcessor {
	
	
	public static List<OutputData> processOutputDirectory(File aChatDirectory) {
        List<OutputData> statsList = new ArrayList<>();

        if (!aChatDirectory.exists() || !aChatDirectory.isDirectory()) {
            System.err.println("Invalid directory: " + aChatDirectory.getAbsolutePath());
            return statsList;
        }

        File[] files = aChatDirectory.listFiles((dir, name) -> name.contains("_segment") && name.endsWith(".txt"));

        if (files == null || files.length == 0) {
            System.err.println("No matching files found in: " + aChatDirectory.getAbsolutePath());
            return statsList;
        }

        for (File file : files) {
            OutputData stats = OutputFileProcessor.processOutputEntry(file);
            if (stats != null) {
                statsList.add(stats);
            } else {
                System.err.println("Skipping unreadable file: " + file.getName());
            }
        }

        return statsList;
    }
	
	 public static void main (String[] args) {
	    	String aFileName = "G:\\My Drive\\533Shared\\s25\\Lectures\\Zoom Chats\\2025-04-15 12.38.35 Comp 533 Lectures\\outputs";
	    	List<OutputData> anOutputData = processOutputDirectory(new File(aFileName));
	    	System.out.println(anOutputData);
	    }

}
