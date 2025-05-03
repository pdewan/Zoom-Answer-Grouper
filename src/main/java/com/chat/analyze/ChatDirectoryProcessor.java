package com.chat.analyze;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class ChatDirectoryProcessor {
	/**
	 * 
	 * Given a diectory such as: "G:\\My Drive\\533Shared\\s25\\Lectures\\Zoom Chats
	 * use processOutputDirectory to process each descendant of the form \\2025-04-15 12.38.35 Comp 533 Lectures\\output
	 * and return a list that concatenates the result of each processOutputDirectory call
	 * 
	 */
	public static  LectureData  processChatDirectory(File sessionDir) {
		LectureData retVal = new LectureData();
//        List<InputOutputData> inputOutputData = new ArrayList<>();
		retVal.lectureInputOutputs = new ArrayList<>();



        if (!sessionDir.exists() || !sessionDir.isDirectory()) {
            System.err.println("Invalid chat  directory: " + sessionDir.getAbsolutePath());
//            return Collections.emptyList();
            return retVal;
        }

       
        	List<OutputData> outputDataList  = new ArrayList();
        	List<InputData> inputDataList = new ArrayList();

            File inputDir = new File(sessionDir, "segmentation");
            if (inputDir.exists() && inputDir.isDirectory()) {
            	inputDataList = InputDirectoryProcessor.processInputDirectory(inputDir);
//                allInpuist<InptData.add(inputData);
//                continue;
            }
            File outputDir = new File(sessionDir, "outputs");
            if (outputDir.exists() && outputDir.isDirectory()) {
            	outputDataList = OutputDirectoryProcessor.processOutputDirectory(outputDir);
//                allOutputData.addAll(outputData);
//                allOutputData.add(outputData);

//                continue;
            }
            
             retVal.lectureInputOutputs = link(inputDataList, outputDataList);
             return retVal;
            
      
    }
	/**
	 * For each InputData in anInputDataList, find the local name of the inputFile such as segment1.txt
	 * Next try to find a matching file (such as Mistakes_segment1.txt) in the first matching OutputData in anOutputDataList
	 * Link the matching InputData and OutputData by making each point to the other through their instance variables.
	 * Also create a new instance of InputOutputData with these two components.
	 * Add that new instance to the returned list.
	 */
	public static List<InputOutputData> link(List<InputData> inputDataList, List<OutputData> outputDataList) {
	    List<InputOutputData> linkedDataList = new ArrayList<>();

	    for (InputData inputData : inputDataList) {
	        File inputFile = inputData.inputFile;
	        String inputFileName = inputFile.getName();

	        // Extract base name (e.g., "segment1.txt" -> "segment1")
	        String inputBaseName = inputFileName;
	        if (inputFileName.contains(".")) {
	            inputBaseName = inputFileName.substring(0, inputFileName.lastIndexOf('.'));
	        }
            InputOutputData linkedData = new InputOutputData();
            linkedData.inputData = inputData;
            linkedDataList.add(linkedData);


	        // Find the matching OutputData
	        for (OutputData outputData : outputDataList) {
	            File outputFile = outputData.outputFile;
	            if (outputFile != null) {
	                String outputFileName = outputFile.getName();
	                if (outputFileName.contains(inputBaseName)) {
	                    // Link the input and output
//	                    inputData.outputData = outputData;	 
	                	outputData.qaLength = inputData.inputFile.length();
	                	outputData.numInputs = inputData.inputStudentData.size();

	                    // Create InputOutputData instance and add to result list
//	                    InputOutputData linkedData = new InputOutputData();
//	                    linkedData.inputData = inputData;
	                    linkedData.outputData = outputData;
//	                    linkedDataList.add(linkedData);
	                    break; // Found match for this inputData
	                }
	            }
	        }
	    }

	    return linkedDataList;
	}
	public static void main(String[] args) {
//	    File chatDir = new File("G:\\My Drive\\533Shared\\s25\\Lectures\\Zoom Chats");
//	    File chatDir = new File("G:\\My Drive\\524Shared\\F24\\Lectures\\Zoom Chats");
    	File chatDir = new File("G:\\My Drive\\533Shared\\s25\\Lectures\\Zoom Chats\\2025-04-15 12.38.35 Comp 533 Lectures");

	    processChatDirectory(chatDir);
	    //G:\My Drive\524Shared\F24\Lectures\Zoom Chats
//	    List<OutputData> allStats = processChatDirectory(chatDir);

	    // Print or save the results
//	    allStats.forEach(System.out::println);
	}

}
