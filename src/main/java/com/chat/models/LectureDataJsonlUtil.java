package com.chat.models;


import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.JOptionPane;

import com.chat.analyze.InputData;
import com.chat.analyze.InputOutputData;
import com.chat.analyze.LectureData;
import com.chat.analyze.OutputData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class LectureDataJsonlUtil {
	private static List<LectureDataModel> lectureDataModels;
	
	public static List<BasicLectureDataModel> toBasicLectureDataModels(List<LectureDataModel> lectureModels ) {
		List<BasicLectureDataModel> retVal = new ArrayList<>();
		for (LectureDataModel aLectureDataModel:lectureModels) {
			retVal.add(aLectureDataModel.toBasicLectureDataModel());
		}
		return retVal;
	}
	
    public static List<LectureDataModel> toLectureDataModels(List<LectureData> lectureDataList)  {
        List<LectureDataModel> lectureModels = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (LectureData lectureData : lectureDataList) {
            LectureDataModel lectureModel = new LectureDataModel();
            if (lectureData == null 
            		|| lectureData.lecturePPTFile == null) {
            	continue;
            }
            lectureModel.setLectureFileName(lectureData.lecturePPTFile.getName());
            Date aDateSaved = new Date (lectureData.lecturePPTFile.lastModified());
            lectureModel.setDateSaved(sdf.format(aDateSaved));
            List<QuestionDataModel> questionModels = new ArrayList<>();

            for (InputOutputData ioData : lectureData.lectureInputOutputs) {
                OutputData outputData = ioData.outputData;
                InputData inputData = ioData.inputData;
                if (outputData == null) {
                	continue;
                }

                QuestionDataModel questionModel = new QuestionDataModel();
                questionModel.setQuestionType(outputData.questionType);

//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                questionModel.setDateAsked(sdf.format(outputData.dateAsked));
                questionModel.setDateAsked(sdf.format(new Date (outputData.timeAsked)));
                
                questionModel.setOutputFileName(outputData.outputFile.getName());

                questionModel.setPredictedQuestion(outputData.predictedQuestion);
                questionModel.setAskedQuestion(inputData.askedQuestion);
                questionModel.setPredictedAnswer(outputData.predictedAnswer);
                List<StudentAnswerModel> studentAnswers = new ArrayList<>();

                for (String studentName : inputData.inputStudentData.keySet()) {
                    StudentAnswerModel studentAnswer = new StudentAnswerModel();
                    studentAnswer.setStudentName(studentName);
                    studentAnswer.setInClassAnswer(inputData.inputStudentData.get(studentName));
                    String gptVal = null;
                    String gptEval = outputData.processedStudentData != null ? outputData.processedStudentData.get(studentName) : null;
                    studentAnswer.setGptEvaluation((gptEval != null) ? gptEval : "No evaluation available");

//                    if (outputData.processedStudentData != null) {
//                    	
//                    
//                    String gptEval = outputData.processedStudentData.get(studentName);
//                    studentAnswer.setGptEvaluation((gptEval != null) ? gptEval : "No evaluation available");
//                    
//                    }

                    studentAnswers.add(studentAnswer);
                }

                questionModel.setStudentAnswers(studentAnswers);
                questionModels.add(questionModel);
            }

            lectureModel.setQuestions(questionModels);
            if (!lectureModel.getQuestions().isEmpty()) {
            lectureModels.add(lectureModel);
            }
        }
        return lectureModels;

//        ObjectMapper mapper = new ObjectMapper();
//        mapper.enable(SerializationFeature.INDENT_OUTPUT);
//        mapper.writeValue(outputFile, lectureModels);
    }
    public static void exportLectureDataToJson(List<LectureData> lectureDataList, File outputFile) throws IOException {
        List<LectureDataModel> lectureModels = toLectureDataModels(lectureDataList);   
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(outputFile, lectureModels);
    }
    
    public static void exportBasicLectureDataToJson(List<LectureData> lectureDataList, File outputFile) throws IOException {
        List<LectureDataModel> lectureModels = toLectureDataModels(lectureDataList); 
        List<BasicLectureDataModel> basicLectureModels = toBasicLectureDataModels(lectureModels);
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(outputFile, basicLectureModels);
    }
	
//    public static void exportLectureDataToJson(List<LectureData> lectureDataList, File outputFile) throws IOException {
//        List<LectureDataModel> lectureModels = new ArrayList<>();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        for (LectureData lectureData : lectureDataList) {
//            LectureDataModel lectureModel = new LectureDataModel();
//            if (lectureData == null 
//            		|| lectureData.lecturePPTFile == null) {
//            	continue;
//            }
//            lectureModel.setLectureFileName(lectureData.lecturePPTFile.getName());
//            Date aDateSaved = new Date (lectureData.lecturePPTFile.lastModified());
//            lectureModel.setDateSaved(sdf.format(aDateSaved));
//            List<QuestionDataModel> questionModels = new ArrayList<>();
//
//            for (InputOutputData ioData : lectureData.lectureInputOutputs) {
//                OutputData outputData = ioData.outputData;
//                InputData inputData = ioData.inputData;
//                if (outputData == null) {
//                	continue;
//                }
//
//                QuestionDataModel questionModel = new QuestionDataModel();
//                questionModel.setQuestionType(outputData.questionType);
//
////                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////                questionModel.setDateAsked(sdf.format(outputData.dateAsked));
//                questionModel.setDateAsked(sdf.format(new Date (outputData.timeAsked)));
//                
//                questionModel.setOutputFileName(outputData.outputFile.getName());
//
//                questionModel.setPredictedQuestion(outputData.predictedQuestion);
//                questionModel.setAskedQuestion(inputData.askedQuestion);
//                questionModel.setPredictedAnswer(outputData.predictedAnswer);
//                List<StudentAnswerModel> studentAnswers = new ArrayList<>();
//
//                for (String studentName : inputData.inputStudentData.keySet()) {
//                    StudentAnswerModel studentAnswer = new StudentAnswerModel();
//                    studentAnswer.setStudentName(studentName);
//                    studentAnswer.setInClassAnswer(inputData.inputStudentData.get(studentName));
//                    String gptVal = null;
//                    String gptEval = outputData.processedStudentData != null ? outputData.processedStudentData.get(studentName) : null;
//                    studentAnswer.setGptEvaluation((gptEval != null) ? gptEval : "No evaluation available");
//
////                    if (outputData.processedStudentData != null) {
////                    	
////                    
////                    String gptEval = outputData.processedStudentData.get(studentName);
////                    studentAnswer.setGptEvaluation((gptEval != null) ? gptEval : "No evaluation available");
////                    
////                    }
//
//                    studentAnswers.add(studentAnswer);
//                }
//
//                questionModel.setStudentAnswers(studentAnswers);
//                questionModels.add(questionModel);
//            }
//
//            lectureModel.setQuestions(questionModels);
//            if (!lectureModel.getQuestions().isEmpty()) {
//            lectureModels.add(lectureModel);
//            }
//        }
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.enable(SerializationFeature.INDENT_OUTPUT);
//        mapper.writeValue(outputFile, lectureModels);
//    }

    public static List<LectureDataModel> importLectureDataFromJson(File jsonFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        LectureDataModel[] lectureModels = mapper.readValue(jsonFile, LectureDataModel[].class);
        return Arrays.asList(lectureModels);
    }
    public static List<LectureDataModel> importLectureDataFromJson(InputStream anInputStream) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        LectureDataModel[] lectureModels = mapper.readValue(anInputStream, LectureDataModel[].class);
        return Arrays.asList(lectureModels);
    }
    public static List<LectureDataModel> getLectureData() {
    	if (lectureDataModels == null) {
        try {
            InputStream is = LectureDataModel.class.getClassLoader().getResourceAsStream("data/lecture_model.JSON");
            if (is == null) throw new RuntimeException("Lecture data file not found.");
            lectureDataModels = LectureDataJsonlUtil.importLectureDataFromJson(is);
        } catch (Exception e) {
        	System.err.println("Failed to load lecture data:");
//            lectureDataModels = List.of(); // fallback to empty list

        	return null;
//            JOptionPane.showMessageDialog(LectureDataModel.cla, "Failed to load lecture data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//            lectureDataModels = List.of(); // fallback to empty list
        }
    	}
    	return lectureDataModels;
    }
    public static StudentAnswerModel findAnswerByStudent(List<StudentAnswerModel> answers, String studentName) {
	    if (answers == null || studentName == null) return null;
	    for (StudentAnswerModel answer : answers) {
	        if (studentName.equalsIgnoreCase(answer.getStudentName().trim())) {
	            return answer;
	        }
	    }
	    return null;  // Not found
	}
}

