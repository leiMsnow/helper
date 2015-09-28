package com.tongban.im.fragment.topic;


import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tongban.corelib.base.api.RequestApiListener;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.utils.DensityUtils;
import com.tongban.corelib.widget.header.RentalsSunHeaderView;
import com.tongban.corelib.widget.view.LoadMoreListView;
import com.tongban.corelib.widget.view.listener.OnLoadMoreListener;
import com.tongban.im.R;
import com.tongban.im.activity.topic.CreateTopicActivity;
import com.tongban.im.adapter.TopicListAdapter;
import com.tongban.im.api.TopicApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.TopicListenerImpl;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.common.TransferPathPrefix;
import com.tongban.im.fragment.base.BaseToolBarFragment;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.utils.PTRHeaderUtils;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 话题/动态页
 * author: chenenyu 15/7/13
 */
public class TopicFragment extends BaseToolBarFragment implements View.OnClickListener,
        AdapterView.OnItemClickListener, OnLoadMoreListener,
        RequestApiListener, PtrHandler {

    private PtrFrameLayout ptrFrameLayout;
    private LoadMoreListView lvTopicList;
    private TopicListAdapter mAdapter;
    private ImageButton ibSearch;
    private ImageButton ibCreate;
    private TextView tvTitle;

    private boolean mIsMainEvent = false;

    private int mCursor = 0;
    private int mPageSize = 10;
    //是否是下拉刷新操作
    private boolean mIsPull = false;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_topic;
    }

    @Override
    protected void initView() {
        ptrFrameLayout = (PtrFrameLayout) mView.findViewById(R.id.fragment_ptr_home_ptr_frame);
        mToolbar = mView.findViewById(R.id.in_topic_toolbar);
        tvTitle = (TextView) mView.findViewById(R.id.tv_title);
        ibSearch = (ImageButton) mView.findViewById(R.id.ib_search);
        ibCreate = (ImageButton) mView.findViewById(R.id.ib_create);
        lvTopicList = (LoadMoreListView) mView.findViewById(R.id.lv_topic_list);

        tvTitle.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initListener() {
        setRequestApiListener(this);
        ibSearch.setOnClickListener(this);
        ibCreate.setOnClickListener(this);
        lvTopicList.setOnItemClickListener(this);
        lvTopicList.setOnLoadMoreListener(this);
    }

    @Override
    protected void initData() {
        tvTitle.setText(getResources().getString(R.string.topic));

        mAdapter = new TopicListAdapter(mContext, R.layout.item_topic_list_main, null);
        mAdapter.setOnClickListener(new TopicListenerImpl(mContext));

        if (getArguments() != null)
            mIsMainEvent = getArguments().getBoolean(Consts.KEY_IS_MAIN, false);

        if (mIsMainEvent) {
            PTRHeaderUtils.getPointListView(mContext,ptrFrameLayout);
            ptrFrameLayout.setPtrHandler(this);
            ptrFrameLayout.autoRefresh();
        } else {
            mToolbar.setVisibility(View.GONE);
        }

        lvTopicList.setAdapter(mAdapter);
        lvTopicList.setPageSize(mPageSize);
    }

    @Override
    public void onClick(View v) {
        if (v == ibSearch) {
            TransferCenter.getInstance().startSearch(TransferPathPrefix.SEARCH_TOPIC, null);
        } else if (v == ibCreate) {
            if (!TransferCenter.getInstance().startLogin())
                return;
            Intent intent = new Intent(mContext, CreateTopicActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mAdapter.getItem(position) != null) {
            TransferCenter.getInstance().startTopicDetails(mAdapter.getItem(position));
        }
    }

    /**
     * 推荐话题事件回调
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.RecommendTopicListEvent obj) {
        //下拉刷新成功
        if (mIsPull) {
            ptrFrameLayout.refreshComplete();
            lvTopicList.resetLoad();
            mIsPull = false;
            mAdapter.clear();
        }
        lvTopicList.setResultSize(obj.topicList.size());
        mAdapter.addAll(obj.topicList);
        lvTopicList.setVisibility(View.VISIBLE);
    }

    /**
     * 话题搜索事件回调
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.SearchTopicListEvent obj) {
        if (!mIsMainEvent) {
            lvTopicList.setResultSize(obj.topicList.size());
            mAdapter.replaceAll(obj.topicList);
            lvTopicList.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 接口返回错误回调
     *
     * @param obj
     */
    public void onEventMainThread(ApiErrorResult obj) {
        if (mIsMainEvent) {
            ptrFrameLayout.refreshComplete();
            if (mAdapter != null && mAdapter.getCount() > 0) {
                hideEmptyView();
            }
        } else {
            mAdapter.clear();
            lvTopicList.setVisibility(View.GONE);
        }
    }

    /**
     * 创建话题成功回调
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.CreateTopicEvent obj) {
        ptrFrameLayout.autoRefresh();
    }

    /**
     * 话题收藏回调
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.TopicCollect obj) {
        updateCurrentData(obj.topic_id, true, obj.status);
    }

    /**
     * 话题评论创建成功回调
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.CreateTopicCommentEvent obj) {
        updateCurrentData(obj.topic_id, false, false);
    }

    @Override
    public void onLoadMore() {
        mCursor++;
        if (mIsMainEvent)
            TopicApi.getInstance().recommendTopicList(mCursor, mPageSize, this);
    }

    /***
     * 更新当前列表数据状态
     *
     * @param topicId   要更新的话题Id
     * @param isCollect true 收藏结果修改；false 评论结果修改
     * @param status    收藏状态(isCollect为true有效)
     */
    private void updateCurrentData(String topicId, boolean isCollect, boolean status) {
        for (int i = 0; i < mAdapter.getCount(); i++) {
            if (mAdapter.getItem(i).getTopic_id().equals(topicId)) {
                int count = Integer.parseInt(isCollect ? mAdapter.getItem(i).getCollect_amount() :
                        mAdapter.getItem(i).getComment_amount());
                //更新收藏数量
                if (isCollect) {
                    mAdapter.getItem(i).setCollect_amount(String.valueOf(status ? ++count : --count));
                }
                //更新评论数量
                else {
                    mAdapter.getItem(i).setComment_amount(String.valueOf(++count));
                }
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public void onRequest() {
        mCursor = 0;
        TopicApi.getInstance().recommendTopicList(mCursor, mPageSize, this);
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View view, View view1) {
        return PtrDefaultHandler.checkContentCanBePulledDown(ptrFrameLayout, view, view1);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frameLayout) {
        mIsPull = true;
        mCursor = 0;
        TopicApi.getInstance().recommendTopicList(mCursor, mPageSize, this);
    }
}