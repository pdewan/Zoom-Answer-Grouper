package com.Chat_View;

public class PromptsChoiceManagerFactory {
	static PromptChoiceManager promptChoiceManager = new BasicPromptChoiceManager();
	public static PromptChoiceManager getPromptChoiceManager() {
		return promptChoiceManager;
	}
	public static void setPromptChoiceManager(PromptChoiceManager newVal) {
		promptChoiceManager = newVal;
	}
}
