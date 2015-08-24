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
        List<Group> groups;

        public List<Group> getGroups() {
            return groups;
        }

        public void setGroups(List<Group> groups) {
            this.groups = groups;
        }
    }

    /**
     * 群推荐列表Event
     */
    public static class RecommendGroupListEvent {
        List<Group> groupList;

        public List<Group> getGroupList() {
            return groupList;
        }

        public void setGroupList(List<Group> groupList) {
            this.groupList = groupList;
        }
    }

    /**
     * 加入圈子的Event
     */
    public static class JoinGroupEvent {
        String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
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
        Group group;

        public Group getGroup() {
            return group;
        }

        public void setGroup(Group group) {
            this.group = group;
        }
    }

    /**
     * 圈子成员Event
     */
    public static class GroupMemberEvent {
        List<User> users;

        public List<User> getUsers() {
            return users;
        }

        public void setUsers(List<User> users) {
            this.users = users;
        }
    }

    /**
     * 重置密码Event
     */
    public static class PwdResetEvent {
        String result;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }

    /**
     * 用户信息Event
     */
    public static class UserInfoEvent {
        User user;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }

    /**
     * 选择标签Event
     */
    public static class LabelEvent {
        String label;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

    /**
     * 创建圈子成功Event
     */
    public static class CreateGroupEvent {
        String groupId;

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }
    }

    /**
     * 创建话题Event
     */
    public static class CreateTopicEvent {
        String topicId;

        public String getTopicId() {
            return topicId;
        }

        public void setTopicId(String topicId) {
            this.topicId = topicId;
        }
    }

    /**
     * 话题信息Event
     */
    public static class TopicInfoEvent {
        Topic topic;

        public Topic getTopic() {
            return topic;
        }

        public void setTopic(Topic topic) {
            this.topic = topic;
        }
    }

    /**
     * 话题列表Event
     */
    public static class TopicListEvent {
        //是否是主界面调用的接口
        boolean isMain = false;
        List<Topic> topicList;

        public List<Topic> getTopicList() {
            return topicList;
        }

        public void setTopicList(List<Topic> topicList) {
            this.topicList = topicList;
        }

        public boolean isMain() {
            return isMain;
        }

        public void setIsMain(boolean isMain) {
            this.isMain = isMain;
        }
    }

    /**
     * 话题搜索结果Event
     */
    public static class SearchTopicListEvent {
        List<Topic> topicList;

        public List<Topic> getTopicList() {
            return topicList;
        }

        public void setTopicList(List<Topic> topicList) {
            this.topicList = topicList;
        }
    }

    /**
     * 话题回复列表Event
     */
    public static class TopicCommentListEvent {
        List<TopicComment> topicCommentList;

        public List<TopicComment> getTopicCommentList() {
            return topicCommentList;
        }

        public void setTopicCommentList(List<TopicComment> topicCommentList) {
            this.topicCommentList = topicCommentList;
        }
    }

    /**
     * 收藏专题的Event
     */
    public static class CollectMultiProductEvent {
        List<MultiProduct> multiProductList;

        public List<MultiProduct> getMultiProductList() {
            return multiProductList;
        }

        public void setMultiProductList(List<MultiProduct> multiProductList) {
            this.multiProductList = multiProductList;
        }
    }

    /**
     * 收藏话题题的Event
     */
    public static class CollectTopicEvent {
        List<Topic> collectTopicList;

        public List<Topic> getCollectTopicList() {
            return collectTopicList;
        }

        public void setCollectTopicList(List<Topic> collectTopicList) {
            this.collectTopicList = collectTopicList;
        }
    }

    /**
     * 收藏单品的Event
     */
    public static class CollectSingleProductEvent {
        List<ProductBook> singleProductList;

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
        List<Topic> myLaunchTopicList;

        public List<Topic> getMyLaunchTopicList() {
            return myLaunchTopicList;
        }

        public void setMyLaunchTopicList(List<Topic> myLaunchTopicList) {
            this.myLaunchTopicList = myLaunchTopicList;
        }
    }

    /**
     * 我的圈子Event
     */
    public static class MyGroupListEvent {
        List<Group> myGroupList;

        public List<Group> getMyGroupList() {
            return myGroupList;
        }

        public void setMyGroupList(List<Group> myGroupList) {
            this.myGroupList = myGroupList;
        }
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

    public static class ConnecteErroEvent{
        String errorMessage;

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

}
