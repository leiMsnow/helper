package com.tongban.im.fragment;


import android.view.View;
import android.widget.ListView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.im.R;
import com.tongban.im.adapter.MyReceiveTopicAdapter;
import com.tongban.im.model.Topic;

import java.util.ArrayList;
import java.util.List;

public class MyReceiveTopicFragment extends BaseApiFragment implements View.OnClickListener{
    private ListView mListView;
    private MyReceiveTopicAdapter mAdapter;
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

        mAdapter = new MyReceiveTopicAdapter(mContext, R.layout.item_topic_list_main, replyTopicList);
        mAdapter.setOnClickListener(this);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onClick(View v) {

    }
}
