package com.tongban.im.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
import com.tongban.corelib.base.api.ApiCallback;
import com.tongban.corelib.model.ApiResult;
import com.tongban.corelib.utils.LogUtil;
import com.tongban.corelib.utils.SDCardUtils;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.App;
import com.tongban.im.common.Consts;
import com.tongban.im.model.ImageUrl;
import com.tongban.im.model.QiniuToken;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 文件上传到七牛服务器的api
 * Created by Cheney on 15/8/6.
 */
public class FileUploadApi extends BaseApi {

    /**
     * 100尺寸大小图片
     */
    public final static String IMAGE_SIZE_100 = "-100";
    public final static String IMAGE_SIZE_500 = "-500";


    private static FileUploadApi mApi;
    private static Context mContext = App.getInstance().getApplicationContext();
    private static String dirPath = SDCardUtils.getSDCardPath() + "tongban" + File.separator
            + "images" + File.separator + "qiniu" + File.separator + "recorder";
    private static Recorder mRecorder;
    private static Configuration config;
    private static UploadManager mUploadManager;

    // 获取上传token的url
    private String UPLOAD_TOKEN = "token/require/qiniu/upload";

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
     *
     * @param callback 回调
     */
    public void fetchUploadToken(final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("space_name", "tongban");
        simpleRequest(UPLOAD_TOKEN, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {

            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<QiniuToken> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<QiniuToken>>() {
                        });
                QiniuToken token = result.getData();
                callback.onComplete(token);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorObj) {

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
        String token = (String) SPUtils.get(mContext, Consts.QINIU_TOKEN, "");
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
        String token = (String) SPUtils.get(mContext, Consts.QINIU_TOKEN, "");
        mUploadManager.put(file, key, token, new MyUpCompletionHandler(minSize, midSize, callback), options);
    }

    /**
     * 批量上传文件接口
     *
     * @param filePaths 文件数组
     * @param minSize   小图尺寸
     * @param midSize   中图尺寸
     * @param callback
     */
    public void uploadFile(@NonNull final List<String> filePaths, final String minSize, final String midSize,
                           @NonNull final MultiUploadFileCallback callback) {
        String token = (String) SPUtils.get(mContext, Consts.QINIU_TOKEN, "");
        final List<ImageUrl> urls = new ArrayList<>();
        for (int i = 0; i < filePaths.size(); i++) {
            final int index = i;
            mUploadManager.put(new File(filePaths.get(i)), null, token,
                    new UpCompletionHandler() {

                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject response) {
                            LogUtil.d("UploadFileCallback-complete", response.toString());
                            if (callback != null) {
                                String responseKey = response.optString("key");
                                String min = Consts.TONGBAN_UPLOAD_HOST_PREFIX + responseKey + minSize;
                                String mid = Consts.TONGBAN_UPLOAD_HOST_PREFIX + responseKey + midSize;
                                String max = Consts.TONGBAN_UPLOAD_HOST_PREFIX + responseKey;
                                ImageUrl url = new ImageUrl(min, mid, max);
                                urls.add(url);
                                if (index == (filePaths.size() - 1))
                                    callback.uploadSuccess(urls);
                            }
                        }
                    }, null);
        }
    }

    /**
     * 上传成功回调
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
            LogUtil.d("UploadFileCallback-complete", response.toString());
            if (mCallback != null) {
                String responseKey = response.optString("key");
                String min = Consts.TONGBAN_UPLOAD_HOST_PREFIX + responseKey + minSize;
                String mid = Consts.TONGBAN_UPLOAD_HOST_PREFIX + responseKey + midSize;
                String max = Consts.TONGBAN_UPLOAD_HOST_PREFIX + responseKey;
                ImageUrl url = new ImageUrl(min, mid, max);
                mCallback.uploadSuccess(url);
            }
        }
    }

}
