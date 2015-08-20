package com.tongban.im.fragment.topic;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.tongban.im.activity.topic.CreateTopicActivity;
import com.tongban.im.activity.PhotoViewPagerActivity;
import com.tongban.im.activity.topic.SearchTopicActivity;
import com.tongban.im.activity.topic.TopicDetailsActivity;
import com.tongban.im.adapter.TopicAdapter;
import com.tongban.im.api.TopicApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.BaseEvent;
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

    private ListView lvTopicList;
    private TopicAdapter mAdapter;
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
        tvTitle.setText(getResources().getString(R.string.topic));
        if (getArguments() != null) {
            boolean toolbarDisplay = getArguments().getBoolean(Consts.KEY_TOPIC_TOOLBAR_DISPLAY, true);
            if (!toolbarDisplay) {
                rlToolBar.setVisibility(View.GONE);
            } else {
                rlToolBar.setVisibility(View.VISIBLE);
            }
        }

        //TopicApi.getInstance().recommendTopicList(0, 10, this);

        List<Topic> listsByHot = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Topic topic = new Topic();
            topic.setContentType(Topic.TEXT);
            if (i % 2 == 0) {
                topic.setContentType(Topic.IMAGE);
//                List<String> smallUrls = new ArrayList<>();
//                smallUrls.add("http://img2.3lian.com/2014/f7/5/d/22.jpg");
//                smallUrls.add("http://pic4.nipic.com/20090803/2618170_095921092_2.jpg");
//                smallUrls.add("http://pic2.nipic.com/20090427/2390580_091546018_2.jpg");
//                smallUrls.add("http://pic2.nipic.com/20090413/406638_125424003_2.jpg");
//                smallUrls.add("http://pic25.nipic.com/20121115/6357173_141620329300_2.jpg");
//                smallUrls.add("http://image.tianjimedia.com/uploadImages/2012/236/5UADNJV31013.jpg");
//                smallUrls.add("http://pic31.nipic.com/20130708/12246968_161410243000_2.jpg");
//                smallUrls.add("http://pic.58pic.com/58pic/11/10/80/20X58PICzs8.jpg");
//                smallUrls.add("http://img3.3lian.com/2013/s1/20/d/56.jpg");
//                smallUrls.add("http://image.tianjimedia.com/uploadImages/2013/022/66DHZ1AXR1IT.jpg");
//                smallUrls.add("http://img3.3lian.com/2013/s1/20/d/57.jpg");
//                smallUrls.add("http://f0.topit.me/0/9b/cd/11438440895facd9b0o.jpg");
//                smallUrls.add("http://www.loveq.cn/store/photo/423/796/423796/328954/1235287844929077825.jpg");
//                smallUrls.add("http://pic.58pic.com/58pic/13/87/82/27Q58PICYje_1024.jpg");
//                smallUrls.add("http://img.taopic.com/uploads/allimg/110915/15-1109150Q30812.jpg");
//                topic.setSmallUrl(smallUrls);
            }
            topic.setTopic_content("RayRay的爸爸：#食物中含有硫酸锌？酸奶？#" + i);
            topic.setTopic_title("什么食物中含有硫酸锌？" + i);
//            topic.setTopicReplyNum("评论" + i);
//            topic.setTopicPraiseNum("赞" + i);
//            topic.setTopicTime("2015-08-05");
            User user = new User();
            user.setSex("男");
            user.setAddress("北京");
            user.setAge(i + "岁宝宝");
            user.setPortrait_url("http://b.hiphotos.baidu.com/image/pic/item/dbb44aed2e738bd4a244792ca38b87d6277ff942.jpg");
            user.setNick_name("小明" + i);
//            topic.setUser(user);
            listsByHot.add(topic);
        }
        mAdapter = new TopicAdapter(mContext, R.layout.item_topic_list_main, listsByHot);
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
                    ArrayList<String> urls = (ArrayList<String>) v.getTag(Integer.MAX_VALUE);
                    startPhotoView(urls, 0);
                    break;
                case R.id.iv_small_img_2:
                    urls = (ArrayList<String>) v.getTag(Integer.MAX_VALUE);
                    startPhotoView(urls, 1);
                    break;
                case R.id.iv_small_img_3:
                    urls = (ArrayList<String>) v.getTag(Integer.MAX_VALUE);
                    startPhotoView(urls, 2);
                    break;
            }
        }

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
    }
}
