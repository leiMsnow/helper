package com.tongban.im.api;

/**
 * 上传文件回调接口
 * Created by zhangleilei on 8/18/15.
 */
public interface UploadFileCallback {
    /**
     * * 上传文件成功
     *
     * @param minUrl 小图
     * @param midUrl 中图
     * @param maxUrl 大图
     */
    void uploadSuccess(String minUrl, String midUrl, String maxUrl);
}
