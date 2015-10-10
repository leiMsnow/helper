package com.tongban.im.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.Recorder;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.qiniu.android.storage.Zone;
import com.qiniu.android.storage.persistent.FileRecorder;
import com.tongban.corelib.base.api.IApiCallback;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.model.ApiResult;
import com.tongban.corelib.utils.LogUtil;
import com.tongban.corelib.utils.SDCardUtils;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.App;
import com.tongban.im.api.base.BaseApi;
import com.tongban.im.api.callback.MultiUploadFileCallback;
import com.tongban.im.api.callback.UploadFileCallback;
import com.tongban.im.api.callback.UploadVoiceCallback;
import com.tongban.im.common.Consts;
import com.tongban.im.model.ImageUrl;
import com.tongban.im.model.QiniuToken;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * 文件上传到七牛服务器的api
 * Created by Cheney on 15/8/6.
 */
public class FileUploadApi extends BaseApi {

    /**
     * 图片尺寸
     */
    public final static String IMAGE_SIZE_100 = "-100";
    public final static String IMAGE_SIZE_300 = "-300";
    public final static String IMAGE_SIZE_500 = "-500";


    private static FileUploadApi mApi;
    private static Context mContext = App.getInstance().getApplicationContext();
    private static String dirPath = SDCardUtils.getSDCardPath() + "tongban" + File.separator
            + "images" + File.separator + "qiniu" + File.separator + "recorder";
    private static Recorder mRecorder;
    private static Configuration config;
    private static UploadManager mUploadManager;

    // 获取上传token的url
    private String UPLOAD_TOKEN = "/token/require/qiniu/upload";

    private FileUploadApi(Context context) {
        super(context);
    }

    public static FileUploadApi getInstance() {
        if (mApi == null) {
            synchronized (FileUploadApi.class) {
                if (mApi == null) {
                    mApi = new FileUploadApi(App.getInstance());
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
     * 获取上传图片的Token
     */
    public void fetchUploadToken() {
        //有效时间
        long expireTime = 7 * 24 * 60 * 60;
        mParams = new HashMap<>();
        mParams.put("space_name", "tongban");
        mParams.put("expire_time", expireTime);
        simpleRequest(UPLOAD_TOKEN, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {

            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<QiniuToken> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<QiniuToken>>() {
                        });
                QiniuToken token = result.getData();
                SPUtils.put(mContext, Consts.QINIU_TOKEN, token.getUpload_token());
            }

            @Override
            public void onFailure(ApiErrorResult result) {

            }
        });
    }

    /**
     * 上传byte数组
     *
     * @param data     byte数组
     * @param key      上传数据名,可以为null
     * @param minSize  小图尺寸
     * @param midSize  中图尺寸
     * @param callback 回调
     */
    public void uploadFile(@NonNull byte[] data, @Nullable String key, String minSize, String midSize,
                           @NonNull UploadFileCallback callback) {
        uploadFile(data, key, minSize, midSize, callback, null);
    }

    /**
     * 上传byte数组
     *
     * @param data     byte数组
     * @param key      上传数据名,可以为null
     * @param minSize  小图尺寸
     * @param midSize  中图尺寸
     * @param callback 回调
     * @param options  配置项,进度监听/取消上传等
     */
    public void uploadFile(@NonNull byte[] data, @Nullable String key, String minSize, String midSize,
                           @NonNull final UploadFileCallback callback,
                           @Nullable UploadOptions options) {
        String token = getToken();
        mUploadManager.put(data, key, token, new MyUpCompletionHandler(minSize, midSize, callback), options);
    }

    /**
     * 上传文件
     *
     * @param filePath 文件路径
     * @param key      上传文件名,可以为null
     * @param minSize  小图尺寸
     * @param midSize  中图尺寸
     * @param callback 回调
     */
    public void uploadFile(@NonNull String filePath, @Nullable String key, String minSize, String midSize,
                           @NonNull UploadFileCallback callback) {
        uploadFile(new File(filePath), key, minSize, midSize, callback);
    }

    /**
     * 上传文件
     *
     * @param filePath 文件路径
     * @param key      上传文件名,可以为null
     * @param minSize  小图尺寸
     * @param midSize  中图尺寸
     * @param callback 回调
     * @param options  配置项,进度监听/取消上传等
     */
    public void uploadFile(@NonNull String filePath, @Nullable String key, String minSize, String midSize,
                           @NonNull UploadFileCallback callback,
                           @Nullable UploadOptions options) {
        uploadFile(new File(filePath), key, minSize, midSize, callback, options);
    }

    /**
     * 上传文件
     *
     * @param file     文件
     * @param key      上传文件名,可以为null
     * @param callback 回调
     */
    public void uploadFile(@NonNull File file, @Nullable String key, String minSize, String midSize,
                           @NonNull UploadFileCallback callback) {
        uploadFile(file, key, minSize, midSize, callback, null);
    }

    /**
     * 上传文件
     *
     * @param file     文件
     * @param key      上传文件名,可以为null
     * @param minSize  小图尺寸
     * @param midSize  中图尺寸
     * @param callback 回调
     * @param options  配置项,进度监听/取消上传等
     */
    public void uploadFile(@NonNull File file, @Nullable String key, String minSize, String midSize,
                           @NonNull UploadFileCallback callback,
                           @Nullable UploadOptions options) {
        String token = getToken();
        mUploadManager.put(file, key, token
                , new MyUpCompletionHandler(minSize, midSize, callback), options);
    }

    /**
     * 上传语音文件
     *
     * @param filePath
     * @param callback
     */
    public void uploadVoice(String filePath
            , UploadVoiceCallback callback) {
        String token = getToken();
        mUploadManager.put(new File(filePath), null, token
                , new MyUpVoiceCompletionHandler(callback), null);
    }

    /**
     * @param resultUrls 回调数据集合
     * @param index      上传图片的索引，传入0
     * @param filePaths  文件数组
     * @param minSize    小图尺寸
     * @param midSize    中图尺寸
     * @param callback
     * @param options
     */
    public void uploadFile(final List<ImageUrl> resultUrls,
                           final int index,
                           final List<String> filePaths,
                           final String minSize,
                           final String midSize,
                           final MultiUploadFileCallback callback,
                           UploadOptions options) {
        String token = getToken();
        mUploadManager.put(new File(filePaths.get(index)), null, token,
                new UpCompletionHandler() {

                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {

                        if (callback != null) {
                            if (response != null) {
                                LogUtil.d("MultiUploadFileCallback-complete", response.toString());
                                String responseKey = response.optString("key");
                                if (!TextUtils.isEmpty(key)) {
                                    responseKey = key;
                                }
                                String min = Consts.TONGBAN_UPLOAD_HOST_PREFIX + responseKey + minSize;
                                String mid = Consts.TONGBAN_UPLOAD_HOST_PREFIX + responseKey + midSize;
                                String max = Consts.TONGBAN_UPLOAD_HOST_PREFIX + responseKey;
                                ImageUrl url = new ImageUrl(min, mid, max);
                                resultUrls.add(0, url);
                                LogUtil.d("resultUrls:", String.valueOf(resultUrls.size()));
                                LogUtil.d("filePaths:", String.valueOf(filePaths.size()));

                                int newIndex = index + 1;
                                if (resultUrls.size() == filePaths.size()) {
                                    callback.uploadSuccess(resultUrls);
                                } else {
                                    uploadFile(resultUrls, newIndex,
                                            filePaths, minSize, midSize, callback, null);
                                }
                            } else {
                                LogUtil.d("uploadFailed:", info.error);
                                callback.uploadFailed(info.error);
                            }
                        }
                    }
                }, options);
    }

    class MyUpVoiceCompletionHandler implements UpCompletionHandler {

        private UploadVoiceCallback mCallback;

        MyUpVoiceCompletionHandler(UploadVoiceCallback callback) {
            this.mCallback = callback;
        }

        @Override
        public void complete(String key, ResponseInfo info, JSONObject response) {
            if (mCallback != null) {
                if (response != null) {
                    LogUtil.d("UploadFileCallback-complete", response.toString());
                    String responseKey = response.optString("key");
                    if (!TextUtils.isEmpty(key)) {
                        responseKey = key;
                    }
                    String max = Consts.TONGBAN_UPLOAD_HOST_PREFIX + responseKey;
                    mCallback.uploadSuccess(max);
                } else {
                    LogUtil.d("uploadFailed:", info.error);
                    mCallback.uploadFailed(info.error);
                }
            }
        }
    }

    /**
     * 上传图片成功回调
     */
    class MyUpCompletionHandler implements UpCompletionHandler {

        private String minSize;
        private String midSize;
        private UploadFileCallback mCallback;

        MyUpCompletionHandler(String minSize, String midSize, UploadFileCallback callback) {
            this.minSize = minSize;
            this.midSize = midSize;
            this.mCallback = callback;
        }


        @Override
        public void complete(String key, ResponseInfo info, JSONObject response) {
            if (mCallback != null) {
                if (response != null) {
                    LogUtil.d("UploadFileCallback-complete", response.toString());
                    String responseKey = response.optString("key");
                    if (!TextUtils.isEmpty(key)) {
                        responseKey = key;
                    }
                    String min = Consts.TONGBAN_UPLOAD_HOST_PREFIX + responseKey + minSize;
                    String mid = Consts.TONGBAN_UPLOAD_HOST_PREFIX + responseKey + midSize;
                    String max = Consts.TONGBAN_UPLOAD_HOST_PREFIX + responseKey;
                    ImageUrl url = new ImageUrl(min, mid, max);
                    mCallback.uploadSuccess(url);
                } else {
                    LogUtil.d("uploadFailed:", info.error);
                    mCallback.uploadFailed(info.error);
                }
            }
        }
    }

    /**
     * token为空时,再次获取
     *
     * @return
     */
    private String getToken() {
        String token = (String) SPUtils.get(mContext, Consts.QINIU_TOKEN, "");
        if ("".equals(token)) {
            fetchUploadToken();
        }
        return token;
    }
}
