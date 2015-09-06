package com.tongban.im.model;

/**
 * 设置孩子信息model
 * Created by fushudi on 2015/9/6.
 */
public class AddChildInfo {
    private String nick_name;
    private String birthday;
    private int sex;
    private String school;

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }
}
