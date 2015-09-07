package com.tongban.im.fragment.discover;


import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.utils.DensityUtils;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.widget.header.RentalsSunHeaderView;
import com.tongban.im.R;
import com.tongban.im.activity.discover.SearchDiscoverActivity;
import com.tongban.im.adapter.DiscoverAdapter;
import com.tongban.im.api.ProductApi;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.TransferCenter;
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
public class DiscoverFragment extends BaseApiFragment implements View.OnClickListener, PtrHandler {

    private TextView tvUsername;
    private TextView tvUserTag;
    private ImageView ivUserIcon;
    private ImageView ivSearchAll;
    private PtrFrameLayout ptrFrameLayout;
    private ListView mListView;
    private DiscoverAdapter mAdapter;
    private List<Discover> mDiscovers;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_discover;
    }

    @Override
    protected void initView() {
        tvUsername = (TextView) mView.findViewById(R.id.tv_user_name);
        tvUserTag = (TextView) mView.findViewById(R.id.tv_user_tag);
        ivUserIcon = (ImageView) mView.findViewById(R.id.iv_user_portrait);
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
//        ptrFrameLayout.autoRefresh();
    }

    @Override
    protected void initListener() {
        ivUserIcon.setOnClickListener(this);
        ivSearchAll.setOnClickListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TransferCenter.getInstance().startThemeDetails(mDiscovers.get(position).getTheme_id());
            }
        });
    }

    @Override
    protected void initData() {
        // 获取顶部个人信息
        if (!"".equals(SPUtils.get(mContext, Consts.USER_ID, "")))
            UserCenterApi.getInstance().fetchPersonalCenterInfo(this);
        // 获取首页数据
        ProductApi.getInstance().fetchHomeInfo(this);
    }

    @Override
    public void onClick(View v) {
        if (v == ivUserIcon) {
            TransferCenter.getInstance().startUserCenter(
                    SPUtils.get(mContext, Consts.USER_ID, "").toString());
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
        if (user != null) {
            if (user.getPortrait_url() != null && user.getPortrait_url().getMin() != null) {
                Glide.with(this).load(user.getPortrait_url().getMin()).into(ivUserIcon);
            }
            tvUsername.setText(user.getNick_name());
            tvUserTag.setText(user.getTags());
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
            if (mAdapter == null) {
                mAdapter = new DiscoverAdapter(mContext, mDiscovers);
                mListView.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }
            // 请求收藏数量数据并更新
            int floor = 0; // 楼层
            for (Discover discover : mDiscovers) {
                ProductApi.getInstance().fetchThemeCollectedAmount(floor, discover.getTheme_id(), this);
                floor++;
            }
        }
        if (ptrFrameLayout.isRefreshing())
            ptrFrameLayout.refreshComplete();
    }

    /**
     * 获取专题收藏数量成功的回调
     *
     * @param event
     */
    public void onEventMainThread(BaseEvent.FetchThemeCollectedAmount event) {
        Discover discover = mDiscovers.get(event.floor);
        discover.setCollect_amount(event.amount);
        // 得到要更新的item的view
        View view = mListView.getChildAt(event.floor);
        try {
            // 从view中取得holder
            DiscoverAdapter.ViewHolder holder = (DiscoverAdapter.ViewHolder) view.getTag();
            // 设置收藏数量
            holder.collectAmount.setText(String.valueOf(event.amount));
        } catch (NullPointerException e) {

        }
    }

    /**
     * 接口返回错误回调
     *
     * @param obj
     */
    public void onEventMainThread(ApiErrorResult obj) {
        if (ptrFrameLayout.isRefreshing())
            ptrFrameLayout.refreshComplete();
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
