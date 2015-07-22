package com.tongban.im.model;

import com.tongban.im.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择群类型数据类
 * Created by zhangleilei on 15/7/22.
 */
public class GroupType {

    private int icon = R.mipmap.ic_create_group;
    private String groupType;
    private String groupDesc;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public static List<GroupType> createGroupType() {
        List<GroupType> groupTypes = new ArrayList<>();
        GroupType group = new GroupType();
        group.setGroupType("同城圈");
        group.setGroupDesc("同一个城市的宝爸爸宝妈妈们");
        groupTypes.add(group);

        group = new GroupType();
        group.setGroupType("同龄圈");
        group.setGroupDesc("同一个年龄的宝贝圈，喜好、成长会不会相同呢");
        groupTypes.add(group);

        group = new GroupType();
        group.setGroupType("同学圈");
        group.setGroupDesc("同一个班级的宝贝家长们，他们有什么经验分享呢");
        groupTypes.add(group);

        group = new GroupType();
        group.setGroupType("生活圈");
        group.setGroupDesc("购物、宝宝日用品");
        groupTypes.add(group);

        group = new GroupType();
        group.setGroupType("活动圈");
        group.setGroupDesc("宝宝面对面，从此不孤单");
        groupTypes.add(group);


        return groupTypes;

    }
}
