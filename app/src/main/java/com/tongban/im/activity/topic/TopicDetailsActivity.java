package com.tongban.im.activity.topic;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tongban.corelib.utils.KeyBoardUtils;
import com.tongban.corelib.widget.view.LoadMoreListView;
import com.tongban.corelib.widget.view.listener.OnLoadMoreListener;
import com.tongban.im.R;
import com.tongban.im.activity.base.TopicDetailsBaseActivity;
import com.tongban.im.adapter.TopicCommentAdapter;
import com.tongban.im.adapter.TopicImgAdapter;
import com.tongban.im.api.TopicApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.TopicListenerImpl;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.ImageUrl;
import com.tongban.im.model.topic.TopicComment;
import com.tongban.im.widget.view.TopicInputView;

import java.util.List;

/**
 * 话题评论界面
 *
 * @author fushudi
 */
public class TopicDetailsActivity extends TopicDetailsBaseActivity
        implements OnLoadMoreListener {

    //头布局控件
    private View mHeader;
    //头布局 top
    private ImageView ivUserPortrait;
    private TextView tvUserName;
    private TextView tvTime;
    //中间布局 content
    private TextView tvTopicTitle;
    private TextView tvTopicContent;
    private GridView gvContent;
    //底布局 bottom
    private TextView tvComment;

    private LoadMoreListView lvReplyList;

    private TopicImgAdapter mTopicImgAdapter;
    private TopicCommentAdapter mAdapter;


    @Override
    protected void initView() {
        super.initView();
        lvReplyList = (LoadMoreListView) findViewById(R.id.lv_reply_list);
        //添加头布局
        mHeader = LayoutInflater.from(mContext).inflate(R.layout.header_topic_details, null);
        ivUserPortrait = (ImageView) mHeader.findViewById(R.id.iv_user_portrait);
        tvUserName = (TextView) mHeader.findViewById(R.id.tv_user_name);
        tvTime = (TextView) mHeader.findViewById(R.id.tv_create_time);

        tvTopicTitle = (TextView) mHeader.findViewById(R.id.tv_topic_title);
        tvTopicContent = (TextView) mHeader.findViewById(R.id.tv_topic_content);
        gvContent = (GridView) mHeader.findViewById(R.id.gv_content);
        tvComment = (TextView) mHeader.findViewById(R.id.tv_comment_count);

        lvReplyList.addHeaderView(mHeader);
    }

    @Override
    protected void initData() {
        super.initData();


        if (!TextUtils.isEmpty(mTopicId)) {
            onRequest();

            mAdapter = new TopicCommentAdapter(mContext, R.layout.item_topic_comment_list, null);
            lvReplyList.setAdapter(mAdapter);
            lvReplyList.setResultSize(mPage);

            mTopicImgAdapter = new TopicImgAdapter(mContext, R.layout.item_topic_grid_img,
                    null);
            gvContent.setAdapter(mTopicImgAdapter);
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mAdapter.setOnClickListener(this);
        mAdapter.setOnImgClickListener(new TopicListenerImpl(mContext));
        mTopicImgAdapter.setImgClickListener(this);
        ivUserPortrait.setOnClickListener(this);
        lvReplyList.setOnLoadMoreListener(this);
    }

    @Override
    public void onRequest() {
        mCursor = 0;
        TopicApi.getInstance().getTopicInfo(mTopicId, this);
    }

    @Override
    public void onClick(View v) {
        // 用户信息查看
        if (v == ivUserPortrait) {
            TransferCenter.getInstance().startUserCenter(mTopicInfo.getUser_info().getUser_id());
        } else {
            super.onClick(v);
        }
    }

    /**
     * 话题详情事件回调
     *
     * @param topicInfoEvent
     */
    public void onEventMainThread(BaseEvent.TopicInfoEvent topicInfoEvent) {
        super.onEventMainThread(topicInfoEvent);
        if (mTopicInfo != null) {
            if (mTopicInfo.getUser_info() != null) {
                tvUserName.setText(mTopicInfo.getUser_info().getNick_name());
                if (mTopicInfo.getUser_info().getPortrait_url() != null) {
                    setUserPortrait(mTopicInfo.getUser_info().getPortrait_url().getMin()
                            , ivUserPortrait);
                } else {
                    ivUserPortrait.setImageResource(Consts.getUserDefaultPortrait());
                }
            }

            tvTime.setText(mTopicInfo.getC_time(mContext));

            tvTopicTitle.setText(mTopicInfo.getTopic_title());
            tvTopicContent.setText(mTopicInfo.getTopic_content());

            tvComment.setText(mTopicInfo.getComment_amount());
            if (mTopicInfo.getTopic_img_url() != null) {
                mTopicImgAdapter.replaceAll(mTopicInfo.getTopic_img_url());
                gvContent.setVisibility(View.VISIBLE);
            }

            lvReplyList.setVisibility(View.VISIBLE);
            // 查询话题评论列表
            TopicApi.getInstance().getTopicCommentList(mTopicId, mCursor, mPage, this);
        } else {
            lvReplyList.setVisibility(View.GONE);
        }
    }

    /**
     * 话题评论成功事件回调
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.CreateTopicCommentEvent obj) {
        super.onEventMainThread(obj);
        int commentCount = Integer.parseInt(tvComment.getText().toString());
        tvComment.setText(String.valueOf(commentCount + 1));
        mCursor = 0;
        TopicApi.getInstance().getTopicCommentList(mTopicId, mCursor, mAdapter.getCount() + 1, this);
    }


    /**
     * 话题评论列表事件回调
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.TopicCommentListEvent obj) {
        if (mCursor == 0) {
            mAdapter.replaceAll(obj.topicCommentList);
        } else {
            mAdapter.addAll(obj.topicCommentList);
        }
        mCursor++;
        lvReplyList.setResultSize(obj.topicCommentList.size());
    }


    @Override
    public void onLoadMore() {
        if (mTopicInfo != null) {
            TopicApi.getInstance().getTopicCommentList(mTopicId, mCursor, mPage, this);
        }
    }
}
