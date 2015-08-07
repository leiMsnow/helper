package com.tongban.im.adapter;

import android.content.Context;
import android.view.View;

import com.baidu.mapapi.search.core.PoiInfo;
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
public class PoiSearchAdapter extends QuickAdapter<PoiInfo> {

    private String currentSelected = "";

    public void setCurrentSelected(String currentSelected) {
        this.currentSelected = currentSelected;
    }

    public PoiSearchAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, PoiInfo item) {
        if (item.name.equals(currentSelected)){
            helper.setVisible(R.id.iv_poi_selected,View.VISIBLE);
        }
        helper.setText(R.id.tv_poi_name, item.name);
        helper.setText(R.id.tv_poi_address, item.address);

    }
}
