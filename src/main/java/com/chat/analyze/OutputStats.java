package com.chat.analyze;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class OutputStats {
	public int numInputs;
	public int numOutputs;
    public Date dateAsked;
    public long timeAsked;
    public long time;  // in seconds
    public long qaLength; // from input
//    public long totalInputLength;
    public long totalOutputLength;
    public String questionType;
    public String predictedQuestionAndAnswer;  // retained for internal use
    public String predictedQuestion;           // extracted question part
    public String predictedAnswer;             // extracted answer part


    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String escapedQuestion = predictedQuestion
                .replace("\t", " \\t ")
                .replace("\n", " \\n ");

        String escapedAnswer = predictedAnswer
                .replace("\t", " \\t ")
                .replace("\n", " \\n ");

        return String.join("\t",
            String.valueOf(timeAsked),
            sdf.format(dateAsked),
            String.valueOf(qaLength),
            String.valueOf(totalOutputLength),
            String.valueOf(time),
            questionType,
            escapedQuestion,
            escapedAnswer
        );
    }

}
