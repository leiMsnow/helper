package com.tb.api.model;

import java.util.List;

/**
 * 问答结果
 * Created by zhangleilei on 11/16/15.
 */
public class AssistAnswer {


    Answers answers;
    String session_id;

    public Answers getAnswers() {
        return answers;
    }

    public void setAnswers(Answers answers) {
        this.answers = answers;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public class Answers {
        List<TalentInfo> producers;

        public List<TalentInfo> getProducers() {
            return producers;
        }

        public void setProducers(List<TalentInfo> producers) {
            this.producers = producers;
        }
    }
}
