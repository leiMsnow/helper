package com.tb.api.model;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tb.api.model.user.User;

/**
 * 达人信息
 * Created by zhangleilei on 11/10/15.
 */
public class TalentInfo {


    private String producer_name;
    private String producer_id;
    private String tags;
    private float score;
    private String producer_desc;
    private ProducerDesc producerDesc;
    private String bg_url;
    private String user_id;

    private User user;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public ProducerDesc getProducer_desc() {
        if (producerDesc != null) {
            return producerDesc;
        }
        if (!TextUtils.isEmpty(producer_desc)) {
            try {

                producerDesc = JSON.parseObject(producer_desc, new TypeReference<ProducerDesc>() {
                });

            } catch (Exception e) {

            }
        }

        return producerDesc;
    }

    public void setProducer_desc(String producer_desc) {
        this.producer_desc = producer_desc;
    }

    public String getBg_url() {
        return bg_url;
    }

    public void setBg_url(String bg_url) {
        this.bg_url = bg_url;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setProducer_name(String producer_name) {
        this.producer_name = producer_name;
    }

    public void setProducer_id(String producer_id) {
        this.producer_id = producer_id;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getProducer_name() {
        return producer_name;
    }

    public String getProducer_id() {
        return producer_id;
    }

    public String getTags() {
        return tags;
    }
}
