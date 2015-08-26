package com.tongban.im.model;

import java.util.List;

/**
 * EventBus事件回调类，主要是将参数封装
 * Created by zhangleilei on 15/7/14.
 */
public class BaseEvent {

    /**
     * 搜索圈子的Event
     */
    public static class SearchGroupListEvent {
        public List<Group> groups;
    }

    /**
     * 群推荐列表Event
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
    }

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

        //注册第一步返回的userid
        private String user_id;
        //注册第二步返回的验证id
        private String verify_id;
        //注册第三步返回的验证结果
        private String verify_type;
        private String freeauth_token;
        //注册步骤
        private RegisterEnum registerEnum;

        public RegisterEnum getRegisterEnum() {
            return registerEnum;
        }

        public void setRegisterEnum(RegisterEnum registerEnum) {
            this.registerEnum = registerEnum;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getVerify_id() {
            return verify_id;
        }

        public void setVerify_id(String verify_id) {
            this.verify_id = verify_id;
        }

        public String getVerify_type() {
            return verify_type;
        }

        public void setVerify_type(String verify_type) {
            this.verify_type = verify_type;
        }

        public String getFreeauth_token() {
            return freeauth_token;
        }

        public void setFreeauth_token(String freeauth_token) {
            this.freeauth_token = freeauth_token;
        }
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
     * 重置密码Event
     */
    public static class PwdResetEvent {
        public String result;
    }

    /**
     * 用户信息Event
     */
    public static class UserInfoEvent {
        public User user;
    }

    /**
     * 选择标签Event
     */
    public static class LabelEvent {
        public String label;
    }

    /**
     * 创建圈子成功Event
     */
    public static class CreateGroupEvent {
       public String groupId;
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
     * 话题列表Event
     */
    public static class TopicListEvent {
        //是否是主界面调用的接口
        public boolean isMain = false;
        public List<Topic> topicList;
    }

    /**
     * 话题搜索结果Event
     */
    public static class SearchTopicListEvent {
        public List<Topic> topicList;
    }

    /**
     * 话题回复列表Event
     */
    public static class TopicCommentListEvent {
        public List<TopicComment> topicCommentList;
    }

    /**
     * 收藏专题的Event
     */
    public static class CollectMultiProductEvent {
        public List<MultiProduct> multiProductList;
    }
    /**
     * 取消收藏专题的Event
     */
    public static class NoCollectMultiProductEvent {

    }

    /**
     * 收藏话题题的Event
     */
    public static class CollectTopicEvent {
        public  List<Topic> collectTopicList;
    }

    /**
     * 收藏单品的Event
     */
    public static class CollectSingleProductEvent {
        public  List<ProductBook> singleProductList;

        public List<ProductBook> getSingleProductList() {
            return singleProductList;
        }

        public void setSingleProductList(List<ProductBook> singleProductList) {
            this.singleProductList = singleProductList;
        }
    }

    /**
     * 我发起的话题Event
     */
    public static class MyLaunchTopicEvent {
        public List<Topic> myLaunchTopicList;
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
        List<User> myFollowList;

        public List<User> getMyFollowList() {
            return myFollowList;
        }

        public void setMyFollowList(List<User> myFollowList) {
            this.myFollowList = myFollowList;
        }
    }

    /**
     * 我的粉丝Event
     */
    public static class MyFansListEvent {
        List<User> myFansList;

        public List<User> getMyFansList() {
            return myFansList;
        }

        public void setMyFansList(List<User> myFansList) {
            this.myFansList = myFansList;
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
    public static class FetchThemeProducts {
        private List<ProductBook> list;

        public List<ProductBook> getList() {
            return list;
        }

        public void setList(List<ProductBook> list) {
            this.list = list;
        }
    }

    /**
     * 回复话题Event
     */
    public static class CreateTopicCommentEvent {
        String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    /**
     * 收藏话题Event
     */
    public static class TopicCollect {
        public boolean status;

        public TopicCollect(boolean status) {
            this.status = status;
        }
    }

    /**
     * 用户中心（他人）Event
     */
    public static class UserCenterEvent {
        User user;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }

    /**
     * 回复我的话题列表Event
     */
    public static class CommentTopicListEvent {
        List<TopicComment> commentTopicList;

        public List<TopicComment> getCommentTopicList() {
            return commentTopicList;
        }

        public void setCommentTopicList(List<TopicComment> commentTopicList) {
            this.commentTopicList = commentTopicList;
        }
    }


    /**
     * 关注用户Event
     */
    public static class FocusEvent {
        boolean isFocus;

        public boolean isFocus() {
            return isFocus;
        }

        public void setIsFocus(boolean isFocus) {
            this.isFocus = isFocus;
        }
    }

    /**
     * 搜索圈子关键字Event
     */
    public static class SearchGroupKeyEvent {
        public String keyword;
    }
}
