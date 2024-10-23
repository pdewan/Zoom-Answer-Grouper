package com.Chat_View;

public class PromptsFileManagerFactory {
	static PromptsFileManager promptsManager = new BasicPromptsFileManager();
	public static PromptsFileManager getPromptsManager() {
		return promptsManager;
	}
	public static void setPromptsManager(PromptsFileManager newVal) {
		promptsManager = newVal;
	}
}
