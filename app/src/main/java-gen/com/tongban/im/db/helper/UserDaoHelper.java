package com.tongban.im.db.helper;

import android.content.Context;
import android.text.TextUtils;

import com.tongban.im.api.UserApi;
import com.tongban.im.db.IMDBManager;
import com.tongban.im.db.bean.UserTable;
import com.tongban.im.db.dao.UserTableDao;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * 用户数据库操作类
 * Created by zhangleilei on 15/7/30.
 */
public class UserDaoHelper extends BaseDaoHelper<UserTableDao, UserTable> {

    private static UserDaoHelper helper;

    private UserDaoHelper(Context context) {
        super(IMDBManager.getInstance(context).getDaoSession().getUserTableDao());
    }

    public static UserDaoHelper get(Context context) {
        if (helper == null) {
            synchronized (UserDaoHelper.class) {
                if (helper == null) {
                    helper = new UserDaoHelper(context);
                }
            }
        }
        return helper;
    }

    @Override
    protected UserTable getDataInfoById(String id) {
        if (tableDao != null && !TextUtils.isEmpty(id)) {
            return tableDao.load(id);
        }
        return null;
    }

    @Override
    protected boolean hasKeyById(String id) {
        if (tableDao == null || TextUtils.isEmpty(id)) {
            return false;
        }
        QueryBuilder<UserTable> qb = tableDao.queryBuilder();
        qb.where(UserTableDao.Properties.User_id.eq(id));
        long count = qb.buildCount().count();
        return count > 0 ? true : false;
    }
}
