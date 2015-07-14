package io.rong.imkit;

import io.rong.database.ConversationDatabase;
import io.rong.imkit.widget.provider.DefaultMessageItemProvider;
import io.rong.imkit.widget.provider.UnknownMessageItemProvider;

/**
 * Created by DragonJ on 15/3/24.
 */
public class ConversationContext extends RongContext {
    RongContext mRongContext;

    static ConversationContext sS;


    static public void init(RongContext context) {
        sS = new ConversationContext(context);
        ConversationDatabase.init(context);
    }

    private ConversationContext(RongContext context) {
        super(context);
        mRongContext = context;
        mRongContext.setDefaultMessageTemplate(new DefaultMessageItemProvider());
    }

    public static ConversationContext getInstance() {
        return sS;
    }
}
