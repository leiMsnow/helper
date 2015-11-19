package com.tb.api.model;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class Knowledge {
        String answers;

        KnowledgeAnswers knowledgeAnswerses;

        public void setAnswers(String answers) {
            this.answers = answers;
        }

        public KnowledgeAnswers getKnowledgeAnswerses() {
            if (knowledgeAnswerses == null) {
                if (!TextUtils.isEmpty(answers)) {
                    knowledgeAnswerses = JSON.parseObject(answers,
                            new TypeReference<KnowledgeAnswers>() {
                            });
                }
            }
            return knowledgeAnswerses;
        }


        
    }