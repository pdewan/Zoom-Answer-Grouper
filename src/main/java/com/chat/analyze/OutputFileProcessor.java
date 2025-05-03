package com.chat.analyze;

import java.io.File;
/*
 * Mistakes_segment1
 * File Name:
 *Input Format
 *
 1 min, 17 sec
Predicted Question: How would you change your implementation to support atomic broadcast, ensuring that a process does not execute a command locally before broadcasting?

Predicted Correct Answer: To support atomic broadcast, the implementation should be changed so that a process does not execute a command locally before broadcasting. Instead, it should send the command to a central relayer or coordinator, which collects commands from all processes, establishes a total order, and then broadcasts the ordered commands back to all processes. Only after receiving the ordered commands from the relayer should a process execute them, ensuring that all processes execute commands in the same order.
 */
/*
 * Output
 * Date:  1 min 17 sec
 * Type: Mistakes
 * Preview:
 * 
 * Time: 1 min 17 sec
 * Next three  lines
 */
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class OutputFileProcessor {
	/**
	 * 
	 
	 */
	public static OutputData processOutputEntry(File anOutputFile) {
	    OutputData outputData = new OutputData();
	    outputData.outputFile = anOutputFile;

	    try {
	        List<String> lines = readFileWithFallback(anOutputFile.toPath());

	        if (lines.size() < 4) {
	            System.err.println("File does not contain enough lines: " + anOutputFile.getName());
	            return null;
	        }

	        // Parse time from the first line
	        String timeLine = lines.get(0).trim();
	        long timeInSeconds = parseTimeToSeconds(timeLine);
	        outputData.time = timeInSeconds;

//	        // Next three lines as predicted Q+A
//	        StringBuilder qnaBuilder = new StringBuilder();
//	        for (int i = 1; i <= 3 && i < lines.size(); i++) {
//	            qnaBuilder.append(lines.get(i)).append("\n");
//	        }
	        
	     // Capture all lines after time for full predicted Q+A
	        StringBuilder qnaBuilder = new StringBuilder();
	        for (int i = 1; i < lines.size(); i++) {
	            qnaBuilder.append(lines.get(i)).append("\n");
	        }
	        outputData.predictedQuestionAndAnswer = qnaBuilder.toString().trim();
	        outputData.qaLength = outputData.predictedQuestionAndAnswer.length();
	        outputData.predictedQuestionAndAnswer = qnaBuilder.toString().trim();
	        outputData.qaLength = outputData.predictedQuestionAndAnswer.length();

//	        // Total length = number of lines in the file
//	        outputData.totalLength = lines.size();

	        // Use file modification time as timeAsked
	        outputData.timeAsked = anOutputFile.lastModified();
	        outputData.totalOutputLength = anOutputFile.length();  // Byte count of the file
	        outputData.dateAsked = new Date(outputData.timeAsked);

	        // Extract question type from filename (before _segment)
	        String filename = anOutputFile.getName();
	        int underscoreIndex = filename.indexOf("_segment");
	        if (underscoreIndex != -1) {
	            outputData.questionType = filename.substring(0, underscoreIndex);
	        } else {
	            outputData.questionType = "Unknown";
	        }

	        // Split Question and Answer based on logic
	        splitQuestionAndAnswer(outputData);

	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }

	    return outputData;
	}

//	private static void splitQuestionAndAnswer(OutputData outputData) {
//	    String[] lines = outputData.predictedQuestionAndAnswer.split("\n");
//
//	    // Extract Question (common for all types)
//	    outputData.predictedQuestion = extractQuestion(lines);
//
//	    // Dispatch based on question type
//	    switch (outputData.questionType.toLowerCase()) {
//	        case "mistakes":
//	            outputData.predictedAnswer = extractMistakesAnswer(lines);
//	            break;
//	        case "classifications":
//	            outputData.predictedAnswer = extractClassificationsAnswer(lines);
//	            break;
//	        default:
//	            outputData.predictedAnswer = extractDefaultAnswer(lines);
//	            break;
//	    }
//
//	    // Handle missing values
//	    if (outputData.predictedQuestion.isEmpty()) {
//	        outputData.predictedQuestion = "(No question found)";
//	    }
//	    if (outputData.predictedAnswer.isEmpty()) {
//	        outputData.predictedAnswer = "(No answer found)";
//	    }
//	}
	private static void splitQuestionAndAnswer(OutputData outputData) {
	    String[] lines = outputData.predictedQuestionAndAnswer.split("\n");

	    switch (outputData.questionType.toLowerCase()) {
	        case "mistakes":
//	            extractMistakesQuestionAndAnswer(lines, outputData);
//	        	extractMistakesStudentDataFlexible(lines, outputData);
//	        	extractMistakesQuestionsAnsersAndStudentMap(lines, outputData);
	        	extractMistakeQuestionAnswersClustersAndStudents(lines, outputData);

	            break;
	        case "classifications":
	        	extractClassificationsQuestionAnswerAndStudentMap(lines, outputData);
//	            extractClassificationsQuestionAndAnswer(lines, outputData);
	            break;
	        case "differences":
//	            extractDifferencesQuestionAndAnswer(lines, outputData);
	            extractDifferencesQuestionAnswerAndStudentMap(lines, outputData);
	            break;
	        case "motivations":
//	            extractMotivationsQuestionAndAnswer(lines, outputData);
	        	extractMotivationQuestionAnswerAndStudentMap(lines, outputData);
	            break;   
	        case "enumeration":
	            extractEnumerationQuestionAnswerAndStudentMap(lines, outputData);

//	            extractEnumerationQuestionAndAnswer(lines, outputData);
	            break;
	        case "definition":
//	            extractDefinitionQuestionAndAnswer(lines, outputData);
	            extractDefinitionQuestionAnswerAndStudentMap(lines, outputData);
	            break;
	        case "tradeoff":
	            extractTradeoffQuestionAndAnswer(lines, outputData);
	            break;
	        case "corrections": 
//	            extractCorrectionsQuestionAndAnswer(lines, outputData);
	            extractCorrectionsQuestionAnswerAndStudentMap(lines, outputData);
	            break;
	        default:
	            extractDefaultQuestionAndAnswer(lines, outputData);
	            break;
	            
	    }

	    if (outputData.predictedQuestion.isEmpty()) {
	        outputData.predictedQuestion = "(No question found)";
	    }
	    if (outputData.predictedAnswer.isEmpty()) {
	        outputData.predictedAnswer = "(No answer found)";
	    }
	}
	
//	private static String extractQuestion(String[] lines) {
//	    StringBuilder questionBuilder = new StringBuilder();
//
//	    for (String line : lines) {
//	        if (line.toLowerCase().contains("question")) {
//	            questionBuilder.append(line).append("\n");
//	        }
//	    }
//
//	    String result = questionBuilder.toString().trim();
//
//	    if (result.isEmpty() && lines.length > 0) {
//	        // Fallback to first line if no question-like line is found
//	        result = lines[0].trim();
//	    }
//
//	    return result;
//	}
	private static void extractMistakesStudentDataFlexible(String[] lines, OutputData data) {
	    Map<String, String> studentMap = new HashMap<>();
	    String currentStudent = null;
	    StringBuilder currentAnswer = new StringBuilder();
	    boolean inAnswer = false;

	    for (String line : lines) {
	        String trimmedLine = line.trim();

	        // Detect start of a new student
	        if (trimmedLine.startsWith("author:") || trimmedLine.startsWith("#### author:")) {
	            // Save previous student data
	            if (currentStudent != null && currentAnswer.length() > 0) {
	                studentMap.put(currentStudent, currentAnswer.toString().trim());
	            }
	            // Start new student
	            currentStudent = trimmedLine.replace("#### author:", "").replace("author:", "").trim();
	            currentAnswer = new StringBuilder();
	            inAnswer = false;
	            continue;
	        }

	        // Detect answer start (markdown or flat)
	        if (trimmedLine.startsWith("- **answer:**") || trimmedLine.startsWith("answer:")) {
	            inAnswer = true;
	            // Capture first line of answer
	            String answerText = trimmedLine.contains(":") ? trimmedLine.substring(trimmedLine.indexOf(":") + 1).trim() : "";
	            currentAnswer.append(answerText).append("\n");
	            continue;
	        }

	        // Stop answer at mistakes/fixes or new author
	        if (trimmedLine.contains("- **mistakes:**") || trimmedLine.startsWith("mistakes:") ||
	            trimmedLine.startsWith("- **fixes:**") || trimmedLine.startsWith("fixes:") ||
	            trimmedLine.startsWith("author:") || trimmedLine.startsWith("#### author:")) {
	            inAnswer = false;
	            continue;
	        }

	        // Continue multi-line answer
	        if (inAnswer && currentStudent != null) {
	            currentAnswer.append(line).append("\n");
	        }
	    }

	    // Save the last student data
	    if (currentStudent != null && currentAnswer.length() > 0) {
	        studentMap.put(currentStudent, currentAnswer.toString().trim());
	    }

	    data.processedStudentData = studentMap;
	}
	private static void extractMistakesQuestionAndAnswer(String[] lines, OutputData outputData) {
	    // Extract question from lines
	    StringBuilder questionBuilder = new StringBuilder();
	    for (String line : lines) {
	        if (line.toLowerCase().contains("question")) {
	            questionBuilder.append(line).append("\n");
	        }
	    }
	    if (questionBuilder.length() == 0 && lines.length > 0) {
	        questionBuilder.append(lines[0].trim());
	    }
	    outputData.predictedQuestion = questionBuilder.toString().trim();

	    // Extract answer
	    StringBuilder answerBuilder = new StringBuilder();
	    int maxLinesToCheck = Math.min(lines.length, MAX_QUESTION_LINES);
	    for (int i = 1; i < maxLinesToCheck; i++) {
	        String line = lines[i].trim().toLowerCase();
	        if (line.contains("answer")) {
	            answerBuilder.append(lines[i]).append("\n");
	        }
	        if (lines[i].startsWith("###") || lines[i].startsWith("**Heading:")) {
	            break;
	        }
	    }
	    outputData.predictedAnswer = answerBuilder.toString().trim();
	}
	private static void extractMistakesQuestionsAnsersAndStudentMap(String[] lines, OutputData outputData) {
	    Map<String, String> studentMap = new HashMap<>();

	    StringBuilder questionBuilder = new StringBuilder();
	    StringBuilder answerBuilder = new StringBuilder();

	    String currentStudent = null;
	    StringBuilder currentAnswer = new StringBuilder();
	    boolean inAnswerSection = false;

	    for (int i = 0; i < lines.length; i++) {
	        String line = lines[i];
	        String trimmedLine = line.trim().toLowerCase();

	        // Question extraction logic
	        if (trimmedLine.contains("question")) {
	            questionBuilder.append(line).append("\n");
	        }

	        // Answer extraction logic (early part of the file, first few lines)
	        if (i < MAX_QUESTION_LINES && trimmedLine.contains("answer") && !trimmedLine.contains("author:")) {
	            answerBuilder.append(line).append("\n");
	        }

	        // Student Data extraction logic
//	        if (trimmedLine.contains("author")) {

	        if (trimmedLine.contains("author:")) {
	            // Save previous student
	            if (currentStudent != null && currentAnswer.length() > 0) {
	                studentMap.put(currentStudent, currentAnswer.toString().trim());
	            }
	            String rawStudent = line.substring(line.toLowerCase().indexOf("author:") + 7).trim();
//	            String rawStudent = line.substring(line.toLowerCase().indexOf("author") + 7).trim();

	            currentStudent = cleanStudentName(rawStudent);
	            currentAnswer = new StringBuilder();
	            inAnswerSection = false;
	            continue;
	        }
	        

	        if (trimmedLine.contains("answer:") && currentStudent != null) {
	            inAnswerSection = true;
	            String answerText = line.substring(line.toLowerCase().indexOf("answer:") + 7).trim();
	            currentAnswer.append(answerText).append("\n");
	            continue;
	        }

	        if (trimmedLine.contains("mistake:") || trimmedLine.contains("fix:") || trimmedLine.contains("author:")) {
	            inAnswerSection = false;
	            continue;
	        }

	        if (inAnswerSection && currentStudent != null) {
	            currentAnswer.append(line).append("\n");
	        }
	    }

	    // Save last student data
	    if (currentStudent != null && currentAnswer.length() > 0) {
	        studentMap.put(currentStudent, currentAnswer.toString().trim());
	    }

	    // Handle defaults if question not found
	    if (questionBuilder.length() == 0 && lines.length > 0) {
	        questionBuilder.append(lines[0].trim());
	    }

	    outputData.predictedQuestion = questionBuilder.toString().trim();
	    outputData.predictedAnswer = answerBuilder.toString().trim();
	    outputData.processedStudentData = studentMap;
	}
	
//	private static String cleanRepresentativeAnswer(String repAnswerLine) {
//	    if (repAnswerLine == null || repAnswerLine.isEmpty()) {
//	        return "";
//	    }
//	    int colonIndex = repAnswerLine.indexOf(":");
//	    if (colonIndex != -1 && colonIndex < repAnswerLine.length() - 1) {
//	        return repAnswerLine.substring(colonIndex + 1).trim();
//	    } else {
//	        return "(No representative answer provided)";
//	    }
//	}
	
//	private static void extractClustersAndStudents(String[] lines) {
//	    Map<String, List<String>> clusterStudentMap = new LinkedHashMap<>();
//	    String currentCluster = null;
//
//	    for (String line : lines) {
//	        String trimmedLine = line.trim();
//
//	        if (trimmedLine.startsWith("**Cluster")) {
//	            currentCluster = trimmedLine.replace("**", "").replace(":", "").trim();
//	            clusterStudentMap.put(currentCluster, new ArrayList<>());
//	            continue;
//	        }
//
//	        if (trimmedLine.startsWith("author:") && currentCluster != null) {
//	            String studentName = trimmedLine.substring("author:".length()).trim();
//	            clusterStudentMap.get(currentCluster).add(studentName);
//	        }
//	    }
//
//	    // Debug print to check if it's working
//	    for (Map.Entry<String, List<String>> entry : clusterStudentMap.entrySet()) {
//	        System.out.println("Cluster: " + entry.getKey());
//	        for (String student : entry.getValue()) {
//	            System.out.println("  - " + student);
//	        }
//	    }
//	}
//	private static void extractClustersAndStudents(String[] lines, OutputData outputData) {
//	    Map<String, String> studentMap = new HashMap<>();
//	    StringBuilder questionBuilder = new StringBuilder();
//	    StringBuilder answerBuilder = new StringBuilder();
//
//	    String currentCluster = null;
//	    String representativeAnswer = "";
//	    boolean inCluster = false;
//	    boolean inStudentList = false;
//
//	    for (int i = 0; i < lines.length; i++) {
//	        String line = lines[i];
//	        String trimmedLine = line.trim();
//
//	        // Question detection (first part)
//	        if (trimmedLine.toLowerCase().contains("predicted question")) {
//	            questionBuilder.append(line).append("\n");
//	        }
//
//	        // Predicted answer detection
//	        if (trimmedLine.toLowerCase().contains("predicted correct answer")) {
//	            answerBuilder.append(line).append("\n");
//	            continue;
//	        }
//
//	        // Cluster start detection
//	        if (trimmedLine.startsWith("**Cluster")) {
//	            inCluster = true;
//	            inStudentList = false;
//	            currentCluster = trimmedLine.replace("**", "").trim(); // Save cluster name
//	            continue;
//	        }
//
//	        // Heading or Representative Answer line
//	        if (trimmedLine.startsWith("Representative Answer:")) {
//	            representativeAnswer = cleanRepresentativeAnswer(trimmedLine);
//	            // Add to predictedAnswer
//	            answerBuilder.append(currentCluster).append("\n");
//	            answerBuilder.append(representativeAnswer).append("\n");
//	            continue;
//	        }
//
//	        // Student list start detection
//	        if (trimmedLine.startsWith("For each answer in the cluster:")) {
//	            inStudentList = true;
//	            continue;
//	        }
//
//	        // Process student entries
//	        if (inStudentList && trimmedLine.toLowerCase().startsWith("author:")) {
//	            String studentName = trimmedLine.substring(7).trim();
//	            String cleanedName = cleanStudentName(studentName);
//	            String combinedAnswer = representativeAnswer.isEmpty() ? "(No representative answer provided)" : representativeAnswer;
//	            studentMap.put(cleanedName, combinedAnswer);
//	            continue;
//	        }
//
//	        // Append other lines to predicted answer if not part of student section
//	        if (!inStudentList && inCluster && !trimmedLine.startsWith("**Cluster")) {
//	            answerBuilder.append(line).append("\n");
//	        }
//	    }
//
//	    if (questionBuilder.length() == 0 && lines.length > 0) {
//	        questionBuilder.append(lines[0].trim());
//	    }
//
//	    outputData.predictedQuestion = questionBuilder.toString().trim();
//	    outputData.predictedAnswer = answerBuilder.toString().trim();
//	    outputData.processedStudentData = studentMap;
//	}
	public static final String AUTHOR_OF_REPRESENTATIVE_ANSWER = "Author of Representative Answer\n";
	private static void extractMistakeQuestionAnswersClustersAndStudents(String[] lines, OutputData outputData) {
	    Map<String, String> studentMap = new HashMap<>();
	    StringBuilder questionBuilder = new StringBuilder();
	    StringBuilder answerBuilder = new StringBuilder();

	    String currentCluster = null;
	    String representativeAnswer = "";
	    String representativeAnswerLine = "";
	    boolean inCluster = false;
	    boolean inStudentSection = false;

	    String currentStudent = null;
	    StringBuilder currentStudentContent = new StringBuilder();

	    for (int i = 0; i < lines.length; i++) {
	        String line = lines[i];
	        String trimmedLine = line.trim();

	        // Extract Question
	        if (trimmedLine.toLowerCase().contains("predicted question")) {
	            questionBuilder.append(line).append("\n");
	        }

	        // Extract Predicted Correct Answer
	        if (trimmedLine.toLowerCase().contains("predicted correct answer")) {
	            answerBuilder.append(line).append("\n");
	            continue;
	        }

	        // Detect Cluster
	        if (trimmedLine.contains("Cluster") 
//	        		|| trimmedLine.contains("Answer Cluster")
	        		)
	        {
	            if (currentStudent != null && currentStudentContent.length() > 0) {
	                studentMap.put(currentStudent, currentStudentContent.toString().trim());
	                currentStudent = null;
	                currentStudentContent = new StringBuilder();
	            }
	            currentCluster = trimmedLine.replace("###", "").replace("**", "").trim();
	            inCluster = true;
	            inStudentSection = false;
//	            answerBuilder.append(currentCluster).append("\n");
	            representativeAnswer = "";
	            continue;
	        }

	        // Representative Answer
	        if (trimmedLine.toLowerCase().contains("representative answer")) {
	        	representativeAnswerLine = trimmedLine;
	            representativeAnswer = cleanRepresentativeAnswer(trimmedLine);
//	            if (!representativeAnswer.equals("(No representative answer provided)")) {
//	                answerBuilder.append("Representative Answer: ").append(representativeAnswer).append("\n");
//	            }
	            continue;
	        }

	        // For each answer start
	        if (trimmedLine.toLowerCase().contains("for each answer in the cluster"))
//	        		trimmedLine.toLowerCase().contains("author")
	        		 {
	            inStudentSection = true;
	            continue;
	        }

	        // Handle individual student entries
	        if (
//	        		inStudentSection && 
	        		trimmedLine.toLowerCase().contains("author")) {
	            if (currentStudent != null && currentStudentContent.length() > 0) {
	            	if (representativeAnswerLine.contains(currentStudent)) {
	            		currentStudentContent.append(AUTHOR_OF_REPRESENTATIVE_ANSWER);
	            	}
	                studentMap.put(currentStudent, currentStudentContent.toString().trim());
	            }
	            String rawStudent = trimmedLine.substring(trimmedLine.toLowerCase().indexOf("author:") + 7).trim();
	            currentStudent = cleanStudentName(rawStudent);
	            currentStudentContent = new StringBuilder();

	            if (!representativeAnswer.isEmpty() && !representativeAnswer.equals("(No representative answer provided)")) {
	                currentStudentContent.append("Representative Answer: ").append(representativeAnswer).append("\n");
	            }
	            continue;
	        }

	        // Collect Answer, Mistakes, Fixes per student
	        if (currentStudent != null ) {
//	        		&& (trimmedLine.toLowerCase().startsWith("answer:")
//	                || trimmedLine.toLowerCase().contains("mistake")
//	                || trimmedLine.toLowerCase().contains("fixes")
//	                || trimmedLine.startsWith("-"))) {
	            currentStudentContent.append(line).append("\n");
	        }
	    }

	    // Save the last student's data
	    if (currentStudent != null && currentStudentContent.length() > 0) {
	        studentMap.put(currentStudent, currentStudentContent.toString().trim());
	    }

	    // Handle default if question not found
	    if (questionBuilder.length() == 0 && lines.length > 0) {
	        questionBuilder.append(lines[0].trim());
	    }

	    outputData.predictedQuestion = questionBuilder.toString().trim();
	    outputData.predictedAnswer = answerBuilder.toString().trim();
	    outputData.processedStudentData = studentMap;
	}
//	private static void extractClustersAndStudents(String[] lines, OutputData outputData) {
//	    Map<String, String> studentMap = new HashMap<>();
//	    StringBuilder questionBuilder = new StringBuilder();
//	    StringBuilder answerBuilder = new StringBuilder();
//
//	    String currentCluster = null;
//	    String representativeAnswer = "";
//	    boolean inCluster = false;
//	    boolean inStudentSection = false;
//
//	    String currentStudent = null;
//	    StringBuilder currentStudentContent = new StringBuilder();
//
//	    for (int i = 0; i < lines.length; i++) {
//	        String line = lines[i];
//	        String trimmedLine = line.trim();
//
//	        // Extract Question
//	        if (trimmedLine.toLowerCase().contains("predicted question")) {
//	            questionBuilder.append(line).append("\n");
//	        }
//
//	        // Extract Predicted Correct Answer
//	        if (trimmedLine.toLowerCase().contains("predicted correct answer")) {
//	            answerBuilder.append(line).append("\n");
//	            continue;
//	        }
//
//	        // Detect Cluster
//	        if (trimmedLine.toLowerCase().contains("cluster")) {
//	            // Save any in-progress student content
//	            if (currentStudent != null && currentStudentContent.length() > 0) {
//	                studentMap.put(currentStudent, currentStudentContent.toString().trim());
//	                currentStudent = null;
//	                currentStudentContent = new StringBuilder();
//	            }
//	            currentCluster = trimmedLine.replace("###", "").replace("**", "").trim();
//	            inCluster = true;
//	            inStudentSection = false;
//	            answerBuilder.append(currentCluster).append("\n");
//	            representativeAnswer = "";
//	            continue;
//	        }
//
//	        // Representative Answer (optional)
//	        if (trimmedLine.toLowerCase().startsWith("representative answer")) {
//	            representativeAnswer = cleanRepresentativeAnswer(trimmedLine);
//	            continue; // Do not append to answerBuilder
//	        }
//
//	        // For each answer start (optional marker, some files skip it)
//	        if (trimmedLine.toLowerCase().contains("for each answer in the cluster")) {
//	            inStudentSection = true;
//	            continue;
//	        }
//
//	        // Handle individual student entries
//	        if (inCluster && trimmedLine.toLowerCase().startsWith("author")) {
//	            if (currentStudent != null && currentStudentContent.length() > 0) {
//	                studentMap.put(currentStudent, currentStudentContent.toString().trim());
//	            }
//	            String rawStudent = trimmedLine.substring(trimmedLine.toLowerCase().indexOf("author:") + 7).trim();
//	            currentStudent = cleanStudentName(rawStudent);
//	            currentStudentContent = new StringBuilder();
//
//	            if (!representativeAnswer.isEmpty() && !representativeAnswer.equals("(No representative answer provided)")) {
//	                currentStudentContent.append("Representative Answer: ").append(representativeAnswer).append("\n");
//	            }
//	            continue;
//	        }
//
//	        // Collect Answer, Mistakes, Fixes per student
//	        if (currentStudent != null && (
//	                trimmedLine.toLowerCase().startsWith("answer:")
//	                || trimmedLine.toLowerCase().startsWith("mistakes:")
//	                || trimmedLine.toLowerCase().startsWith("fixes:")
//	                || trimmedLine.startsWith("-"))) {
//	            currentStudentContent.append(line).append("\n");
//	        }
//	    }
//
//	    // Save last student data
//	    if (currentStudent != null && currentStudentContent.length() > 0) {
//	        studentMap.put(currentStudent, currentStudentContent.toString().trim());
//	    }
//
//	    // Handle default if question not found
//	    if (questionBuilder.length() == 0 && lines.length > 0) {
//	        questionBuilder.append(lines[0].trim());
//	    }
//
//	    outputData.predictedQuestion = questionBuilder.toString().trim();
//	    outputData.predictedAnswer = answerBuilder.toString().trim();
//	    outputData.processedStudentData = studentMap;
//	}



	// Helper function for cleaning representative answer
	private static String cleanRepresentativeAnswer(String repAnswerLine) {
	    if (repAnswerLine == null || repAnswerLine.isEmpty()) {
	        return "";
	    }

	    // Find the position after "Representative Answer:" to avoid the first colon.
	    int firstColonIndex = repAnswerLine.indexOf(":");
	    if (firstColonIndex == -1) {
	        return "(No representative answer provided)";
	    }

	    // Now, find the next colon after the first one to locate the student's colon.
	    int secondColonIndex = repAnswerLine.indexOf(":", firstColonIndex + 1);
	    if (secondColonIndex != -1 && secondColonIndex < repAnswerLine.length() - 1) {
	        return repAnswerLine.substring(secondColonIndex + 1).trim();
	    } else {
	        return "(No representative answer provided)";
	    }
	}


	private static final int MAX_QUESTION_LINES = 4;

//	private static String extractMistakesAnswer(String[] lines) {
//	    StringBuilder answerBuilder = new StringBuilder();
//	    int maxLinesToCheck = Math.min(lines.length, MISTAKES_MAX_LINES_TO_CHECK);
//
//	    for (int i = 1; i < maxLinesToCheck; i++) {
//	        String line = lines[i].trim().toLowerCase();
//	        if (line.contains("answer")) {
//	            answerBuilder.append(lines[i]).append("\n");
//	        }
//	        if (lines[i].startsWith("###") || lines[i].startsWith("**Heading:")) {
//	            break;
//	        }
//	    }
//
//	    return answerBuilder.toString().trim();
//	}
	private static void extractClassificationsQuestionAnswerAndStudentMap(String[] lines, OutputData outputData) {
	    Map<String, String> studentDataMap = new HashMap<>();

	    // Extract question
	    StringBuilder questionBuilder = new StringBuilder();
	    for (String line : lines) {
	        if (line.toLowerCase().contains("question")) {
	            questionBuilder.append(line).append("\n");
	        }
	    }
	    if (questionBuilder.length() == 0 && lines.length > 0) {
	        questionBuilder.append(lines[0].trim());
	    }
	    outputData.predictedQuestion = questionBuilder.toString().trim();

	    // Extract answer and student data
	    StringBuilder answerBuilder = new StringBuilder();
	    String currentObject = "";

	    for (String line : lines) {
	        line = line.trim();
	        if (line.isEmpty()) continue;

	        // Object header line
	        if (line.startsWith("**Object")) {
	            currentObject = line.replace("**", "").replace(":", "").trim();  // e.g., "Object 1"
	            answerBuilder.append(line).append("\n");
	            continue;
	        }

	        // Category line with student list
	        if (line.startsWith("-")) {
	            int colonIndex = line.indexOf(':');
	            if (colonIndex != -1) {
	                String category = line.substring(0, colonIndex).replace("-", "").trim();  // e.g., "Concurrent"
	                String studentList = line.substring(colonIndex + 1).trim();  // e.g., "Krish Patel, ..."

	                answerBuilder.append(category).append("\n");

	                String[] students = studentList.split(",");
	                for (String student : students) {
	                    String cleanedName = student.trim().replaceAll("\\(.*?\\)", "").replaceAll("\\[.*?\\]", "").trim();
	                    if (!cleanedName.isEmpty()) {
	                        String previousEntry = studentDataMap.getOrDefault(cleanedName, "");
	                        String newEntry = previousEntry + currentObject + ": " + category + "\n";
	                        studentDataMap.put(cleanedName, newEntry);
	                    }
	                }
	            } else {
	                answerBuilder.append(line).append("\n");
	            }
	        }
	    }

	    outputData.predictedAnswer = answerBuilder.toString().trim();
	    outputData.processedStudentData = studentDataMap;
	}

	private static void extractClassificationsQuestionAndAnswer(String[] lines, OutputData outputData) {
	    // Extract question
	    StringBuilder questionBuilder = new StringBuilder();
	    for (String line : lines) {
	        if (line.toLowerCase().contains("question")) {
	            questionBuilder.append(line).append("\n");
	        }
	    }
	    if (questionBuilder.length() == 0 && lines.length > 0) {
	        questionBuilder.append(lines[0].trim());
	    }
	    outputData.predictedQuestion = questionBuilder.toString().trim();

	    // Extract answer
	    StringBuilder answerBuilder = new StringBuilder();
	    for (String line : lines) {
	        line = line.trim();
	        if (line.isEmpty()) continue;

	        if (line.startsWith("**Object")) {
	            answerBuilder.append(line).append("\n");
	        } else if (line.startsWith("-")) {
	            int colonIndex = line.indexOf(':');
	            if (colonIndex != -1) {
	                String category = line.substring(0, colonIndex).trim();
	                answerBuilder.append(category).append("\n");
	            } else {
	                answerBuilder.append(line).append("\n");
	            }
	        }
	    }
	    outputData.predictedAnswer = answerBuilder.toString().trim();
	}
//	private static String extractClassificationsAnswer(String[] lines) {
//	    StringBuilder answerBuilder = new StringBuilder();
//
//	    for (String line : lines) {
//	        line = line.trim();
//	        if (line.isEmpty()) {
//	            continue; // skip empty lines
//	        }
//
//	        if (line.startsWith("**Object")) {
//	            // Keep Object header
//	            answerBuilder.append(line).append("\n");
//	        } else if (line.startsWith("-")) {
//	            // Keep category label, strip student names
//	            int colonIndex = line.indexOf(':');
//	            if (colonIndex != -1) {
//	                String category = line.substring(0, colonIndex).trim();
//	                answerBuilder.append(category).append("\n");
//	            }
//	        }
//	        // else ignore other narrative or explanation lines
//	    }
//
//	    return answerBuilder.toString().trim();
//	}

//	private static String extractDefaultAnswer(String[] lines) {
//	    StringBuilder answerBuilder = new StringBuilder();
//	    boolean afterQuestion = false;
//	    for (String line : lines) {
//	        if (!afterQuestion && line.toLowerCase().contains("question")) {
//	            afterQuestion = true;
//	            continue;
//	        }
//	        if (afterQuestion) {
//	            answerBuilder.append(line).append("\n");
//	        }
//	    }
//	    return answerBuilder.toString().trim();
//	}
	private static void extractDefinitionQuestionAndAnswer(String[] lines, OutputData outputData) {
	    // Extract Question
	    StringBuilder questionBuilder = new StringBuilder();
	    boolean reachedClusterStart = false;

	    for (String line : lines) {
	        String trimmedLine = line.trim();
	        if (trimmedLine.startsWith("Cluster 1:")) {
	            reachedClusterStart = true;
	            break;
	        }
	        questionBuilder.append(line).append("\n");
	    }

	    outputData.predictedQuestion = questionBuilder.toString().trim();

	    // Extract Answer
	    StringBuilder answerBuilder = new StringBuilder();
	    boolean inAnswerSection = false;
	    boolean skipClusterStudentLists = false;
	    boolean includeFinalSummary = false;

	    for (String line : lines) {
	        String trimmedLine = line.trim();

	        if (trimmedLine.startsWith("Cluster ")) {
	            inAnswerSection = true;
	            skipClusterStudentLists = true;
	            answerBuilder.append(trimmedLine).append("\n");
	            continue;
	        }

	        if (inAnswerSection) {
	            if (skipClusterStudentLists && trimmedLine.startsWith("-")) {
	                continue; // skip cluster student lists
	            }

	            if (trimmedLine.startsWith("Representative Answer:")) {
	                skipClusterStudentLists = false;
	                answerBuilder.append(line).append("\n");
	                continue;
	            }

	            if (trimmedLine.startsWith("Variations:")) {
	                String cleanedLine = line.replaceAll("\\(like .*?\\)", "").replaceAll(" +", " ").trim();
	                answerBuilder.append(cleanedLine).append("\n");
	                continue;
	            }

	            if (trimmedLine.startsWith("Number of Students in Each Cluster:")) {
	                includeFinalSummary = true;
	                answerBuilder.append(trimmedLine).append("\n");
	                continue;
	            }

	            if (includeFinalSummary) {
	                // Add everything after "Number of Students..." line
	                answerBuilder.append(line).append("\n");
	                continue;
	            }

	            if (!trimmedLine.startsWith("-")) {
	                answerBuilder.append(line).append("\n");
	            }
	        }
	    }

	    outputData.predictedAnswer = answerBuilder.toString().trim();
	    // Simplify ". Name1, Name2, and Name3 discuss" with ". Some discuss"
	    outputData.predictedAnswer = outputData.predictedAnswer.replaceAll("\\.\\s*[^\\.]*?\\s+discuss", ". Some discuss");
	}
	private static void extractDefinitionQuestionAnswerAndStudentMap(String[] lines, OutputData outputData) {
	    // Extract Question
	    StringBuilder questionBuilder = new StringBuilder();
	    boolean reachedClusterStart = false;

	    // Student Data Map
	    Map<String, String> studentMap = new HashMap<>();

	    // For managing cluster parsing
	    List<String> currentClusterStudents = new ArrayList<>();
	    String currentRepresentativeAnswer = "";

	    for (String line : lines) {
	        String trimmedLine = line.trim();

	        if (trimmedLine.startsWith("Cluster ")) {
	            reachedClusterStart = true;
	            break;
	        }
	        questionBuilder.append(line).append("\n");
	    }
	    outputData.predictedQuestion = questionBuilder.toString().trim();

	    // Extract Answer + Populate Student Map
	    StringBuilder answerBuilder = new StringBuilder();
	    boolean inAnswerSection = false;
	    boolean skipClusterStudentLists = false;
	    boolean includeFinalSummary = false;
	    boolean readingRepresentative = false;

	    for (String line : lines) {
	        String trimmedLine = line.trim();

	        if (trimmedLine.startsWith("Cluster ")) {
	            // Process previous cluster's students
	            if (!currentClusterStudents.isEmpty() && !currentRepresentativeAnswer.isEmpty()) {
	                for (String student : currentClusterStudents) {
	                    String cleanedStudent = cleanStudentName(student);
	                    studentMap.put(cleanedStudent, currentRepresentativeAnswer.trim());
	                }
	            }

	            // Reset for new cluster
	            currentClusterStudents.clear();
	            currentRepresentativeAnswer = "";

	            inAnswerSection = true;
	            skipClusterStudentLists = true;
	            readingRepresentative = false;
	            answerBuilder.append(trimmedLine).append("\n");
	            continue;
	        }

	        if (inAnswerSection) {
	            if (skipClusterStudentLists && trimmedLine.startsWith("-")) {
	                currentClusterStudents.add(trimmedLine);
	                continue; // Collect student names for current cluster
	            }

	            if (trimmedLine.startsWith("Representative Answer:")) {
	                skipClusterStudentLists = false;
	                readingRepresentative = true;

	                // Start collecting representative answer content
	                currentRepresentativeAnswer = trimmedLine.replace("Representative Answer:", "").trim();
	                answerBuilder.append(trimmedLine).append("\n");
	                continue;
	            }

	            if (readingRepresentative) {
	                if (trimmedLine.startsWith("Variations:") || trimmedLine.startsWith("Improvements:") ||
	                    trimmedLine.startsWith("Cluster ") || trimmedLine.startsWith("Number of Students")) {
	                    readingRepresentative = false; // End of Representative Answer
	                } else {
	                    // Collect multi-line Representative Answer
	                    currentRepresentativeAnswer += " " + trimmedLine;
	                }
	            }

	            if (trimmedLine.startsWith("Variations:")) {
	                String cleanedLine = line.replaceAll("\\(like .*?\\)", "").replaceAll(" +", " ").trim();
	                answerBuilder.append(cleanedLine).append("\n");
	                continue;
	            }

	            if (trimmedLine.startsWith("Number of Students in Each Cluster:")) {
	                includeFinalSummary = true;
	                answerBuilder.append(trimmedLine).append("\n");
	                continue;
	            }

	            if (includeFinalSummary) {
	                answerBuilder.append(line).append("\n");
	                continue;
	            }

	            if (!trimmedLine.startsWith("-")) {
	                answerBuilder.append(line).append("\n");
	            }
	        }
	    }

	    // Process last cluster students if any
	    if (!currentClusterStudents.isEmpty() && !currentRepresentativeAnswer.isEmpty()) {
	        for (String student : currentClusterStudents) {
	            String cleanedStudent = cleanStudentName(student);
	            studentMap.put(cleanedStudent, currentRepresentativeAnswer.trim());
	        }
	    }

	    outputData.predictedAnswer = answerBuilder.toString().trim();
	    outputData.predictedAnswer = outputData.predictedAnswer.replaceAll("\\.\\s*[^\\.]*?\\s+discuss", ". Some discuss");
	    outputData.processedStudentData = studentMap;
	}

	public static String cleanStudentName(String rawName) {
	    rawName = rawName.trim();

	    // Find first and last letter
	    int firstLetterIndex = -1, lastLetterIndex = -1;
	    for (int i = 0; i < rawName.length(); i++) {
	        if (Character.isLetter(rawName.charAt(i))) {
	            if (firstLetterIndex == -1) {
	                firstLetterIndex = i;
	            }
	            lastLetterIndex = i;
	        }
	    }

	    if (firstLetterIndex == -1 || lastLetterIndex == -1) {
	        return rawName.trim(); // fallback if no letters found
	    }

	    String cleaned = rawName.substring(firstLetterIndex, lastLetterIndex + 1);

	    // Remove suffix starting with "[" if present
	    int bracketIndex = cleaned.indexOf('[');
	    if (bracketIndex != -1) {
	        cleaned = cleaned.substring(0, bracketIndex).trim();
	    }
	    int parenIndex = cleaned.indexOf('(');
	    if (parenIndex != -1) {
	        cleaned = cleaned.substring(0, parenIndex).trim();
	    }

	    return cleaned.trim();
	}

	private static void extractDefaultQuestionAndAnswer(String[] lines, OutputData outputData) {
	    // Extract question
	    StringBuilder questionBuilder = new StringBuilder();
	    for (String line : lines) {
	        if (line.toLowerCase().contains("question")) {
	            questionBuilder.append(line).append("\n");
	        }
	    }
	    if (questionBuilder.length() == 0 && lines.length > 0) {
	        questionBuilder.append(lines[0].trim());
	    }
	    outputData.predictedQuestion = questionBuilder.toString().trim();

	    // Extract everything after the question
	    StringBuilder answerBuilder = new StringBuilder();
	    boolean afterQuestion = false;
	    for (String line : lines) {
	        if (!afterQuestion && line.toLowerCase().contains("question")) {
	            afterQuestion = true;
	            continue;
	        }
	        if (afterQuestion) {
	            answerBuilder.append(line).append("\n");
	        }
	    }
	    outputData.predictedAnswer = answerBuilder.toString().trim();
	}

	private static void extractDifferencesQuestionAndAnswer(String[] lines, OutputData outputData) {
	    // Extract Question: Everything before "Start of Differences..."
	    StringBuilder questionBuilder = new StringBuilder();
	    boolean reachedDifferencesStart = false;

	    for (String line : lines) {
	        String trimmedLine = line.trim();
	        if (trimmedLine.startsWith("Start of Differences")) {
	            reachedDifferencesStart = true;
	            break;
	        }
	        questionBuilder.append(line).append("\n");
	    }

	    outputData.predictedQuestion = questionBuilder.toString().trim();

	    // Extract Answer: Structured differences only, exclude representative and student lines
	    StringBuilder answerBuilder = new StringBuilder();
	    boolean inAnswerSection = false;

	    for (String line : lines) {
	        String trimmedLine = line.trim();

	        if (trimmedLine.startsWith("Start of Differences")) {
	            inAnswerSection = true;
	            answerBuilder.append(trimmedLine).append("\n");
	            continue;
	        }

	        if (inAnswerSection) {
	            // Stop adding when representative or student lines start
	            if (trimmedLine.startsWith("- Representative answer by") ||
	                trimmedLine.startsWith("- Students who identified this difference:")) {
	                continue;  // Skip these lines and any following
	            }

	            // Also skip student bullet points
	            if (trimmedLine.startsWith("-") && trimmedLine.contains(":")) {
	                continue;
	            }

	            answerBuilder.append(line).append("\n");
	        }
	    }

	    outputData.predictedAnswer = answerBuilder.toString().trim();
	}
	private static void extractDifferencesQuestionAnswerAndStudentMap(String[] lines, OutputData outputData) {
	    StringBuilder questionBuilder = new StringBuilder();
	    StringBuilder answerBuilder = new StringBuilder();
	    Map<String, String> studentDataMap = new HashMap<>();

	    boolean inAnswerSection = false;
	    String currentDifferenceTitle = "";

	    for (int i = 0; i < lines.length; i++) {
	        String trimmedLine = lines[i].trim();

	        // Extract question until "Start of Differences"
	        if (!inAnswerSection) {
	            if (trimmedLine.startsWith("Start of Differences")) {
	                inAnswerSection = true;
	                answerBuilder.append(trimmedLine).append("\n");
	                continue;
	            }
	            questionBuilder.append(lines[i]).append("\n");
	            continue;
	        }

	        // We're in the answer section now
	        if (inAnswerSection) {
	            // If it's a difference title line (numbered line)
	            if (trimmedLine.matches("^\\d+\\.\\s.*")) {
	                currentDifferenceTitle = trimmedLine.replaceFirst("^\\d+\\.\\s*", "").trim();
	                answerBuilder.append(trimmedLine).append("\n");
	                continue;
	            }

	            // Skip representative and student header lines in answer
	            if (trimmedLine.startsWith("- Representative answer by") ||
	                trimmedLine.startsWith("- Students who identified this difference:")) {
	                continue;
	            }

	            // Handle student bullet points
	            if (trimmedLine.startsWith("-") && trimmedLine.contains(":")) {
	                String studentLine = trimmedLine.replaceFirst("^-\\s*", "");
	                int colonIndex = studentLine.indexOf(":");
	                if (colonIndex > 0) {
	                    String studentName = studentLine.substring(0, colonIndex).trim();
	                    String studentComment = studentLine.substring(colonIndex + 1).trim();
	                    String combinedValue = currentDifferenceTitle + ": " + studentComment;
	                    studentDataMap.put(studentName, combinedValue);
	                }
	                continue; // skip adding to answerBuilder
	            }

	            // Normal answer content
	            answerBuilder.append(lines[i]).append("\n");
	        }
	    }

	    outputData.predictedQuestion = questionBuilder.toString().trim();
	    outputData.predictedAnswer = answerBuilder.toString().trim();
	    outputData.processedStudentData = studentDataMap;
	}
	private static void extractMotivationQuestionAnswerAndStudentMap(String[] lines, OutputData outputData) {
	    Map<String, String> studentDataMap = new HashMap<>();
	    StringBuilder questionBuilder = new StringBuilder();
	    StringBuilder answerBuilder = new StringBuilder();

	    // Phase 1: Extract Question
	    int i = 0;
	    for (; i < lines.length; i++) {
	        String trimmedLine = lines[i].trim();
	        if (trimmedLine.startsWith("**C1:")) {
	            break;  // Stop at concept start
	        }
	        questionBuilder.append(lines[i]).append("\n");
	    }

	    outputData.predictedQuestion = questionBuilder.toString().trim();

	    // Phase 2: Extract Answer and Student Data
	    String currentRepresentativeContent = "";
	    for (; i < lines.length; i++) {
	        String trimmedLine = lines[i].trim();

	        // Concept Headers (**C1: etc.)
	        if (trimmedLine.startsWith("**C1:") || trimmedLine.startsWith("**C2:")) {
	            answerBuilder.append(trimmedLine).append("\n");
	            continue;
	        }

	        // Sections (a), (b), (c)
	        if (trimmedLine.startsWith("(a)") || trimmedLine.startsWith("(b)") || trimmedLine.startsWith("(c)")) {
	            answerBuilder.append(lines[i]).append("\n");
	            continue;
	        }

	        // Section (d) Representative Example
	        if (trimmedLine.startsWith("(d)")) {
	            int colonIndex = trimmedLine.indexOf(":");
	            int apostropheIndex = trimmedLine.indexOf("'", colonIndex);  // Find apostrophe after colon

	            if (colonIndex != -1 && apostropheIndex != -1) {
	                String repStudent = trimmedLine.substring(colonIndex + 1, apostropheIndex).trim();
	                String restOfLine = trimmedLine.substring(apostropheIndex + 1).trim();

	                // Try to extract the content after "response" if present
	                int responseIndex = restOfLine.toLowerCase().indexOf("response");
	                if (responseIndex != -1) {
	                    currentRepresentativeContent = restOfLine.substring(responseIndex + "response".length()).trim();
	                } else {
	                    currentRepresentativeContent = restOfLine; // Fallback if "response" is not there
	                }
                    currentRepresentativeContent = Character.toUpperCase(currentRepresentativeContent.charAt(0)) + currentRepresentativeContent.substring(1);


	                studentDataMap.put(repStudent, currentRepresentativeContent);
	            }
	            continue; // skip adding (d) lines to answer
	        }
	        // Section (e) Student List
	        if (trimmedLine.startsWith("(e)")) {
	            continue; // skip header
	        }

	        // Student names under (e)
//	        if (trimmedLine.startsWith("-")) {
//	            String studentName = trimmedLine.replaceAll("^[^A-Za-z']*", "").replaceAll(":.*", "").trim();
//	            if (!studentName.isEmpty()) {
//	                studentDataMap.put(studentName, currentRepresentativeContent);
//	            }
//	            continue;
//	        }
	        if (trimmedLine.startsWith("-")) {
	            // Extract student name
	            String studentName = trimmedLine.replaceAll("^[^A-Za-z']*", "").replaceAll(":.*", "").trim();

	            // Extract student-specific text after colon
	            int colonIdx = trimmedLine.indexOf(":");
	            String studentQuote = "";
	            if (colonIdx != -1 && colonIdx + 1 < trimmedLine.length()) {
	                studentQuote = trimmedLine.substring(colonIdx + 1).trim();
	            }

	            // Combine representative content + specific student quote
	            if (!studentName.isEmpty()) {
	                String fullContribution = currentRepresentativeContent;
	                if (!studentQuote.isEmpty()) {
	                    fullContribution += " " + studentQuote;
	                }
	                studentDataMap.put(studentName, fullContribution);
	            }
	            continue;
	        }

	        // Any additional explanation after concepts
	        answerBuilder.append(lines[i]).append("\n");
	    }

	    outputData.predictedAnswer = answerBuilder.toString().trim();
	    outputData.processedStudentData = studentDataMap;
	    outputData.numOutputs = studentDataMap.size();
	}
//
//
//	
	private static void extractMotivationsQuestionAndAnswer(String[] lines, OutputData outputData) {
	    // Extract Question: Everything before **C1:
	    StringBuilder questionBuilder = new StringBuilder();
	    boolean reachedConceptStart = false;

	    for (String line : lines) {
	        String trimmedLine = line.trim();
	        if (trimmedLine.startsWith("**C1:")) {
	            reachedConceptStart = true;
	            break;
	        }
	        questionBuilder.append(line).append("\n");
	    }

	    outputData.predictedQuestion = questionBuilder.toString().trim();

	    // Extract Answer: Only (a)-(c) for each concept, skip (d)-(e) and student lines
	    StringBuilder answerBuilder = new StringBuilder();
	    boolean inAnswerSection = false;
	    boolean skipBlock = false;

	    for (String line : lines) {
	        String trimmedLine = line.trim();

	        if (trimmedLine.startsWith("**C1:") || trimmedLine.startsWith("**C2:")) {
	            inAnswerSection = true;
	            skipBlock = false;
	            answerBuilder.append(trimmedLine).append("\n");
	            continue;
	        }

	        if (inAnswerSection) {
	            if (trimmedLine.startsWith("(a)") || trimmedLine.startsWith("(b)") || trimmedLine.startsWith("(c)")) {
	                skipBlock = false;
	                answerBuilder.append(line).append("\n");
	            } else if (trimmedLine.startsWith("(d)") || trimmedLine.startsWith("(e)")) {
	                skipBlock = true;  // skip these and any student lists after
	            } else if (!skipBlock && !trimmedLine.startsWith("-")) {
	                answerBuilder.append(line).append("\n");
	            }
	        }
	    }

	    outputData.predictedAnswer = answerBuilder.toString().trim();
	}
//	private static void processMotivationsStudentData(String[] lines, OutputData outputData) {
//	    Map<String, String> studentDataMap = new HashMap<>();
//	    StringBuilder questionBuilder = new StringBuilder();
//	    StringBuilder answerBuilder = new StringBuilder();
//
//	    boolean inAnswerSection = false;
//	    boolean skipStudentLists = false;
//
//	    String currentRepresentativeStudent = null;
//	    String currentRepresentativeContent = "";
//
//	    for (int i = 0; i < lines.length; i++) {
//	        String line = lines[i];
//	        String trimmedLine = line.trim();
//
//	        // Question extraction before first concept (C1:, **C1:, etc.)
//	        if (!inAnswerSection && trimmedLine.matches("\\*{0,2}C\\d+:.*")) {
//	            inAnswerSection = true;
//	        }
//	        if (!inAnswerSection) {
//	            questionBuilder.append(line).append("\n");
//	            continue;
//	        }
//
//	        // Concept headers (lines containing ":")
//	        if (trimmedLine.contains(":")) {
//	            answerBuilder.append(trimmedLine).append("\n");
//	            skipStudentLists = false;
//	            currentRepresentativeStudent = null;
//	            currentRepresentativeContent = "";
//	            continue;
//	        }
//
//	        // Parts extraction based on keywords
//	        if (trimmedLine.toLowerCase().contains("title") || trimmedLine.toLowerCase().contains("motivation") || trimmedLine.toLowerCase().contains("validity")) {
//	            skipStudentLists = false;
//	            answerBuilder.append(line).append("\n");
//	            continue;
//	        }
//
//	        // Representative Example handling (d)
//	        if (trimmedLine.toLowerCase().contains("representative example")) {
//	            skipStudentLists = false;
//	            Matcher matcher = Pattern.compile(".*?([A-Za-z][A-Za-z'\\- ]+)").matcher(trimmedLine);
//	            if (matcher.find()) {
//	                currentRepresentativeStudent = matcher.group(1).trim();
//	            }
//	            if ((i + 1) < lines.length) {
//	                currentRepresentativeContent = lines[i + 1].trim();
//	                answerBuilder.append(trimmedLine).append("\n");
//	                answerBuilder.append(currentRepresentativeContent).append("\n");
//	            }
//	            continue;
//	        }
//
//	        // Student list (e)
//	        if (trimmedLine.toLowerCase().contains("students with similar motivations")) {
//	            skipStudentLists = true;
//	            continue;
//	        }
//
//	        // Student names
//	        if (skipStudentLists && trimmedLine.startsWith("-")) {
//	            String cleanedName = trimmedLine.replaceAll("^[^A-Za-z']*", "").replaceAll(":.*", "").trim();
//	            String representativeContent = currentRepresentativeContent;
//	            if (!cleanedName.isEmpty()) {
//	                String existing = studentDataMap.getOrDefault(cleanedName, "");
//	                studentDataMap.put(cleanedName, existing + representativeContent + "\n");
//	            }
//	            continue;
//	        }
//
//	        // Final summary lines (if any)
//	        if (!skipStudentLists && !trimmedLine.startsWith("-")) {
//	            answerBuilder.append(line).append("\n");
//	        }
//	    }
//
//	    outputData.predictedQuestion = questionBuilder.toString().trim();
//	    outputData.predictedAnswer = answerBuilder.toString().trim();
//	    outputData.studentData = studentDataMap;
//	}
	private static void extractEnumerationQuestionAnswerAndStudentMap(String[] lines, OutputData outputData) {
	    Map<String, String> studentDataMap = new HashMap<>();
	    StringBuilder questionBuilder = new StringBuilder();
	    StringBuilder answerBuilder = new StringBuilder();

	    boolean inAnswerSection = false;
	    boolean skipStudentLines = false;
	    String currentProperty = null;

	    for (int i = 0; i < lines.length; i++) {
	        String line = lines[i];
	        String trimmedLine = line.trim();

	        // Question extraction before first property
	        if (!inAnswerSection && trimmedLine.startsWith("### Property:")) {
	            inAnswerSection = true;
	        }
	        if (!inAnswerSection) {
	            questionBuilder.append(line).append("\n");
	            continue;
	        }

	        // Property header
	        if (trimmedLine.startsWith("### Property:")) {
	            currentProperty = trimmedLine.replace("### Property:", "").trim();
	            answerBuilder.append(trimmedLine).append("\n");
	            continue;
	        }

	        // Summary and description
	        if (trimmedLine.startsWith("**Summary and Description:**")) {
	            skipStudentLines = false;
	            answerBuilder.append(line).append("\n");
	            continue;
	        }

	        // Student responses
	        if (trimmedLine.startsWith("-")) {
	            skipStudentLines = true;
	            int colonIndex = trimmedLine.indexOf(":");
	            if (colonIndex != -1 && currentProperty != null) {
	                String studentName = trimmedLine.substring(1, colonIndex).trim();
	                String studentResponse = trimmedLine.substring(colonIndex + 1).trim();
	                // Append if already exists
	                String existing = studentDataMap.getOrDefault(studentName, "");
	                String separator = existing.isEmpty() ? "" : "\n";
	                String updated = existing + separator + currentProperty + ": " + studentResponse;
	                studentDataMap.put(studentName, updated);
	            }
	            continue;
	        }

	        // Non-student lines within answer section
	        if (!skipStudentLines && !trimmedLine.isEmpty()) {
	            answerBuilder.append(line).append("\n");
	        }
	    }

	    outputData.predictedQuestion = questionBuilder.toString().trim();
	    outputData.predictedAnswer = answerBuilder.toString().trim();
	    outputData.processedStudentData = studentDataMap;
	}
	private static void extractEnumerationQuestionAndAnswer(String[] lines, OutputData outputData) {
	    // Extract Question: Everything before the first "### Property:"
	    StringBuilder questionBuilder = new StringBuilder();
	    boolean reachedPropertyStart = false;

	    for (String line : lines) {
	        String trimmedLine = line.trim();
	        if (trimmedLine.startsWith("### Property:")) {
	            reachedPropertyStart = true;
	            break;
	        }
	        questionBuilder.append(line).append("\n");
	    }

	    outputData.predictedQuestion = questionBuilder.toString().trim();

	    // Extract Answer: Only properties and summaries, exclude student quotes
	    StringBuilder answerBuilder = new StringBuilder();
	    boolean inAnswerSection = false;
	    boolean skipStudentLines = false;

	    for (String line : lines) {
	        String trimmedLine = line.trim();

	        if (trimmedLine.startsWith("### Property:")) {
	            inAnswerSection = true;
	            skipStudentLines = false;
	            answerBuilder.append(trimmedLine).append("\n");
	            continue;
	        }

	        if (inAnswerSection) {
	            // Include Summary and Description
	            if (trimmedLine.startsWith("**Summary and Description:**")) {
	                skipStudentLines = false;
	                answerBuilder.append(line).append("\n");
	                continue;
	            }

	            // Stop adding when student quotes start (lines starting with "- Name:")
	            if (trimmedLine.startsWith("-")) {
	                skipStudentLines = true;
	                continue;
	            }

	            // Continue only if not in student quotes
	            if (!skipStudentLines && !trimmedLine.isEmpty()) {
	                answerBuilder.append(line).append("\n");
	            }
	        }
	    }

	    outputData.predictedAnswer = answerBuilder.toString().trim();
	}
	
	private static void extractTradeoffQuestionAndAnswer(String[] lines, OutputData outputData) {
	    StringBuilder questionBuilder = new StringBuilder();
	    StringBuilder answerBuilder = new StringBuilder();

	    int lineIndex = 0;
	    int questionLineCount = 0;

	    // First loop: build the question
	    while (lineIndex < lines.length && questionLineCount < MAX_QUESTION_LINES) {
	        String trimmedLine = lines[lineIndex].trim();

	        if (trimmedLine.startsWith("Solutions Predicted:")) {
	            break; // Stop question early if Solutions start
	        }

	        questionBuilder.append(lines[lineIndex]).append("\n");
	        questionLineCount++;
	        lineIndex++;
	    }

	    // Correct: Set inAnswerSection true since we are now processing the answer
//	    boolean inAnswerSection = true;
	    boolean skipStudentList = false;

	    // Second loop: build the answer
	    while (lineIndex < lines.length) {
	        String trimmedLine = lines[lineIndex].trim();

	        // Stop at feedback section
	        if (trimmedLine.toLowerCase().contains("each student")) {
	            break;
	        }

	        // Skip (b) Names of students list
	        if (trimmedLine.contains("Names of students")) {
	            skipStudentList = true;
	            lineIndex++;
	            continue;
	        }

	        // Skip student names under (b)
	        if (skipStudentList) {
	            if (trimmedLine.isEmpty()) {
	                skipStudentList = false; // End skipping after blank line
	            }
	            lineIndex++;
	            continue;
	        }

	        // Append valid answer lines
	        answerBuilder.append(lines[lineIndex]).append("\n");
	        lineIndex++;
	    }

	    outputData.predictedQuestion = questionBuilder.toString().trim();

	    // Clean Answer Post-Processing:
	    String rawAnswer = answerBuilder.toString().trim();

	    // Remove " - Name" after representative quotes
	    rawAnswer = rawAnswer.replaceAll("\"\\s*-\\s*\\w[^\\n]*", "\"");

	    outputData.predictedAnswer = rawAnswer;
	}


	private static void extractCorrectionsQuestionAndAnswer(String[] lines, OutputData outputData) {
	    StringBuilder questionBuilder = new StringBuilder();
	    String mistakeLine = "";
	    String fixLine = "";
	    
	    boolean inPredictedQuestion = false;
	    boolean inLikelyCorrectCluster = false;

	    for (String line : lines) {
	        String trimmedLine = line.trim();

	        // Extract Predicted Question
	        if (trimmedLine.startsWith("Predicted Question:")) {
	            inPredictedQuestion = true;
	            questionBuilder.append(line).append("\n");
	            continue;
	        }

	        if (inPredictedQuestion) {
	            if (trimmedLine.isEmpty()) {
	                inPredictedQuestion = false; // End of question block
	            } else {
	                questionBuilder.append(line).append("\n");
	            }
	            continue;
	        }

	        // Detect start of Likely Correct Cluster
	        if (trimmedLine.startsWith("Likely Correct Cluster")) {
	            inLikelyCorrectCluster = true;
	            continue;
	        }

	        // Stop after Fix is found in the correct cluster
	        if (inLikelyCorrectCluster && trimmedLine.startsWith("Outlier Mistake")) {
	            break;
	        }

	        // Capture Mistake and Fix lines
	        if (inLikelyCorrectCluster) {
	            if (trimmedLine.startsWith("Mistake:")) {
	                mistakeLine = line.trim();
	            } else if (trimmedLine.startsWith("Fix:")) {
	                fixLine = line.trim();
	            }
	        }
	    }

	    outputData.predictedQuestion = questionBuilder.toString().trim();
	    outputData.predictedAnswer = (mistakeLine + "\n" + fixLine).trim();
	}
	
	private static void extractCorrectionsQuestionAnswerAndStudentMap(String[] lines, OutputData outputData) {
	    Map<String, String> studentDataMap = new HashMap<>();
	    StringBuilder questionBuilder = new StringBuilder();
	    String mistakeLine = "";
	    String fixLine = "";

	    boolean inPredictedQuestion = false;
	    boolean inLikelyCorrectCluster = false;

	    for (int i = 0; i < lines.length; i++) {
	        String line = lines[i];
	        String trimmedLine = line.trim();

	        // Extract Predicted Question
	        if (trimmedLine.startsWith("Predicted Question:")) {
	            inPredictedQuestion = true;
	            questionBuilder.append(line).append("\n");
	            continue;
	        }

	        if (inPredictedQuestion) {
	            if (trimmedLine.isEmpty()) {
	                inPredictedQuestion = false; // End of question block
	            } else {
	                questionBuilder.append(line).append("\n");
	            }
	            continue;
	        }

	        // Detect start of Likely Correct Cluster
	        if (trimmedLine.startsWith("Likely Correct Cluster")) {
	            inLikelyCorrectCluster = true;
	            continue;
	        }

	        // Stop after Fix is found in the correct cluster
	        if (inLikelyCorrectCluster && trimmedLine.startsWith("Outlier Mistake")) {
	            break;
	        }

	        // Capture Mistake and Fix lines
	        if (inLikelyCorrectCluster) {
	            if (trimmedLine.startsWith("Mistake:")) {
	                mistakeLine = line.trim();
	            } else if (trimmedLine.startsWith("Fix:")) {
	                fixLine = line.trim();
	            }
	        }

	        // Student mapping logic
	        if (inLikelyCorrectCluster && trimmedLine.startsWith("-")) {
	            String name = null;
	            String contribution = null;

	            int colonIndex = trimmedLine.indexOf(":");
	            if (colonIndex != -1) {
	                name = trimmedLine.substring(1, colonIndex).trim();
	                contribution = trimmedLine.substring(colonIndex + 1).trim();
	            } else {
	                // No colon, find name by detecting first verb
	                String content = trimmedLine.substring(1).trim();
	                String[] verbs = {"identified", "mentioned", "described", "suggested", "provided"};
	                for (String verb : verbs) {
	                    int verbIndex = content.indexOf(verb);
	                    if (verbIndex != -1) {
	                        name = content.substring(0, verbIndex).trim();
	                        contribution = content.substring(verbIndex).trim();
	                        break;
	                    }
	                }
	            }

	            if (name != null && contribution != null) {
	                if (contribution.contains("Similar to")) {
	                    contribution = contribution.substring(0, contribution.indexOf("Similar to")).trim();
	                }
//	                String combined = mistakeLine + "\n" + fixLine + "\nContribution: " + contribution;
	                String combined = String.format("%s\n%s\nContribution: %s", mistakeLine, fixLine, contribution);
	                studentDataMap.put(name, combined);
	            }
	        }
	    }

	    outputData.predictedQuestion = questionBuilder.toString().trim();
	    outputData.predictedAnswer = (mistakeLine + "\n" + fixLine).trim();
	    outputData.processedStudentData = studentDataMap;
	}



    public static long parseTimeToSeconds(String timeLine) {
        try {
            String[] parts = timeLine.split("[ ,]+");
            long minutes = Long.parseLong(parts[0]);
            long seconds = Long.parseLong(parts[2]);
            return minutes * 60 + seconds;
        } catch (Exception e) {
            System.err.println("Failed to parse time from line: " + timeLine);
            return -1;
        }
    }
    private static final List<Charset> CHARSETS_TO_TRY = Arrays.asList(
            StandardCharsets.UTF_8,
            Charset.forName("windows-1252"),
            Charset.forName("ISO-8859-1")
        );

    public static List<String> readFileWithFallback(Path path) throws IOException {
        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            throw new IOException("File does not exist or is not a regular file: " + path.toString());
        }

        for (Charset charset : CHARSETS_TO_TRY) {
            try {
                return Files.readAllLines(path, charset);
            } catch (IOException e) {
                System.err.println("Failed to read with charset: " + charset.name() + " for file: " + path.toString());
            }
        }

        throw new IOException("Unable to read file with supported charsets: " + path.toString());
    }
    
    public static void main (String[] args) {
//    	String aFileName = "G:\\My Drive\\533Shared\\s25\\Lectures\\Zoom Chats\\2025-04-15 12.38.35 Comp 533 Lectures\\outputs\\Mistakes_segment1.txt";
//    	String aFileName = "G:\\My Drive\\533Shared\\s25\\Lectures\\Zoom Chats\\2025-04-24 12.44.15 Comp 533 Lectures\\outputs\\Mistakes_segment1.txt";
//    	String aFileName = "G:\\My Drive\\533Shared\\s25\\Lectures\\Zoom Chats\\2025-04-24 12.44.15 Comp 533 Lectures\\outputs\\Mistakes_segment2.txt";

//     	String aFileName = "G:\\My Drive\\533Shared\\s25\\Lectures\\Zoom Chats\\2025-01-14 12.26.46 Comp 533 Lectures\\outputs\\Classifications_segment3.txt";
// G:\My Drive\533Shared\s25\Lectures\Zoom Chats\2025-01-21 12.26.31 Comp 533 Lectures\outputs
//    	String aFileName = "G:\\My Drive\\533Shared\\s25\\Lectures\\Zoom Chats\\2025-01-14 12.26.46 Comp 533 Lectures\\outputs\\Differences_segment4.txt";
//    	String aFileName = "G:\\My Drive\\533Shared\\s25\\Lectures\\Zoom Chats\\2025-01-14 12.26.46 Comp 533 Lectures\\outputs\\Motivations_segment5.txt";
//    	String aFileName = "G:\\My Drive\\533Shared\\s25\\Lectures\\Zoom Chats\\2025-01-21 12.26.31 Comp 533 Lectures\\outputs\\Enumeration_segment2.txt";
    	String aFileName = "G:\\My Drive\\524Shared\\F24\\Lectures\\Zoom Chats\\2024-12-03 12.41.24 Comp 524 Lectures\\outputs\\Definition_segment3.txt";
//    	String aFileName = "G:\\My Drive\\524Shared\\F24\\Lectures\\Zoom Chats\\2024-12-03 12.41.24 Comp 524 Lectures\\outputs\\Tradeoff_segment2.txt";
//    	String aFileName = "G:\\My Drive\\524Shared\\F24\\Lectures\\Zoom Chats\\2024-11-14 12.36.04 Comp 524 Lectures\\outputs\\Corrections_segment2.txt";
//    	String aFileName = "G:\\My Drive\\533Shared\\s25\\Lectures\\Zoom Chats\\2025-02-04 12.51.10 Comp 533 Lectures\\outputs\\Mistakes_segment1.txt";

    //2025-02-04 12.51.10 Comp 533 Lectures
   //G:\My Drive\524Shared\F24\Lectures\Zoom Chats\2024-11-14 12.36.04 Comp 524 Lectures\outputs
    //Tradeoff_segment2.txt
    	//G:\My Drive\524Shared\F24\Lectures\Zoom Chats\2024-12-03 12.41.24 Comp 524 Lectures\outputs
    	OutputData anOutputData = processOutputEntry(new File(aFileName));
    	System.out.println(anOutputData);
    }

}
