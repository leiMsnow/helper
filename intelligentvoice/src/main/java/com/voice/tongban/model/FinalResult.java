package com.voice.tongban.model;

/**
 * Created by zhangleilei on 10/14/15.
 */
public class FinalResult {


    public static final int USER_QUESTION = 0;

    public static final int ANSWER = 1;

    private int finalType ;

    private String question;

    private MoreResults moreResults;

    public int getFinalType() {
        return finalType;
    }

    public void setFinalType(int finalType) {
        this.finalType = finalType;
    }

    public MoreResults getMoreResults() {
        return moreResults;
    }

    public void setMoreResults(MoreResults moreResults) {
        this.moreResults = moreResults;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


}
