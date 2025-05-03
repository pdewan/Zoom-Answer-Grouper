package com.chat.ui;

import com.chat.models.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.List;

public class WelcomeScreen extends JFrame {

    private JTextField nameField;
    private JButton submitButton;
    private List<LectureDataModel> lectures;
    private static String studentName;
    

    public static String getStudentName() {
		return studentName;
	}
	public static void setStudentName(String studentName) {
		WelcomeScreen.studentName = studentName;
	}
	public WelcomeScreen(Runnable onSuccess) {
        super("Welcome to the Chat Review Tool");
//    	StudentProgressModel aStudentProgressModel = StudentProgressModel.getInstance();

        // Load the lectures from ./data/lecture_model.JSON

        loadLectureData();

        // Try to skip screen if student.config exists and is valid
        String existingName = readStudentNameFromConfig();
       
        if (existingName != null && isValidStudent(existingName)) {
            System.out.println("Auto-login for student: " + existingName);
            setStudentName(existingName);

            onSuccess.run();
//            this.setVisible(false);

//            dispose();
//            aStudentProgressModel.setStudentName(existingName);
            return;
        }


        setLayout(new BorderLayout());

        JLabel promptLabel = new JLabel("\"Enter your Zoom Chat name without pronouns suffix such as (she/her):\"");
        nameField = new JTextField(20);
        submitButton = new JButton("Start");

        JPanel inputPanel = new JPanel();
        inputPanel.add(promptLabel);
        inputPanel.add(nameField);
        inputPanel.add(submitButton);

        add(inputPanel, BorderLayout.CENTER);

        submitButton.addActionListener(e -> {
            String enteredName = nameField.getText().trim();
            if (isValidStudent(enteredName)) {
                saveStudentNameToConfig(enteredName);
//                aStudentProgressModel.setStudentName(enteredName);
                setStudentName(enteredName);


                onSuccess.run();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Name not found. Please enter the exact name used in Zoom without pronouns.");
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 120);
        setLocationRelativeTo(null);
        setVisible(true);
    }
//
    private void loadLectureDataLocal() {
        try {
            File jsonFile = new File("data/lecture_model.JSON");
            ObjectMapper mapper = new ObjectMapper();
            LectureDataModel[] lectureArray = mapper.readValue(jsonFile, LectureDataModel[].class);
            lectures = Arrays.asList(lectureArray);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Could not load lecture data.");
            System.exit(1);
        }
    }
    private void loadLectureData() {
    	lectures = LectureDataJsonlUtil.getLectureData();
    	if (lectures == null) {
    		JOptionPane.showMessageDialog(this, "Failed to load lecture data.");
//            loadLectureDataLocal();
            System.exit(1);
    	}
//        try (InputStream is = getClass().getClassLoader().getResourceAsStream("data/lecture_model.JSON")) {
//            if (is == null) {;
//                JOptionPane.showMessageDialog(this, "Could not find lecture_model.JSON in classpath.");
//                System.exit(1);
//            }
//            ObjectMapper mapper = new ObjectMapper();
//            LectureDataModel[] lectureArray = mapper.readValue(is, LectureDataModel[].class);
//            lectures = Arrays.asList(lectureArray);
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(this, "Failed to load lecture data.");
//            loadLectureDataLocal();
//            System.exit(1);
//        }
    }


    private boolean isValidStudent(String name) {
        if (name == null || name.isEmpty()) return false;

        return lectures.stream()
                .flatMap(lecture -> lecture.getQuestions().stream())
                .flatMap(q -> q.getStudentAnswers().stream())
                .map(StudentAnswerModel::getStudentName)
                .anyMatch(studentName -> studentName.equalsIgnoreCase(name.trim()));
    }

    private void saveStudentNameToConfig(String name) {
        try (PrintWriter writer = new PrintWriter("student.config")) {
            writer.println(name.trim());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readStudentNameFromConfig() {
        File configFile = new File("student.config");
        if (!configFile.exists()) return null;
        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            return reader.readLine();
        } catch (IOException e) {
            return null;
        }
    }
//  public static void main(String[] args) {
//  SwingUtilities.invokeLater(WelcomeScreen::new);
//}
}

//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
//import java.io.*;
//import java.util.List;
//import java.util.stream.Collectors;
//import com.chat.models.LectureDataModel;
//import com.chat.models.LectureDataJsonlUtil;
//
//public class WelcomeScreen extends JFrame {
//    private JTextField nameField;
//    private JButton startButton;
//    private JLabel statusLabel;
//    private List<LectureDataModel> lectures;
//
//    private static final File CONFIG_FILE = new File("student.config");
//    private static final File LECTURE_JSON_FILE = new File("data/lecture_model.JSON");
//
//    public WelcomeScreen() {
//        setTitle("Welcome to the Study Tool");
//        setSize(400, 200);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        setLayout(new BorderLayout());
//
//        JPanel inputPanel = new JPanel(new FlowLayout());
//        nameField = new JTextField(20);
//        startButton = new JButton("Start");
//        statusLabel = new JLabel(" ");
//
//        inputPanel.add(new JLabel("Enter your Zoom Chat Name Without Pronouns:"));
//        inputPanel.add(nameField);
//        inputPanel.add(startButton);
//
//        add(inputPanel, BorderLayout.CENTER);
//        add(statusLabel, BorderLayout.SOUTH);
//
//        try {
//            lectures = LectureDataJsonlUtil.importLectureDataFromJson(LECTURE_JSON_FILE);
//        } catch (IOException e) {
//            showError("Failed to load lecture data: " + e.getMessage());
//            startButton.setEnabled(false);
//        }
//
//        if (CONFIG_FILE.exists()) {
//            try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
//                String savedName = reader.readLine();
//                if (isValidStudent(savedName)) {
//                    proceedToLectureList(savedName);
//                } else {
//                    showError("Saved name is invalid. Please re-enter.");
//                }
//            } catch (IOException e) {
//                showError("Error reading saved config.");
//            }
//        }
//
//        // Button behavior
//        startButton.addActionListener(e -> {
//            String enteredName = nameField.getText().trim();
//            if (enteredName.isEmpty()) {
//                showError("Please enter your name.");
//                return;
//            }
//            if (isValidStudent(enteredName)) {
//                saveStudentName(enteredName);
//                proceedToLectureList(enteredName);
//            } else {
////                showError("Name not found. Please check spelling or attend class!");
//                showError("Name not found. Please check spelling and no proouns please!");
//
//            }
//        });
//
//        setVisible(true);
//    }
//
//    private void proceedToLectureList(String studentName) {
//        dispose();
////        new LectureListScreen(studentName, lectures);  // You can implement this next
//    }
//
//    private boolean isValidStudent(String name) {
//        return lectures.stream()
//            .flatMap(lecture -> lecture.getQuestions().stream())
//            .flatMap(q -> q.getStudentAnswers().stream())
//            .map(ans -> ans.getStudentName().trim())
//            .anyMatch(student -> student.equalsIgnoreCase(name.trim()));
//    }
//
//    private void saveStudentName(String name) {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
//            writer.write(name.trim());
//        } catch (IOException e) {
//            showError("Error saving name.");
//        }
//    }
//
// 
//    private void showError(String message) {
//        statusLabel.setText("!" + message);
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(WelcomeScreen::new);
//    }
//}