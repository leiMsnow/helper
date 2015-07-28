package com.tongban.im.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.widget.Toast;

import com.tongban.im.utils.*;
import com.tongban.im.utils.ImageUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

public class Utils {
	public static final int OPEN_ALBUM = 100;
	public static final int OPEN_CAMERA = 101;
	public static final int PHOTO_REQUEST_CUT = 102;

	public static File getDiskCacheDir(Context context, String uniqueName) {
		final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()) ? getExternalCacheDir(context)
				.getPath() : context.getCacheDir().getPath();

		return new File(cachePath + File.separator + uniqueName);
	}

	public static File getExternalCacheDir(Context context) {
		final String cacheDir = "/Android/data/" + context.getPackageName()
				+ "/cache/";
		return new File(Environment.getExternalStorageDirectory().getPath()
				+ cacheDir);
	}

	public static int getAppVersion(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 1;
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

	public static void openPhotoAlbum(Context context, Fragment fragment) {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		if (fragment != null) {
			fragment.getParentFragment().startActivityForResult(intent,
					OPEN_ALBUM);
			return;
		}
		((Activity) context).startActivityForResult(intent, OPEN_ALBUM);
	}

	public static void takePhoto(Context context, Fragment fragment) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = getImageFile();
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		List<ResolveInfo> infos = context.getPackageManager()
				.queryIntentActivities(intent,
						PackageManager.MATCH_DEFAULT_ONLY);
		if (infos == null || infos.size() == 0) {
			Toast.makeText(context, "没有支持的应用", Toast.LENGTH_SHORT).show();
		} else {
			if (fragment != null) {
				fragment.getParentFragment().startActivityForResult(intent,
						OPEN_CAMERA);
				return;
			}
			((Activity) context).startActivityForResult(intent, OPEN_CAMERA);
		}
	}

	public static void startPhotoZoom(Context context, Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");

		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		intent.putExtra("noFaceDetection", true);
		((Activity) context).startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	/**
	 * 相册图片处理
	 * 
	 * @param context
	 * @param data
	 */
	public static void choosePicture(Context context, Intent data) {

		String picturePath = com.tongban.im.util.ImageUtils.searchUriFile(context, data);
		if (picturePath != null) {
			String newFile = ImageUtils.saveToSD(picturePath);
			File file = new File(newFile);
			startPhotoZoom(context, Uri.fromFile(file));
		}
	}

	public static File getImageFile() {
		String filePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/test/";
		String filename = filePath + "temp.jpg";
		if (!new File(filePath).exists()) {
			new File(filePath).mkdirs();
		}
		File file = new File(filename);
		return file;
	}

}
