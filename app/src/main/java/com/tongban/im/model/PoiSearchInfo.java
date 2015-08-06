package com.tongban.im.model;

import com.baidu.mapapi.search.core.PoiInfo;

/**
 * 地图搜索结果
 * Created by zhangleilei on 15/8/6.
 */
public class PoiSearchInfo extends PoiInfo {

    private boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
