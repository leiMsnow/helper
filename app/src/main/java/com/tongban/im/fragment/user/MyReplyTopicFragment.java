package com.tongban.im.fragment.user;


import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.activity.topic.TopicDetailsActivity;
import com.tongban.im.activity.user.UserCenterActivity;
import com.tongban.im.adapter.MyReplyTopicAdapter;
import com.tongban.im.model.Topic;
import com.tongban.im.model.TopicReply;

import java.util.ArrayList;
import java.util.List;

/**
 * 回复我的话题
 *
 * @author fushudi
 */
public class MyReplyTopicFragment extends BaseApiFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView mListView;
    private MyReplyTopicAdapter mAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_my_receive_topic;
    }

    @Override
    protected void initView() {
        mListView = (ListView) mView.findViewById(R.id.lv_receive_topic_list);
    }

    @Override
    protected void initData() {
        List<Topic> replyTopicList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Topic topic = new Topic();
            TopicReply topicReply = new TopicReply();
            topicReply.setReplyTime("2015-8-17 18:31");
            topicReply.setReplyContent("得到大家积极扭扭捏捏" + i);
            topicReply.setPortrait_url("http://img2.imgtn.bdimg.com/it/u=606613155,1633300277&fm=23&gp=0.jpg");
            topicReply.setReplyNickName("小强" + i);
//            topic.setTopicReply(topicReply);
            topic.setTopic_content("小强很坚强小强很坚强小强很坚强小强很坚强小强很坚强小强很坚强小强很坚强小强很坚强小强很坚强小强很坚强小强很坚强");
            replyTopicList.add(topic);
        }
        mAdapter = new MyReplyTopicAdapter(mContext, R.layout.item_my_reply_topic_list, replyTopicList);
        mAdapter.setOnClickListener(this);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_topic_icon:
                startActivity(new Intent(mContext, UserCenterActivity.class));
                break;
            case R.id.tv_reply:
                ToastUtil.getInstance(mContext).showToast("回复");
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(mContext, TopicDetailsActivity.class));
    }
}
