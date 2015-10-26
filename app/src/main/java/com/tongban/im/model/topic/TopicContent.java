package com.tongban.im.model.topic;

import com.tongban.im.model.ImageUrl;
import com.voice.tongban.model.BaseTopicContent;

import java.util.List;

/**
 * 创建话题的内容实体类
 * Created by zhangleilei on 10/10/15.
 */
public class TopicContent extends BaseTopicContent {

    private List<ImageUrl> topic_img_url;

    public List<ImageUrl> getTopic_img_url() {
        return topic_img_url;
    }

    public void setTopic_img_url(List<ImageUrl> topic_img_url) {
        this.topic_img_url = topic_img_url;
    }
}

