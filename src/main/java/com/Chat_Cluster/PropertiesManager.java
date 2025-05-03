package com.Chat_Cluster;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class PropertiesManager {
	private static final String SHOW_CHAT = "ShowChat";
	private static boolean DEFAULT_SHOW_CHAT = true;
	private static final String RESEGMENT = "Resegment";
	private static boolean DEFAULT_RESEGMENT = true;
	private static String SHOW_PROMPT = "ShowPrompt";
	private static boolean DEFAULT_SHOW_PROMPT = false;
	private static final String LATEST_CHAT = "LatestChat";
	private static final boolean DEFAULT_LATEST_CHAT = true;
	private static final String DOCUMENTS = "Documents";
	private static final String ANSWER_GROUPER = "AnswerGrouper";
	private static final String OUTPUTS = "Outputs";
	private static final String SEGMENTATION = "Segmentation";
	private static final String PROMPTS = "Prompts";
	private static final String ZOOM = "Zoom";
	private static final String CHAT_FILE = "meeting_saved_chat.txt";
	private static final String CONFIGURATION_FILE_NAME = "config.properties";
	private static PropertiesConfiguration propertiesConfiguration;
	private static final String INSTRUCTOR_NAME = "InstructorName";
	private static String DEFAULT_INSTRUCTOR_NAME = "Prasun Dewan";
	
	
	public static PropertiesConfiguration getPropertiesConfiguration() {
		return propertiesConfiguration;
	}

	public static void setPropertiesConfiguration(PropertiesConfiguration newVal) {
		PropertiesManager.propertiesConfiguration = newVal;
	}

	public static Path getConfigurationFilePath() {
		return Paths.get(System.getProperty("user.home"), DOCUMENTS,  ANSWER_GROUPER, CONFIGURATION_FILE_NAME);

	}	
	private static Path getDefaultZoomChatDirectory() {
		return Paths.get(System.getProperty("user.home"), DOCUMENTS,  ZOOM);

	}	
	private static Path getDefaultAnswerDirectoryPath() {
		return Paths.get(System.getProperty("user.home"), DOCUMENTS,  ANSWER_GROUPER);
	}
//	private static Path getDefaultOutputsDirectoryPath) {
//		return Paths.get(getDefaultAnswerDirectoryPath().toString(), OUTPUTS);	
//	}
//	private static Path getDefaultSegmentationDirectoryPath() {
//		return Paths.get(getDefaultAnswerDirectoryPath().toString(), SEGMENTATION);	
//	}
	private static Path getDefaultPromptsDirectoryPath() {
		return Paths.get(getDefaultAnswerDirectoryPath().toString(), PROMPTS);
	}
	public static Path getZoomChatDirectory() {
		String aFileName = propertiesConfiguration.getString(ZOOM, getDefaultZoomChatDirectory().toString());
		return Paths.get(aFileName);
	}
	public static Path getPromptsDirectory() {
		String aFileName = propertiesConfiguration.getString(PROMPTS, getDefaultPromptsDirectoryPath().toString());
		return Paths.get(aFileName);
	}
	public static String getSegmentationDirectory (String aDefault) {
		if (propertiesConfiguration == null) {
			return aDefault;
		}
		String aFileName = propertiesConfiguration.getString(SEGMENTATION, aDefault);
		return aFileName;
	}
	public static String getOutputsDirectory (String aDefault) {
		if (propertiesConfiguration == null) {
			return aDefault;
		}
		String aFileName = propertiesConfiguration.getString(OUTPUTS, aDefault);
		return aFileName;
	}
	public static boolean getLatestChat() {
		if (propertiesConfiguration == null) {
			return DEFAULT_LATEST_CHAT;
		}
		return propertiesConfiguration.getBoolean(LATEST_CHAT, DEFAULT_LATEST_CHAT);
	}
	public static boolean getResegment() {
		if (propertiesConfiguration == null) {
			return DEFAULT_RESEGMENT;
		}
		return propertiesConfiguration.getBoolean(RESEGMENT, DEFAULT_RESEGMENT);
	}
	public static boolean getShowChat() {
		if (propertiesConfiguration == null) {
			return DEFAULT_SHOW_CHAT;
		}
		return propertiesConfiguration.getBoolean(SHOW_CHAT, DEFAULT_SHOW_CHAT);
	}
	public static boolean getShowPrompt() {
		if (propertiesConfiguration == null) {
			return DEFAULT_SHOW_PROMPT;
		}
		return propertiesConfiguration.getBoolean(SHOW_PROMPT, DEFAULT_SHOW_PROMPT);
	}
	
	public static String getInstructorName() {
		if (propertiesConfiguration == null) {
			return DEFAULT_INSTRUCTOR_NAME;
		}
		return propertiesConfiguration.getString(INSTRUCTOR_NAME, DEFAULT_INSTRUCTOR_NAME);

	}
	
}
