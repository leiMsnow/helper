package com.tb.api.model.group;

import java.io.Serializable;

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

    private int src ;
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
}
