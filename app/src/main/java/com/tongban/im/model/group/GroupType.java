package com.tongban.im.model.group;

import com.tongban.im.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 选择群类型数据类
 * 群类型,0:私密群; >0:公开群
 * 1:同城;2:同龄3:同学;4：生活;5:活动;6:达人
 * Created by zhangleilei on 15/7/22.
 */
public class GroupType implements Serializable{

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

    private int src = R.mipmap.ic_group_create;
    private String groupTypeName;
    private String groupDesc;
    private int groupType;
    private int groupBGColor;

    public int getGroupBGColor() {
        return groupBGColor;
    }

    public void setGroupBGColor(int groupBGColor) {
        this.groupBGColor = groupBGColor;
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

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    /**
     * 创建圈子类型数据
     *
     * @return
     */
    public static List<GroupType> createGroupType() {
        List<GroupType> groupTypes = new ArrayList<>();
        GroupType group = new GroupType();
        group.setGroupType(CITY);
        group.setGroupTypeName("同城圈");
        group.setSrc(R.mipmap.ic_group_city);
        group.setGroupDesc("同一个城市的宝爸爸宝妈妈们");
        group.setGroupBGColor(R.drawable.shape_group_icon_pressed_deep_purple);

        groupTypes.add(group);

        group = new GroupType();
        group.setGroupType(AGE);
        group.setGroupTypeName("同龄圈");
        group.setSrc(R.mipmap.ic_group_age);
        group.setGroupDesc("同一个年龄的宝贝圈，喜好、成长会不会相同呢");
        group.setGroupBGColor(R.drawable.shape_group_icon_pressed_pink);

        groupTypes.add(group);

        group = new GroupType();
        group.setGroupType(CLASSMATE);
        group.setGroupTypeName("同学圈");
        group.setSrc(R.mipmap.ic_group_classmate);
        group.setGroupDesc("同一个班级的宝贝家长们，他们有什么经验分享呢");
        group.setGroupBGColor(R.drawable.shape_group_icon_pressed_light_blue);

        groupTypes.add(group);

        group = new GroupType();
        group.setGroupType(LIFE);
        group.setGroupTypeName("生活圈");
        group.setSrc(R.mipmap.ic_group_life);
        group.setGroupDesc("购物、宝宝日用品");
        group.setGroupBGColor(R.drawable.shape_group_icon_pressed_light_green);

        groupTypes.add(group);

        group = new GroupType();
        group.setGroupType(ACTIVITY);
        group.setGroupTypeName("活动圈");
        group.setSrc(R.mipmap.ic_group_activity);
        group.setGroupDesc("宝宝面对面，从此不孤单");
        group.setGroupBGColor(R.drawable.shape_group_icon_pressed_yellow);

        groupTypes.add(group);

        return groupTypes;

    }
}
