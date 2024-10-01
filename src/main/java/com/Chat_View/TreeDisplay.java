package com.Chat_View;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import com.Chat_Filter.ZoomChatSegmenter.SingleChat;
import com.Chat_Group.StudentGroup;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class TreeDisplay extends JFrame {
    private JTree tree;

    public TreeDisplay(Hashtable<StudentGroup, List<SingleChat>> groupedAnswersTable) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500); // Set the size of the window

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Chats");
        Enumeration<StudentGroup> groups = groupedAnswersTable.keys();
        
        while (groups.hasMoreElements()) {
            StudentGroup group = groups.nextElement();
            List<SingleChat> chats = groupedAnswersTable.get(group);
            
            if (!chats.isEmpty()) {
                // Use the first chat as the root for this group
                SingleChat firstChat = chats.get(0);
                TreeNode firstChatNode = new TreeNode(firstChat, false);
                DefaultMutableTreeNode firstChatTreeNode = new DefaultMutableTreeNode(firstChatNode);

                // Add a node for the group itself under the first chat
                TreeNode groupNode = new TreeNode(group, true);
                firstChatTreeNode.add(new DefaultMutableTreeNode(groupNode));

                // Add the remaining chats as children of the first chat
                for (int i = 1; i < chats.size(); i++) {
                    TreeNode chatNode = new TreeNode(chats.get(i), false);
                    firstChatTreeNode.add(new DefaultMutableTreeNode(chatNode));
                }

                root.add(firstChatTreeNode);
            }
        }

        tree = new JTree(root);
        JScrollPane scrollPane = new JScrollPane(tree);
        add(scrollPane); // Add the tree to the window

        setVisible(true); // Make the window visible
    }

    // Example usage
    public static void main(String[] args) {
        Hashtable<StudentGroup, List<SingleChat>> groupedAnswersTable = new Hashtable<>();
        // Populate groupedAnswersTable with your data
        new TreeDisplay(groupedAnswersTable);
    }
}
