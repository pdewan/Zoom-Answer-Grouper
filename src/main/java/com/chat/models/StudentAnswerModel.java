package com.chat.models;

public class StudentAnswerModel {
	private String studentName;
    private String inClassAnswer;
    private String gptEvaluation;

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getInClassAnswer() { return inClassAnswer; }
    public void setInClassAnswer(String inClassAnswer) { this.inClassAnswer = inClassAnswer; }

    public String getGptEvaluation() { return gptEvaluation; }
    public void setGptEvaluation(String gptEvaluation) { this.gptEvaluation = gptEvaluation; }
}
