package com.tongban.im.common;

import com.tongban.im.R;

import java.util.Random;

/**
 * 常量
 * version:1.0  15/7/15
 */
public class Consts {

    //------------------------------VisitFile相关---------------------------------------------------
    //-----------VisitFile文件下的缓存标记{@link com.tongban.corelib.utils.SPUtils}-------------------
    //----------------随着APP安装后,不会被清除的文件信息标记的KEY放在这里---------------------------------
    /**
     * 第一次安装APP标记
     */
    public static final String FIRST_SET_CHILD_INFO = "FIRST_SET_CHILD_INFO";
    /**
     * 默认头像标记
     */
    public static final String KEY_DEFAULT_PORTRAIT = "KEY_DEFAULT_PORTRAIT";
    /**
     * 宝宝生日
     */
    public static final String CHILD_BIRTHDAY = "CHILD_BIRTHDAY";
    /**
     * 宝宝性别
     */
    public static final String CHILD_SEX = "CHILD_SEX";
    //------------------------------默认值相关-------------------------------------------------------
    /**
     * Double默认值
     */
    public static final double DEFAULT_DOUBLE = -1.1;
    /**
     * 图片服务器地址前缀
     */
    public static final String TONGBAN_UPLOAD_HOST_PREFIX = "http://7xkuqd.com2.z0.glb.qiniucdn.com/";
    //------------------------------其它相关key------------------------------------------------------
    /**
     * 是否进入/打开首页
     */
    public static final String KEY_IS_MAIN = "KEY_IS_MAIN";
    /**
     * 其它设备登录
     */
    public static final String KEY_OTHER_CLIENT = "KEY_OTHER_CLIENT";
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
     * 圈子信息
     */
    public static final String KEY_GROUP_INFO = "KEY_GROUP_INFO";
    /**
     * 圈子类型名称
     */
    public static final String KEY_GROUP_TYPE_NAME = "KEY_GROUP_TYPE_NAME";
    /**
     * 圈子类型icon
     */
    public static final String KEY_GROUP_TYPE_ICON = "KEY_GROUP_TYPE_ICON";
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
     * 话题ID
     */
    public static final String KEY_TOPIC_ID = "KEY_TOPIC_ID";
    /**
     * 历史搜索-话题搜索记录
     */
    public static final String HISTORY_SEARCH_TOPIC = "HISTORY_SEARCH_TOPIC";

    //------------------------------用户相关key------------------------------------------------------
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
     * 话题列表标记
     * 1.我的收藏列表：0 "MY_COLLECT_TOPIC_LIST"
     * 2.我发起的列表：1 "MY_SEND_TOPIC_LIST"
     */
    public static final String KEY_MY_TOPIC_LIST = "KEY_MY_TOPIC_LIST";
    /**
     * 区分粉丝、关注界面的key
     */
    public static final String KEY_TAG = "TAG";
    /**
     * 粉丝
     */
    public static final String TAG_FANS = "TAG_FANS";
    /**
     * 关注
     */
    public static final String TAG_FOLLOW = "TAG_FOLLOW";
    /**
     * 是否进入设置昵称界面
     */
    public static final String KEY_EDIT_USER = "KEY_EDIT_USER";
    /**
     * 修改个人资料Key
     */
    public static final String KEY_UPDATE_PERSONAL_INFO = "KEY_UPDATE_PERSONAL_INFO";
    /**
     * 修改昵称
     */
    public static final String KEY_UPDATE_NICKNAME = "KEY_UPDATE_NICKNAME";
    /**
     * 修改性别
     */
    public static final String KEY_UPDATE_SEX = "KEY_UPDATE_SEX";
    //------------------------------商品相关key------------------------------------------------------
    /**
     * 专题id
     */
    public static final String KEY_THEME_ID = "KEY_THEME_ID";
    /**
     * 图书单品id
     */
    public static final String KEY_PRODUCT_BOOK_ID = "KEY_PRODUCT_BOOK_ID";


    //------------------------------bundle-key-end--------------------------------------------------
    /**
     * 随机返回默认头像的资源ID
     *
     * @return
     */
    public static int getUserDefaultPortrait() {
        Random random = new Random();
        int portraits[] = new int[]{R.mipmap.ic_default_portrait1
                , R.mipmap.ic_default_portrait2
                , R.mipmap.ic_default_portrait3
                , R.mipmap.ic_default_portrait4
                , R.mipmap.ic_default_portrait5
                , R.mipmap.ic_default_portrait6
                , R.mipmap.ic_default_portrait7
                , R.mipmap.ic_default_portrait8
                , R.mipmap.ic_default_portrait9
                , R.mipmap.ic_default_portrait10};
        return portraits[random.nextInt(portraits.length)];
    }
}
