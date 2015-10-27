package com.tb.api.model.discover;

/**
 * 图书单品的价格和平台
 * Created by Cheney on 15/8/25.
 */
public class PriceInfo {
    private String platform;
    private String price;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
