package com.Chat_Cluster;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.Chat_Filter.ZoomChatSegmenter.SingleChat;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class ChatTreeBuilder {

    // Method that creates a tree representation using Swing's JTree
    public static JTree buildChatTree(Hashtable<StudentGroup, List<SingleChat>> chatData) {
        // Create the root of the tree
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Answer");

        // Create nodes for 'True' and 'False' answers
        DefaultMutableTreeNode trueNode = new DefaultMutableTreeNode("True");
        DefaultMutableTreeNode falseNode = new DefaultMutableTreeNode("False");

        // Iterate through the hashtable and add tree nodes accordingly
        Enumeration<StudentGroup> groupKeys = chatData.keys();
        while (groupKeys.hasMoreElements()) {
            StudentGroup group = groupKeys.nextElement();
            List<SingleChat> chats = chatData.get(group);
            
            // Create a group node
            DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(group.getDescription());

            // Create chat nodes under the group node
            for (SingleChat chat : chats) {
                DefaultMutableTreeNode chatNode = new DefaultMutableTreeNode(chat.getMessage());
                DefaultMutableTreeNode studentNode = new DefaultMutableTreeNode(chat.getPersonName());
                chatNode.add(studentNode);
                groupNode.add(chatNode);
            }

            // Add the group node to the appropriate parent ('True' or 'False')
            if (group.isCorrect()) {
                trueNode.add(groupNode);
            } else {
                falseNode.add(groupNode);
            }
        }

        // Add the 'True' and 'False' nodes to the root
        root.add(trueNode);
        root.add(falseNode);

        // Create the tree by passing in the root node
        return new JTree(root);
    }
}