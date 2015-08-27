package com.tongban.im.model;

import java.util.List;

/**
 * 图书的model
 * Created by Cheney on 15/8/19.
 */
public class ProductBook {
    // 图书id
    private String product_id;
    // 图书名
    private String product_name;
    // 图书类型(图书,童书等)
    private String product_type;
    // 商品标签
    private String product_tags;
    // 商品图片
    private List<ImageUrl> product_img_url;
    // 商品在不同渠道的价格信息
    private List<PriceInfo> price_info;
    // 外部平台商品详情页链接
    private String product_url;
    // 图书作者
    private String book_author;
    // 出版社
    private String publisher;
    // 出版时间
    private String publish_time;
    // ISBN号码
    private String isbn;
    // 适读人群
    private String suitable_for;
    // 适读情景
    private String scene_for;
    // 内容简介
    private String book_content_desc;
    // 作者简介
    private String author_desc;
    // 图书目录
    private String book_catalog;
    // 推荐指数
    private int recommend_expo;
    // 推荐理由
    private String recommend_cause;
    // 缺点
    private String weakness;
    // 状态 0表示无效 1表示有效
    private String status;
    // 创建时间
    private long c_time;
    // 修改时间
    private long m_time;
    // 收藏状态
    private boolean collect_status;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getProduct_tags() {
        return product_tags;
    }

    public void setProduct_tags(String product_tags) {
        this.product_tags = product_tags;
    }

    public List<ImageUrl> getProduct_img_url() {
        return product_img_url;
    }

    public void setProduct_img_url(List<ImageUrl> product_img_url) {
        this.product_img_url = product_img_url;
    }

    public List<PriceInfo> getPrice_info() {
        return price_info;
    }

    public void setPrice_info(List<PriceInfo> price_info) {
        this.price_info = price_info;
    }

    public String getProduct_url() {
        return product_url;
    }

    public void setProduct_url(String product_url) {
        this.product_url = product_url;
    }

    public String getBook_author() {
        return book_author;
    }

    public void setBook_author(String book_author) {
        this.book_author = book_author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getSuitable_for() {
        return suitable_for;
    }

    public void setSuitable_for(String suitable_for) {
        this.suitable_for = suitable_for;
    }

    public String getScene_for() {
        return scene_for;
    }

    public void setScene_for(String scene_for) {
        this.scene_for = scene_for;
    }

    public String getBook_content_desc() {
        return book_content_desc;
    }

    public void setBook_content_desc(String book_content_desc) {
        this.book_content_desc = book_content_desc;
    }

    public String getAuthor_desc() {
        return author_desc;
    }

    public void setAuthor_desc(String author_desc) {
        this.author_desc = author_desc;
    }

    public String getBook_catalog() {
        return book_catalog;
    }

    public void setBook_catalog(String book_catalog) {
        this.book_catalog = book_catalog;
    }

    public int getRecommend_expo() {
        return recommend_expo;
    }

    public void setRecommend_expo(int recommend_expo) {
        this.recommend_expo = recommend_expo;
    }

    public String getRecommend_cause() {
        return recommend_cause;
    }

    public void setRecommend_cause(String recommend_cause) {
        this.recommend_cause = recommend_cause;
    }

    public String getWeakness() {
        return weakness;
    }

    public void setWeakness(String weakness) {
        this.weakness = weakness;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getC_time() {
        return c_time;
    }

    public void setC_time(long c_time) {
        this.c_time = c_time;
    }

    public long getM_time() {
        return m_time;
    }

    public void setM_time(long m_time) {
        this.m_time = m_time;
    }

    public boolean isCollect_status() {
        return collect_status;
    }

    public void setCollect_status(boolean collect_status) {
        this.collect_status = collect_status;
    }
}
