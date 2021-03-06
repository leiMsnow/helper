package com.tongban.im.db.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.tongban.im.db.bean.UserTable;
import com.tongban.im.db.bean.GroupTable;

import com.tongban.im.db.dao.UserTableDao;
import com.tongban.im.db.dao.GroupTableDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig userTableDaoConfig;
    private final DaoConfig groupTableDaoConfig;

    private final UserTableDao userTableDao;
    private final GroupTableDao groupTableDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        userTableDaoConfig = daoConfigMap.get(UserTableDao.class).clone();
        userTableDaoConfig.initIdentityScope(type);

        groupTableDaoConfig = daoConfigMap.get(GroupTableDao.class).clone();
        groupTableDaoConfig.initIdentityScope(type);

        userTableDao = new UserTableDao(userTableDaoConfig, this);
        groupTableDao = new GroupTableDao(groupTableDaoConfig, this);

        registerDao(UserTable.class, userTableDao);
        registerDao(GroupTable.class, groupTableDao);
    }
    
    public void clear() {
        userTableDaoConfig.getIdentityScope().clear();
        groupTableDaoConfig.getIdentityScope().clear();
    }

    public UserTableDao getUserTableDao() {
        return userTableDao;
    }

    public GroupTableDao getGroupTableDao() {
        return groupTableDao;
    }

}
