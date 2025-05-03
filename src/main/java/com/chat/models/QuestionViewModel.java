package com.chat.models;

public class QuestionViewModel {
    private String studentName; // NEW FIELD!

	
    private String lectureFileName;       // Lecture this question belongs to (e.g., "Lecture12.pptx")
    private String inputFileName;          // Input file name (e.g., "segment23.txt")

    private String askedQuestion;          // Actual question asked in class (human-authored)
    private String predictedQuestion;      // GPT-generated version of the question
    private String predictedAnswer;        // GPT-generated answer

    private String inClassStudentAnswer;   // Student's original in-class answer (if any)
    private String gptEvaluation;          // GPT evaluation of their in-class answer (if any)
    private String newStudentAnswer;       // New answer student types in the UI
    private boolean completed;             // Whether the student has completed this question

    // Getters and Setters
    public String getLectureFileName() {
        return lectureFileName;
    }
    public void setLectureFileName(String lectureFileName) {
        this.lectureFileName = lectureFileName;
    }
    public String getInputFileName() {
        return inputFileName;
    }
    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }
    public String getAskedQuestion() {
        return askedQuestion;
    }
    public void setAskedQuestion(String askedQuestion) {
        this.askedQuestion = askedQuestion;
    }
    public String getPredictedQuestion() {
        return predictedQuestion;
    }
    public void setPredictedQuestion(String predictedQuestion) {
        this.predictedQuestion = predictedQuestion;
    }
    public String getPredictedAnswer() {
        return predictedAnswer;
    }
    public void setPredictedAnswer(String predictedAnswer) {
        this.predictedAnswer = predictedAnswer;
    }
    public String getInClassStudentAnswer() {
        return inClassStudentAnswer;
    }
    public void setInClassStudentAnswer(String inClassStudentAnswer) {
        this.inClassStudentAnswer = inClassStudentAnswer;
    }
    public String getGptEvaluation() {
        return gptEvaluation;
    }
    public void setGptEvaluation(String gptEvaluation) {
        this.gptEvaluation = gptEvaluation;
    }
    public String getNewStudentAnswer() {
        return newStudentAnswer;
    }
    public void setNewStudentAnswer(String newStudentAnswer) {
        this.newStudentAnswer = newStudentAnswer;
    }
    public boolean isCompleted() {
        return completed;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
}

