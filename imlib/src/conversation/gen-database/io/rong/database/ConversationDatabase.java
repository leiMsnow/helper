package io.rong.database;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by DragonJ on 15/3/25.
 */
public class ConversationDatabase {
    static DraftDao mDraftDao;

    public static void init(Context context){
        SQLiteOpenHelper openHelper = new ConversationMaster.DevOpenHelper(context,"rong_conversation",null);
        ConversationMaster daoMaster = new ConversationMaster(openHelper.getWritableDatabase());
        ConversationSession session = daoMaster.newSession();
        mDraftDao = session.getDraftDao();
    }

    public static DraftDao getDraftDao() {

        if(mDraftDao == null)
            throw new ExceptionInInitializerError("RongConversationDatabase was not init");

        return mDraftDao;
    }
}
