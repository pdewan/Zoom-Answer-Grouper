package com.chat.models;
import com.chat.models.LectureDataModel;
import com.chat.models.QuestionDataModel;
import com.chat.models.StudentAnswerModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class StudentFeedbackExporter {

    public static void exportStudentFeedback(File jsonInputFile, File outputDirectory) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        LectureDataModel[] lectures = mapper.readValue(jsonInputFile, LectureDataModel[].class);

        // Map from student name to list of formatted feedback entries
        Map<String, List<String>> studentFeedbackMap = new HashMap<>();

        for (LectureDataModel lecture : lectures) {
            String pptName = lecture.getLectureFileName();

            for (QuestionDataModel question : lecture.getQuestions()) {
                String asked = question.getAskedQuestion();
                String predicted = question.getPredictedQuestion();

                for (StudentAnswerModel studentAnswer : question.getStudentAnswers()) {
                    String studentName = cleanStudentName(studentAnswer.getStudentName());

                    StringBuilder entry = new StringBuilder();
                    entry.append("Lecture: ").append(pptName).append("\n");
                    entry.append("Asked Question: ").append(asked).append("\n");
                    entry.append("Inferred Question: ").append(predicted).append("\n");
                    entry.append("Your Answer:\n").append(studentAnswer.getInClassAnswer()).append("\n");
                    entry.append("\n---\n\n");

                    studentFeedbackMap.computeIfAbsent(studentName, k -> new ArrayList<>()).add(entry.toString());
                }
            }
        }

        // Ensure output directory exists
        Path outputPath = outputDirectory.toPath();
        if (!Files.exists(outputPath)) {
            Files.createDirectories(outputPath);
        }

        // Write each student's file
        for (Map.Entry<String, List<String>> entry : studentFeedbackMap.entrySet()) {
            String studentFileName = entry.getKey().replaceAll("[^a-zA-Z0-9]", " ").trim().replaceAll("\\s+", " ") + ".txt";
            Path studentFilePath = outputPath.resolve(studentFileName);

            Files.write(studentFilePath, entry.getValue(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }

        System.out.println("Student feedback files written to: " + outputPath.toAbsolutePath());
    }

    private static String cleanStudentName(String rawName) {
        int bracketIndex = rawName.indexOf('[');
        if (bracketIndex != -1) {
            return rawName.substring(0, bracketIndex).trim();
        }
        return rawName.trim();
    }

    public static void main(String[] args) throws IOException {
        File inputJson = new File("data/lecture_model.JSON"); // or args[0]
        File outputDir = new File("data/student_feedback"); // or args[1]
        exportStudentFeedback(inputJson, outputDir);
    }
}
