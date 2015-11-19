package com.voice.tongban.model;

import com.tb.api.model.Knowledge;
import com.tb.api.model.TalentInfo;

/**
 * Created by zhangleilei on 10/14/15.
 */
public class FinalResult {

    // 用户提问
    public static final int USER_QUESTION = 0;
    // 没有答案
    public static final int ANSWER_ERROR = 1;
    // 文本类型
    public static final int ANSWER_TEXT = 2;

    public static final int ANSWER_TALENT = 3;

    public static final int ANSWER_KNOWLEDGES = 4;

    private int finalType;

    private String question;

    private String errorInfo;

    private MoreResults moreResults;

    private TalentInfo talentInfo;

    private Knowledge knowledgeAnswers;

    public Knowledge getKnowledgeAnswers() {
        return knowledgeAnswers;
    }

    public void setKnowledgeAnswers(Knowledge knowledgeAnswers) {
        this.knowledgeAnswers = knowledgeAnswers;
    }

    public TalentInfo getTalentInfo() {
        return talentInfo;
    }

    public void setTalentInfo(TalentInfo talentInfo) {
        this.talentInfo = talentInfo;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }


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
