package com.Chat_View;

import com.Chat_Cluster.PropertiesManager;

public class ChatSegmentsFinderFactory {
	private static ChildrenFileFinder currentChatSegmentFinder;
	private static ChildrenFileFinder latestChatSegmentFinder = new LastAlphabeticallySortedFilesFinder();
	private static ChildrenFileFinder allChatSegmentsFinder = new AlphabeticallySortedFilesFinder();

	public static ChildrenFileFinder getSegmentFilesFinder() {
		if (currentChatSegmentFinder != null) {
			return currentChatSegmentFinder;
		} else {			
			return PropertiesManager.getLatestChat()?					
				latestChatSegmentFinder:
				allChatSegmentsFinder;			
		}
	}

	public static void setSegmentsFileFinder(ChildrenFileFinder newVal) {
		currentChatSegmentFinder = newVal;
	}
	
}
