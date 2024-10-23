package com.Chat_View;

import java.io.File;

import com.Chat_Filter.QuestionType;

public interface PromptChoiceManager {
	public static String PROMPT_PROLOG = "PromptProlog";
	public static String PROMPT_EPILOG = "PromptEpilog";
    public void setPromptChoice(File aPromptsDirectory, QuestionType type);

}
