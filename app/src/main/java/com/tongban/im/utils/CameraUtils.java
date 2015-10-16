package com.tongban.im.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import com.tongban.corelib.utils.ImageUtils;
import com.tongban.corelib.utils.LogUtil;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.activity.PhotoAlbumActivity;


/**
 * 拍照/选择相片相关工具类
 *
 * @author zhangleilei
 */
public class CameraUtils {
    public static int OPEN_ALBUM = 20;
    public static int OPEN_CAMERA = 21;
    public static int PHOTO_REQUEST_CUT = 22;

    public static String searchUriFile(Context context, Intent data) {
        String localFile = null;
        if (null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = context.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            if (cursor == null) {
                return null;
            }
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            localFile = cursor.getString(columnIndex);
            cursor.close();
        }
        return localFile;
    }

    public static Bitmap stringtoBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    public static void openPhotoAlbum(Context context) {
        openPhotoAlbum(context, 0, 1);
    }

    public static void openPhotoAlbum(Context context, int current, int maxSelect) {
        Intent intent = new Intent(context, PhotoAlbumActivity.class);
        intent.putExtra("current", current);
        intent.putExtra("maxSelect", maxSelect);
        ((Activity) context).startActivityForResult(intent, OPEN_ALBUM);
    }

    public static void takePhoto(Context context) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = getImageFile();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        List<ResolveInfo> infos = context.getPackageManager()
                .queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        if (infos == null || infos.size() == 0) {
            ToastUtil.getInstance(context).showToast("没有支持的应用");
        } else {
            ((Activity) context).startActivityForResult(intent, OPEN_CAMERA);
        }
    }

    public static File getImageFile() {
        String filePath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/temp/";
        String filename = filePath + "temp.jpg";
        if (!new File(filePath).exists()) {
            new File(filePath).mkdirs();
        }
        File file = new File(filename);
        return file;
    }

    // 删除文件内容
    public static void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] tempList = file.list();
        File temp;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
            }
        }
    }

    // 删除文件夹
    private static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath);  //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete();  //删除空文件夹

        } catch (Exception e) {
            LogUtil.e("CameraUtils", "删除文件夹操作出错");
            e.printStackTrace();
        }

    }

}
