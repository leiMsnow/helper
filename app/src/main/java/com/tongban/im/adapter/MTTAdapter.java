package com.tongban.im.adapter;

import com.tongban.im.db.bean.GroupTable;
import com.tongban.im.db.bean.UserTable;
import com.tongban.im.model.Group;
import com.tongban.im.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 将业务数据对象转换成数据库对象
 * Model to Table
 * Created by zhangleilei on 15/7/30.
 */
public class MTTAdapter {
    /**
     * user转换本地数据库user
     *
     * @param user 业务user
     */
    public static UserTable userToTable(User user) {

        UserTable userTable = new UserTable();
        userTable.setUser_id(user.getUser_id());
        userTable.setNick_name(user.getNick_name());
        userTable.setMobile_phone(user.getMobile_phone());
        userTable.setDeclaration(user.getDeclaration());
        userTable.setPortrait_url(user.getPortrait_url());

        return userTable;
    }

    /**
     * 业务数据group转换本地数据库group
     *
     * @param group
     * @return
     */
    public static GroupTable groupToTable(Group group) {

        GroupTable groupTable = new GroupTable();
        groupTable.setGroup_id(group.getGroup_id());
        groupTable.setGroup_avatar(group.getGroup_avatar());
        groupTable.setGroup_name(group.getGroup_name());

        return groupTable;
    }

    /**
     * 业务数据集合转换本地数据库
     *
     * @param groups
     * @return
     */
    public static List<GroupTable> groupToTable(List<Group> groups) {

        List<GroupTable> groupTableList = new ArrayList<>();
        for (Group group : groups) {
            groupTableList.add(groupToTable(group));
        }

        return groupTableList;
    }


}
