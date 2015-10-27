package com.tb.api.model;

import java.io.Serializable;

/**
 * 图片地址model
 * Created by zhangleilei on 8/20/15.
 */
public class ImageUrl implements Serializable{

    //小图
    private String min;
    //中图
    private String mid;
    //大图
    private String max;

    public ImageUrl() {
    }

    public ImageUrl(String min, String mid, String max) {
        this.min = min;
        this.mid = mid;
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }
}
