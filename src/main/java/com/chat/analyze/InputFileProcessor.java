package com.chat.analyze;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.Chat_Cluster.PropertiesManager;

public class InputFileProcessor {
	static String instructorName = PropertiesManager.getInstructorName();
	/**
	 * Given an output file  d/outputs/X_segmentN.txt in some root directory r 
	 * Find the file d/segmentation/segmentN.txt in root directory and return its length
	 * Please do not abbreviate names in camelcase code
	 */
//    public static long processSegmentEntry(File anOutputFile) {
//        String originalFileName = anOutputFile.getName();
//        int underscoreIndex = originalFileName.indexOf('_');
//        if (underscoreIndex == -1) {
//            System.err.println("Filename does not contain '_': " + originalFileName);
//            return 0;
//        }
//
//        // Extract "segmentN.txt" from "X_segmentN.txt"
//        String segmentFileName = originalFileName.substring(underscoreIndex + 1);
//
//        // Navigate from d/outputs to d/segmentation
//        File outputDirectory = anOutputFile.getParentFile(); // .../outputs
//        File baseDirectory = outputDirectory.getParentFile(); // d
//        File segmentationDirectory = new File(baseDirectory, "segmentation");
//        File segmentFile = new File(segmentationDirectory, segmentFileName);
//
//        if (!segmentFile.exists() || !segmentFile.isFile()) {
//            System.err.println("Segment file not found: " + segmentFile.getAbsolutePath());
//            return 0;
//        }
////        return segmentFile.length();
//
//        try {
//            List<String> lines = OutputFileProcessor.readFileWithFallback(segmentFile.toPath());
//            return lines.size();
//        } catch (IOException e) {
//            System.err.println("Error reading segment file: " + segmentFile.getAbsolutePath());
//            return 0;
//        }
//    }
	public static InputData processInputEntry(File anInputFile) {
	    InputData data = new InputData();
	    Map<String, StringBuilder> studentResponses = new HashMap<>();

	    try {
//	        List<String> lines = Files.readAllLines(anInputFile.toPath());
	        List<String> lines = OutputFileProcessor.readFileWithFallback(anInputFile.toPath());

	        String currentStudent = null;

	        for (String line : lines) {
	            line = line.trim();
	            if (line.isEmpty()) continue;

	            if (line.matches("\\d{2}:\\d{2}:\\d{2} From .+: .+")) {
	                // New message
	                int fromIndex = line.indexOf("From ") + 5;
	                int colonIndex = line.indexOf(":", fromIndex);

	                if (fromIndex != -1 && colonIndex != -1) {
	                    String studentName = line.substring(fromIndex, colonIndex).trim();
	                    studentName = OutputFileProcessor.cleanStudentName(studentName);
	                    String message = line.substring(colonIndex + 1).trim();

	                    currentStudent = studentName;
	                    studentResponses.putIfAbsent(currentStudent, new StringBuilder());
	                    studentResponses.get(currentStudent).append(message).append("\n");
	                }
	            } else if (currentStudent != null) {
	                // Continuation of previous message
	                studentResponses.get(currentStudent).append(line).append("\n");
	            }
	        }

	        // Convert StringBuilder map to String map
	        Map<String, String> resultMap = new HashMap<>();
	        for (Map.Entry<String, StringBuilder> entry : studentResponses.entrySet()) {
	            resultMap.put(entry.getKey(), entry.getValue().toString().trim());
	        }

	        data.inputFile = anInputFile;
	        data.inputStudentData = resultMap;
	        String aQuestion = resultMap.get(instructorName);
	        if (aQuestion != null) {
	        	data.askedQuestion = aQuestion;
	        	resultMap.remove(instructorName);
	        }
	        

	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return data;
	}
    public static void main (String[] args) {
    	String aFileName = "G:\\My Drive\\533Shared\\s25\\Lectures\\Zoom Chats\\2025-04-15 12.38.35 Comp 533 Lectures\\segmentation\\segment1.txt";
    	InputData anInputData = processInputEntry(new File(aFileName));
    	System.out.println(anInputData.inputStudentData);
    }
}


