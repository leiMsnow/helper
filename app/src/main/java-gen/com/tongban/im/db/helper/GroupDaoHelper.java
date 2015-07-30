package com.tongban.im.db.helper;

import android.content.Context;
import android.text.TextUtils;

import com.tongban.im.db.IMDBManager;
import com.tongban.im.db.bean.GroupTable;
import com.tongban.im.db.dao.GroupTableDao;
import com.tongban.im.db.dao.UserTableDao;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * 用户数据库操作类
 * Created by zhangleilei on 15/7/30.
 */
public class GroupDaoHelper extends BaseDaoHelper<GroupTableDao, GroupTable> {

    private static GroupDaoHelper helper;

    private GroupDaoHelper(Context context) {
        super(IMDBManager.getInstance(context).getDaoSession().getGroupTableDao());
    }

    public static GroupDaoHelper get(Context context) {
        if (helper == null) {
            synchronized (GroupDaoHelper.class) {
                if (helper == null) {
                    helper = new GroupDaoHelper(context);
                }
            }
        }
        return helper;
    }

    @Override
    protected GroupTable getDataInfoById(String id) {
        if (tableDao != null && !TextUtils.isEmpty(id)) {
            if (tableDao.load(id) == null) {
                //获取网络数据
//                GroupApi.getInstance().getUserInfoByUserId(id, null);
            }
        }
        return tableDao.load(id);
    }

    @Override
    protected boolean hasKeyById(String id) {
        if (tableDao == null || TextUtils.isEmpty(id)) {
            return false;
        }
        QueryBuilder<GroupTable> qb = tableDao.queryBuilder();
        qb.where(UserTableDao.Properties.User_id.eq(id));
        long count = qb.buildCount().count();
        return count > 0 ? true : false;
    }
}
