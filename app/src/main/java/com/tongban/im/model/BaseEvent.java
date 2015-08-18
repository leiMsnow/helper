package com.tongban.im.model;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.model.Conversation;

/**
 * EventBus事件回调类，主要是将参数封装
 * Created by zhangleilei on 15/7/14.
 */
public class BaseEvent {

    public static class DrawerLayoutMenu {
        private int icon;
        private String text;
        private boolean isSelected;
        private String targetId;
        private Conversation.ConversationType chatType;

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }

        public String getTargetId() {
            return targetId;
        }

        public void setTargetId(String targetId) {
            this.targetId = targetId;
        }

        public Conversation.ConversationType getChatType() {
            return chatType;
        }

        public void setChatType(Conversation.ConversationType chatType) {
            this.chatType = chatType;
        }

        public DrawerLayoutMenu(String text, String targetId, Conversation.ConversationType type, int icon, boolean isSelect) {
            this.text = text;
            this.targetId = targetId;
            this.chatType = type;
            this.icon = icon;
            this.isSelected = isSelect;
        }
    }


    /**
     * 加入群组的Event
     */
    public static class SearchGroupEvent {
        List<Group> groups = new ArrayList<>();

        public List<Group> getGroups() {
            return groups;
        }

        public void setGroups(List<Group> groups) {
            this.groups = groups;
        }
    }

    /**
     * 加入群组的Event
     */
    public static class JoinGroupEvent {
        String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    /**
     * 注册event
     */
    public static class RegisterEvent {

        public enum RegisterEnum {
            /**
             * 注册第一步，获取验证码
             */
            SMS_CODE,
            /**
             * 注册第二步，提交用户信息和验证码注册
             */
            REGISTER,
            /**
             * 找回密码，验证步骤
             */
            VERIFY_CODE,
            /**
             * 第三方注册，授权
             */
            THIRD_REG
        }

        //注册第一步返回的userid
        private String user_id;
        //注册第二步返回的验证id
        private String verify_id;
        //注册第三步返回的验证结果
        private String verify_type;
        private String freeauth_token;
        //注册步骤
        private RegisterEnum registerEnum;

        public RegisterEnum getRegisterEnum() {
            return registerEnum;
        }

        public void setRegisterEnum(RegisterEnum registerEnum) {
            this.registerEnum = registerEnum;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getVerify_id() {
            return verify_id;
        }

        public void setVerify_id(String verify_id) {
            this.verify_id = verify_id;
        }

        public String getVerify_type() {
            return verify_type;
        }

        public void setVerify_type(String verify_type) {
            this.verify_type = verify_type;
        }

        public String getFreeauth_token() {
            return freeauth_token;
        }

        public void setFreeauth_token(String freeauth_token) {
            this.freeauth_token = freeauth_token;
        }
    }

    /**
     * 话题Event
     */
    public static class TopicEvent {
        Topic topic;

        public Topic getTopic() {
            return topic;
        }

        public void setTopic(Topic topic) {
            this.topic = topic;
        }
    }

    /**
     * 群信息Event
     */
    public static class GroupInfoEvent {
        Group group;

        public Group getGroup() {
            return group;
        }

        public void setGroup(Group group) {
            this.group = group;
        }
    }

    /**
     * 群成员Event
     */
    public static class GroupMemberEvent {
        List<User> users;

        public List<User> getUsers() {
            return users;
        }

        public void setUsers(List<User> users) {
            this.users = users;
        }
    }

    /**
     * 重置密码Event
     */
    public static class PwdResetEvent {
        String result;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }

    /**
     * 用户信息Event
     */
    public static class UserInfoEvent {
        User user;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }

    /**
     * 选择标签Event
     */
    public static class LabelEvent{
        String label;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }


}
