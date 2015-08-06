package com.tongban.im.adapter;

import android.content.Context;
import android.view.View;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;
import com.tongban.im.model.Label;
import com.tongban.im.model.PoiSearchInfo;

import java.util.List;

/**
 * 位置数据adapter
 * Created by zhangleilei on 2015/7/18.
 */
public class PoiSearchAdapter extends QuickAdapter<PoiSearchInfo> {

    public PoiSearchAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, PoiSearchInfo item) {
        if (item.isSelected()) {
            helper.setVisible(R.id.iv_poi_selected, View.VISIBLE);
        } else {
            helper.setVisible(R.id.iv_poi_selected, View.GONE);
        }
        helper.setText(R.id.tv_poi_name, item.name);
        helper.setText(R.id.tv_poi_address, item.address);

    }
}
