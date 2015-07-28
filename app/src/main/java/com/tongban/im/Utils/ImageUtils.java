package com.tongban.im.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {

	public static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
		if (bitmap == null)
			return null;
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		// Setting post rotate to 90
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

	public static int readPictureDegree(String path) {
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

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
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

	public static Bitmap decodeFile(File f, int reqWidth, int reqHeight) {
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

	public static String saveToSDCard(String localPath, Bitmap bitmap,
			String filePath) {
		String status = Environment.getExternalStorageState();
		String filename = localPath + System.currentTimeMillis() + ""
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
		} else {

		}
		return filename;
	}

	public static String saveToSD(String filePath) {
		Bitmap bitmap = decodeFile(new File(filePath), 720, 1280);
		return saveToSD(bitmap, filePath);
	}

	public static String searchUriFile(Context context, Intent data) {
		String filelocal = null;
		if (null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

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

	public static String saveToSD(Bitmap bitmap, String filePath) {
		String localPath = Environment.getExternalStorageDirectory()
				.getAbsoluteFile() + "/test/";
		return saveToSDCard(localPath, bitmap, filePath);
	}

}
