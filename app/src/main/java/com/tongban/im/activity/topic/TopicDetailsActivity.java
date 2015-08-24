package com.tongban.im.activity.topic;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.activity.CommonImageResultActivity;
import com.tongban.im.adapter.TopicImgAdapter;
import com.tongban.im.adapter.TopicReplyAdapter;
import com.tongban.im.api.TopicApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.Topic;
import com.tongban.im.widget.view.TopicInputView;

/**
 * 话题评论界面
 *
 * @author fushudi
 */
public class TopicDetailsActivity extends CommonImageResultActivity implements View.OnClickListener {

    //头布局控件
    private View mHeader;
    //头布局 top
    private ImageView ivUserPortrait;
    private TextView tvUserName;
    private TextView tvAge;
    private TextView tvTime;
    //中间布局 content
    private TextView tvTopicTitle;
    private TextView tvTopicContent;
    private GridView gvContent;
    //底布局 bottom
    private ImageView ivComment;
    private TextView tvComment;
    private ImageView ivCollect;
    private TextView tvCollect;


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
        ivUserPortrait = (ImageView) mHeader.findViewById(R.id.iv_user_portrait);
        tvUserName = (TextView) mHeader.findViewById(R.id.tv_user_name);
        tvTime = (TextView) mHeader.findViewById(R.id.tv_create_time);

        tvTopicTitle = (TextView) mHeader.findViewById(R.id.tv_topic_title);
        tvTopicContent = (TextView) mHeader.findViewById(R.id.tv_topic_content);
        gvContent = (GridView) mHeader.findViewById(R.id.gv_content);
        gvContent.setVisibility(View.VISIBLE);

        ivComment = (ImageView) mHeader.findViewById(R.id.iv_comment);
        tvComment = (TextView) mHeader.findViewById(R.id.tv_comment_count);
        ivCollect = (ImageView) mHeader.findViewById(R.id.iv_collect);
        tvCollect = (TextView) mHeader.findViewById(R.id.tv_collect_count);

        lvReplyList.addHeaderView(mHeader);
    }

    @Override
    protected void initData() {
        topicInputView.setAdapterImgCount(3);
        if (getIntent().getExtras() != null) {
            mTopicId = getIntent().getExtras().getString(Consts.KEY_TOPIC_ID, "");
        }

        TopicApi.getInstance().getTopicInfo(mTopicId, this);
        TopicApi.getInstance().getTopicCommentList(mTopicId, 0, 10, this);

        mAdapter = new TopicReplyAdapter(mContext, R.layout.item_topic_reply_list, null);
        lvReplyList.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        ivComment.setOnClickListener(this);
        ivCollect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == ivComment) {
            ToastUtil.getInstance(mContext).showToast("ivComment");
        } else if (v == ivCollect) {
            ToastUtil.getInstance(mContext).showToast("ivCollect");
        }
    }

    /**
     * 话题详情事件回调
     *
     * @param topicInfoEvent
     */
    public void onEventMainThread(BaseEvent.TopicInfoEvent topicInfoEvent) {
        mTopicInfo = topicInfoEvent.getTopic();
        if (mTopicInfo != null) {

            if (mTopicInfo.getUser_info() != null) {
                tvUserName.setText(mTopicInfo.getUser_info().getNick_name());
                Glide.with(TopicDetailsActivity.this).load(mTopicInfo.getUser_info().getPortrait_url().getMin())
                        .placeholder(R.drawable.rc_default_portrait).into(ivUserPortrait);
            }
            tvTime.setText(mTopicInfo.getC_time(mContext));

            tvTopicTitle.setText(mTopicInfo.getTopic_title());
            tvTopicContent.setText(mTopicInfo.getTopic_content());

            tvComment.setText(mTopicInfo.getComment_amount());
            tvCollect.setText(mTopicInfo.getCollect_amount());

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
