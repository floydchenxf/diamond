package com.floyd.diamond.ui.multiimage.common;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import com.floyd.diamond.R;
import com.floyd.diamond.biz.tools.ThumbnailUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * 
 * @author yuanyi.rss
 * @author editor floyd.chenxf
 * 
 */
public class MultiPickImageGetTask extends AsyncTask<String, Void, Bitmap> {

	private String mPath;
	private WeakReference<ImageView> mWeakImageView;
	private static Set<String> loadingPath = new HashSet<String>();
	private static Object lock = new Object();
	private LruCache<String, Bitmap> mBitmapCache;
	private String oriPath;
	private static final int DEFAULT_SIZE = 120;
	private int orientation;

	public MultiPickImageGetTask(String path, ImageView imageView,
								 LruCache<String, Bitmap> bitmapCache, String oriPath,
								 int orientation) {
		this.mPath = path;
		mWeakImageView = new WeakReference<ImageView>(imageView);
		this.mBitmapCache = bitmapCache;
		this.oriPath = oriPath;
		this.orientation = orientation;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		synchronized (lock) {
			if (loadingPath.contains(mPath)) {
				mWeakImageView = new WeakReference<ImageView>(null);
				return null;
			}
			loadingPath.add(mPath);
		}
		Log.d("test", "MultiPickImageGetTask:" + mPath);
		return decodeBitmap(mPath);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		ImageView imageView = mWeakImageView.get();
		if (imageView != null && result != null) {
			mBitmapCache.put(mPath, result);
			int size = 0;
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
				size = result.getRowBytes() * result.getHeight();
			} else {
				size = result.getByteCount();
			}
			Log.d("test", "size:" + size);

			Object object = imageView.getTag();
			if (object != null && object instanceof String) {
				String realPahtString = (String) object;
				if (realPahtString.equals(mPath)) {
					imageView.setImageBitmap(result);
				} else {
					return;
				}
			}
		} else if (imageView != null && result == null) {
			imageView.setImageResource(R.drawable.tuqian);
		}
		synchronized (lock) {
			loadingPath.remove(mPath);
		}
	}

	private Bitmap decodeBitmap(String filePath) {
		long start = System.currentTimeMillis();
		Bitmap cacheBitmap = mBitmapCache.get(filePath);
		if (cacheBitmap != null) {
			return cacheBitmap;
		}

		BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
		Bitmap bitmap = null;
		decodeOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, decodeOptions);

		Log.d("test", "decodeBitmap time 1:" + (System.currentTimeMillis() - start));

		int actualWidth = decodeOptions.outWidth;
		int actualHeight = decodeOptions.outHeight;

		int[] desiredDim = getResizedDimension(actualWidth, actualHeight);
		int desiredWidth = desiredDim[0];
		int desiredHeight = desiredDim[1];

		// Decode to the nearest power of two scaling factor.
		decodeOptions.inJustDecodeBounds = false;
		decodeOptions.inPreferredConfig = Config.ARGB_8888;

		decodeOptions.inSampleSize = findBestSampleSize(actualWidth,
				actualHeight, desiredWidth, desiredHeight);

		Bitmap tempBitmap = BitmapFactory.decodeFile(filePath, decodeOptions);

		Log.d("test", "decodeBitmap time 2:"
				+ (System.currentTimeMillis() - start));

		if (tempBitmap != null) {
			bitmap = ThumbnailUtils.getCropAndScaledBitmap(tempBitmap,
					desiredWidth, desiredHeight, desiredDim[2], desiredDim[3],
					true);
		}

		Log.d("test", "decodeBitmap time 3:"
				+ (System.currentTimeMillis() - start));

		if (orientation != 0) {
			Log.d("test", "orientation:" + orientation);
			bitmap = ThumbnailUtils.rotateBitmap(bitmap, orientation);
		}
		Log.d("test", "decodeBitmap time:"
				+ (System.currentTimeMillis() - start));

		return bitmap;
	}

	static int findBestSampleSize(int actualWidth, int actualHeight,
			int desiredWidth, int desiredHeight) {
		double wr = (double) actualWidth / desiredWidth;
		double hr = (double) actualHeight / desiredHeight;
		double ratio = Math.min(wr, hr);
		float n = 1.0f;
		while ((n * 2) <= ratio) {
			n *= 2;
		}
		return (int) n;
	}

	public static int[] getResizedDimension(int actualWidth, int actualHeight) {
		int[] resizedResults = new int[4];
		int DEFAULT_WIDTH = DEFAULT_SIZE;
		int DEFAULT_HEIGHT = DEFAULT_SIZE;
		int mMaxHeight = DEFAULT_SIZE;
		int mMinWidth = DEFAULT_SIZE;

		if (actualWidth <= 0 || actualHeight <= 0) {
			resizedResults[0] = DEFAULT_WIDTH;
			resizedResults[1] = DEFAULT_HEIGHT;
			resizedResults[2] = DEFAULT_WIDTH;
			resizedResults[3] = DEFAULT_HEIGHT;
			return resizedResults;
		}
		if (actualWidth <= actualHeight) {
			if (actualHeight > mMaxHeight) {
				double ratio = (double) actualHeight / (double) mMaxHeight;
				int tmpWidth = (int) (actualWidth / ratio);
				if (tmpWidth > mMinWidth) {
					resizedResults[0] = tmpWidth; // 最终尺寸的宽度
					resizedResults[1] = mMaxHeight;// 最终尺寸的高度
					resizedResults[2] = actualWidth; // 剪裁尺寸的宽度
					resizedResults[3] = actualHeight; // 裁剪尺寸的高度
				} else {
					ratio = (double) mMinWidth / (double) actualWidth;
					int tmpHeight = (int) (actualHeight * ratio);
					if (tmpHeight > mMaxHeight) {
						resizedResults[0] = mMinWidth; // 最终尺寸的宽度
						resizedResults[1] = mMaxHeight;// 最终尺寸的高度
						resizedResults[2] = actualWidth; // 剪裁尺寸的宽度
						resizedResults[3] = (int) ((double) actualWidth
								* mMaxHeight / (double) mMinWidth);
					} else {
						resizedResults[0] = mMinWidth; // 最终尺寸的宽度
						resizedResults[1] = tmpHeight;// 最终尺寸的高度
						resizedResults[2] = actualWidth; // 剪裁尺寸的宽度
						resizedResults[3] = actualHeight; // 裁剪尺寸的高度
					}
				}
			} else {
				if (actualWidth < mMinWidth) {
					double ratio = (double) mMinWidth / (double) actualWidth;
					int tmpHeight = (int) (actualHeight * ratio);
					if (tmpHeight > mMaxHeight) {
						resizedResults[0] = mMinWidth; // 最终尺寸的宽度
						resizedResults[1] = mMaxHeight;// 最终尺寸的高度
						resizedResults[2] = actualWidth; // 剪裁尺寸的宽度
						resizedResults[3] = (int) ((double) actualWidth
								* mMaxHeight / (double) mMinWidth);
					} else {
						resizedResults[0] = mMinWidth; // 最终尺寸的宽度
						resizedResults[1] = tmpHeight;// 最终尺寸的高度
						resizedResults[2] = actualWidth; // 剪裁尺寸的宽度
						resizedResults[3] = actualHeight; // 裁剪尺寸的高度
					}
				} else {
					resizedResults[0] = actualWidth; // 最终尺寸的宽度
					resizedResults[1] = actualHeight;// 最终尺寸的高度
					resizedResults[2] = actualWidth; // 剪裁尺寸的宽度
					resizedResults[3] = actualHeight; // 裁剪尺寸的高度
				}
			}
		} else {
			int[] results = getResizedDimension(actualHeight, actualWidth);
			resizedResults[0] = results[1];
			resizedResults[1] = results[0];
			resizedResults[2] = results[3];
			resizedResults[3] = results[2];
		}
		return resizedResults;
	}

	@Override
	protected void onPreExecute() {
		ImageView imageView = mWeakImageView.get();
		if (imageView != null) {
			Object tag = imageView.getTag();
			if (tag != null && tag instanceof String) {
				String prePathString = (String) tag;
				if (mPath != null && !mPath.equals(prePathString)) {
					imageView.setImageResource(R.drawable.tuqian);
				}
			}
			imageView.setTag(mPath);
		}
		super.onPreExecute();
	}

	public final AsyncTask<String, Void, Bitmap> executeOnThreadPool(
			String... params) {
		if (Build.VERSION.SDK_INT < 11) {
			// The execute() method uses a thread pool
			return execute(params);
		} else {
			// The execute() method uses a single thread,
			// so call executeOnExecutor() instead.
			try {
				Method method = android.os.AsyncTask.class.getMethod(
						"executeOnExecutor", Executor.class, Object[].class);
				Field field = android.os.AsyncTask.class
						.getField("THREAD_POOL_EXECUTOR");
				Object executor = field.get(null);
				method.invoke(this, executor, params);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException("Unexpected NoSuchMethodException",
						e);
			} catch (NoSuchFieldException e) {
				throw new RuntimeException("Unexpected NoSuchFieldException", e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Unexpected IllegalAccessException",
						e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(
						"Unexpected InvocationTargetException", e);
			}
			return this;
		}
	}

}
