package com.tb.api.callback;

/**
 * 上传文件回调接口
 * Created by zhangleilei on 8/18/15.
 */
public interface UploadVoiceCallback {
    /**
     * * 上传文件成功
     *
     * @param url 图片对象
     */
    void uploadSuccess(String url);

    /**
     * 上传失败
     *
     * @param error
     */
    void uploadFailed(String error);

}
