package com.tongban.im.adapter;

import android.content.Context;

import com.tb.api.model.user.User;
import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;
import com.tongban.im.common.Consts;

import java.util.List;

/**
 * 达人 can
 * Created by zhangleilei on 15/11/10.
 */
public class TalentCanDoAdapter extends QuickAdapter<String> {


    public TalentCanDoAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, String item) {

    }
}
