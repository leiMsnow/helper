package com.tongban.im.model;

import io.rong.imlib.model.Conversation;

/**
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
    public static class JoinGroupEvent {

    }

    /**
     * 注册event
     */
    public static class RegisterEvent {

        public enum RegisterEnum {
            /**
             * 注册第一步，填写用户信息
             */
            REG,
            /**
             * 第三方注册，授权
             */
            THIRD_REG,
            /**
             * 注册第二步，根据userid获取验证码
             */
            FETCH,
            /**
             * 注册第三步，校验验证码
             */
            EXAM
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

}
