package com.chat.models;

import java.util.List;

public class BasicQuestionDataModel {
	private String questionType;
    private String dateAsked;
    
	private String askedQuestion;
    private String outputFileName;
//    private String answerInClass;

    
	
	private String predictedQuestion;
    private String predictedAnswer;
//    private List<StudentAnswerModel> studentAnswers;
    public BasicQuestionDataModel() {
    	
    }
    public BasicQuestionDataModel(String questionType, String dateAsked, String askedQuestion, String outputFileName,
			String predictedQuestion, String predictedAnswer) {
		super();
		this.questionType = questionType;
		this.dateAsked = dateAsked;
		this.askedQuestion = askedQuestion;
		this.outputFileName = outputFileName;
		this.predictedQuestion = predictedQuestion;
		this.predictedAnswer = predictedAnswer;
	}
    public String getQuestionType() { return questionType; }
    public void setQuestionType(String questionType) { this.questionType = questionType; }

    public String getDateAsked() { return dateAsked; }
    public void setDateAsked(String dateAsked) { this.dateAsked = dateAsked; }

    public String getPredictedQuestion() { return predictedQuestion; }
    public void setPredictedQuestion(String question) { this.predictedQuestion = question; }

    public String getPredictedAnswer() { return predictedAnswer; }
    public void setPredictedAnswer(String predictedAnswer) { this.predictedAnswer = predictedAnswer; }

//    public List<StudentAnswerModel> getStudentAnswers() { return studentAnswers; }
//    public void setStudentAnswers(List<StudentAnswerModel> studentAnswers) { this.studentAnswers = studentAnswers; }
    public String getAskedQuestion() {
		return askedQuestion;
	}
	public void setAskedQuestion(String actualQuestion) {
		this.askedQuestion = actualQuestion;
	}
	public String getOutputFileName() {
		return outputFileName;
	}
	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}
	
}
