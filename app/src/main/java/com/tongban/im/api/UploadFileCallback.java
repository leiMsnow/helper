package com.tongban.im.api;

import com.tongban.im.model.ImageUrl;

/**
 * 上传文件回调接口
 * Created by zhangleilei on 8/18/15.
 */
public interface UploadFileCallback {
    /**
     * * 上传文件成功
     *
     * @param url 图片对象
     */
    void uploadSuccess(ImageUrl url);
}
