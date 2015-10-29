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

    //------------------------------其它相关key------------------------------------------------------








    //------------------------------bundle-key-start------------------------------------------------


    //------------------------------圈子相关key------------------------------------------------------

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
     * 搜索圈子POI的名称
     */
    public static final String KEY_SELECTED_POI_NAME = "KEY_SELECTED_POI_NAME";
    /**
     * 圈子搜索结果
     */
    public static final String KEY_SEARCH_VALUE = "KEY_SEARCH_VALUE";


    //------------------------------话题相关key------------------------------------------------------
    /**
     * 历史搜索-话题搜索记录
     */
    public static final String HISTORY_SEARCH_TOPIC = "HISTORY_SEARCH_TOPIC";

    //------------------------------用户相关key------------------------------------------------------

    /**
     * 话题列表标记
     * 1.我的收藏列表：0 "MY_COLLECT_TOPIC_LIST"
     * 2.我发起的列表：1 "MY_SEND_TOPIC_LIST"
     */
    public static final String KEY_MY_TOPIC_LIST = "KEY_MY_TOPIC_LIST";

    /**
     * 粉丝
     */
    public static final String TAG_FANS = "TAG_FANS";
    /**
     * 关注
     */
    public static final String TAG_FOLLOW = "TAG_FOLLOW";

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
    /**
     * 消息通知
     * 1、消息通知  1 消息通知
     * 2、消息不通知 0 消息不通知
     */
    public static final String KEY_MESSAGE_NOTIFY = "KEY_MESSAGE_NOTIFY";



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
