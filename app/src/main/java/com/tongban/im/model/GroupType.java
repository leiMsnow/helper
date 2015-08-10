package com.tongban.im.model;

import android.content.Context;

import com.tongban.im.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择群类型数据类
 * 群类型,0:私密群; >0:公开群
 * 1:同城;2:同龄3:同学;4：生活;5:活动;6:达人
 * Created by zhangleilei on 15/7/22.
 */
public class GroupType {

    /**
     * 私密圈
     */
    public final static int PRIVATE = 0;
    /**
     * 同城圈
     */
    public final static int CITY = 1;
    /**
     * 同龄圈
     */
    public final static int AGE = 2;
    /**
     * 同学圈
     */
    public final static int CLASSMATE = 3;
    /**
     * 生活圈
     */
    public final static int LIFE = 4;
    /**
     * 活动圈
     */
    public final static int ACTIVITY = 5;
    /**
     * 达人圈
     */
    public final static int TALENT = 6;

    private int icon = R.drawable.shape_group_icon_pressed_red;
    private String groupTypeName;
    private String groupDesc;
    private int groupType;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getGroupTypeName() {
        return groupTypeName;
    }

    public void setGroupTypeName(String groupTypeName) {
        this.groupTypeName = groupTypeName;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public int getGroupType() {
        return groupType;
    }

    public void setGroupType(int groupType) {
        this.groupType = groupType;
    }

    /**
     * 创建圈子类型数据
     *
     * @return
     */
    public static List<GroupType> createGroupType() {
        List<GroupType> groupTypes = new ArrayList<>();
        GroupType group = new GroupType();
        group.setGroupTypeName("同城圈");
        group.setGroupDesc("同一个城市的宝爸爸宝妈妈们");
        group.setGroupType(CITY);
        group.setIcon(R.drawable.shape_group_icon_pressed_deep_purple);
        groupTypes.add(group);

        group = new GroupType();
        group.setGroupTypeName("同龄圈");
        group.setGroupDesc("同一个年龄的宝贝圈，喜好、成长会不会相同呢");
        group.setGroupType(AGE);
        group.setIcon(R.drawable.shape_group_icon_pressed_pink);
        groupTypes.add(group);

        group = new GroupType();
        group.setGroupTypeName("同学圈");
        group.setGroupDesc("同一个班级的宝贝家长们，他们有什么经验分享呢");
        group.setGroupType(CLASSMATE);
        group.setIcon(R.drawable.shape_group_icon_pressed_light_blue);

        groupTypes.add(group);

        group = new GroupType();
        group.setGroupTypeName("生活圈");
        group.setGroupDesc("购物、宝宝日用品");
        group.setGroupType(LIFE);
        group.setIcon(R.drawable.shape_group_icon_pressed_light_green);

        groupTypes.add(group);

        group = new GroupType();
        group.setGroupTypeName("活动圈");
        group.setGroupDesc("宝宝面对面，从此不孤单");
        group.setGroupType(ACTIVITY);
        group.setIcon(R.drawable.shape_group_icon_pressed_yellow);
        groupTypes.add(group);

        return groupTypes;

    }
}
