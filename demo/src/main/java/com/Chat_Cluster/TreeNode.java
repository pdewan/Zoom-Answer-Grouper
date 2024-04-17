package com.Chat_Cluster;

import com.Chat_Filter.ZoomChatSegmenter.SingleChat;

public class TreeNode {
    private Object userObject;
    private boolean isGroup;

    public TreeNode(Object userObject, boolean isGroup) {
        this.userObject = userObject;
        this.isGroup = isGroup;
    }

    public Object getUserObject() {
        return userObject;
    }

    public boolean isGroup() {
        return isGroup;
    }

    @Override
    public String toString() {
        if (isGroup) {
            StudentGroup group = (StudentGroup) userObject;
            return group.getDescription(); // Customize this based on how you want to display groups
        } else {
            SingleChat chat = (SingleChat) userObject;
            return chat.getPersonName() + ": " + chat.getMessage(); // Customize this based on how you want to display chats
        }
    }
}
