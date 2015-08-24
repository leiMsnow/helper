package com.tongban.im.fragment.user;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.corelib.fragment.PhotoViewFragment;
import com.tongban.im.R;
import com.tongban.im.activity.PhotoViewPagerActivity;
import com.tongban.im.activity.topic.CreateTopicActivity;
import com.tongban.im.activity.topic.SearchTopicActivity;
import com.tongban.im.activity.topic.TopicDetailsActivity;
import com.tongban.im.adapter.TopicListAdapter;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.ImageUrl;
import com.tongban.im.model.Topic;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 我的话题（我的收藏中的、我发起的话题）
 *
 * @author fushudi
 */
public class MyTopicFragment extends BaseApiFragment implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private ListView lvTopicList;
    private TopicListAdapter mAdapter;
    private FloatingActionButton mFab;
    private ImageView ivSearch;
    private TextView tvTitle;
    private RelativeLayout rlToolBar;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_topic;
    }

    @Override
    protected void initView() {
        rlToolBar = (RelativeLayout) mView.findViewById(R.id.rl_toolbar);
        tvTitle = (TextView) mView.findViewById(R.id.tv_title);
        ivSearch = (ImageView) mView.findViewById(R.id.iv_search_topic);
        lvTopicList = (ListView) mView.findViewById(R.id.lv_topic_list);
        mFab = (FloatingActionButton) mView.findViewById(R.id.fab_add);

    }

    @Override
    protected void initListener() {
        ivSearch.setOnClickListener(this);
        lvTopicList.setOnItemClickListener(this);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CreateTopicActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            if (getArguments().getInt(Consts.KEY_MY_TOPIC_LIST) == Topic.MY_COLLECT_TOPIC_LIST) {
                UserCenterApi.getInstance().fetchCollectTopicList(0, 10, this);
            } else if (getArguments().getInt(Consts.KEY_MY_TOPIC_LIST) == Topic.MY_SEND_TOPIC_LIST) {
                UserCenterApi.getInstance().fetchLaunchTopicList(0, 10, this);
            }
        }

        mAdapter = new TopicListAdapter(mContext, R.layout.item_topic_list_main, null);
        mAdapter.setOnClickListener(this);
        lvTopicList.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v == ivSearch) {
            Intent intent = new Intent(mContext, SearchTopicActivity.class);
            startActivity(intent);
        } else {
            switch (v.getId()) {
                case R.id.iv_small_img_1:
                    List<ImageUrl> imageUrls = (List<ImageUrl>) v.getTag(Integer.MAX_VALUE);
                    startPhotoView(setImageUrls(imageUrls), 0);
                    break;
                case R.id.iv_small_img_2:
                    imageUrls = (List<ImageUrl>) v.getTag(Integer.MAX_VALUE);
                    startPhotoView(setImageUrls(imageUrls), 1);
                    break;
                case R.id.iv_small_img_3:
                    imageUrls = (List<ImageUrl>) v.getTag(Integer.MAX_VALUE);
                    startPhotoView(setImageUrls(imageUrls), 2);
                    break;
            }
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

    private void startPhotoView(ArrayList<String> urls, int currentIndex) {
        Intent intent = new Intent(mContext, PhotoViewPagerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(PhotoViewFragment.KEY_URL, urls);
        bundle.putInt(PhotoViewFragment.KEY_CURRENT_INDEX, currentIndex);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(mContext, TopicDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Consts.KEY_TOPIC_ID, mAdapter.getItem(position).getTopic_id());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void onEventMainThread(BaseEvent.TopicListEvent obj) {

        mAdapter.replaceAll(obj.getTopicList());
        lvTopicList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }
}