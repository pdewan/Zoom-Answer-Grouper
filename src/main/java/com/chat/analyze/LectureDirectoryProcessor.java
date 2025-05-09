package com.chat.analyze;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.Chat_Cluster.ExtensibleMain;
import com.chat.models.LectureDataJsonlUtil;

public class LectureDirectoryProcessor {
	
	public static  List<LectureData>  processLecturesDirectory(File aLecturesDirectory) {
		File aZoomChatsDirectory = new File(aLecturesDirectory, "Zoom Chats");
		if (!aZoomChatsDirectory.exists() || !aZoomChatsDirectory.isDirectory()) {
			return Collections.emptyList();
        }
		List<LectureData> retVal = ClassChatDirectoriesProcessor.processChatDirectories(aZoomChatsDirectory);
		List<File> aPPTFiles = findPPTFiles(aLecturesDirectory);
		for (LectureData aLectureData:retVal) {
			aLectureData.lecturePPTFile = matchFirstInputOrOutputFile(aLectureData, aPPTFiles);
			if (aLectureData.lecturePPTFile != null) {
				aPPTFiles.remove(aLectureData.lecturePPTFile);
			}
		}
		
		
		return retVal;
	}
	public static File matchFirstInputOrOutputFile(LectureData aLectureData, List<File> aPPTFiles) {
		File retVal = null;
		for (InputOutputData anInputOutputData:aLectureData.lectureInputOutputs) {
			if (anInputOutputData.outputData != null && anInputOutputData.outputData.outputFile != null) {
				File aTargetFile = anInputOutputData.outputData.outputFile;

			    long targetTime = extractTimestampFromFilename(aTargetFile);
			    anInputOutputData.outputData.timeAsked = targetTime;

				retVal = findFileWithClosestTimestamp(aPPTFiles, targetTime);
				if (retVal != null) {
					return retVal;
				}				
			}
			if (anInputOutputData.inputData != null && anInputOutputData.inputData.inputFile != null) {
				File aTargetFile = anInputOutputData.inputData.inputFile;
			    long targetTime = extractTimestampFromFilename(aTargetFile);

				retVal = findFileWithClosestTimestamp(aPPTFiles, targetTime);
				if (retVal != null) {
					return retVal;
				}				
			}
			
		}
		return retVal;
	}
	private static List<File> findPPTFiles(File aDirectory) {
	    List<File> pptFiles = new ArrayList<>();

	    if (aDirectory == null || !aDirectory.isDirectory()) {
	        return pptFiles; // Return empty list if null or not a directory
	    }

	    File[] files = aDirectory.listFiles();
	    if (files == null) {
	        return pptFiles; // Handle permission issues or empty directory
	    }

	    for (File file : files) {
	        if (file.isFile() && 
	            (file.getName().toLowerCase().endsWith(".ppt") || file.getName().toLowerCase().endsWith(".pptx"))) {
	            pptFiles.add(file); // Add PPT or PPTX files
	        }
	    }

	    return pptFiles;
	}
//	public static File findFileWithClosestTimestamp(List<File> aCandidateFiles, File aTargetFile) {
//	    if (aCandidateFiles == null || aCandidateFiles.isEmpty() || aTargetFile == null) {
//	        return null; // Handle invalid input
//	    }
//
//	    long targetTime = aTargetFile.lastModified();
//	    File closestFile = null;
//	    long smallestDifference = Long.MAX_VALUE;
//
//	    for (File candidate : aCandidateFiles) {
//	        long diff = Math.abs(candidate.lastModified() - targetTime);
//	        if (diff < smallestDifference) {
//	            smallestDifference = diff;
//	            closestFile = candidate;
//	        }
//	    }
//
//	    return closestFile;
//	}
	public static File findFileWithClosestTimestamp(List<File> aCandidateFiles, long aTargetTime) {
	    if (aCandidateFiles == null || aCandidateFiles.isEmpty() ) {
	        return null;
	    }

//	    long targetTime = extractTimestampFromFilename(aTargetFile);
	    
	    Date aTargetDate = new Date(aTargetTime);
	    File closestFile = null;
	    long smallestDifference = Long.MAX_VALUE;

	    for (File candidate : aCandidateFiles) {
	        long candidateTime = candidate.lastModified();
	        Date aCandidateDate = new Date(candidateTime);
	        if (candidateTime < aTargetTime) {
	        	continue; // file is saved after question is asked
	        }
	        long diff = Math.abs(candidateTime - aTargetTime);
	        if (diff < smallestDifference) {
	            smallestDifference = diff;
	            closestFile = candidate;
	        }
	    }

	    return closestFile;
	}
//	public static File findFileWithClosestTimestamp(List<File> aCandidateFiles, File aTargetFile) {
//	    if (aCandidateFiles == null || aCandidateFiles.isEmpty() || aTargetFile == null) {
//	        return null;
//	    }
//
//	    long targetTime = extractTimestampFromFilename(aTargetFile);
//
//	    File closestFile = null;
//	    long smallestDifference = Long.MAX_VALUE;
//
//	    for (File candidate : aCandidateFiles) {
//	        long candidateTime = candidate.lastModified();
//	        if (candidateTime < targetTime) {
//	        	continue; // file is saved after question is asked
//	        }
//	        long diff = Math.abs(candidateTime - targetTime);
//	        if (diff < smallestDifference) {
//	            smallestDifference = diff;
//	            closestFile = candidate;
//	        }
//	    }
//
//	    return closestFile;
//	}

	private static long extractTimestampFromFilename(File file) {
	//	G:\My Drive\524Shared\F24\Lectures\Zoom Chats\2024-08-20 13.02.04 Comp 524 Lectures\segmentation\segment1.txt
	    
	    long aTimeModified = file.lastModified();

		if (file.getParentFile() == null || file.getParentFile().getParentFile() == null) {
	    	return aTimeModified;
	    }

		File aChatFolder = file.getParentFile().getParentFile();
		File aChatFile = ExtensibleMain.getChatFile(aChatFolder);
		if (aChatFile != null) {
			long aChatFileTime = aChatFile.lastModified();
			return aChatFileTime < aTimeModified?
					aChatFileTime:
	        			aTimeModified;
		}
		String grandParentName = file.getParentFile().getParentFile().getName();
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
	    formatter.setTimeZone(TimeZone.getTimeZone("America/New_York")); // EST/EDT based on date
	    try {
	        // Extract first 19 characters (length of "yyyy-MM-dd HH.mm.ss")
	        String datePart = grandParentName.substring(0, 19);
	        long aNameTime = formatter.parse(datePart).getTime();
	        return aNameTime < aTimeModified?
	        		aNameTime:
	        			aTimeModified;
//	        return formatter.parse(datePart).getTime();
	    } catch (ParseException | StringIndexOutOfBoundsException e) {
	        // If parsing fails, fall back to last modified time
//	        return file.lastModified();
	        return aTimeModified;
	    }
	}
	

//	private static final String QA_FileName = "data/lecture_model.JSON";
//	private static final String QA_FileName = "data/533_lecture_model.JSON";
	private static final String QA_FileName = "data/524_lecture_model.JSON";


//	private static final String BASIC_QA_FILE_NAME = "data/basic_lecture_model.JSON";
//	private static final String BASIC_QA_FILE_NAME = "data/533_basic_lecture_model.JSON";
	private static final String BASIC_QA_FILE_NAME = "data/524_basic_lecture_model.JSON";


	
	public static void writeQA() throws Exception {
//		File chatDir = new File("G:\\My Drive\\533Shared\\s25\\Lectures");
	    File chatDir = new File("G:\\My Drive\\524Shared\\F24\\Lectures");
	    File anOutputFile = new File(QA_FileName);
	    
	    List<LectureData> aLecturesData = processLecturesDirectory(chatDir);
	    if (!anOutputFile.exists()) {
		    anOutputFile.createNewFile();
		    }
	    LectureDataJsonlUtil.exportLectureDataToJson(aLecturesData, anOutputFile);
	    System.out.println(aLecturesData);
	    
//	    List<File> pptFiles = findPPTFiles(chatDir);
//	    System.out.println(pptFiles);
	    
	    //G:\My Drive\524Shared\F24\Lectures\Zoom Chats
//	    List<OutputData> allStats = processChatDirectory(chatDir);

	    // Print or save the results
//	    allStats.forEach(System.out::println);
	}
	
	
	public static void writeBasicQA() throws Exception {
//		File chatDir = new File("G:\\My Drive\\533Shared\\s25\\Lectures");
	    File chatDir = new File("G:\\My Drive\\524Shared\\F24\\Lectures");
	    File anOutputFile = new File(BASIC_QA_FILE_NAME);
	    
	    List<LectureData> aLecturesData = processLecturesDirectory(chatDir);
	    if (!anOutputFile.exists()) {
		    anOutputFile.createNewFile();
		    }
	    LectureDataJsonlUtil.exportBasicLectureDataToJson(aLecturesData, anOutputFile);
//	    System.out.println(aLecturesData);
	    
//	    List<File> pptFiles = findPPTFiles(chatDir);
//	    System.out.println(pptFiles);
	    
	    //G:\My Drive\524Shared\F24\Lectures\Zoom Chats
//	    List<OutputData> allStats = processChatDirectory(chatDir);

	    // Print or save the results
//	    allStats.forEach(System.out::println);
	}

	public static void main(String[] args) throws Exception {
		writeQA();
//		writeBasicQA();
//	    File chatDir = new File("G:\\My Drive\\533Shared\\s25\\Lectures");
////	    File chatDir = new File("G:\\My Drive\\524Shared\\F24\\Lectures");
//	    File anOutputFile = new File(QA_FileName);
//	    
//	    List<LectureData> aLecturesData = processLecturesDirectory(chatDir);
//	    if (!anOutputFile.exists()) {
//		    anOutputFile.createNewFile();
//		    }
//	    LectureDataJsonlUtil.exportLectureDataToJson(aLecturesData, anOutputFile);
//	    System.out.println(aLecturesData);
//	    
////	    List<File> pptFiles = findPPTFiles(chatDir);
////	    System.out.println(pptFiles);
//	    
//	    //G:\My Drive\524Shared\F24\Lectures\Zoom Chats
////	    List<OutputData> allStats = processChatDirectory(chatDir);
//
//	    // Print or save the results
////	    allStats.forEach(System.out::println);
	}

}
