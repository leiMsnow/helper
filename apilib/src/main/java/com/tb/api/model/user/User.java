package com.tb.api.model.user;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tb.api.model.ImageUrl;

import java.io.Serializable;
import java.util.List;

/**
 * 用户信息model
 * Created by zhangleilei on 15/7/3.
 */
public class User implements Serializable {
    // 用户id
    private String user_id;
    // 手机号码
    private String mobile_phone;
    // 用户免认证token
    private String freeauth_token;
    // 第三方账号类型：默认为0，1QQ 2微信 3淘宝 4微博 5当当 6京东 7美丽说
    private String thirdparty_access_type;
    // im绑定token串，用于聊天
    private String im_bind_token;
    // 昵称
    private String nick_name;
    // 用户头像
    private String portrait_url;

    private ImageUrl portraitUrl;
    //用户标签
    private String tags;
    //用户地址
    private String address;
    //加入的群数量
    private String joined_group_amount;
    //创建的群数量
    private String created_group_amount;
    //粉丝数量
    private String fans_amount;
    //关注数量
    private String focused_amount;
    //地址类型
    private String address_type;
    //孩子信息
    private String child_info;

    private List<Child> childInfo;
    //是否已经关注
    private boolean is_focused;

    public String getCreated_group_amount() {
        return created_group_amount;
    }

    public void setCreated_group_amount(String created_group_amount) {
        this.created_group_amount = created_group_amount;
    }

    /**
     * 返回用户加入和创建的圈子
     *
     * @return
     */
    public String getGroupAmount() {
        int joinedAmount = Integer.parseInt(joined_group_amount);
        int createdAmount = Integer.parseInt(created_group_amount);
        return String.valueOf(joinedAmount + createdAmount);
    }

    public boolean is_focused() {
        return is_focused;
    }

    public void setIs_focused(boolean is_focused) {
        this.is_focused = is_focused;
    }

    public void setPortrait_url(String portrait_url) {
        this.portrait_url = portrait_url;
    }

    public ImageUrl getPortraitUrl() {
        if (portraitUrl != null) {
            return portraitUrl;
        }
        if (!TextUtils.isEmpty(portrait_url)) {
            portraitUrl = JSON.parseObject(portrait_url,
                    new TypeReference<ImageUrl>() {
                    });
        }

        return portraitUrl;
    }

    public void setChild_info(String child_info) {
        this.child_info = child_info;
    }

    public List<Child> getChildInfo() {
        if (childInfo != null) {
            return childInfo;
        }
        if (!TextUtils.isEmpty(child_info)) {
            childInfo = JSON.parseObject(child_info,
                    new TypeReference<List<Child>>() {
                    });
        }
        return childInfo;
    }

    public String getJoined_group_amount() {
        return joined_group_amount;
    }

    public void setJoined_group_amount(String joined_group_amount) {
        this.joined_group_amount = joined_group_amount;
    }

    public String getFans_amount() {
        return fans_amount;
    }

    public void setFans_amount(String fans_amount) {
        this.fans_amount = fans_amount;
    }

    public String getFocused_amount() {
        return focused_amount;
    }

    public void setFocused_amount(String focused_amount) {
        this.focused_amount = focused_amount;
    }

    public String getAddress_type() {
        return address_type;
    }

    public void setAddress_type(String address_type) {
        this.address_type = address_type;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public String getFreeauth_token() {
        return freeauth_token;
    }

    public void setFreeauth_token(String freeauth_token) {
        this.freeauth_token = freeauth_token;
    }

    public String getThirdparty_access_type() {
        return thirdparty_access_type;
    }

    public void setThirdparty_access_type(String thirdparty_access_type) {
        this.thirdparty_access_type = thirdparty_access_type;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getIm_bind_token() {
        return im_bind_token;
    }

    public void setIm_bind_token(String im_bind_token) {
        this.im_bind_token = im_bind_token;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }


}
