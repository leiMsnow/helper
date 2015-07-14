package io.rong.imkit.model;

import io.rong.imlib.model.Conversation;

/**
 * Created by jenny_zhou1980 on 15/4/11.
 */
public class ConversationInfo {
    Conversation.ConversationType conversationType;
    String targetId;

    ConversationInfo(){}

    public static ConversationInfo obtain(Conversation.ConversationType type,String id){
        ConversationInfo  info = new ConversationInfo();
        info.conversationType = type;
        info.targetId = id;
        return info;
    }

    public Conversation.ConversationType getConversationType(){
        return conversationType;
    }

    public String getTargetId(){
        return targetId;
    }
}
