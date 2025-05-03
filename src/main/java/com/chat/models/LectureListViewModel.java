package com.chat.models;

public class LectureListViewModel {
    private String lectureFileName;      // e.g., "Lecture12.pptx"
    private int totalQuestions;           // Total number of questions in this lecture
    private int answeredQuestions;        // Number of questions answered by the student
    private String lastAccessDate;         // Last date when this lecture was opened (optional, for future)

    // Getters and Setters
    public String getLectureFileName() {
        return lectureFileName;
    }
    public void setLectureFileName(String lectureFileName) {
        this.lectureFileName = lectureFileName;
    }
    public int getTotalQuestions() {
        return totalQuestions;
    }
    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }
    public int getAnsweredQuestions() {
        return answeredQuestions;
    }
    public void setAnsweredQuestions(int answeredQuestions) {
        this.answeredQuestions = answeredQuestions;
    }
    public String getLastAccessDate() {
        return lastAccessDate;
    }
    public void setLastAccessDate(String lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }
}
