package com.tb.api.callback;


import com.tb.api.model.ImageUrl;

import java.util.List;

/**
 * 批量上传文件回调接口
 * Created by zhangleilei on 8/18/15.
 */
public interface MultiUploadFileCallback {
    /**
     * * 上传文件成功
     *
     * @param urls 图片数组
     */
    void uploadSuccess(List<ImageUrl> urls);

    /**
     * 上传失败
     *
     * @param error
     */
    void uploadFailed(String error);
}
