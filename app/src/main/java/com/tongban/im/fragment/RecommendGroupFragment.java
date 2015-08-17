package com.tongban.im.fragment;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ListView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.im.R;
import com.tongban.im.activity.ChooseGroupTypeActivity;
import com.tongban.im.api.TopicApi;

/**
 * 推荐圈子的Fragment
 * Created by Cheney on 15/8/3.
 */
public class RecommendGroupFragment extends BaseApiFragment {
    private ListView mListView;
    private FloatingActionButton mFab;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_recommend_circle;
    }

    @Override
    protected void initView() {
        mListView = (ListView) mView.findViewById(R.id.lv_circle);
        mFab = (FloatingActionButton) mView.findViewById(R.id.fab_add);
    }

    @Override
    protected void initListener() {
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChooseGroupTypeActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
//        TopicApi.getInstance().recommendTopicList(this);
    }

}
