package com.tongban.im.fragment.discover;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.tongban.corelib.base.adapter.IMultiItemTypeSupport;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.widget.view.CircleImageView;
import com.tongban.im.R;
import com.tongban.im.activity.discover.SearchDiscoverActivity;
import com.tongban.im.adapter.DiscoverAdapter;
import com.tongban.im.api.ProductApi;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.fragment.base.BaseToolBarFragment;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.discover.Discover;
import com.tongban.im.model.user.User;
import com.tongban.im.utils.PTRHeaderUtils;
import com.voice.tongban.activity.VoiceInputActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 发现页
 * author: chenenyu 15/7/13
 */
public class DiscoverFragment extends BaseToolBarFragment
        implements PtrHandler {

    @Bind(R.id.lv_discover)
    ListView lvDiscover;
    @Bind(R.id.fragment_ptr_home_ptr_frame)
    PtrFrameLayout ptrFrameLayout;
    @Bind(R.id.iv_user_portrait)
    CircleImageView ivUserPortrait;
    @Bind(R.id.iv_search_all)
    ImageButton ivSearchAll;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.fab_add)
    FloatingActionButton fabAdd;

    private DiscoverAdapter mAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_discover;
    }

    @Override
    protected void initData() {

        tvTitle.setText(getString(R.string.discover));
        tvTitle.setVisibility(View.VISIBLE);
        ivUserPortrait.setVisibility(View.VISIBLE);
        ivSearchAll.setVisibility(View.VISIBLE);

        PTRHeaderUtils.getMaterialView(mContext, ptrFrameLayout);
        ptrFrameLayout.setPtrHandler(this);
        ptrFrameLayout.autoRefresh(true);
        // 显示默认头像
        if ("".equals(SPUtils.get(mContext, Consts.USER_ID, ""))) {
            ivUserPortrait.setImageResource(Consts.getUserDefaultPortrait());
        }
        // 登录后显示真实头像
        else {
            UserCenterApi.getInstance().fetchPersonalCenterInfo(this);
        }
        mAdapter = new DiscoverAdapter(mContext, null, new IMultiItemTypeSupport<Discover>() {
            @Override
            public int getLayoutId(int position, Discover discover) {
                switch (Integer.parseInt(discover.getComponent_id())) {
                    case 1: // 横排3图
                        return R.layout.item_discover_img3_horizontal;
                    case 2:// 竖排3图
                        return R.layout.item_discover_img3_vertical;
                    case 3:// 图文单图
                        return R.layout.item_discover_text_img;
                    case 4:// 单图
                        return R.layout.item_discover_img;
                    default:
                        return 0;

                }

            }

            @Override
            public int getViewTypeCount() {
                return 4;
            }

            @Override
            public int getItemViewType(int position, Discover discover) {
                return Integer.parseInt(discover.getComponent_id()) - 1;
            }
        });
        lvDiscover.setAdapter(mAdapter);
    }

    @OnClick({R.id.iv_user_portrait, R.id.iv_search_all, R.id.fab_add})
    public void onClick(View v) {
        if (v == ivUserPortrait) {
            TransferCenter.getInstance().startUserCenter(
                    SPUtils.get(mContext, Consts.USER_ID, "").toString(), true);
        } else if (v == ivSearchAll) {
            startActivity(new Intent(mContext, SearchDiscoverActivity.class));
        } else if (v == fabAdd) {
            startActivity(new Intent(mContext, VoiceInputActivity.class));
        }
    }

    @OnItemClick(R.id.lv_discover)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TransferCenter.getInstance().startThemeDetails(mAdapter.getItem(position).getTheme_id());
    }

    /**
     * 获取个人数据的Event
     *
     * @param userInfo {@link BaseEvent.PersonalCenterEvent}
     */
    public void onEventMainThread(BaseEvent.PersonalCenterEvent userInfo) {
        User user = userInfo.user;
        if (user != null && user.getPortrait_url() != null) {
            setUserPortrait(user.getPortrait_url().getMin(), ivUserPortrait);
        }
    }

    /**
     * 获取首页数据成功的事件
     *
     * @param homeInfo {@link BaseEvent.FetchHomeInfo}
     */
    public void onEventMainThread(BaseEvent.FetchHomeInfo homeInfo) {
        if (ptrFrameLayout.isRefreshing())
            ptrFrameLayout.refreshComplete();
        if (homeInfo.list != null && homeInfo.list.size() > 0) {
            lvDiscover.setVisibility(View.VISIBLE);
            mAdapter.replaceAll(homeInfo.list);
            // 请求收藏数量数据并更新
            int floor = 0; // 楼层
            for (Discover discover : homeInfo.list) {
                ProductApi.getInstance().fetchThemeCollectedAmount(floor, discover.getTheme_id(), this);
                floor++;
            }
        }

    }

    /**
     * 获取专题收藏数量成功的回调
     *
     * @param event
     */
    public void onEventMainThread(BaseEvent.FetchThemeCollectedAmount event) {
        mAdapter.getItem(event.floor).setCollect_amount(event.amount);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 接口返回错误回调
     *
     * @param obj
     */
    public void onEventMainThread(ApiErrorResult obj) {
        if (obj.getApiName().equals(ProductApi.FETCH_HOME_INFO)) {
            if (ptrFrameLayout.isRefreshing())
                ptrFrameLayout.refreshComplete();
            if (mAdapter != null && mAdapter.getCount() > 0) {
                hideEmptyView();
            }
        }
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View view, View view1) {
        return PtrDefaultHandler.checkContentCanBePulledDown(ptrFrameLayout, view, view1);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
        onRequest();
    }

    @Override
    public void onRequest() {
        // 获取首页数据
        ProductApi.getInstance().fetchHomeInfo(this);
    }

}
