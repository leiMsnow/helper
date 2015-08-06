package com.tongban.im.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.Recorder;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.qiniu.android.storage.Zone;
import com.qiniu.android.storage.persistent.FileRecorder;
import com.tongban.corelib.utils.SDCardUtils;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.App;

import java.io.File;
import java.io.IOException;

/**
 * 文件上传到七牛服务器的api
 * Created by Cheney on 15/8/6.
 */
public class FileUploadApi {
    private static FileUploadApi mApi;
    private static Context mContext = App.getInstance().getApplicationContext();
    private static String dirPath = SDCardUtils.getSDCardPath() + "tongban" + File.separator
            + "images" + File.separator + "qiniu" + File.separator + "recorder";
    private static Recorder mRecorder;
    private static Configuration config;
    private static UploadManager mUploadManager;

    public static FileUploadApi getInstance() {
        if (mApi == null) {
            synchronized (FileUploadApi.class) {
                if (mApi == null) {
                    mApi = new FileUploadApi();
                    try {
                        mRecorder = new FileRecorder(dirPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    config = new Configuration.Builder()
                            .chunkSize(256 * 1024)  //分片上传时，每片的大小。 默认 256K
                            .putThreshhold(512 * 1024)  // 启用分片上传阀值。默认 512K
                            .connectTimeout(10) // 链接超时。默认 10秒
                            .responseTimeout(60) // 服务器响应超时。默认 60秒
                            .recorder(mRecorder)  // recorder 分片上传时，已上传片记录器。默认 null
                            .zone(Zone.zone0) // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                            .build();
                    mUploadManager = new UploadManager(config);
                }
            }
        }
        return mApi;
    }

    /**
     * 上传byte数组
     *
     * @param data     byte数组
     * @param key      上传数据名,可以为null
     * @param callback 回调
     */
    public void uploadFile(@NonNull byte[] data, @Nullable String key, @NonNull UpCompletionHandler callback) {
        uploadFile(data, key, callback, null);
    }

    /**
     * 上传byte数组
     *
     * @param data     byte数组
     * @param key      上传数据名,可以为null
     * @param callback 回调
     * @param options  配置项,进度监听/取消上传等
     */
    public void uploadFile(@NonNull byte[] data, @Nullable String key, @NonNull UpCompletionHandler callback,
                           @Nullable UploadOptions options) {
        String token = (String) SPUtils.get(mContext, "QINIU_TOKEN", "");
        mUploadManager.put(data, key, token, callback, options);
    }

    /**
     * 上传文件
     *
     * @param filePath 文件路径
     * @param key      上传文件名,可以为null
     * @param callback 回调
     */
    public void uploadFile(@NonNull String filePath, @Nullable String key, @NonNull UpCompletionHandler callback) {
        uploadFile(new File(filePath), key, callback);
    }

    /**
     * 上传文件
     *
     * @param filePath 文件路径
     * @param key      上传文件名,可以为null
     * @param callback 回调
     * @param options  配置项,进度监听/取消上传等
     */
    public void uploadFile(@NonNull String filePath, @Nullable String key, @NonNull UpCompletionHandler callback,
                           @Nullable UploadOptions options) {
        uploadFile(new File(filePath), key, callback, options);
    }

    /**
     * 上传文件
     *
     * @param file     文件
     * @param key      上传文件名,可以为null
     * @param callback 回调
     */
    public void uploadFile(@NonNull File file, @Nullable String key, @NonNull UpCompletionHandler callback) {
        uploadFile(file, key, callback, null);
    }

    /**
     * 上传文件
     *
     * @param file     文件
     * @param key      上传文件名,可以为null
     * @param callback 回调
     * @param options  配置项,进度监听/取消上传等
     */
    public void uploadFile(@NonNull File file, @Nullable String key, @NonNull UpCompletionHandler callback,
                           @Nullable UploadOptions options) {
        String token = (String) SPUtils.get(mContext, "QINIU_TOKEN", "");
        mUploadManager.put(file, key, token, callback, options);
    }

}
