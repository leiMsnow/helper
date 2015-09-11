package com.tongban.im.activity.topic;

import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongban.corelib.utils.KeyBoardUtils;
import com.tongban.corelib.widget.view.LoadMoreListView;
import com.tongban.corelib.widget.view.listener.OnLoadMoreListener;
import com.tongban.im.R;
import com.tongban.im.activity.base.CommonImageResultActivity;
import com.tongban.im.adapter.TopicCommentAdapter;
import com.tongban.im.adapter.TopicImgAdapter;
import com.tongban.im.api.TopicApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.TopicListenerImpl;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.ImageUrl;
import com.tongban.im.model.Topic;
import com.tongban.im.model.TopicComment;
import com.tongban.im.widget.view.TopicInputView;

import java.util.List;

/**
 * 话题评论界面
 *
 * @author fushudi
 */
public class TopicDetailsActivity extends CommonImageResultActivity implements View.OnClickListener,
        TopicInputView.IOnClickCommentListener, OnLoadMoreListener {

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
    private ImageView ivComment;
    private TextView tvComment;

    private LoadMoreListView lvReplyList;

    private TopicImgAdapter mTopicImgAdapter;
    private TopicCommentAdapter mAdapter;

    private Topic mTopicInfo;
    private String mTopicId;

    private int mCursor = 0;
    private int mPage = 10;

    private MenuItem menuItem;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_topic_details;
    }

    @Override
    protected void initView() {

        lvReplyList = (LoadMoreListView) findViewById(R.id.lv_reply_list);

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

        lvReplyList.addHeaderView(mHeader);
    }

    @Override
    protected void initData() {
        topicInputView.setAdapterImgCount(3);
        topicInputView.setOnClickCommentListener(this);

        if (getIntent() != null) {
            Uri uri = getIntent().getData();
            mTopicId = uri.getQueryParameter(Consts.KEY_TOPIC_ID);
            if (!TextUtils.isEmpty(mTopicId)) {
                TopicApi.getInstance().getTopicInfo(mTopicId, this);
                TopicApi.getInstance().getTopicCommentList(mTopicId, mCursor, mPage, this);

                mAdapter = new TopicCommentAdapter(mContext, R.layout.item_topic_comment_list, null);
                mAdapter.setOnClickListener(this);
                mAdapter.setOnImgClickListener(new TopicListenerImpl(mContext));
                lvReplyList.setAdapter(mAdapter);
                lvReplyList.setResultSize(mPage);

                mTopicImgAdapter = new TopicImgAdapter(mContext, R.layout.item_topic_grid_img,
                        null);
                mTopicImgAdapter.setImgClickListener(this);
                gvContent.setAdapter(mTopicImgAdapter);
            }
        }
    }

    @Override
    protected void initListener() {
        ivUserPortrait.setOnClickListener(this);
        ivComment.setOnClickListener(this);
        lvReplyList.setOnLoadMoreListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_topic_detail, menu);
        menuItem = menu.findItem(R.id.menu_collect);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_collect) {
            menuItem.setEnabled(false);
            TopicApi.getInstance().collectTopic(!mTopicInfo.isCollect_status(), mTopicId, this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        //重置回复话题
        if (v == ivComment) {
            if (!TransferCenter.getInstance().startLogin()) {
                return;
            }
            topicInputView.clearCommentInfo();
            topicInputView.focusEdit();
        }
        // 用户信息查看
        else if (v == ivUserPortrait) {
            TransferCenter.getInstance().startUserCenter(mTopicInfo.getUser_info().getUser_id());
        } else {
            switch (v.getId()) {
                //回复评论
                case R.id.tv_comment:
                    TopicComment comment = (TopicComment) v.getTag();
                    topicInputView.setCommentInfo(comment.getComment_id(),
                            comment.getUser_info().getNick_name(),
                            comment.getUser_info().getUser_id());
                    break;
                case R.id.iv_topic_img:
                    List<ImageUrl> imageUrls = (List<ImageUrl>) v.getTag(Integer.MAX_VALUE);
                    int position = (int) v.getTag(Integer.MIN_VALUE);
                    TopicListenerImpl.startPhotoView(mContext,
                            TopicListenerImpl.setImageUrls(imageUrls), position);
                    break;
                case R.id.iv_user_portrait:
                    String userId = v.getTag(Integer.MAX_VALUE).toString();
                    TransferCenter.getInstance().startUserCenter(userId);
                    break;
            }
        }

    }

    @Override
    public void onClickComment(String commentContent, String repliedCommentId,
                               String repliedName, String repliedUserId, List<ImageUrl> selectedFile) {
        TopicApi.getInstance().createCommentForTopic(mTopicId, commentContent, repliedCommentId,
                repliedName, repliedUserId, selectedFile, this);
    }

    /**
     * 话题详情事件回调
     *
     * @param topicInfoEvent
     */
    public void onEventMainThread(BaseEvent.TopicInfoEvent topicInfoEvent) {
        mTopicInfo = topicInfoEvent.topic;
        if (mTopicInfo != null) {
            setTitle(mTopicInfo.getTopic_title());
            if (mTopicInfo.getUser_info() != null) {
                tvUserName.setText(mTopicInfo.getUser_info().getNick_name());
                if (mTopicInfo.getUser_info().getPortrait_url() != null) {
                    Glide.with(TopicDetailsActivity.this).load(mTopicInfo.getUser_info().
                            getPortrait_url().getMin()).placeholder(R.drawable.rc_default_portrait).
                            into(ivUserPortrait);
                } else {
                    ivUserPortrait.setImageResource(R.drawable.rc_default_portrait);
                }
            }

            if (mTopicInfo.isCollect_status()) {
                menuItem.setIcon(R.mipmap.ic_menu_collected);
            } else {
                menuItem.setIcon(R.mipmap.ic_menu_collect);
            }
            tvTime.setText(mTopicInfo.getC_time(mContext));

            tvTopicTitle.setText(mTopicInfo.getTopic_title());
            tvTopicContent.setText(mTopicInfo.getTopic_content());

            tvComment.setText(mTopicInfo.getComment_amount());

            mTopicImgAdapter.replaceAll(mTopicInfo.getTopic_img_url());
        } else {
            lvReplyList.removeHeaderView(mHeader);
        }
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

    /**
     * 话题评论成功事件回调
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.CreateTopicCommentEvent obj) {

        int commentCount = Integer.parseInt(tvComment.getText().toString());
        tvComment.setText(String.valueOf(commentCount + 1));
        topicInputView.clearCommentInfo();
        KeyBoardUtils.closeKeyboard(topicInputView.getEtComment(), mContext);
        mCursor = 0;
        TopicApi.getInstance().getTopicCommentList(mTopicId, mCursor, mAdapter.getCount() + 1, this);
    }

    /**
     * 收藏话题事件回调
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.TopicCollect obj) {
        mTopicInfo.setCollect_status(obj.status);
        menuItem.setEnabled(true);
        if (obj.status) {
            menuItem.setIcon(R.mipmap.ic_menu_collected);
        } else {
            menuItem.setIcon(R.mipmap.ic_menu_collect);
        }
    }

    @Override
    public void onLoadMore() {
        if (mTopicInfo != null) {
            TopicApi.getInstance().
                    getTopicCommentList(mTopicId, mCursor, mPage, this);
        }
    }
}
