package com.tongban.im.model;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * 孩子信息model
 * Created by zhangleilei on 8/22/15.
 */
public class Child implements Serializable{

    private String nick_name;
    private String birthday;
    private String constellation;
    private String sex;
    private String school;
    private String age;

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

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getSex() {
        return sex;
    }
    public String StrSex() {
        if (TextUtils.isEmpty(sex)){
            return "1";
        }
        return sex.equals("1") ? "男" : "女";
    }


    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getAge() {
        if (TextUtils.isEmpty(age))
            return "0";
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
