package com.tongban.corelib.base.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.squareup.leakcanary.RefWatcher;
import com.tongban.corelib.base.BaseApplication;

public abstract class BaseFragment extends Fragment {

    protected Context mContext;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = BaseApplication.getRefWatcher();
        refWatcher.watch(this);
    }
}
