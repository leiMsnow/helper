package com.tongban.im.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tongban.corelib.utils.DensityUtils;
import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.corelib.widget.view.ptz.PullToZoomListViewEx;
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

    private PullToZoomListViewEx lvUserCenter;

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
        lvUserCenter = (PullToZoomListViewEx) findViewById(R.id.lv_user_center);
    }

    @Override
    protected void initData() {
        String[] adapterData = new String[]{"Activity", "Service", "Content Provider", "Intent", "BroadcastReceiver", "ADT", "Sqlite3", "HttpClient",
                "DDMS", "Android Studio", "Fragment", "Loader", "Activity", "Service", "Content Provider", "Intent",
                "BroadcastReceiver", "ADT", "Sqlite3", "HttpClient", "Activity", "Service", "Content Provider", "Intent",
                "BroadcastReceiver", "ADT", "Sqlite3", "HttpClient"};

        lvUserCenter.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, adapterData));
        int mScreenWidth = ScreenUtils.getScreenWidth(mContext);
        AbsListView.LayoutParams localObject = new AbsListView.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
        lvUserCenter.setHeaderLayoutParams(localObject);
    }

    @Override
    protected void initListener() {

    }

}
