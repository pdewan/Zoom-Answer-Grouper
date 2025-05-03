package com.chat.ui;


import com.chat.models.LectureDataJsonlUtil;
import com.chat.models.LectureDataModel;
import com.chat.models.QuestionDataModel;
import com.chat.models.StudentProgressIO;
import com.chat.models.StudentProgressModel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public class LectureListView extends JFrame {
    private JList<String> lectureList;
    private DefaultListModel<String> listModel;

    private List<LectureDataModel> lectureDataModels;
    private StudentProgressModel studentProgress;

    public LectureListView() {
        super("Lecture Selection");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
//        studentProgress = StudentProgressModel.getInstance();
//        String studentName = studentProgress.getStudentName();
        String studentName = WelcomeScreen.getStudentName();

//        String studentName = StudentConfigManager.loadStudentName();
        File progressFile = StudentProgressModel.getDefaultProgressFile(studentName);

        // Ensure the parent directory exists
        File parentDir = progressFile.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        if (progressFile.exists()) {
            try {
				studentProgress = StudentProgressIO.loadProgress(progressFile);
			} catch (IOException e) {
	            JOptionPane.showMessageDialog(this, "Failed to load progress data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
	            studentProgress = StudentProgressModel.createNew(studentName);
			}
        } else {
        	studentProgress = StudentProgressModel.createNew(studentName);
//            studentProgress = new StudentProgressModel();
//            studentProgress = StudentProgressModel.getInstance();
//            studentProgress.setStudentName(studentName);
//            studentProgress.setLectureProgress(new HashMap<>());
            // Defer saving until there's data
        }
//        studentProgress = null;
//        		StudentProgressIO.loadProgress(file)

        loadLectureData();
        initializeUI();
        
        
    }

    private void loadLectureData() {
    	lectureDataModels = LectureDataJsonlUtil.getLectureData();
    	if (lectureDataModels == null) {
    		JOptionPane.showMessageDialog(this, "Failed to load lecture data.");
//            loadLectureDataLocal();
            System.exit(1);
    	}
//        try {
//            InputStream is = getClass().getClassLoader().getResourceAsStream("data/lecture_model.JSON");
//            if (is == null) throw new RuntimeException("Lecture data file not found.");
//            lectureDataModels = LectureDataJsonlUtil.importLectureDataFromJson(is);
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Failed to load lecture data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//            lectureDataModels = List.of(); // fallback to empty list
//        }
    }

    private void initializeUI() {
        listModel = new DefaultListModel<>();
        for (LectureDataModel lecture : lectureDataModels) {
            String fileName = lecture.getLectureFileName();
            int total = lecture.getQuestions().size();
            long answered = lecture.getQuestions().stream()
                .filter(q -> studentProgress.hasAnswered(q.getOutputFileName()))
                .count();
            listModel.addElement(String.format("%s (%d/%d answered)", fileName, answered, total));
        }

        lectureList = new JList<>(listModel);
        lectureList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lectureList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = lectureList.getSelectedIndex();
                if (index >= 0) {
//                    LectureDataModel selectedLecture = lectureDataModels.get(index);
                    // Later: Launch question viewer
//                    JOptionPane.showMessageDialog(this,
//                            "You selected: " + selectedLecture.getLectureFileName(),
//                            "Lecture Selected", JOptionPane.INFORMATION_MESSAGE);
                    QuestionView questionView = new QuestionView(index, lectureDataModels, studentProgress);
                    questionView.setVisible(true);
                    dispose(); // Close the lecture list window if you want
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(lectureList);
        add(scrollPane, BorderLayout.CENTER);
    }
}

