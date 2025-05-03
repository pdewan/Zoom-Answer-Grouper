package com.chat.models;

import java.util.*;
import java.util.stream.Collectors;

public class LectureViewModelBuilder {

    /**
     * Builds a list of LectureListViewModel from the LectureDataModel list (read from JSON)
     */
    public static List<LectureListViewModel> buildLectureListViewModels(List<LectureDataModel> lectureDataModels, StudentProgressModel studentProgress) {
        List<LectureListViewModel> result = new ArrayList<>();

        for (LectureDataModel lecture : lectureDataModels) {
            LectureListViewModel viewModel = new LectureListViewModel();
            viewModel.setLectureFileName(lecture.getLectureFileName());
            viewModel.setLastAccessDate(lecture.getDateSaved());
            
            int totalQuestions = lecture.getQuestions() != null ? lecture.getQuestions().size() : 0;
            viewModel.setTotalQuestions(totalQuestions);

            int answered = 0;
            if (lecture.getQuestions() != null) {
                for (QuestionDataModel question : lecture.getQuestions()) {
                    if (studentProgress.hasAnswered(question.getOutputFileName())) {
                        answered++;
                    }
                }
            }
            viewModel.setAnsweredQuestions(answered);

            result.add(viewModel);
        }

        return result;
    }

    /**
     * Builds a list of QuestionViewModel for a particular lecture
     */
    public static List<QuestionViewModel> buildQuestionViewModels(LectureDataModel lectureDataModel, StudentProgressModel studentProgress) {
        List<QuestionViewModel> result = new ArrayList<>();

        if (lectureDataModel.getQuestions() != null) {
            for (QuestionDataModel question : lectureDataModel.getQuestions()) {
                QuestionViewModel viewModel = new QuestionViewModel();
                viewModel.setLectureFileName(lectureDataModel.getLectureFileName());
                viewModel.setInputFileName(question.getOutputFileName()); // IMPORTANT: we use OutputFileName as identifier

                viewModel.setAskedQuestion(question.getAskedQuestion());
                viewModel.setPredictedQuestion(question.getPredictedQuestion());
                viewModel.setPredictedAnswer(question.getPredictedAnswer());

                // Find student's original in-class answer and GPT evaluation (if any)
                StudentAnswerModel match = findStudentAnswerModel(question, studentProgress.getStudentName());
                if (match != null) {
                    viewModel.setInClassStudentAnswer(match.getInClassAnswer());
                    viewModel.setGptEvaluation(match.getGptEvaluation());
                }

                // Check if student has already answered during this review session
                if (studentProgress.hasAnswered(question.getOutputFileName())) {
                    viewModel.setNewStudentAnswer(studentProgress.getAnswer(question.getOutputFileName()));
                    viewModel.setCompleted(true);
                } else {
                    viewModel.setCompleted(false);
                }

                result.add(viewModel);
            }
        }

        return result;
    }

    // Helper to find the in-class answer for this student
    private static StudentAnswerModel findStudentAnswerModel(QuestionDataModel question, String studentName) {
        if (question.getStudentAnswers() == null) return null;
        for (StudentAnswerModel answer : question.getStudentAnswers()) {
            if (answer.getStudentName().equalsIgnoreCase(studentName)) {
                return answer;
            }
        }
        return null;
    }
}

