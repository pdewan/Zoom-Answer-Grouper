package com.Chat_Cluster;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import com.Chat_Filter.ZoomChatSegmenter;
import com.Chat_Filter.ZoomChatSegmenter.SingleChat;

public class RepresentationViewTree {
    public static void main(String[] args) {
        QuestionType questionType = new QuestionType();
        PromptChoiceView.promptChoice(questionType);
        GroupParser groupParser = new GroupParser();
        AnswerParser answerParser = new AnswerParser();
        String userRoot = System.getProperty("user.home");
        System.out.println("userRoot: " + userRoot);
        String rawFilePath = userRoot + "/Downloads/meeting_saved_chat.txt";
        ZoomChatSegmenter.segment(rawFilePath, 2);
        File rootDir = new File(userRoot + "/Downloads");
        int segmentNum = OpenAI.getGPTAnswerForNewestSegmentation(rootDir, questionType);
        try {
            // Parse groups and answers from the files
            Hashtable<StudentGroup, List<SingleChat>> groupedAnswersTable = new Hashtable<>();

            // Map<Integer, StudentGroup> studentGroups = groupParser
            //         .representationViewParse("data/Zoom-Chats/2023-09-19 12.55.01 Comp 524 Lectures Fall 2023/outputs/2.txt");
            // Map<String, String> studentAnswers = answerParser
            //         .parse("data/Zoom-Chats/2023-09-19 12.55.01 Comp 524 Lectures Fall 2023/segmentation/segment2.txt");

            Map<Integer, StudentGroup> studentGroups = groupParser.representationViewParse(rootDir + "/outputs/" + segmentNum + ".txt");
            Map<String, String> studentAnswers = answerParser.parse(rootDir + "/segmentation/segment" + segmentNum + ".txt");

            // Go through each parsed student answer and add it to the correct group in the
            // hashtable
            for (Map.Entry<Integer, StudentGroup> groupEntry : studentGroups.entrySet()) {
                StudentGroup group = groupEntry.getValue();
                for (Map.Entry<String, String> studentAnswer : studentAnswers.entrySet()) {
                    if (group.getStudents().contains(studentAnswer.getKey())) {
                        groupedAnswersTable.putIfAbsent(group, new ArrayList());
                        SingleChat chat = new SingleChat(null, studentAnswer.getKey(), studentAnswer.getValue());
                        groupedAnswersTable.get(group).add(chat);
                    }
                }
            }
            // Instantiate and populate your Hashtable<StudentGroup, List<SingleChat>>
            // Hashtable<StudentGroup, List<SingleChat>> chatData = new Hashtable<>();
            // (Add data to chatData as necessary)

            // Build the JTree with the chat data
            new TreeDisplay(groupedAnswersTable);
            LabelController.labelCsvFileGenerator(groupedAnswersTable);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
