package com.tongban.im.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.tongban.im.db.dao.DaoMaster;

/**
 * 数据库更新操作类
 */
public class UpdateOpenHelper extends DaoMaster.OpenHelper {

    public UpdateOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                //创建新表，注意createTable()是静态方法
                // SchoolDao.createTable(db, true);     

                // 加入新字段
                // db.execSQL("ALTER TABLE 'moments' ADD 'audio_path' TEXT;");  

                // TODO
                break;
        }
    }
}