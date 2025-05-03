package com.chat.models;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StudentProgressModel {

	private String studentName;
	private Map<String, List<QuestionProgress>> lectureProgress;
	private Map<String, String> answersByLectureAndOutputFile;

	// New: progress file to persist answers
	private transient File progressFile;

	private StudentProgressModel() {
		this.lectureProgress = new HashMap<>();
		this.answersByLectureAndOutputFile = new HashMap<>();
	}

	private StudentProgressModel(String studentName) {
		this.studentName = studentName;
		this.lectureProgress = new HashMap<>();
		this.answersByLectureAndOutputFile = new HashMap<>();
		this.progressFile = getDefaultProgressFile(studentName);
	}

	// Factory method
	public static StudentProgressModel createNew(String studentName) {
		return new StudentProgressModel(studentName);
	}

	// File location for saving
	public static File getDefaultProgressFile(String studentName) {
		String safeName = studentName.trim().replaceAll("[^a-zA-Z0-9\\-_ ]", "").replaceAll("\\s+", "_");
		return new File("data/student_progress", safeName + ".json");
	}

	// Getters and setters
	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public Map<String, List<QuestionProgress>> getLectureProgress() {
		return lectureProgress;
	}

	public void setLectureProgress(Map<String, List<QuestionProgress>> lectureProgress) {
		this.lectureProgress = lectureProgress;
	}

	public Map<String, String> getAnswersByLectureAndOutputFile() {
		return answersByLectureAndOutputFile;
	}

	public void setAnswersByLectureAndOutputFile(Map<String, String> newVal) {
		this.answersByLectureAndOutputFile = newVal;
	}

	public boolean hasAnswered(String inputFileName) {
		return answersByLectureAndOutputFile.containsKey(inputFileName);
	}

	public String getAnswer(String inputFileName) {
		return answersByLectureAndOutputFile.get(inputFileName);
	}

	public void recordAnswer(String aLectureAndOutputFile, String answer) {
		answersByLectureAndOutputFile.put(aLectureAndOutputFile, answer);
		saveToDisk();
	}

	// Assign file (e.g. when loading progress externally)
	public void setProgressFile(File file) {
		this.progressFile = file;
	}

	public File getProgressFile() {
		if (progressFile == null) {
			progressFile = getDefaultProgressFile(studentName);
		}
		return progressFile;
	}

	// Save method
	private void saveToDisk() {
		try {
			StudentProgressIO.saveProgress(this, getProgressFile());
		} catch (IOException e) {
			System.err.println("Failed to save student progress: " + e.getMessage());
		}
	}
}


//public class StudentProgressModel {
//	private String studentName;
//	private Map<String, List<QuestionProgress>> lectureProgress;
//	private Map<String, String> answersByLectureAndOutputFile;
//
//	// Constructor for first-time users (no saved progress yet)
//	private StudentProgressModel() {
//		this.studentName = "";
//		this.lectureProgress = new HashMap<>();
//		this.answersByLectureAndOutputFile = new HashMap<>();
//	}
//
//	private StudentProgressModel(String studentName) {
//		this.studentName = studentName;
//		this.lectureProgress = new HashMap<>();
//		this.answersByLectureAndOutputFile = new HashMap<>();
//	}
//
//	// Getters and Setters
//	public String getStudentName() {
//		return studentName;
//	}
//
//	public void setStudentName(String studentName) {
//		this.studentName = studentName;
//	}
//
//	public Map<String, List<QuestionProgress>> getLectureProgress() {
//		return lectureProgress;
//	}
//
//	public void setLectureProgress(Map<String, List<QuestionProgress>> lectureProgress) {
//		this.lectureProgress = lectureProgress;
//	}
//
//	public Map<String, String> getAnswersByLectureAndOutputFile() {
//		return answersByLectureAndOutputFile;
//	}
//
//	public void setAnswersByLectureAndOutputFile(Map<String, String> answersByInputFile) {
//		this.answersByLectureAndOutputFile = answersByInputFile;
//	}
//
//	// Helpers
//	public boolean hasAnswered(String inputFileName) {
//		return answersByLectureAndOutputFile.containsKey(inputFileName);
//	}
//
//	public String getAnswer(String inputFileName) {
//		return answersByLectureAndOutputFile.get(inputFileName);
//	}
//
//	public void recordAnswer(String aLectureAndOutputFile, String answer) {
//		answersByLectureAndOutputFile.put(aLectureAndOutputFile, answer);
//	}
//
//	// Factory method for new students
//	public static StudentProgressModel createNew(String studentName) {
//		return new StudentProgressModel(studentName);
//	}
//
//	/**
//	 * Returns the default progress file location for a given student. Assumes
//	 * progress files are stored in: data/student_progress/
//	 */
//	public static File getDefaultProgressFile(String studentName) {
//		String safeName = studentName.trim().replaceAll("[^a-zA-Z0-9\\-_ ]", "").replaceAll("\\s+", "_");
//		return new File("data/student_progress", safeName + ".json");
//	}
//}
