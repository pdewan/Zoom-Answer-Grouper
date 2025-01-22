package com.Chat_Cluster;

import java.awt.Desktop;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.DirectoryStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import org.apache.commons.configuration.PropertiesConfiguration;

import com.Chat_Filter.AnswerParser;
import com.Chat_Filter.LabelController;
import com.Chat_Filter.QuestionType;
import com.Chat_Filter.ZoomChatSegmenter;
import com.Chat_Filter.ZoomChatSegmenter.SingleChat;
import com.Chat_Group.GroupParser;
import com.Chat_Group.StudentGroup;
import com.Chat_View.ChatFoldersFinderFactory;
import com.Chat_View.ChatSegmentsFinderFactory;
import com.Chat_View.PromptChoiceManager;
import com.Chat_View.PromptChoiceView;
import com.Chat_View.PromptsChoiceManagerFactory;
import com.Chat_View.PromptsFileManagerFactory;
import com.Chat_View.TreeDisplay;

public class ExtensibleMain {
	private static final String DOCUMENTS_DIRECTORY = "Documents";
	private static final String ANSWER_GROUPER_DIRECTORY = "AnswerGrouper";
	private static final String OUTPUTS_DIRECTORY = "Outputs";
	private static final String SEGMENTATION_DIRECTORY = "Segmentation";
	private static final String PROMPTS_DIRECTORY = "Prompts";
	private static final String ZOOM_DIRECTORY = "Zoom";
	private static final String CHAT_FILE_NAME = "meeting_saved_chat.txt";
	private static final String CHAT_FILE_NAME_SUFFIX = "chat.txt";

	private static final String CONFIGURATION_FILE_NAME = "config.properties";

	private static Path getZoomChatDirectory() {
		return Paths.get(System.getProperty("user.home"), DOCUMENTS_DIRECTORY, ZOOM_DIRECTORY);

	}

	private static Path getAnswerDirectoryPath() {
		return Paths.get(System.getProperty("user.home"), DOCUMENTS_DIRECTORY, ANSWER_GROUPER_DIRECTORY);
	}

	private static Path getOutputsDirectoryPath() {
		return Paths.get(getAnswerDirectoryPath().toString(), OUTPUTS_DIRECTORY);
	}

	private static Path getSegmentationDirectoryPath() {
		return Paths.get(getAnswerDirectoryPath().toString(), SEGMENTATION_DIRECTORY);
	}

	private static Path getPromptsDirectoryPath() {
		return Paths.get(getAnswerDirectoryPath().toString(), PROMPTS_DIRECTORY);
	}

	private static List<String> getChatFileNames() {
//		File aParentFolder = getZoomChatDirectory().toFile();
		File aParentFolder = PropertiesManager.getZoomChatDirectory().toFile();
		List<File> aChatFolders = ChatFoldersFinderFactory.getChatFolderFinder().findChildren(aParentFolder);
		List<String> aChatFiles = new ArrayList();
		for (File aChatFolder : aChatFolders) {
			File[] aChildren = aChatFolder.listFiles();
			for (File aChild:aChildren) {
				String aChildName = aChild.getName();
				if (aChildName.endsWith(CHAT_FILE_NAME_SUFFIX)) {
					aChatFiles.add(aChild.getAbsolutePath());
				}
			}
//			String aChatFileName = aChatFolder.getAbsolutePath() + "/" + CHAT_FILE_NAME;
//			aChatFiles.add(aChatFileName);
		}
		return aChatFiles;
	}

	static boolean showTreeView = false;

	public static void main(String[] args) throws IOException {
		Path aConfigurationPath = PropertiesManager.getConfigurationFilePath();
		PropertiesConfiguration aPropertiesConfiguration = null;
		File aConfigurationFile = aConfigurationPath.toFile();
		if (aConfigurationFile.exists()) {
			try {
				aPropertiesConfiguration = new PropertiesConfiguration(aConfigurationFile);
			} catch (Exception e) {
				aPropertiesConfiguration = new PropertiesConfiguration();
			} 
		} else {
			aPropertiesConfiguration = new PropertiesConfiguration();
		}
		PropertiesManager.setPropertiesConfiguration(aPropertiesConfiguration);
//		try {
//			// Delete existing outputs
////            Path directoryToDelete = Paths.get(System.getProperty("user.home"), "Downloads", "outputs");
//			Path directoryToDelete = getOutputsDirectoryPath();
//
//			deleteDirectoryIfExists(directoryToDelete);
//		} catch (IOException e) {
//			System.err.println("Error deleting directory: " + e.getMessage());
//		}
//		try {
//			// Delete existing segmentations
////            Path directoryToDelete = Paths.get(System.getProperty("user.home"),ANSWER_GROUPER_DIRECTORY, "segmentation");
//			Path directoryToDelete = getSegmentationDirectoryPath();
//
//			deleteDirectoryIfExists(directoryToDelete);
//		} catch (IOException e) {
//			System.err.println("Error deleting directory: " + e.getMessage());
//		}
		QuestionType questionType = new QuestionType();
//		File aPromptsDirectory = getPromptsDirectoryPath().toFile();
		File aPromptsDirectory = PropertiesManager.getPromptsDirectory().toFile();

		PromptsFileManagerFactory.getPromptsManager().processPromptFiles(aPromptsDirectory);
		PromptsChoiceManagerFactory.getPromptChoiceManager().setPromptChoice(aPromptsDirectory, questionType);
		if (questionType.getType() == null) {
			System.err.println("No prompt choice taken");
			System.exit(-1);
		}
//        PromptChoiceView.promptChoice(questionType);
		GroupParser groupParser = new GroupParser();
		AnswerParser answerParser = new AnswerParser();
//        
//		String userRoot = System.getProperty("user.home");
//        String rawFilePath = userRoot + "/Downloads/meeting_saved_chat.txt";
		List<String> rawFilePaths = getChatFileNames();
		for (String rawFilePath : rawFilePaths) {
			File aParentFolder = ZoomChatSegmenter.segment(rawFilePath, 2);
			List<File> aSegmentationFiles = ChatSegmentsFinderFactory.getSegmentFilesFinder()
					.findChildren(aParentFolder);
			for (File aSegmentationFile : aSegmentationFiles) {
//			File rootDir = new File(userRoot + "/Downloads");
//			int segmentNum = OpenAI.getGPTAnswerForNewestSegmentation(rootDir, questionType);
				if (PropertiesManager.getShowChat()) {

				Desktop.getDesktop().edit(aSegmentationFile);
				}

				Path anOutputFilePath = ExtensibleOpenAI.getGPTAnswerForSegmentation(aSegmentationFile,
						aPromptsDirectory, questionType);
				Desktop.getDesktop().edit(anOutputFilePath.toFile());
				if (!showTreeView) {
					continue;
				}
				try {
					// Parse groups and answers from the files
					Hashtable<StudentGroup, List<SingleChat>> groupedAnswersTable = new Hashtable<>();

					// Map<Integer, StudentGroup> studentGroups = groupParser
					// .representationViewParse("data/Zoom-Chats/2023-09-19 12.55.01 Comp 524
					// Lectures Fall 2023/outputs/2.txt");
					// Map<String, String> studentAnswers = answerParser
					// .parse("data/Zoom-Chats/2023-09-19 12.55.01 Comp 524 Lectures Fall
					// 2023/segmentation/segment2.txt");

//				aSegmentationFile.getAbsolutePath();

					Map<Integer, StudentGroup> studentGroups = groupParser
							.representationViewParse(anOutputFilePath.toString());
					Map<String, String> studentAnswers = answerParser.parse(aSegmentationFile.getAbsolutePath());
//				
//				Map<Integer, StudentGroup> studentGroups = groupParser
//						.representationViewParse(rootDir + "/outputs/" + segmentNum + ".txt");
//				Map<String, String> studentAnswers = answerParser
//						.parse(rootDir + "/segmentation/segment" + segmentNum + ".txt");

					// Go through each parsed student answer and add it to the correct group in the
					// hashtable
					for (Map.Entry<Integer, StudentGroup> groupEntry : studentGroups.entrySet()) {
						StudentGroup group = groupEntry.getValue();
						for (Map.Entry<String, String> studentAnswer : studentAnswers.entrySet()) {
							if (group.getStudents().contains(studentAnswer.getKey())) {
								groupedAnswersTable.putIfAbsent(group, new ArrayList());
								SingleChat chat = new SingleChat(null, studentAnswer.getKey(),
										studentAnswer.getValue());
								groupedAnswersTable.get(group).add(chat);
							}
						}
					}
					// Instantiate and populate your Hashtable<StudentGroup, List<SingleChat>>
					// Hashtable<StudentGroup, List<SingleChat>> chatData = new Hashtable<>();
					// (Add data to chatData as necessary)

					// Build the JTree with the chat data
					new TreeDisplay(groupedAnswersTable);
					LabelController.labelCsvFileGenerator(groupedAnswersTable);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static void deleteDirectoryIfExists(Path dirPath) throws IOException {
		if (Files.exists(dirPath)) {
			// If directory exists, proceed with deleting its contents
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath)) {
				for (Path entry : stream) {
					if (Files.isDirectory(entry)) {
						// Recursively delete if it is a directory
						deleteDirectoryIfExists(entry);
					} else {
						// Delete the file
						Files.delete(entry);
					}
				}
			}
			// After all contents are deleted, delete the directory itself
			Files.delete(dirPath);
			System.out.println("Directory and all contents have been deleted.");
		} else {
			System.out.println("Directory does not exist, no action taken.");
		}
	}
}
