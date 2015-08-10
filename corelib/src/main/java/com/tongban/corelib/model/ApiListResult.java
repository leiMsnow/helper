package com.tongban.corelib.model;

import java.util.List;

/**
 * 列表数据接口的model
 * Created by Cheney on 15/8/10.
 */
public class ApiListResult<T> {
    /**
     * 状态码
     */
    public int statusCode;
    /**
     * 状态描述
     */
    public String statusDesc;
    /**
     * 数据(包含result和分页信息两部分)
     */
    public Data<T> data;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public Data<T> getData() {
        return data;
    }

    public void setData(Data<T> data) {
        this.data = data;
    }

    public static class Data<T> {
        /**
         * 列表数据
         */
        public List<T> searchResult;
        /**
         * 分页信息
         */
        public Pagination pagination;

        public List<T> getSearchResult() {
            return searchResult;
        }

        public void setSearchResult(List<T> searchResult) {
            this.searchResult = searchResult;
        }

        public Pagination getPagination() {
            return pagination;
        }

        public void setPagination(Pagination pagination) {
            this.pagination = pagination;
        }
    }

    public static class Pagination {
        /**
         * 每页的数据量
         */
        public int page_size;
        /**
         * 第几页
         */
        public int page_num;
        /**
         * 总页数
         */
        public int page_no;

        public int getPage_size() {
            return page_size;
        }

        public void setPage_size(int page_size) {
            this.page_size = page_size;
        }

        public int getPage_num() {
            return page_num;
        }

        public void setPage_num(int page_num) {
            this.page_num = page_num;
        }

        public int getPage_no() {
            return page_no;
        }

        public void setPage_no(int page_no) {
            this.page_no = page_no;
        }
    }
}
