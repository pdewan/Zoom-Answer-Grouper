package com.chat.models;


public class QuestionProgress {
    private String inputFileName;    // e.g., "segment1.txt"
    private String studentAnswer;    // student's new answer typed in UI
    private boolean completed;       // true if they have submitted an answer

    // Getters and Setters
    public String getInputFileName() {
        return inputFileName;
    }
    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }
    public String getStudentAnswer() {
        return studentAnswer;
    }
    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }
    public boolean isCompleted() {
        return completed;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}

