package com.chat.models;

import java.util.ArrayList;
import java.util.List;

public class LectureDataModel extends AbstractLectureDataModel {
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

	private List<QuestionDataModel> questions;

	public List<QuestionDataModel> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuestionDataModel> questions) {
		this.questions = questions;
	}
	
	public BasicLectureDataModel toBasicLectureDataModel() {
		
		BasicLectureDataModel retVal = new BasicLectureDataModel(
				getLectureFileName(), getDateSaved());
		List<BasicQuestionDataModel> aBasicQuestions = new ArrayList<>();
		for (QuestionDataModel aQuestionDataModel:getQuestions()) {
			aBasicQuestions.add(aQuestionDataModel.toBasicQuestionDataModel());			
		}
		retVal.setQuestions(aBasicQuestions);
		return retVal;
	}
}
