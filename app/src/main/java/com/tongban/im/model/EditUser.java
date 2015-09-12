package com.tongban.im.model;

import java.io.Serializable;
import java.util.List;

/**
 * 修改用户信息model
 * Created by zhangleilei on 15/09/09.
 */
public class EditUser implements Serializable {

    // 昵称
    private String nick_name;

    // 用户头像
    private ImageUrl portrait_url;

    //孩子信息
    private List<AddChildInfo> updateChildInfoList;

    public List<AddChildInfo> getUpdateChildInfoList() {
        return updateChildInfoList;
    }

    public void setUpdateChildInfoList(List<AddChildInfo> updateChildInfoList) {
        this.updateChildInfoList = updateChildInfoList;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public ImageUrl getPortrait_url() {
        return portrait_url;
    }

    public void setPortrait_url(ImageUrl portrait_url) {
        this.portrait_url = portrait_url;
    }
}
