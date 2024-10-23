package com.Chat_View;

import com.Chat_Cluster.PropertiesManager;

public class ChatFoldersFinderFactory {
	private static ChildrenFileFinder currentChatFolderFinder;
	private static ChildrenFileFinder latestChatFolderFinder = new LatestSubfolderFinder();
	private static ChildrenFileFinder allChatFoldersFinder = new AllSubfoldersFinder();

	public static ChildrenFileFinder getChatFolderFinder() {
		if (currentChatFolderFinder != null) {
			return currentChatFolderFinder;
		} else {			
			return PropertiesManager.getLatestChat()?					
				latestChatFolderFinder:
				allChatFoldersFinder;			
		}
	}

	public static void setChatFolderFinder(ChildrenFileFinder newVal) {
		currentChatFolderFinder = newVal;
	}
	
}
