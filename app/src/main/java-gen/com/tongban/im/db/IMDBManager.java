package com.tongban.im.db;

import android.content.Context;

import com.tongban.im.db.dao.DaoMaster;
import com.tongban.im.db.dao.DaoSession;
import com.tongban.im.db.helper.UpdateOpenHelper;

/**
 * tongban数据库管理类
 */
public class IMDBManager {

    private static IMDBManager instance;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    public static IMDBManager getInstance(Context context) {
        if (instance == null) {
            synchronized (IMDBManager.class) {
                if (instance == null) {
                    instance = new IMDBManager(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    private IMDBManager(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                UpdateOpenHelper helper = new UpdateOpenHelper(context, "im_db", null);
                daoMaster = new DaoMaster(helper.getWritableDatabase());
            }
            daoSession = daoMaster.newSession();
        }
    }

    public DaoMaster getDaoMaster() {
        return daoMaster;
    }

    public void setDaoMaster(DaoMaster daoMaster) {
        this.daoMaster = daoMaster;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public void setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
    }
}
