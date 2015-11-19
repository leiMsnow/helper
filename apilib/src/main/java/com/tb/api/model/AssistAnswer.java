package com.tb.api.model;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

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

        List<Knowledge> knowledges;

        public List<Knowledge> getKnowledges() {
            return knowledges;
        }

        public void setKnowledges(List<Knowledge> knowledges) {
            this.knowledges = knowledges;
        }

        public List<TalentInfo> getProducers() {
            return producers;
        }

        public void setProducers(List<TalentInfo> producers) {
            this.producers = producers;
        }
    }


}
