package com.chat.models;

import java.util.List;

public class QuestionDataModel extends BasicQuestionDataModel{
//public QuestionDataModel(String questionType, String dateAsked, String askedQuestion, String outputFileName,
//			String predictedQuestion, String predictedAnswer) {
//		super(questionType, dateAsked, askedQuestion, outputFileName, predictedQuestion, predictedAnswer);
//		// TODO Auto-generated constructor stub
//	}

	//	private String questionType;
//    private String dateAsked;
//    private String askedQuestion;
//    private String outputFileName;
////    private String answerInClass;
//
//    
//	
//	private String predictedQuestion;
//    private String predictedAnswer;
    private List<StudentAnswerModel> studentAnswers;

//    public String getQuestionType() { return questionType; }
//    public void setQuestionType(String questionType) { this.questionType = questionType; }
//
//    public String getDateAsked() { return dateAsked; }
//    public void setDateAsked(String dateAsked) { this.dateAsked = dateAsked; }
//
//    public String getPredictedQuestion() { return predictedQuestion; }
//    public void setPredictedQuestion(String question) { this.predictedQuestion = question; }
//
//    public String getPredictedAnswer() { return predictedAnswer; }
//    public void setPredictedAnswer(String predictedAnswer) { this.predictedAnswer = predictedAnswer; }

    public List<StudentAnswerModel> getStudentAnswers() { return studentAnswers; }
    public void setStudentAnswers(List<StudentAnswerModel> studentAnswers) { this.studentAnswers = studentAnswers; }
    
    public BasicQuestionDataModel toBasicQuestionDataModel() {
    	BasicQuestionDataModel retVal = new BasicQuestionDataModel(
    			getQuestionType(), 
    			getDateAsked(), 
    			getAskedQuestion(), 
    			getOutputFileName(), 
    			getPredictedQuestion(), 
    			getPredictedAnswer());
    	return retVal;
    }
//    public String getAskedQuestion() {
//		return askedQuestion;
//	}
//	public void setAskedQuestion(String actualQuestion) {
//		this.askedQuestion = actualQuestion;
//	}
//	public String getOutputFileName() {
//		return outputFileName;
//	}
//	public void setOutputFileName(String outputFileName) {
//		this.outputFileName = outputFileName;
//	}
	
}
