package com.tongban.im.common;

/**
 * 常量
 * version:1.0  15/7/15
 */
public class Consts {

    /**
     * 图片服务器地址前缀
     */
    public static final String TONGBAN_UPLOAD_HOST_PREFIX = "http://7xkuqd.com2.z0.glb.qiniucdn.com/";
    /**
     * IM系统的Token
     */
    public static final String IM_BIND_TOKEN = "IM_BIND_TOKEN";
    /**
     * 免认证登录Token
     */
    public static final String FREEAUTH_TOKEN = "FREEAUTH_TOKEN";
    /**
     * 七牛上传的token
     */
    public static final String QINIU_TOKEN = "QINIU_TOKEN";
    /**
     * 用户id
     */
    public static final String USER_ID = "USER_ID";
    /**
     * 用户昵称
     */
    public static final String NICK_NAME = "NICK_NAME";
    /**
     * 用户账号
     */
    public static final String USER_ACCOUNT = "USER_ACCOUNT";

    /**
     * 经度
     */
    public static final String LONGITUDE = "LONGITUDE";
    /**
     * 纬度
     */
    public static final String LATITUDE = "LATITUDE";
    /**
     * 省份
     */
    public static final String PROVINCE = "PROVINCE";
    /**
     * 城市
     */
    public static final String CITY = "CITY";
    /**
     * 区县
     */
    public static final String COUNTY = "COUNTY";
    /**
     * 地址
     */
    public static final String ADDRESS = "ADDRESS";
    /**
     * 历史搜索-话题搜索记录
     */
    public static final String HISTORY_SEARCH_TOPIC = "HISTORY_SEARCH_TOPIC";

    //------------------------------bundle-key-start------------------------------------------------


    //------------------------------圈子相关key------------------------------------------------------
    /**
     * 圈子ID
     */
    public static final String KEY_GROUP_ID = "KEY_GROUP_ID";
    /**
     * 圈子类型
     */
    public static final String KEY_GROUP_TYPE = "KEY_GROUP_TYPE";
    /**
     * 圈子类型名称
     */
    public static final String KEY_GROUP_TYPE_NAME = "KEY_GROUP_TYPE_NAME";
    /**
     * 搜索圈子POI的名称
     */
    public static final String KEY_SELECTED_POI_NAME = "KEY_SELECTED_POI_NAME";
    /**
     * 圈子搜索结果
     */
    public static final String KEY_SEARCH_VALUE = "KEY_SEARCH_VALUE";
    /**
     * 是否已经加入
     */
    public static final String KEY_IS_JOIN = "KEY_IS_JOIN";

    //------------------------------话题相关key------------------------------------------------------
    /**
     * 是否显示话题页的标题
     */
    public static final String KEY_TOPIC_TOOLBAR_DISPLAY = "KEY_TOPIC_TOOLBAR_DISPLAY";
    /**
     * 话题ID
     */
    public static final String KEY_TOPIC_ID = "KEY_TOPIC_ID";


    //------------------------------用户相关key------------------------------------------------------
    /**
     * 话题列表标记
     * 1.我的收藏列表：0 "MY_COLLECT_TOPIC_LIST"
     * 2.我发起的列表：1 "MY_SEND_TOPIC_LIST"
     */
    public static final String KEY_MY_TOPIC_LIST = "KEY_MY_TOPIC_LIST";
    /**
     * 是否是我已经关注的
     * 1.已经关注的：0 "FOCUS"
     * 2.未关注的：1 "UN_FOCUS"
     */
    public static final String KEY_FOCUS = "KEY_FOCUS";
    /**
     * 被关注者的Id
     */
    public static final int KEY_FOCUS_ID = 1;

    //------------------------------商品相关key------------------------------------------------------
    /**
     * 专题id
     */
    public static final String KEY_THEME_ID = "KEY_THEME_ID";
    /**
     * 图书单品id
     */
    public static final String KEY_PRODUCT_BOOK_ID = "KEY_PRODUCT_BOOK_ID";

    //------------------------------其它相关key------------------------------------------------------
    /**
     * 是否是首页
     */
    public static final String KEY_IS_MAIN = "KEY_IS_MAIN";

    //------------------------------bundle-key-end--------------------------------------------------

}
