package com.tongban.im.fragment.user;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.tongban.corelib.fragment.PhotoViewFragment;
import com.tongban.corelib.widget.view.LoadMoreListView;
import com.tongban.corelib.widget.view.listener.OnLoadMoreListener;
import com.tongban.im.R;
import com.tongban.im.activity.PhotoViewPagerActivity;
import com.tongban.im.adapter.TopicListAdapter;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.fragment.base.BaseToolBarFragment;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.ImageUrl;
import com.tongban.im.model.topic.Topic;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * 我的话题（我的收藏中的、我发起的话题）
 *
 * @author fushudi
 */
public class MyTopicFragment extends BaseToolBarFragment implements
        OnLoadMoreListener {

    @Bind(R.id.lv_topic_list)
    LoadMoreListView lvTopicList;

    private TopicListAdapter mAdapter;

    private int mCursor = 0;
    private int mPageSize = 10;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_my_topic;
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            //我的收藏 - 话题列表
            if (getArguments().getInt(Consts.KEY_MY_TOPIC_LIST) == Topic.MY_COLLECT_TOPIC_LIST) {
                UserCenterApi.getInstance().fetchCollectTopicList(mCursor, mPageSize, this);
            }
            //我发起的话题列表
            else if (getArguments().getInt(Consts.KEY_MY_TOPIC_LIST) == Topic.MY_SEND_TOPIC_LIST) {
                UserCenterApi.getInstance().fetchLaunchTopicList(mCursor, mPageSize, this);
            }
        }

        mAdapter = new TopicListAdapter(mContext, R.layout.item_topic_list_main, null);
        lvTopicList.setAdapter(mAdapter);
        lvTopicList.setPageSize(mPageSize);

        lvTopicList.setOnLoadMoreListener(this);
    }

    @OnClick({R.id.iv_small_img_1, R.id.iv_small_img_2, R.id.iv_small_img_3, R.id.iv_user_portrait})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_small_img_1:
                List<ImageUrl> imageUrls = (List<ImageUrl>) v.getTag(Integer.MAX_VALUE);
                PhotoViewPagerActivity.startPhotoView(mContext, setImageUrls(imageUrls), 0);
                break;
            case R.id.iv_small_img_2:
                imageUrls = (List<ImageUrl>) v.getTag(Integer.MAX_VALUE);
                PhotoViewPagerActivity.startPhotoView(mContext, setImageUrls(imageUrls), 1);
                break;
            case R.id.iv_small_img_3:
                imageUrls = (List<ImageUrl>) v.getTag(Integer.MAX_VALUE);
                PhotoViewPagerActivity.startPhotoView(mContext, setImageUrls(imageUrls), 2);
                break;
            case R.id.iv_user_portrait:
                String userId = v.getTag(Integer.MAX_VALUE).toString();
                TransferCenter.getInstance().startUserCenter(userId);
                break;
        }
    }

    //取出大图地址存入集合中
    private ArrayList<String> setImageUrls(List<ImageUrl> imageUrls) {
        ArrayList<String> urls = new ArrayList<>();
        for (int i = 0; i < imageUrls.size(); i++) {
            urls.add(imageUrls.get(i).getMax());
        }
        return urls;
    }


    @OnItemClick(R.id.lv_topic_list)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TransferCenter.getInstance().startTopicDetails(mAdapter.getItem(position));
    }

    /**
     * 获取我的话题列表Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.TopicListEvent obj) {
        mCursor++;
        mAdapter.addAll(obj.topicList);
        lvTopicList.setVisibility(View.VISIBLE);
        lvTopicList.setResultSize(obj.topicList.size());
    }

    @Override
    public void onLoadMore() {
        //我的收藏 - 话题列表
        if (getArguments().getInt(Consts.KEY_MY_TOPIC_LIST) == Topic.MY_COLLECT_TOPIC_LIST) {
            UserCenterApi.getInstance().fetchCollectTopicList(mCursor, mPageSize, this);
        }
        //我发起的话题列表
        else if (getArguments().getInt(Consts.KEY_MY_TOPIC_LIST) == Topic.MY_SEND_TOPIC_LIST) {
            UserCenterApi.getInstance().fetchLaunchTopicList(mCursor, mPageSize, this);
        }
    }
}
