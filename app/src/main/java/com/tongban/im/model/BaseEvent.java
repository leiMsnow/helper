package com.tongban.im.model;

import android.support.v4.widget.DrawerLayout;

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
}
