package com.chat.models;

public abstract class AbstractLectureDataModel {
	private String lectureFileName;
	private String dateSaved;
//    private List<BasicQuestionDataModel> questions;

    public AbstractLectureDataModel(String lectureFileName, String dateSaved) {
		super();
		this.lectureFileName = lectureFileName;
		this.dateSaved = dateSaved;
	}
    public AbstractLectureDataModel() {
    	super();
    }
	public String getLectureFileName() { return lectureFileName; }
    public void setLectureFileName(String lectureFileName) { this.lectureFileName = lectureFileName; }

	public String getDateSaved() {
		return dateSaved;
	}
	public void setDateSaved(String dateSaved) {
		this.dateSaved = dateSaved;
	}
}

