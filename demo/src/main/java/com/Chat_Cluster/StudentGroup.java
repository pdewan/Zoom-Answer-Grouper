package com.Chat_Cluster;

import java.util.ArrayList;
import java.util.List;

public class StudentGroup {
    private String description;
    private List<String> students;
    private boolean isCorrect;

    public StudentGroup(){
        this.students = new ArrayList<>();
    }

    public StudentGroup(String description, boolean isCorrect) {
        this.description = description;
        this.students = new ArrayList<>();
        this.isCorrect = isCorrect;
    }

    public void addStudent(String studentName) {
        students.add(studentName);
    }

    public List<String> getStudents() {
        return students;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Other necessary getters and setters
}