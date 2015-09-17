package com.tongban.im.fragment.discover;


import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.tongban.corelib.base.adapter.IMultiItemTypeSupport;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.utils.DensityUtils;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.widget.header.RentalsSunHeaderView;
import com.tongban.im.R;
import com.tongban.im.activity.discover.SearchDiscoverActivity;
import com.tongban.im.adapter.DiscoverAdapter;
import com.tongban.im.api.ProductApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.fragment.base.BaseToolBarFragment;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.Discover;
import com.tongban.im.model.User;

import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 发现页
 * author: chenenyu 15/7/13
 */
public class DiscoverFragment extends BaseToolBarFragment implements View.OnClickListener, PtrHandler {

    private ImageView ivUserPortrait;
    private ImageView ivSearchAll;
    private PtrFrameLayout ptrFrameLayout;
    private ListView mListView;
    private DiscoverAdapter mAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_discover;
    }

    @Override
    protected void initView() {
        ivUserPortrait = (ImageView) mView.findViewById(R.id.iv_user_portrait);
        ivSearchAll = (ImageView) mView.findViewById(R.id.iv_search_all);
        ptrFrameLayout = (PtrFrameLayout) mView.findViewById(R.id.fragment_ptr_home_ptr_frame);
        mListView = (ListView) mView.findViewById(R.id.lv_discover);

        RentalsSunHeaderView header = new RentalsSunHeaderView(mContext);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, DensityUtils.dp2px(mContext, 16), 0, DensityUtils.dp2px(mContext, 16));
        header.setUp(ptrFrameLayout);

        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.setPtrHandler(this);
    }

    @Override
    protected void initListener() {
        ivUserPortrait.setOnClickListener(this);
        ivSearchAll.setOnClickListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TransferCenter.getInstance().startThemeDetails(mAdapter.getItem(position).getTheme_id());
            }
        });
    }

    @Override
    protected void initData() {
        // 登录后显示真实头像
        if ("".equals(SPUtils.get(mContext, Consts.USER_ID, ""))) {
            // 显示默认头像
            ivUserPortrait.setImageResource(Consts.getUserDefaultPortrait());
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
        mListView.setAdapter(mAdapter);
        // 获取首页数据
        ProductApi.getInstance().fetchHomeInfo(this);
    }

    @Override
    public void onClick(View v) {
        if (v == ivUserPortrait) {
            TransferCenter.getInstance().startUserCenter(
                    SPUtils.get(mContext, Consts.USER_ID, "").toString(), true);
        } else if (v == ivSearchAll) {
            mContext.startActivity(new Intent(mContext, SearchDiscoverActivity.class));
        }
    }

    /**
     * 获取个人数据的Event
     *
     * @param userInfo {@link com.tongban.im.model.BaseEvent.PersonalCenterEvent}
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

        if (homeInfo != null && homeInfo.getList().size() > 0) {
            mAdapter.replaceAll(homeInfo.getList());
            // 请求收藏数量数据并更新
            int floor = 0; // 楼层
            for (Discover discover : homeInfo.getList()) {
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
        if (ptrFrameLayout.isRefreshing())
            ptrFrameLayout.refreshComplete();
        if (mAdapter.getCount() > 0) {
            showEmptyText("", false);
        }
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View view, View view1) {
        return PtrDefaultHandler.checkContentCanBePulledDown(ptrFrameLayout, view, view1);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
        // 获取首页数据
        ProductApi.getInstance().fetchHomeInfo(this);
    }
}
