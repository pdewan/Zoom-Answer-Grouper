package com.Chat_Cluster;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.Chat_Filter.QuestionType;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

public class OpenAI {

        public static String prompt = "As a teaching assistant in an advanced programming language course, your task is to cluster students' answers to a question asked in the class, finding a representation answer for each group. The representation answer must contains all the information of other answers within that group. You could put one student in one group if their answer is unique. Notice, the students answered the question via Zoom chat. Therefore, it will be in the format of Zoom chat.\n"
                        + //
                        "Please don't provide extra information. Please ignore the chat of the instructor, Parsun Dewan, and follow the output format below:\n"
                        + //
                        "\n" + //
                        "[group 1]: [representation answer student name, list of other students' name]\n" + //
                        "[group 2]: [representation answer student name, list of other students' name]\n" + //
                        "...\n" + //
                        "\n" + //
                        "Students' answers:\n";

        public static String conceptualPrompt = "As a teaching assistant in an advanced programming language course, your task is to cluster students' answers to a conceptual question asked in the class. First, find all the unique points made by all the students. Second, group them based on the points they made. For each group, find a representation answer. The representation answer must contains all the points of other answers within that group. Make the representation answer be the first one in the group. You could put one student in one group if their answer is unique. Notice, the students answered the question via Zoom chat. Therefore, it will be in the format of Zoom chat.\n"
                        + //
                        "Please don't provide extra information. Please ignore the chat of the instructor, Parsun Dewan, and follow the output format below:\n"
                        + //
                        "\n" + //
                        "[group 1]: [representation answer student name, list of other students' name]\n" + //
                        "[group 2]: [representation answer student name, list of other students' name]\n" + //
                        "...\n" + //
                        "\n" + //
                        "Students' answers:\n";

        static String prompt_question_type = "As a teaching assistant in an advanced programming language course, your task is to cluster students' answers to a question asked in the class, finding a representation answer for each group. The representation answer must contains all the information of other answers within that group. You could put one student in one group if their answer is unique. Notice, the students answered the question via Zoom chat. Therefore, it will be in the format of Zoom chat.\n"
                        + //
                        "Please don't provide extra information. Please ignore the chat of the instructor, Parsun Dewan, and follow the output format below:\n"
                        + //
                        "\n" + //
                        "[group 1]: [representation answer student name, list of other students' name]\n" + //
                        "[group 2]: [representation answer student name, list of other students' name]\n" + //
                        "...\n" + //
                        "\n" + //
                        "Given question type:\n" + //
                        // "Code tracing.\n" + //Segment1
                        // "Concept.\n" + //Segment2
                        // "Concept.\n" + //Segment3"
                        // "Code tracing.\n" + //Segment4
                        // "Code tracing.\n" + //Segment5
                        // "Code tracing.\n" + //Segment6
                        // "Write code.\n" + //Segment7
                        // "Concept.\n" + //Segment8
                        // "Write code.\n" + //Segment9
                        "Concept.\n" + //Segment10
                        "Students' answers:\n";

        public static String prompt_question = "As a teaching assistant in an advanced programming language course, your task is to cluster students' answers to a question asked in the class, finding a representation answer for each group. The representation answer must contains all the information of other answers within that group. You could put one student in one group if their answer is unique. Notice, the students answered the question via Zoom chat. Therefore, it will be in the format of Zoom chat.\n"
                        + //
                        "Please don't provide extra information. Please ignore the chat of the instructor, Parsun Dewan, and follow the output format below:\n"
                        + //
                        "\n" + //
                        "[group 1]: [representation answer student name, list of other students' name]\n" + //
                        "[group 2]: [representation answer student name, list of other students' name]\n" + //
                        "...\n" + //
                        "\n" + //
                        "Given question:\n" + //
                        // "Formula? Put yourself in the shoes of an ML program.\n" + //Segment1
                        // "Why support instance methods in Java? (Hint: Why support new?)\n" + //Segment2
                        // "What are design patterns? Why study them, that is, why were they invented?\n" + //Segment3"
                        // "Try to interpret left box in English based on intuition and syntax and knowledge gained from previous slides. Translate these lines into a sequence of English sentences.\n" + //Segment4
                        // "What will be the results?\n" + //Segment5
                        // "What is the output when you ask for another result?\n" + //Segment6
                        // "Fill in the ????\n" + //Segment7
                        // "Difference between [] and () structuring operations? Why the error?\n" + //Segment8
                        // "Give the actual parameter that should be passed to printSelectedCutoffs to simulate printTotalGreaterThanFinalCutoffs.\n" + //Segment9
                        "Which of the above is true and why?\n" + //Segment10
                        "Students' answers:\n";

        public static String prompt_answer = "As a teaching assistant in an advanced programming language course, your task is to cluster students' answers to a question asked in the class, finding a representation answer for each group. The representation answer must contains all the information of other answers within that group. You could put one student in one group if their answer is unique. Notice, the students answered the question via Zoom chat. Therefore, it will be in the format of Zoom chat.\n"
                        + //
                        "Please don't provide extra information. Please ignore the chat of the instructor, Parsun Dewan, and follow the output format below:\n"
                        + //
                        "\n" + //
                        "[group 1]: [representation answer student name, list of other students' name]\n" + //
                        "[group 2]: [representation answer student name, list of other students' name]\n" + //
                        "...\n" + //
                        "\n" + //
                        "Correct answer:\n" + //
                        // "Total >= 60 || (Total, Final) >= (40, 80)\n" +  //segment1
                        // "Java static methods cannot be put in interfaces (Mesa module methods could be put in interfaces). Java static methods cannot be overridden (Smalltalk class methods could be overridden)\n" + //segment2
                        // "Difficult to decompose programs into classes (modules and ADTs) on our own. A set of participating class kinds with prescribed roles  communicating with each other in a prescribed way that is independent of any specific application is a design pattern. The pattern names each kind of participating class andspecifies the abstract aspects of  the methods it defines and calls.\n" + //segment3
                        // "Facts: John’s father is Joe. Joe’s father is Jack. Mary’s father is Jack. Alice’s father is Bob. Ann’s mother is Mary. John’s mother is Alice. John’s father is Joe. Joe’s father is Jack. Mary’s father is Jack. Alice’s father is Bob. Ann’s mother is Mary. John’s mother is Alice. Rules: Person’s grandfather is Grandfather if (Person’s father is Father and Father’s father is Grandfather) or (Person’s mother is Mother and Mother’s father is Grandfather)\n" + //segment4
                        // "Person = \"John\",\n" + "Father = \"Joe\" :-;\n" + "Person = \"Ann\",\n" + "Mother = \"Mary\".\n" + //segment5
                        // "Infinite recursion!\n" + //segment6
                        // "append([], ListToBeAppended, ListToBeAppended).\n" + "append([Head|Tail], ListToBeAppended, [Head|AppendedTail]) :- \n" + "\tappend(Tail, ListToBeAppended, AppendedTail).\n" + //segment7
                        // "[] creates an arbitrary sized list with elements constrained to be of the same type. If T is the element type then the type of the list is T list. () creates a fixed size tuple with elements of arbitrary type. If T1, .. TN are the types of the elements then the type of the tuple is T1*T2*…TN.,\n" + //segment8"
                        // "fun totalGreaterThanFinal (total, final) = total > final;\n" + "fun finalGreaterThanTotal (total, final) = final > total;\n" + //segment9
                        "Tree of tables to support multiple returned functions\n" + //segment10 
                        "Students' answers:\n";

        public static String prompt_question_answer = "As a teaching assistant in an advanced programming language course, your task is to cluster students' answers to a question asked in the class, finding a representation answer for each group. The representation answer must contains all the information of other answers within that group. You could put one student in one group if their answer is unique. Notice, the students answered the question via Zoom chat. Therefore, it will be in the format of Zoom chat.\n"
                        + //
                        "Please don't provide extra information. Please ignore the chat of the instructor, Parsun Dewan, and follow the output format below:\n"
                        + //
                        "\n" + //
                        "[group 1]: [representation answer student name, list of other students' name]\n" + //
                        "[group 2]: [representation answer student name, list of other students' name]\n" + //
                        "...\n" + //
                        "\n" + //
                        "Given question:\n" + //
                        // "Formula? Put yourself in the shoes of an ML program.\n" + //Segment1
                        // "Why support instance methods in Java? (Hint: Why support new?)\n" + //Segment2
                        "What are design patterns? Why study them, that is, why were they invented?\n" + //Segment3"
                        // "Try to interpret left box in English based on intuition and syntax and knowledge gained from previous slides. Translate these lines into a sequence of English sentences.\n" + //Segment4
                        // "What will be the results?\n" + //Segment5
                        // "What is the output when you ask for another result?\n" + //Segment6
                        // "Fill in the ????\n" + //Segment7
                        // "Difference between [] and () structuring operations? Why the error?\n" + //Segment8
                        // "Give the actual parameter that should be passed to printSelectedCutoffs to simulate printTotalGreaterThanFinalCutoffs.\n" + //Segment9
                        // "Which of the above is true and why?\n" + //Segment10
                        "Given answer\n" + //
                        // "Total >= 60 || (Total, Final) >= (40, 80)\n" +  //segment1
                        // "Java static methods cannot be put in interfaces (Mesa module methods could be put in interfaces). Java static methods cannot be overridden (Smalltalk class methods could be overridden)\n" + //segment2
                        "Difficult to decompose programs into classes (modules and ADTs) on our own. A set of participating class kinds with prescribed roles  communicating with each other in a prescribed way that is independent of any specific application is a design pattern. The pattern names each kind of participating class andspecifies the abstract aspects of  the methods it defines and calls.\n" + //segment3
                        // "Facts: John’s father is Joe. Joe’s father is Jack. Mary’s father is Jack. Alice’s father is Bob. Ann’s mother is Mary. John’s mother is Alice. John’s father is Joe. Joe’s father is Jack. Mary’s father is Jack. Alice’s father is Bob. Ann’s mother is Mary. John’s mother is Alice. Rules: Person’s grandfather is Grandfather if (Person’s father is Father and Father’s father is Grandfather) or (Person’s mother is Mother and Mother’s father is Grandfather)\n" + //segment4
                        // "Person = \"John\",\n" + "Father = \"Joe\" :-;\n" + "Person = \"Ann\",\n" + "Mother = \"Mary\".\n" + //segment5
                        // "Infinite recursion!\n" + //segment6
                        // "append([], ListToBeAppended, ListToBeAppended).\n" + "append([Head|Tail], ListToBeAppended, [Head|AppendedTail]) :- \n" + "\tappend(Tail, ListToBeAppended, AppendedTail).\n" + //segment7
                        // "[] creates an arbitrary sized list with elements constrained to be of the same type. If T is the element type then the type of the list is T list. () creates a fixed size tuple with elements of arbitrary type. If T1, .. TN are the types of the elements then the type of the tuple is T1*T2*…TN.,\n" + //segment8"
                        // "fun totalGreaterThanFinal (total, final) = total > final;\n" + "fun finalGreaterThanTotal (total, final) = final > total;\n" + //segment9
                        // "Tree of tables to support multiple returned functions\n" + //segment10 
                        "Students' answers:\n";

        public static void main(String... args) {

                String tempPrompt = prompt_question_answer;
                File dir = new File("/Users/shanw25/College/Research/DEWAN_Research/Code-Clustering/data/Zoom-Chats-experiment/question-answer");
                File[] folderList = dir.listFiles();
                for (File folder : folderList) {
                        if (folder.isDirectory() && folder.getName().equals("segmentation")) {
                                
                                File[] segmentations = folder.listFiles();
                                for (File segment : segmentations) {
                                        if(segment.getName().equals("segment10.txt")){
                                                try {
                                                        List<String> lines = Files.readAllLines(
                                                                        Paths.get(segment.getPath()));
                                                        tempPrompt = prompt_question_answer;
                                                        for (String line : lines) {
                                                                tempPrompt += line + "\n";
                                                        }
                                                        String fileName = String.valueOf(segment.getName().charAt(segment.getName().length() -5));
                                                        getCompletionRequest(tempPrompt, dir, fileName);
                                                } catch (IOException e) {
                                                        e.printStackTrace();
                                                }
                                        }
                                }
                        }
                }
        }

        public static void getGPTAnswer(File classDir) {
                String tempPrompt;
                if (classDir.isDirectory()) {
                        File[] folderList = classDir.listFiles();
                        for (File folder : folderList) {
                                if (folder.isDirectory() && folder.getName().equals("segmentation")) {
                                        File[] segmentations = folder.listFiles();
                                        for (File segment : segmentations) {
                                                try {
                                                        List<String> lines = Files.readAllLines(
                                                                        Paths.get(segment.getPath()));
                                                        tempPrompt = prompt;
                                                        for (String line : lines) {
                                                                tempPrompt += line + "\n";
                                                        }
                                                        String fileName = String.valueOf(segment.getName().charAt(segment.getName().length() -5));
                                                        getCompletionRequest(tempPrompt, classDir, fileName);
                                                } catch (IOException e) {
                                                        e.printStackTrace();
                                                }
                                        }
                                }
                        }
                }
        }

        public static int getGPTAnswerForNewestSegmentation(File classDir, QuestionType type) throws IOException {
                Path filePath = Paths.get(System.getProperty("user.home"), "Downloads", "prompts", type.getType() + ".txt");
                
                // Read all bytes from the file
                byte[] fileBytes = Files.readAllBytes(filePath);

                // Convert the bytes to a String
                String targetPrompt = new String(fileBytes, StandardCharsets.UTF_8);
                System.out.println(targetPrompt);
                if(type.getType().equals("Conceptual")){
                        targetPrompt = conceptualPrompt;
                }
                String tempPrompt;
                int retVal = 1;
                if (classDir.isDirectory()) {
                        File folder = new File(classDir.getAbsolutePath() + "\\segmentation");
                        // File[] folderList = classDir.listFiles();
                        // for (File folder : folderList) {
                        //         if (folder.isDirectory() && folder.getName().equals("segmentation")) {
                                        File[] segmentations = folder.listFiles();
                                        File segmentation = segmentations[0];
                                        String regex = "(\\d+)\\.txt$";
                                        Pattern pattern = Pattern.compile(regex);
                                        int previousNum = 1;
                                        for(File a : segmentations){
                                                Matcher matcher = pattern.matcher(a.getName());
                                                if(matcher.find()){
                                                        String numbertemp = matcher.group(1);
                                                        int numberStr = Integer.parseInt(numbertemp);
                                                        if(numberStr > previousNum){
                                                                retVal = numberStr;
                                                                previousNum = numberStr;
                                                                segmentation = a;
                                                        }
                                                }
                                        }
                                        try {
                                                String tempPath = segmentation.getPath();
                                                Path tempPathsGet = Paths.get(tempPath);
                                                
                                                List<String> lines = Files.readAllLines(tempPathsGet, StandardCharsets.ISO_8859_1);
                                                tempPrompt = targetPrompt;
                                                for (String line : lines) {
                                                        tempPrompt += line + "\n";
                                                }
                                                String fileName = String.valueOf(retVal);
                                                getCompletionRequest(tempPrompt, classDir, fileName);
                                        } catch (IOException e) {
                                                e.printStackTrace();
                                        }
                                }
                //         }
                // }
                return retVal;
        }


        private static void getCompletionRequest(String prompt, File classDir, String fileName) {
                String token = System.getenv("OPENAI_API_KEY_DEWAN");
                OpenAiService service = new OpenAiService(token, Duration.ofSeconds(180));
                List<ChatMessage> chatMessageList = new ArrayList<>();
                String systemInstuction = "You are a intelligent assistant.";
                ChatMessage systemMsg = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemInstuction);
                chatMessageList.add(systemMsg);
                ChatMessage userMsg = new ChatMessage(ChatMessageRole.USER.value(), prompt);
                chatMessageList.add(userMsg);
                ChatCompletionRequest completionRequest = ChatCompletionRequest
                                .builder()
                                .model("gpt-4-1106-preview")
                                .messages(chatMessageList)
                                .n(1)
                                .stop(null)
                                .temperature(0.0)
                                .build();

                String result = service.createChatCompletion(completionRequest).getChoices().get(0).getMessage().getContent();
                Path filePath = Paths.get(classDir + "/outputs/" + fileName + ".txt");
                Path dirPath = Paths.get(classDir + "/outputs/");
                if(!Files.exists(dirPath)){
                        try{
                                Files.createDirectory(dirPath);
                        }catch(IOException e){
                                e.printStackTrace();
                        }
                }
                try{
                        Files.createFile(filePath);
                        FileWriter writer = new FileWriter(filePath.toString());
                        writer.write(result);
                        writer.close();
                }catch(IOException e){
                        e.printStackTrace();
                }
        }


        private static void getCompletionRequestPrint(String prompt, File classDir, char fileName) {
                String token = System.getenv("OPENAI_API_KEY_DEWAN");
                OpenAiService service = new OpenAiService(token, Duration.ofSeconds(180));
                List<ChatMessage> chatMessageList = new ArrayList<>();
                String systemInstuction = "You are a intelligent assistant.";
                ChatMessage systemMsg = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemInstuction);
                chatMessageList.add(systemMsg);
                ChatMessage userMsg = new ChatMessage(ChatMessageRole.USER.value(), prompt);
                chatMessageList.add(userMsg);
                ChatCompletionRequest completionRequest = ChatCompletionRequest
                                .builder()
                                .model("gpt-4-1106-preview")
                                .messages(chatMessageList)
                                .n(1)
                                .stop(null)
                                .temperature(0.0)
                                .build();

                String result = service.createChatCompletion(completionRequest).getChoices().get(0).getMessage().getContent();
        }
}
