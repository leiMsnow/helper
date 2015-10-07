package com.tongban.im.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tongban.corelib.adapter.PhotoAlbumAdapter;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.model.ImageFolder;
import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.corelib.widget.view.BaseProgressDialog;
import com.tongban.corelib.widget.view.ListImageDirPopupWindow;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.api.base.BaseApi;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 相册选择器
 */
public class PhotoAlbumActivity extends BaseToolBarActivity implements
        ListImageDirPopupWindow.OnImageDirSelected
        , PhotoAlbumAdapter.ISelectIMGListener {

    @Bind(R.id.id_gridView)
    GridView mGirdView;
    @Bind(R.id.id_choose_dir)
    TextView mChooseDir;
    @Bind(R.id.id_total_count)
    TextView mImageCount;
    @Bind(R.id.rl_bottom_parent)
    RelativeLayout mBottomParent;

    private BaseProgressDialog mProgressDialog;

    /**
     * 存储文件夹中的图片数量
     */
    private int mPicsSize;
    /**
     * 图片数量最多的文件夹
     */
    private File mImgDir;

    private PhotoAlbumAdapter mAdapter;
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<>();
    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<ImageFolder> mImageFolders = new ArrayList<>();

    private String mChooseName = "";


    private ListImageDirPopupWindow mListImageDirPopupWindow;

    private int currentSelect = 0;
    private int maxSelect = 1;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_photo_grid;
    }

    @Override
    protected void initData() {
        if (getIntent().getExtras() != null) {
            maxSelect = getIntent().getIntExtra("maxSelect", 1);
            currentSelect = getIntent().getIntExtra("current", 0);
            setToolbarTitle();
        }
        mAdapter = new PhotoAlbumAdapter(mContext, null,
                com.tongban.corelib.R.layout.item_grid_photo);
        mAdapter.setCurrentCount(currentSelect);
        mAdapter.setMaxCount(maxSelect);
        mGirdView.setAdapter(mAdapter);
        getImages();

        mAdapter.setSelectIMGListener(this);

    }

    private void setToolbarTitle() {
        setTitle("已选择(" + currentSelect + "/" + maxSelect + ")");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo_album, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_finish) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("selectedImages", mAdapter.mSelectedImage);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    @OnClick(R.id.rl_bottom_parent)
    public void onClick(View v) {
        mListImageDirPopupWindow.setSelectDir("/" + mChooseName);
        mListImageDirPopupWindow
                .setAnimationStyle(com.tongban.corelib.R.style.AnimPopupDir);
        mListImageDirPopupWindow.showAsDropDown(mBottomParent, 0, 0);

        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            ApiErrorResult errorResult = new ApiErrorResult();
            errorResult.setDisplayType(DisplayType.View);
            errorResult.setErrorCode(BaseApi.API_URL_ERROR);
            errorResult.setErrorMessage("暂无外部存储");
            setEmptyView(errorResult);
            return;
        }
        // 显示进度条
        mProgressDialog = new BaseProgressDialog(mContext);
        mProgressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                String firstImage = null;

                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = PhotoAlbumActivity.this
                        .getContentResolver();

                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);

                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));

                    // 拿到第一张图片的路径
                    if (firstImage == null)
                        firstImage = path;
                    // 获取该图片的父路径名
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;
                    String dirPath = parentFile.getAbsolutePath();
                    ImageFolder imageFolder;
                    // 利用一个HashSet防止多次扫描同一个文件夹
                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        // 初始化imageFolder
                        imageFolder = new ImageFolder();
                        imageFolder.setDir(dirPath);
                        imageFolder.setFirstImagePath(path);
                    }

                    if (parentFile.list() == null)
                        continue;

                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.toLowerCase().endsWith(".jpg") ||
                                    filename.toLowerCase().endsWith(".png") ||
                                    filename.toLowerCase().endsWith(".jpeg"))
                                return true;
                            return false;
                        }
                    }).length;

                    imageFolder.setCount(picSize);
                    mImageFolders.add(imageFolder);

                    if (picSize > mPicsSize) {
                        mPicsSize = picSize;
                        mImgDir = parentFile;
                    }
                }
                mCursor.close();
                // 扫描完成，辅助的HashSet释放内存
                mDirPaths = null;
                // 通知Handler扫描图片完成
                mHandler.sendEmptyMessage(0x110);

            }
        }).start();

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            mProgressDialog.dismiss();
            // 为View绑定数据
            data2View();
            // 初始化展示文件夹的popupWindow
            initListDirPopupWindow();
        }
    };

    /**
     * 为View绑定数据
     */
    private void data2View() {
        if (mImgDir == null) {
            ApiErrorResult errorResult = new ApiErrorResult();
            errorResult.setDisplayType(DisplayType.View);
            errorResult.setErrorCode(BaseApi.API_URL_ERROR);
            errorResult.setErrorMessage("没有找到照片");
            setEmptyView(errorResult);
            return;
        }

        mChooseName = mImgDir.getName();
        replacePic(mImgDir);
    }

    @Override
    public void selected(ImageFolder folder) {
        File imageDir = new File(folder.getDir());
        mChooseName = imageDir.getName();
        replacePic(imageDir);
        mListImageDirPopupWindow.dismiss();
    }

    private void replacePic(File file) {
        List<String> images = Arrays.asList(file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.toLowerCase().endsWith(".jpg") ||
                        filename.toLowerCase().endsWith(".png") ||
                        filename.toLowerCase().endsWith(".jpeg"))
                    return true;
                return false;
            }
        }));
        mChooseName = file.getName();
        // 文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
        mAdapter.setmDirPath(file.getAbsolutePath());
        mAdapter.replaceAll(images);

        mChooseDir.setText(mChooseName);
        mImageCount.setText(images.size() + "张");

    }


    /**
     * 初始化展示文件夹的popupWindw
     */
    private void initListDirPopupWindow() {
        mListImageDirPopupWindow = new ListImageDirPopupWindow(
                LayoutParams.MATCH_PARENT, (int) (ScreenUtils.getScreenHeight(mContext) * 0.7),
                mImageFolders, LayoutInflater.from(getApplicationContext())
                .inflate(com.tongban.corelib.R.layout.popup_list_dir, null));

        mListImageDirPopupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        // 设置选择文件夹的回调
        mListImageDirPopupWindow.setOnImageDirSelected(this);
    }

    @Override
    public void selectChanged(int count) {
        currentSelect = count;
        setToolbarTitle();
    }

}
