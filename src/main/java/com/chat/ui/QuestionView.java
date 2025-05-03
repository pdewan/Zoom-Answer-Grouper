package com.chat.ui;

import com.chat.models.*;


import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class QuestionView extends JFrame {

    private final List<LectureDataModel> lectures;
    private final StudentProgressModel studentProgress;
    private String currentLectureAndOutputFile; 

//    private String currentLectureFile;

    private int currentLectureIndex = 0;
    private int currentQuestionIndex = 0;

    private JLabel questionLabel;
    private JTextArea questionArea;
    private JTextArea feedbackArea;
    private JTextArea curentAnswerArea;
    private JButton nextButton;
    private JButton prevButton;
    private JButton submitButton;
    private JLabel statusLabel;
    private static String INITIAL_ANSWER = "Please enter your answer today and then press submit to see yor previous and GPT answer";
    private static String INITIAL_FEEDBACK = "Your previous answer and GPT feedback will be available after you press submit";

    public QuestionView(int anIndex, List<LectureDataModel> lectures, StudentProgressModel studentProgress) {
        this.lectures = lectures;
        this.studentProgress = studentProgress;
        currentLectureIndex = anIndex;
        setTitle("Question Review");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initUI();
        loadCurrentQuestion();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        questionLabel = new JLabel("Question", JLabel.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(questionLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(3, 1));

        questionArea = createTextArea("Question Information");
        curentAnswerArea = createEditableTextArea("Your Current Answer");
//        studentAnswerArea.setText("Edit Me");
        feedbackArea = createTextArea("Your Previous and GPT's Answer");
//        feedbackArea.setText("Available when you press submit to submit current answer");

        centerPanel.add(new JScrollPane(questionArea));
        centerPanel.add(new JScrollPane(curentAnswerArea));
        centerPanel.add(new JScrollPane(feedbackArea));

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel();

        prevButton = new JButton("Previous");
        nextButton = new JButton("Next");
        submitButton = new JButton("Submit");

        prevButton.addActionListener(this::handlePrev);
        nextButton.addActionListener(this::handleNext);
        submitButton.addActionListener(this::handleSubmit);

        buttonPanel.add(prevButton);
        buttonPanel.add(submitButton);
        buttonPanel.add(nextButton);

        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        statusLabel = new JLabel(" ", JLabel.CENTER);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JTextArea createTextArea(String title) {
        JTextArea area = new JTextArea(title + ":\n");
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
//        area.setBorder(BorderFactory.createTitledBorder(title));
        area.setBorder(BorderFactory.createTitledBorder(
        	    BorderFactory.createLineBorder(Color.GRAY),
        	    title,
        	    TitledBorder.CENTER,  // <- This centers the title
        	    TitledBorder.TOP,
        	    new Font("SansSerif", Font.BOLD, 12),
        	    Color.DARK_GRAY
        	));
        return area;
    }

    private JTextArea createEditableTextArea(String title) {
//        JTextArea area = new JTextArea(title + ":\n");
//        area.setLineWrap(true);
//        area.setWrapStyleWord(true);
    	JTextArea area = createTextArea(title);
//    	area.setText("Edit Me");
    	area.setEditable(true);
//        area.setBorder(BorderFactory.createTitledBorder(
//        	    BorderFactory.createLineBorder(Color.GRAY),
//        	    title,
//        	    TitledBorder.CENTER,  // <- This centers the title
//        	    TitledBorder.TOP,
//        	    new Font("SansSerif", Font.BOLD, 12),
//        	    Color.DARK_GRAY
//        	));
        return area;
    }

    private LectureDataModel getCurrentLecture() {
        return lectures.get(currentLectureIndex);
    }

    private List<QuestionDataModel> getCurrentQuestions() {
        return getCurrentLecture().getQuestions();
    }

    private QuestionDataModel getCurrentQuestion() {
        return getCurrentQuestions().get(currentQuestionIndex);
    }
    private QuestionDataModel question;
    private String previousAnswer;
    private  String composeQuestionAreaText(QuestionDataModel aQuestion) {
    	String[] anAkedQuestionLines = aQuestion.getAskedQuestion().split("\\n");
    	String anAskedQuestion = anAkedQuestionLines[0]; 
    	String aQuestionText = "Question Date:" + aQuestion.getDateAsked() + "\n" +
                "Question Asked:" + anAskedQuestion + "\n" +
        		"Question Predicted:" + aQuestion.getPredictedQuestion();
    	return aQuestionText;
    }
    
    private  String composeFeedbackreaText(QuestionDataModel aQuestion) {
    	StudentAnswerModel aPreviousAnswer = LectureDataJsonlUtil.findAnswerByStudent(aQuestion.getStudentAnswers(), studentProgress.getStudentName() );
    	
    	String aClassAnswer = "Anwer in class:" +  
    			(aPreviousAnswer != null?  
    			aPreviousAnswer.getInClassAnswer() : 
    			"None found") + 
    			"\n";
    	String aGPTFeedback = "Feedback:" +
    			(aPreviousAnswer != null?  
    	    			aPreviousAnswer.getGptEvaluation() : 
    	    			"None found") + 
    	    			"\n";
    	String aGPTAnswer = "GPT's Answer:" + aQuestion.getPredictedAnswer();
    	return aClassAnswer + aGPTFeedback + aGPTAnswer;
    }
     
    private void loadCurrentQuestion() {
         question = getCurrentQuestion();
        
        questionLabel.setText(String.format("Lecture: %s | Question %d of %d",
                getCurrentLecture().getLectureFileName(),
                currentQuestionIndex + 1,
                getCurrentQuestions().size()));
        String aQuestionAreaText = composeQuestionAreaText(question);
        questionArea.setText(aQuestionAreaText);
      
//        predictedAnswerArea.setText("Predicted Answer:\n" + question.getPredictedAnswer());

//        String inputFile = question.getOutputFileName();
//        String prevAnswer = studentProgress.getAnswer(inputFile);
        currentLectureAndOutputFile = lectures.get(currentLectureIndex).getLectureFileName() + ":" + question.getOutputFileName();

        String prevAnswer = studentProgress.getAnswer(currentLectureAndOutputFile);

//        if (prevAnswer != null) {
//            studentAnswerArea.setText(prevAnswer);
//
//        }
        curentAnswerArea.setText((prevAnswer != null) ? prevAnswer : INITIAL_ANSWER);
        feedbackArea.setText(INITIAL_FEEDBACK);

        statusLabel.setText(String.format("Answered: %s", 
//        		(prevAnswer != null ? "Y" : "N")));
        (prevAnswer != null ? "\u2714" : "\u00D7") ));

        prevButton.setEnabled(!(currentLectureIndex == 0 && currentQuestionIndex == 0));
        nextButton.setEnabled(!isLastQuestion());
    }

    private void handleNext(ActionEvent e) {
        if (currentQuestionIndex < getCurrentQuestions().size() - 1) {
            currentQuestionIndex++;
        } else if (currentLectureIndex < lectures.size() - 1) {
            currentLectureIndex++;
            currentQuestionIndex = 0;
        }
        loadCurrentQuestion();
    }

    private void handlePrev(ActionEvent e) {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
        } else if (currentLectureIndex > 0) {
            currentLectureIndex--;
            currentQuestionIndex = getCurrentQuestions().size() - 1;
        }
        loadCurrentQuestion();
    }

    private void handleSubmit(ActionEvent e) {
        String input = curentAnswerArea.getText().trim();
        if (input.equals(INITIAL_ANSWER)) {
//        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "It would have been useful if you had edited the current answer before submitting.");
//            return;
        }

        String inputFile = getCurrentQuestion().getOutputFileName();
//        String aLectureAndOutputFile = lectures.get(currentLectureIndex).getLectureFileName() + ":" + inputFile;
        studentProgress.recordAnswer(currentLectureAndOutputFile, input);

        statusLabel.setText("Answer saved.");
        feedbackArea.setText(composeFeedbackreaText(question));
        
    }

    private boolean isLastQuestion() {
        return currentLectureIndex == lectures.size() - 1
                && currentQuestionIndex == getCurrentQuestions().size() - 1;
    }
}



