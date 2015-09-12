package com.tongban.im.model;

/**
 * 修改孩子信息
 * Created by fushudi on 2015/9/11.
 */
public class UpdateChildInfo {
    //性别
    private int childSex;
    //出生日期
    private String birthday;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getChildSex() {
        return childSex;
    }

    public void setChildSex(int childSex) {
        this.childSex = childSex;
    }
}
