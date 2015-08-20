package com.tongban.im.model;

/**
 * Created by fushudi on 2015/8/10.
 */
public class Product {
    //产品头像
    private String product_icon_url;
    //产品名称
    private String product_name;
    //产品内容（图片）
    private String product_url;
    //产品介绍
    private String productIntroduction;
    //产品参数
    private String productParameter;
    //产品优势
    private String productAdvantage;
    //产品不足
    private String productDisAdvantage;
    //产品收藏数量
    private String productCollectNum;
    //产品描述
    private String product_description;

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProductIntroduction() {
        return productIntroduction;
    }

    public void setProductIntroduction(String productIntroduction) {
        this.productIntroduction = productIntroduction;
    }

    public String getProductParameter() {
        return productParameter;
    }

    public void setProductParameter(String productParameter) {
        this.productParameter = productParameter;
    }

    public String getProductAdvantage() {
        return productAdvantage;
    }

    public void setProductAdvantage(String productAdvantage) {
        this.productAdvantage = productAdvantage;
    }

    public String getProductDisAdvantage() {
        return productDisAdvantage;
    }

    public void setProductDisAdvantage(String productDisAdvantage) {
        this.productDisAdvantage = productDisAdvantage;
    }

    public String getProductCollectNum() {
        return productCollectNum;
    }

    public void setProductCollectNum(String productCollectNum) {
        this.productCollectNum = productCollectNum;
    }

    public String getProduct_icon_url() {
        return product_icon_url;
    }

    public void setProduct_icon_url(String product_icon_url) {
        this.product_icon_url = product_icon_url;
    }

    public String getProduct_url() {
        return product_url;
    }

    public void setProduct_url(String product_url) {
        this.product_url = product_url;
    }
}
