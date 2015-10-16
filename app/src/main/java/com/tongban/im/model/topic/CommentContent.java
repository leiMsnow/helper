package com.tongban.im.model.topic;

import com.tongban.im.model.ImageUrl;

import java.util.List;

/**
 * 创建话题的内容实体类
 * Created by zhangleilei on 10/10/15.
 */
public class CommentContent {

    private String comment_content_text;
    private List<ImageUrl> comment_img_url;

    public String getComment_content_text() {
        return comment_content_text;
    }

    public void setComment_content_text(String topic_content) {
        this.comment_content_text = topic_content;
    }

    public List<ImageUrl> getComment_img_url() {
        return comment_img_url;
    }

    public void setComment_img_url(List<ImageUrl> comment_img_url) {
        this.comment_img_url = comment_img_url;
    }
}

