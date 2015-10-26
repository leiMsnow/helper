package com.voice.tongban.model;

/**
 * 创建话题的内容实体类
 * Created by zhangleilei on 10/10/15.
 */
public class BaseTopicContent {

    private String topic_content_text;
    private String topic_content_voice;

    public String getTopic_content_text() {
        return topic_content_text;
    }

    public void setTopic_content_text(String topic_content) {
        this.topic_content_text = topic_content;
    }

    public String getTopic_content_voice() {
        return topic_content_voice;
    }

    public void setTopic_content_voice(String topic_content_voice) {
        this.topic_content_voice = topic_content_voice;
    }

}

