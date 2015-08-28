package com.tongban.im.fragment.user;


import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.corelib.widget.view.LoadMoreListView;
import com.tongban.corelib.widget.view.listener.OnLoadMoreListener;
import com.tongban.im.R;
import com.tongban.im.activity.user.UserCenterActivity;
import com.tongban.im.adapter.MyCommentTopicAdapter;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.BaseEvent;

/**
 * 我的话题 - 回复我的话题
 *
 * @author fushudi
 */
public class MyCommentTopicFragment extends BaseApiFragment implements View.OnClickListener,
        AdapterView.OnItemClickListener, OnLoadMoreListener {
    private LoadMoreListView mListView;
    private MyCommentTopicAdapter mAdapter;

    private int mCursor = 0;
    private int mPageSize = 10;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_my_receive_topic;
    }

    @Override
    protected void initView() {
        mListView = (LoadMoreListView) mView.findViewById(R.id.lv_receive_topic_list);
    }

    @Override
    protected void initData() {
        mAdapter = new MyCommentTopicAdapter(mContext, R.layout.item_my_comment_topic_list, null);
        mAdapter.setOnClickListener(this);
        mListView.setAdapter(mAdapter);
        mListView.setPageSize(mPageSize);
        UserCenterApi.getInstance().fetchReplyTopicList(mCursor, mPageSize, this);
    }

    @Override
    protected void initListener() {
        mListView.setOnItemClickListener(this);
        mListView.setOnLoadMoreListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_user_portrait:
                String visitorId = v.getTag().toString();
                Intent intent = new Intent(mContext, UserCenterActivity.class);
                intent.putExtra("visitorId", visitorId);
                startActivity(intent);
                break;
            case R.id.tv_comment:
                ToastUtil.getInstance(mContext).showToast("回复");
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TransferCenter.getInstance().startTopicDetails(mAdapter.getItem(position).getUser_info().getUser_id());
    }

    /**
     * 回复我的话题列表Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.CommentTopicListEvent obj) {
        mCursor++;
        mAdapter.replaceAll(obj.commentTopicList);
        mListView.setResultSize(obj.commentTopicList.size());
    }

    @Override
    public void onLoadMore() {
        UserCenterApi.getInstance().fetchReplyTopicList(mCursor, mPageSize, this);
    }
}
