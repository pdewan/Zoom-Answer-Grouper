package com.chat.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class StudentProgressIO {

    // Save student progress to a JSON file
    public static void saveProgress(StudentProgressModel progress, File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(file, progress);
    }

    // Load student progress from a JSON file
    public static StudentProgressModel loadProgress(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, StudentProgressModel.class);
    }
    public static LectureDataModel readLectureData(InputStream inputStream) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(inputStream, LectureDataModel.class);
    }

    
}
