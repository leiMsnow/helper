package com.tongban.im.fragment.topic;


import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.im.R;
import com.tongban.im.activity.topic.CreateTopicActivity;
import com.tongban.im.adapter.TopicListAdapter;
import com.tongban.im.api.TopicApi;
import com.tongban.im.common.TopicListenerImpl;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.common.TransferPathPrefix;
import com.tongban.im.model.BaseEvent;

/**
 * 话题/动态页
 * author: chenenyu 15/7/13
 */
public class TopicFragment extends BaseApiFragment implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private ListView lvTopicList;
    private TopicListAdapter mAdapter;
    private ImageButton ibSearch;
    private ImageButton ibCreate;
    private TextView tvTitle;

    private int mCursor = 0;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_topic;
    }

    @Override
    protected void initView() {
        tvTitle = (TextView) mView.findViewById(R.id.tv_title);
        ibSearch = (ImageButton) mView.findViewById(R.id.ib_search);
        ibCreate = (ImageButton) mView.findViewById(R.id.ib_create);
        lvTopicList = (ListView) mView.findViewById(R.id.lv_topic_list);

        tvTitle.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initListener() {
        ibSearch.setOnClickListener(this);
        ibCreate.setOnClickListener(this);
        lvTopicList.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        tvTitle.setText(getResources().getString(R.string.topic));

        TopicApi.getInstance().recommendTopicList(0, 10, this);

        mAdapter = new TopicListAdapter(mContext, R.layout.item_topic_list_main, null);
        mAdapter.setOnClickListener(new TopicListenerImpl(mContext));
        lvTopicList.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v == ibSearch) {
            TransferCenter.getInstance().startSearch(TransferPathPrefix.SEARCH_TOPIC, null);
        } else if (v == ibCreate) {
            Intent intent = new Intent(mContext, CreateTopicActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TransferCenter.getInstance().startTopicDetails(mAdapter.getItem(position).getTopic_id());
    }

    /**
     * 推荐话题事件回调
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.TopicListEvent obj) {
        if (obj.isMain()) {
            mAdapter.replaceAll(obj.getTopicList());
            lvTopicList.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 接口返回错误回调
     *
     * @param obj
     */
    public void onEventMainThread(ApiErrorResult obj) {
        mAdapter.clear();
        lvTopicList.setVisibility(View.GONE);
    }

    /**
     * 创建话题成功回调
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.CreateTopicEvent obj) {
        TopicApi.getInstance().recommendTopicList(mCursor, mAdapter.getCount(), this);
    }

    public void onEventMainThread(BaseEvent.TopicCollect obj) {
        TopicApi.getInstance().recommendTopicList(mCursor, mAdapter.getCount(), this);
    }

    public void onEventMainThread(BaseEvent.CreateTopicCommentEvent obj) {
        TopicApi.getInstance().recommendTopicList(mCursor, mAdapter.getCount(), this);
    }
}