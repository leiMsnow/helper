package com.tongban.im.activity.topic;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tongban.im.R;
import com.tongban.im.activity.CommonImageResultActivity;
import com.tongban.im.adapter.TopicImgAdapter;
import com.tongban.im.adapter.TopicReplyAdapter;
import com.tongban.im.api.TopicApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.Topic;
import com.tongban.im.model.TopicReply;
import com.tongban.im.widget.view.TopicInputView;

import java.util.ArrayList;
import java.util.List;

/**
 * 话题评论界面
 *
 * @author fushudi
 */
public class TopicDetailsActivity extends CommonImageResultActivity implements View.OnClickListener {

    //头布局控件
    private View mHeader;
    private ImageView ivUserIcon;
    private TextView tvUserName;
    private TextView tvAge;
    private TextView tvTime;
    private TextView tvTopicTitle;
    private TextView tvTopicContent;
    private GridView gvContent;
    private TextView tvCollect;
    private TextView tvReply;

    private ListView lvReplyList;

    private TopicImgAdapter mTopicImgAdapter;
    private TopicReplyAdapter mAdapter;

    private Topic mTopicInfo;
    private String mTopicId;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_topic_detail;
    }

    @Override
    protected void initView() {

        lvReplyList = (ListView) findViewById(R.id.lv_reply_list);

        topicInputView = (TopicInputView) findViewById(R.id.topic_input);

        //添加头布局
        mHeader = LayoutInflater.from(mContext).inflate(R.layout.header_topic_details, null);
        ivUserIcon = (ImageView) mHeader.findViewById(R.id.iv_user_portrait);
        gvContent = (GridView) mHeader.findViewById(R.id.gv_content);
        gvContent.setVisibility(View.VISIBLE);
        lvReplyList.addHeaderView(mHeader);

    }

    @Override
    protected void initData() {
        topicInputView.setAdapterImgCount(3);
        if (getIntent().getExtras() != null) {
            mTopicId = getIntent().getExtras().getString(Consts.KEY_TOPIC_ID, "");
        }
        TopicApi.getInstance().getTopicInfo(mTopicId, this);

        List<TopicReply> replyList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TopicReply topicReply = new TopicReply();
            topicReply.setReplyAge("1" + i);
            topicReply.setComment_content("说的很有道理，讲的很有道理，写的很有道理" + i);
            topicReply.setReplyNickName("打不死的小强");
            topicReply.setReplyNum("赞" + i);
            topicReply.setReplySex("男");
            topicReply.setReplyTime("08-01 14:28");
            replyList.add(topicReply);
        }

        mAdapter = new TopicReplyAdapter(mContext, R.layout.item_topic_reply_list, replyList);
        lvReplyList.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onClick(View v) {

    }

    public void onEventMainThread(BaseEvent.TopicInfoEvent topicInfoEvent) {
        mTopicInfo = topicInfoEvent.getTopic();
        if (mTopicInfo != null) {
            if (mTopicInfo.getContentType() == Topic.IMAGE) {
                mTopicImgAdapter = new TopicImgAdapter(mContext, R.layout.item_topic_grid_img,
                        mTopicInfo.getTopic_img_url());
                gvContent.setAdapter(mTopicImgAdapter);
            }
        } else {
            lvReplyList.removeHeaderView(mHeader);
        }
    }
}
