package com.tongban.im.fragment.discover;


import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.corelib.utils.LogUtil;
import com.tongban.im.R;
import com.tongban.im.activity.discover.MultiProductActivity;
import com.tongban.im.activity.discover.SearchDiscoverActivity;
import com.tongban.im.activity.user.PersonalCenterActivity;
import com.tongban.im.adapter.DiscoverAdapter;
import com.tongban.im.api.ProductApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.Discover;

import java.util.List;

/**
 * 发现页
 * author: chenenyu 15/7/13
 */
public class DiscoverFragment extends BaseApiFragment implements View.OnClickListener {

    private ImageView ivUserIcon;
    private ImageView ivSearchAll;
    private ListView mListView;
    private DiscoverAdapter mAdapter;
    private List<Discover> mDiscovers;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_discover;
    }

    @Override
    protected void initView() {
        ivUserIcon = (ImageView) mView.findViewById(R.id.iv_user_portrait);
        ivSearchAll = (ImageView) mView.findViewById(R.id.iv_search_all);
        mListView = (ListView) mView.findViewById(R.id.lv_discover);
    }

    @Override
    protected void initListener() {
        ivUserIcon.setOnClickListener(this);
        ivSearchAll.setOnClickListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, MultiProductActivity.class);
                intent.putExtra(Consts.KEY_MULTI_PRODUCT_ID, mDiscovers.get(position).getTheme_id());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        ProductApi.getInstance().fetchHomeInfo(this);
    }

    @Override
    public void onStart() {
        super.onStart();
//        if (mAdapter != null) {
//            mListView.setAdapter(mAdapter);
//        }
    }

    @Override
    public void onClick(View v) {
        if (v == ivUserIcon) {
            mContext.startActivity(new Intent(mContext, PersonalCenterActivity.class));
        } else if (v == ivSearchAll) {
            mContext.startActivity(new Intent(mContext, SearchDiscoverActivity.class));
        }
    }

    /**
     * 获取首页数据成功的事件
     *
     * @param homeInfo {@link BaseEvent.FetchHomeInfo}
     */
    public void onEventMainThread(BaseEvent.FetchHomeInfo homeInfo) {
        mDiscovers = homeInfo.getList();
        if (mDiscovers != null && mDiscovers.size() > 0) {
            mAdapter = new DiscoverAdapter(mContext, mDiscovers);
            mListView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
//        mListView.setAdapter(null);
    }
}
