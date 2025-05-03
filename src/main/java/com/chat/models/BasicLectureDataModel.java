package com.chat.models;

import java.util.List;

public class BasicLectureDataModel extends AbstractLectureDataModel {
//	private String lectureFileName;
//	private String dateSaved;

//
//    public String getLectureFileName() { return lectureFileName; }
//    public void setLectureFileName(String lectureFileName) { this.lectureFileName = lectureFileName; }
//
//
//	public String getDateSaved() {
//		return dateSaved;
//	}
//	public void setDateSaved(String dateSaved) {
//		this.dateSaved = dateSaved;
//	}
	private List<BasicQuestionDataModel> questions;

	public BasicLectureDataModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BasicLectureDataModel(String lectureFileName, String dateSaved) {
		super(lectureFileName, dateSaved);
		// TODO Auto-generated constructor stub
	}

	public List<BasicQuestionDataModel> getQuestions() {
		return questions;
	}

	public void setQuestions(List<BasicQuestionDataModel> questions) {
		this.questions = questions;
	}
}
