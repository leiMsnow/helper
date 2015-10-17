package com.tongban.im.model.discover;

import android.text.TextUtils;

import java.util.List;

/**
 * 发现页数据model
 * Created by Cheney on 15/8/14.
 */
public class Discover {
    // 标题
    private String title;
    // 描述
    private String description;
    // 图片
    private List<ImgMap> img_map;
    //
    private String sort;
    // 提示语
    private String soft_word;
    // 专题id
    private String topic_id;
    // 组件名字
    private String component_name;
    // 组件id
    private String component_id;

    private int[] collect_amount;

    public void setCollect_amount(int[] collect_amount) {
        this.collect_amount = collect_amount;
    }

    public class ImgMap {
        // 图片地址
        private String img_url;
        // 跳转地址
        private String link_url;

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getLink_url() {
            return link_url;
        }

        public void setLink_url(String link_url) {
            this.link_url = link_url;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ImgMap> getImg_map() {
        return img_map;
    }

    public void setImg_map(List<ImgMap> img_map) {
        this.img_map = img_map;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSoft_word() {
        return soft_word;
    }

    public void setSoft_word(String soft_word) {
        this.soft_word = soft_word;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public int getCollect_amount() {
        if (collect_amount != null) {
            int collectCount = 0;
            for (int i = 0; i < collect_amount.length; i++) {
                collectCount += collect_amount[i];
            }
            return collectCount;
        }
        return 0;
    }

    public String getComponent_name() {
        return component_name;
    }

    public void setComponent_name(String component_name) {
        this.component_name = component_name;
    }

    public String getComponent_id() {
        return component_id;
    }

    public void setComponent_id(String component_id) {
        this.component_id = component_id;
    }
}
