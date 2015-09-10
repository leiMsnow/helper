package com.tongban.im.model;

import java.util.List;

/**
 * EventBus事件回调类，主要是将参数封装
 * Created by zhangleilei on 15/7/14.
 */
public class BaseEvent {

    //--------------------------------------话题模块-------------------------------------------------


    /**
     * 搜索话题关键字Event
     */
    public static class SearchTopicKeyEvent {
        public String keyword;
    }

    /**
     * 创建话题Event
     */
    public static class CreateTopicEvent {
        public String topicId;
    }

    /**
     * 话题信息Event
     */
    public static class TopicInfoEvent {
        public Topic topic;
    }
    /**
     * 官方话题信息Event
     */
    public static class OfficialTopicInfoEvent {
        public List<ProductBook> productBookList;
    }
    /**
     * 话题列表Event
     */
    public static class TopicListEvent {
        public List<Topic> topicList;
    }

    /**
     * 推荐话题列表Event
     */
    public static class RecommendTopicListEvent {
        public List<Topic> topicList;
    }

    /**
     * 话题搜索结果Event
     */
    public static class SearchTopicListEvent {
        public List<Topic> topicList;
    }

    /**
     * 话题评论列表Event
     */
    public static class TopicCommentListEvent {
        public List<TopicComment> topicCommentList;
    }

    /**
     * 创建话题评论Event
     */
    public static class CreateTopicCommentEvent {
        public String topic_id;
    }

    /**
     * 收藏话题Event
     */
    public static class TopicCollect {

        public String topic_id;
        public boolean status;
    }

    //--------------------------------------圈子模块-------------------------------------------------

    /**
     * 搜索圈子关键字Event
     */
    public static class SearchGroupKeyEvent {
        public String keyword;
    }

    /**
     * 搜索圈子结果的Event
     */
    public static class SearchGroupListEvent {
        public List<Group> groups;
    }

    /**
     * 圈子推荐列表Event
     */
    public static class RecommendGroupListEvent {
        public List<Group> groupList;
    }

    /**
     * 加入圈子的Event
     */
    public static class JoinGroupEvent {
        public String group_id;
        public String group_name;
        public boolean is_verify;
    }

    /**
     * 圈子信息Event
     */
    public static class GroupInfoEvent {
        public Group group;
    }

    /**
     * 圈子成员Event
     */
    public static class GroupMemberEvent {
        public List<User> users;
    }

    /**
     * 创建圈子成功Event
     */
    public static class CreateGroupEvent {
        public String groupId;
    }

    /**
     * 选择标签Event
     */
    public static class LabelEvent {
        public String label;
    }
    //--------------------------------------用户模块-------------------------------------------------

    /**
     * 注册event
     */
    public static class RegisterEvent {

        public enum RegisterEnum {
            /**
             * 注册第一步，获取验证码
             */
            SMS_CODE,
            /**
             * 注册第二步，提交用户信息和验证码注册
             */
            REGISTER,
            /**
             * 找回密码，验证步骤
             */
            VERIFY_CODE,
            /**
             * 第三方注册，授权
             */
            THIRD_REG
        }

        //注册第一步返回的userId
        public String user_id;
        //注册第二步返回的验证id
        public String verify_id;
        //注册第三步返回的验证结果
        public String verify_type;
        public String freeauth_token;
        //注册步骤
        public RegisterEnum registerEnum;

    }

    public static class UserLoginEvent {
        public User user;
    }

    public static class EditUserEvent {
        public User user;
    }

    /**
     * 重置密码Event
     */
    public static class PwdResetEvent {
        public String result;
    }

    /**
     * 我的圈子Event
     */
    public static class MyGroupListEvent {
        public List<Group> myGroupList;
    }

    /**
     * 我的关注Event
     */
    public static class MyFollowListEvent {
        public List<User> myFollowList;
    }

    /**
     * 我的粉丝Event
     */
    public static class MyFansListEvent {
        public List<User> myFansList;
    }

    /**
     * 用户中心（他人）Event
     */
    public static class UserCenterEvent {
        public User user;
    }

    /**
     * 个人中心Event
     */
    public static class PersonalCenterEvent {
        public User user;
    }

    /**
     * 用户资料Event
     */
    public static class UserInfoEvent {
        public User user;
    }

    /**
     * 回复我的话题列表Event
     */
    public static class CommentTopicListEvent {
        public List<TopicComment> commentTopicList;
    }


    /**
     * 关注用户Event
     */
    public static class FocusEvent {
        public boolean isFocus;
        public String userIds;
    }

    /**
     * 设置宝宝信息Event
     */
    public static class ChildCreateEvent {
        public String childBirthday;
        public int childSex;
    }

    /**
     * 设置宝宝信息成功Event
     */
    public static class ChildCreateSuccessEvent {
        public boolean isSetSuccess;
    }
    //--------------------------------------商品模块-------------------------------------------------

    /**
     * 获取标签的Event
     */
    public static class FetchTags {
        public List<Tag> tags;
        public String type;
    }

    /**
     * 获取专题收藏数量
     */
    public static class FetchThemeCollectedAmount {
        public int floor;
        public int amount;
    }

    /**
     * 搜索专题成功的Event
     */
    public static class SearchThemeResultEvent {
        public List<Theme> mThemes;
    }

    /**
     * 搜索单品成功的Event
     */
    public static class SearchProductResultEvent {
        public List<ProductBook> mProductBooks;
    }

    /**
     * 收藏专题的Event
     */
    public static class CollectThemeEvent {

    }

    /**
     * 取消收藏专题的Event
     */
    public static class NoCollectThemeEvent {

    }

    /**
     * 收藏商品的Event
     */
    public static class CollectProductEvent {

    }

    /**
     * 取消收藏商品的Event
     */
    public static class NoCollectProductEvent {

    }

    /**
     * 获取已经收藏的专题列表的Event
     */
    public static class FetchCollectedThemeEvent {
        public List<Theme> mThemeList;
    }

    /**
     * 获取已经收藏的单品的Event
     */
    public static class FetchCollectedProductEvent {
        public List<ProductBook> productBookList;

        public List<ProductBook> getProductBookList() {
            return productBookList;
        }

        public void setProductBookList(List<ProductBook> productBookList) {
            this.productBookList = productBookList;
        }
    }

    /**
     * 获取首页数据的Event
     */
    public static class FetchHomeInfo {
        private List<Discover> list;

        public List<Discover> getList() {
            return list;
        }

        public void setList(List<Discover> list) {
            this.list = list;
        }
    }

    /**
     * 获取专题下的商品列表Event
     */
    public static class FetchProductBooksInTheme {
        private List<ProductBook> list;

        public List<ProductBook> getList() {
            return list;
        }

        public void setList(List<ProductBook> list) {
            this.list = list;
        }
    }
    //--------------------------------------其它模块-------------------------------------------------

}
