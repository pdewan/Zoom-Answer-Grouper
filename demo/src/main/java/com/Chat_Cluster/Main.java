package com.Chat_Cluster;

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

import java.awt.Dimension;

public class Main {
    public static void main(String[] args) {
        GroupParser groupParser = new GroupParser();
        AnswerParser answerParser = new AnswerParser();
        String userRoot = System.getProperty("user.home");
        System.out.println("userRoot: " + userRoot);
        String rawFilePath = userRoot + "/Downloads/meeting_saved_chat.txt";
        ZoomChatSegmenter.segment(rawFilePath, 2);
        File rootDir = new File(userRoot + "/Downloads");
        OpenAI.getGPTAnswer(rootDir);
        // try {
        //     // Parse groups and answers from the files
        //     Hashtable<StudentGroup, List<SingleChat>> groupedAnswersTable = new Hashtable<>();

        //     Map<Integer, StudentGroup> studentGroups = groupParser.parse(rootDir + "/outputs/1.txt");
        //     Map<String, String> studentAnswers = answerParser.parse(rootDir + "/segmentation/segment1.txt");

        //     // Go through each parsed student answer and add it to the correct group in the hashtable
        //     for (Map.Entry<Integer, StudentGroup> groupEntry : studentGroups.entrySet()) {
        //         StudentGroup group = groupEntry.getValue();
        //         for (Map.Entry<String, String> studentAnswer : studentAnswers.entrySet()) {
        //             if (group.getStudents().contains(studentAnswer.getKey())) {
        //                 groupedAnswersTable.putIfAbsent(group, new ArrayList());
        //                 SingleChat chat = new SingleChat(null, studentAnswer.getKey(), studentAnswer.getValue());
        //                 groupedAnswersTable.get(group).add(chat);
        //             }
        //         }
        //     }
        //     // Instantiate and populate your Hashtable<StudentGroup, List<SingleChat>>
        //     // Hashtable<StudentGroup, List<SingleChat>> chatData = new Hashtable<>();
        //     // (Add data to chatData as necessary)

        //     // Build the JTree with the chat data
        //     JTree chatTree = ChatTreeBuilder.buildChatTree(groupedAnswersTable);

        //     // Create a JScrollPane that will contain the JTree
        //     JScrollPane treeScrollPane = new JScrollPane(chatTree);

        //     // Create and display a JFrame containing the tree
        //     JFrame frame = new JFrame("Chat Tree View");
        //     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //     frame.add(treeScrollPane);
        //     frame.setPreferredSize(new Dimension(400, 600)); // Set your desired size
        //     frame.pack();
        //     frame.setLocationRelativeTo(null);
        //     frame.setVisible(true);

        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    }
}