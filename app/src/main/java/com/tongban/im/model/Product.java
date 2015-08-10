package com.tongban.im.model;

/**
 * Created by fushudi on 2015/8/10.
 */
public class Product {
    //产品名称
    private String productName;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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
}
