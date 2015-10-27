package com.tb.api.model.topic;

import com.tb.api.model.ImageUrl;

import java.util.List;

/**
 * 创建话题的内容实体类
 * Created by zhangleilei on 10/10/15.
 */
public class TopicContent   {

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
    private List<ImageUrl> topic_img_url;

    public List<ImageUrl> getTopic_img_url() {
        return topic_img_url;
    }

    public void setTopic_img_url(List<ImageUrl> topic_img_url) {
        this.topic_img_url = topic_img_url;
    }
}

