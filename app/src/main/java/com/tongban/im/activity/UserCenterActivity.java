package com.tongban.im.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.tongban.corelib.utils.DensityUtils;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

/**
 * 用户中心
 */
public class UserCenterActivity extends BaseToolBarActivity {

    private ListView lvUserCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_user_center;
    }

    @Override
    protected void initView() {
        lvUserCenter = (ListView) findViewById(R.id.lv_user_center);
        final PtrFrameLayout ptrFrameLayout = (PtrFrameLayout) findViewById(R.id.ptr_frame);
        StoreHouseHeader header = new StoreHouseHeader(mContext);
        header.setPadding(0, DensityUtils.dp2px(mContext,20), 0, DensityUtils.dp2px(mContext,20));
        header.initWithString("Ultra PTR");

        ptrFrameLayout.setDurationToCloseHeader(1500);
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrameLayout.refreshComplete();
                    }
                }, 1500);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

}
