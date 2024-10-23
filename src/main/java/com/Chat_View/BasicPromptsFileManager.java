package com.Chat_View;

import javax.swing.*;

import com.Chat_Filter.QuestionType;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class BasicPromptsFileManager implements PromptsFileManager {
//	public static String PROMPT_PROLOG = "PomptProlog";
	private static final String DEFAULT_PROMPT_PROLOG = "You are a intelligent assistant.";;
//	public static String PROMPT_EPILOG = "PomptEpilog";
	private static final String DEFAULT_PROMPT_EPILOG = "";;

    // Default content for the text files
    private static final String FACTUAL_PROMPT = "As a teaching assistant in an advanced programming language course, your task is to cluster students' answers to a factual question asked in the class. First, find all the unique answers. Answers with similar content but different format aren't unique. Second, group similar answers to the same group. Third, for each group, find a representation answer. The representation answer must contains all the points of other answers within that group. Fourth, make the representation answer be the first one in the group. You could put one student in one group if their answer is unique. Notice, the students answered the question via Zoom chat. Therefore, it will be in the format of Zoom chat.\n"
    + //
    "Please don't provide extra information. Please ignore the chat of the instructor, Parsun Dewan, and follow the output format below:\n"
    + //
    "\n" + //
    "[group 1]: [representation answer student name, list of other students' name]\n" + //
    "[group 2]: [representation answer student name, list of other students' name]\n" + //
    "...\n" + //
    "\n" + //
    "Students' answers:\n";
    private static final String CONCEPTUAL_PROMPT = "As a teaching assistant in an advanced programming language course, your task is to cluster students' answers to a conceptual question asked in the class. First, find all the unique points made by all the students. Second, group them based on the points they made. Third, for each group, find a representation answer. The representation answer must contains all the points of other answers within that group. Fourth, make the representation answer be the first one in the group. You could put one student in one group if their answer is unique. Notice, the students answered the question via Zoom chat. Therefore, it will be in the format of Zoom chat.\n"
    + //
    "Please don't provide extra information. Please ignore the chat of the instructor, Parsun Dewan, and follow the output format below:\n"
    + //
    "\n" + //
    "[group 1]: [representation answer student name, list of other students' name]\n" + //
    "[group 2]: [representation answer student name, list of other students' name]\n" + //
    "...\n" + //
    "\n" + //
    "Students' answers:\n";

    private static final String ALGORITHM_PROMPT = "As a teaching assistant in an advanced programming language course, your task is to cluster students' answers to a algorithm question asked in the class. First, find all the unique algorithmic steps made by all the students. Second, group them based on the algorithmic steps they made. Third, for each group, find a representation answer. The representation answer must contains all the points of other answers within that group. Fourth, make the representation answer be the first one in the group. You could put one student in one group if their answer is unique. Notice, the students answered the question via Zoom chat. Therefore, it will be in the format of Zoom chat.\n"
    + //
    "Please don't provide extra information. Please ignore the chat of the instructor, Parsun Dewan, and follow the output format below:\n"
    + //
    "\n" + //
    "[group 1]: [representation answer student name, list of other students' name]\n" + //
    "[group 2]: [representation answer student name, list of other students' name]\n" + //
    "...\n" + //
    "\n" + //
    "Students' answers:\n";

    private static final String TRADEOFF_PROMPT = "As a teaching assistant in an advanced programming language course, your task is to cluster students' answers to a tradeoff question asked in the class. First, find all the unique pros and cons made by all the students. Second, group them based on the pros and cons they mentioned. Third, for each group, find a representation answer. The representation answer must contains all the points of other answers within that group. Fourth, make the representation answer be the first one in the group. You could put one student in one group if their answer is unique. Notice, the students answered the question via Zoom chat. Therefore, it will be in the format of Zoom chat.\n"
    + //
    "Please don't provide extra information. Please ignore the chat of the instructor, Parsun Dewan, and follow the output format below:\n"
    + //
    "\n" + //
    "[group 1]: [representation answer student name, list of other students' name]\n" + //
    "[group 2]: [representation answer student name, list of other students' name]\n" + //
    "...\n" + //
    "\n" + //
    "Students' answers:\n";

    private static final String EXPLANATION_PROMPT = "As a teaching assistant in an advanced programming language course, your task is to cluster students' answers to a explanation question asked in the class. First, find all the unique reasons made by all the students. Second, group them based on the reasons they mentioned. Third, for each group, find a representation answer. The representation answer must contains all the points of other answers within that group. Fourth, make the representation answer be the first one in the group. You could put one student in one group if their answer is unique. Notice, the students answered the question via Zoom chat. Therefore, it will be in the format of Zoom chat.\n"
    + //
    "Please don't provide extra information. Please ignore the chat of the instructor, Parsun Dewan, and follow the output format below:\n"
    + //
    "\n" + //
    "[group 1]: [representation answer student name, list of other students' name]\n" + //
    "[group 2]: [representation answer student name, list of other students' name]\n" + //
    "...\n" + //
    "\n" + //
    "Students' answers:\n";
    

    private static final String[] prompts = {
    		DEFAULT_PROMPT_PROLOG,
    		FACTUAL_PROMPT,
    		CONCEPTUAL_PROMPT,
    		ALGORITHM_PROMPT,
    		TRADEOFF_PROMPT,
    		DEFAULT_PROMPT_EPILOG
    };
    private static final String[] promptNames = {
    		PromptChoiceManager.PROMPT_PROLOG,
    		"Factual",
    		"Conceptual",
    		"Algorithm",
    		"Tradeoff",
    		PromptChoiceManager.PROMPT_EPILOG,

    };

    public static void promptChoice(QuestionType type) {
        final JDialog dialog = new JDialog((Frame) null, "Question Type Selection", true);
        dialog.setLayout(new FlowLayout());
        dialog.setSize(300, 200); // Adjusted size to potentially accommodate more buttons

        File downloadFolder = new File(System.getProperty("user.home") + "/Downloads");
        File promptDirectory = new File(downloadFolder, "prompts");

        // Ensure the directory exists or create it with default files
        if (!promptDirectory.exists()) {
            promptDirectory.mkdir(); // Create the directory if it does not exist
            try {
            	for (int aPromptIndex = 0; aPromptIndex < promptNames.length; aPromptIndex++ ) {
            		String aPromptName = promptNames[aPromptIndex];
            		String aPromptFileName = aPromptName + ".txt";
            		String aPrompt = prompts[aPromptIndex];
                createDefaultPromptFile(new File(promptDirectory, aPromptFileName), aPrompt);
//                createDefaultPromptFile(new File(promptDirectory, "conceptual.txt"), CONCEPTUAL_PROMPT);
//                createDefaultPromptFile(new File(promptDirectory, "algorithm.txt"), ALGORITHM_PROMPT);
//                createDefaultPromptFile(new File(promptDirectory, "tradeoff.txt"), TRADEOFF_PROMPT);
//                createDefaultPromptFile(new File(promptDirectory, "explanation.txt"), EXPLANATION_PROMPT);
            	}
            	} catch (IOException e) {
                e.printStackTrace();
            }
        }

        // List all text files in the directory
        File[] files = promptDirectory.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files != null) {
            for (File file : files) {
                String fileNameWithoutExtension = file.getName().replace(".txt", "");
                JButton button = new JButton(fileNameWithoutExtension);
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        type.setType(fileNameWithoutExtension);
                        dialog.dispose();
                    }
                });
                dialog.add(button);
            }
        }

        // If no files found, add a default message or a button
        if (files == null || files.length == 0) {
            JLabel label = new JLabel("No question types available.");
            dialog.add(label);
        }

        dialog.setVisible(true);
    }

    // Helper method to create a file and write default content to it
    private static void createDefaultPromptFile(File file, String content) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(content);
            }
        }
    }

	@Override
	public void processPromptFiles(File aPromptsDirectory) {
//		File downloadFolder = new File(System.getProperty("user.home") + "/Downloads");
//        File promptDirectory = new File(downloadFolder, "prompts");

        // Ensure the directory exists or create it with default files
        if (!aPromptsDirectory.exists()) {
            aPromptsDirectory.mkdirs(); // Create the directory if it does not exist
        }
            try {
            	for (int aPromptIndex = 0; aPromptIndex < promptNames.length; aPromptIndex++ ) {
            		String aPromptName = promptNames[aPromptIndex];
            		String aPromptFileName = aPromptName + ".txt";
            		String aPrompt = prompts[aPromptIndex];
                createDefaultPromptFile(new File(aPromptsDirectory, aPromptFileName), aPrompt);
//                createDefaultPromptFile(new File(promptDirectory, "conceptual.txt"), CONCEPTUAL_PROMPT);
//                createDefaultPromptFile(new File(promptDirectory, "algorithm.txt"), ALGORITHM_PROMPT);
//                createDefaultPromptFile(new File(promptDirectory, "tradeoff.txt"), TRADEOFF_PROMPT);
//                createDefaultPromptFile(new File(promptDirectory, "explanation.txt"), EXPLANATION_PROMPT);
            	}
            	} catch (IOException e) {
                e.printStackTrace();
            }
        }		
//	}
}
