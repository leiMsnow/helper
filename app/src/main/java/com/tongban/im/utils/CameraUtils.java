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

/**
 * 拍照/选择相片相关工具类
 *
 * @author zhangleilei
 */
public class CameraUtils {
    public static int OPEN_ALBUM = 20;
    public static int OPEN_CAMERA = 21;
    public static int PHOTO_REQUEST_CUT = 22;

    public static String saveToSD(String filePath) {
        Bitmap bitmap = decodeFile(new File(filePath), 320, 640);
        return saveToSD(bitmap, filePath);
    }

    public static String searchUriFile(Context context, Intent data) {
        String filelocal = null;
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
            filelocal = cursor.getString(columnIndex);
            cursor.close();
        }
        return filelocal;
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

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        return baos.toByteArray();
    }

    public static void openPhotoAlbum(Context context) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
            Toast.makeText(context, "没有支持的应用", Toast.LENGTH_SHORT).show();
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

    private static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
        if (bitmap == null)
            return null;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix mtx = new Matrix();
        Bitmap createBitmap = null;
        try {
            mtx.postRotate(rotate);
            createBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
        } catch (Throwable e) {
            mtx.setScale(0.5f, 0.5f);
            mtx.postRotate(rotate);
            createBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
        }
        return createBitmap;
    }

    private static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            final float totalPixels = width * height;
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;
            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    private static Bitmap decodeFile(File f, int reqWidth, int reqHeight) {
        BitmapFactory.Options o2 = null;
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inPreferredConfig = Bitmap.Config.RGB_565;
            o.inPurgeable = true;
            o.inInputShareable = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            o2 = new BitmapFactory.Options();
            o2.inSampleSize = calculateInSampleSize(o, reqWidth, reqHeight);
            Bitmap bitmap = null;
            try {
                o2.inPreferredConfig = Bitmap.Config.RGB_565;
                o2.inPurgeable = true;
                o2.inInputShareable = true;
                bitmap = BitmapFactory.decodeStream(new FileInputStream(f),
                        null, o2);
            } catch (Throwable e) {
                if (e instanceof OutOfMemoryError) {
                    o2.inSampleSize *= 2;
                    bitmap = BitmapFactory.decodeStream(new FileInputStream(f),
                            null, o2);
                }
            }
            if (bitmap == null) {
                return null;
            }
            int degree = readPictureDegree(f.getAbsolutePath());
            if (degree != 0) {
                bitmap = rotateBitmap(bitmap, degree);
            }
            return bitmap;
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    private static String saveToSDCard(String localPath, Bitmap bitmap,
                                       String filePath) {
        String status = Environment.getExternalStorageState();
        String filename = localPath + System.currentTimeMillis()
                + filePath.substring(filePath.lastIndexOf("."));
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(localPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            try {
                File imageFile = new File(filename);
                imageFile.createNewFile();
                FileOutputStream fout = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fout);
                fout.flush();
                fout.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filename;
    }

    private static String saveToSD(Bitmap bitmap, String filePath) {
        String localPath = Environment.getExternalStorageDirectory()
                .getAbsoluteFile() + "/temp/";
        return saveToSDCard(localPath, bitmap, filePath);
    }

}
