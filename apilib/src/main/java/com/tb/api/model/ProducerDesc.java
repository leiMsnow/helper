package com.tb.api.model;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.List;

/**
 * Created by zhangleilei on 11/13/15.
 */
public class ProducerDesc {


    String desc;
    String skills;
    List<Skills> skillsData;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Skills> getSkills() {
        if (skillsData != null) {
            return skillsData;
        }
        if (!TextUtils.isEmpty(skills)) {
            skillsData = JSON.parseObject(skills,
                    new TypeReference<List<Skills>>() {
                    });
        }
        return skillsData;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }
}
