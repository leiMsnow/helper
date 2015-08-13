package com.tongban.im.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.corelib.fragment.PhotoViewFragment;
import com.tongban.im.R;
import com.tongban.im.activity.CreateTopicActivity;
import com.tongban.im.activity.PhotoViewPagerActivity;
import com.tongban.im.activity.SearchTopicActivity;
import com.tongban.im.activity.TopicDetailsActivity;
import com.tongban.im.adapter.TopicAdapter;
import com.tongban.im.model.Topic;
import com.tongban.im.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 话题/动态页
 * author: chenenyu 15/7/13
 */
public class TopicFragment extends BaseApiFragment implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private ListView mListView;
    private TopicAdapter mAdapter;
    private FloatingActionButton mFab;
    private Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_topic;
    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) mView.findViewById(R.id.in_toolbar);
        toolbar.setTitle(R.string.topic);
        mListView = (ListView) mView.findViewById(R.id.lv_topic_list);
        mFab = (FloatingActionButton) mView.findViewById(R.id.fab_add);

    }

    @Override
    protected void initListener() {
        mListView.setOnItemClickListener(this);
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
        List<Topic> listsByHot = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Topic topic = new Topic();
            topic.setContentType(Topic.TEXT);
            if (i % 2 == 0) {
                topic.setContentType(Topic.IMAGE);
                List<String> smallUrls = new ArrayList<>();
                smallUrls.add("http://img2.3lian.com/2014/f7/5/d/22.jpg");
                smallUrls.add("http://img2.3lian.com/2014/f7/5/d/22.jpg");
                smallUrls.add("http://img2.3lian.com/2014/f7/5/d/22.jpg");
                topic.setSmallUrl(smallUrls);
            }
            topic.setTopicContent("RayRay的爸爸：#食物中含有硫酸锌？酸奶？#" + i);
            topic.setTopicName("什么食物中含有硫酸锌？" + i);
            topic.setTopicReplyNum("评论" + i);
            topic.setTopicPraiseNum("赞" + i);
            topic.setTopicTime("2015-08-05");
            User user = new User();
            user.setSex("男");
            user.setAddress("北京");
            user.setAge(i + "岁宝宝");
            user.setPortrait_url("http://b.hiphotos.baidu.com/image/pic/item/dbb44aed2e738bd4a244792ca38b87d6277ff942.jpg");
            user.setNick_name("小明" + i);
            topic.setUser(user);
            listsByHot.add(topic);
        }
        mAdapter = new TopicAdapter(mContext, listsByHot);
        mAdapter.setOnClickListener(this);
        mListView.setAdapter(mAdapter);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_topic, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            Intent intent = new Intent(mContext, SearchTopicActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_topic_img) {
            ArrayList<String> urls = (ArrayList<String>) v.getTag(Integer.MAX_VALUE);
            Intent intent = new Intent(mContext, PhotoViewPagerActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(PhotoViewFragment.KEY_URL, urls);
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(mContext, TopicDetailsActivity.class));

    }
}
