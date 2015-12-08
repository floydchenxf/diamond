package com.floyd.diamond.biz.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Log;

import com.floyd.diamond.biz.constants.EnvConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by floyd on 15-11-29.
 */
public class ThumbnailUtils {

    private static final String TAG = "ThumbnailUtils";

    public static final String GIF = "GIF";

    public static final String PNG = "PNG";

    public static final String JPG = "JPG";

    private static final float FACTOR = 0.4f;


    public static String getType(byte[] data) {
        String type = null;
        if (data == null || data.length <= 9) {
            return null;
        }
        try {
            // Png test:
            if (data[1] == 'P' && data[2] == 'N' && data[3] == 'G') {
                type = PNG;
                return type;
            }
            // Gif test:
            if (data[0] == 'G' && data[1] == 'I' && data[2] == 'F') {
                type = GIF;
                return type;
            }
            // JPG test:
            if (data[6] == 'J' && data[7] == 'F' && data[8] == 'I'
                    && data[9] == 'F') {
                type = JPG;
                return type;
            }
        } catch (Exception e) {
            // EXCEPTION_TODO: handle exception
        }
        return type;
    }

    public static Bitmap rotateBitmap(Bitmap b, int degrees) {
        if (degrees != 0 && b != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, b.getWidth() / 2, b.getHeight() / 2);
            try {
                Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
                        b.getHeight(), m, true);
                return b2;
            } catch (Exception e) {
                e.printStackTrace();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public static Bitmap getImageThumbnail(File file, int width, int height,
                                           String saveName, boolean needRotate) {
        if (file != null && file.exists()) {
            int sampleSize = 1;
            while (true) {
                try {
                    long length = file.length();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = sampleSize;
                    FileInputStream fis = new FileInputStream(file);
                    Bitmap bitmap = BitmapFactory.decodeFileDescriptor(
                            fis.getFD(), null, options);
                    fis.close();
                    fis = null;
                    int outWidth = options.outWidth;
                    int outHeight = options.outHeight;
                    if (outWidth > outHeight && needRotate) {
                        int temp = outWidth;
                        outWidth = outHeight;
                        outHeight = temp;
                    }
                    float tw = (float) outWidth / (float) width;
                    float t = tw;
                    if (t <= (1.0 + FACTOR)) {
                        String savePath = EnvConstants.imageRootPath + File.separator + saveName;
                        if (bitmap != null && length > 0) {
                            if (bitmap.getWidth() > bitmap.getHeight()
                                    && needRotate) {
                                Bitmap tempBitmap = bitmap;
                                Matrix matrix = new Matrix();
                                matrix.setRotate((float) 90.0);
                                bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                                        bitmap.getWidth(), bitmap.getHeight(),
                                        matrix, true);
                                if (tempBitmap != bitmap) {
                                    tempBitmap.recycle();
                                    tempBitmap = null;
                                }
                            }
                            FileOutputStream fos = null;
                            File imageRoot = new File(EnvConstants.imageRootPath);
                            if (!imageRoot.exists()) {
                                imageRoot.mkdirs();
                            }
                            File savefile = new File(savePath);
                            if (!savefile.exists()) {
                                try {
                                    savefile.createNewFile();
                                } catch (IOException e) {
                                    // AUTO_TODO Auto-generated catch block
                                    Log.w(TAG, e);
                                }
                            }
                            try {
                                fos = new FileOutputStream(savefile);
                            } catch (FileNotFoundException e) {
                                // AUTO_TODO Auto-generated catch block
                                Log.w(TAG, e);
                            }
                            if (fos != null) {
                                if (bitmap.compress(Bitmap.CompressFormat.JPEG,
                                        60, fos)) {
                                    try {
                                        fos.flush();
                                        fos.close();
                                    } catch (IOException e) {
                                        // AUTO_TODO Auto-generated catch block
                                        Log.w(TAG, e);
                                    }
                                    bitmap.recycle();
                                    bitmap = null;
                                    bitmap = FileTools.readBitmap(savePath);
                                }
                            }
                        } else {
                            break;
                        }
                        return bitmap;
                    } else {
                        if (bitmap != null) {
                            bitmap.recycle();
                        }
                        float f = sampleSize * t;
                        int i = (int) (sampleSize * t);
                        if ((f - FACTOR) > i) {
                            sampleSize = (i + 1);
                        } else {
                            sampleSize = i;
                        }
                    }
                } catch (OutOfMemoryError oe) {
                    sampleSize++;
                } catch (FileNotFoundException e) {
                    Log.w(TAG, e);
                } catch (IOException e) {
                    Log.w(TAG, e);
                }
            }
        }
        return null;
    }

    public static Bitmap getImageThumbnailFromAlbum(Context context, Uri uri,
                                                    int width, int height, String saveName, int orientation) {
        return getImageThumbnailFromAlbum(context, uri, width, height,
                saveName, orientation, false);
    }

    public static Bitmap getImageThumbnailFromAlbum(Context context, Uri uri,
                                                    int width, int height, String saveName, int orientation,
                                                    boolean shouldCut) {
        InputStream in = null;
        int sampleSize = 1;
        String type = JPG;

        int length = 0;
        try {
            in = context.getContentResolver().openInputStream(uri);
            length = in.available();
            BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
            decodeOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, decodeOptions);
            int actualWidth = decodeOptions.outWidth;
            int actualHeight = decodeOptions.outHeight;
            decodeOptions.inJustDecodeBounds = false;
            decodeOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
            sampleSize = findBestSampleSize(actualWidth, actualHeight, width,
                    height);
            if (sampleSize == 1
                    && (actualWidth > width || actualHeight > height)) {
                sampleSize = 2;
            }

            while (true) {
                try {
                    try {
                        in = context.getContentResolver().openInputStream(uri);
                        length = in.available();
                    } catch (FileNotFoundException e) {
                        // AUTO_TODO Auto-generated catch block
                        Log.w(TAG, e);
                    } catch (IOException e) {
                        // AUTO_TODO Auto-generated catch block
                        Log.w(TAG, e);
                    } catch (OutOfMemoryError e) {
                        Log.w(TAG, e);
                    }

                    if (in != null) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = sampleSize;
                        Bitmap bitmap = BitmapFactory.decodeStream(in, null,
                                options);
                        int outWidth = options.outWidth;
                        int outHeight = options.outHeight;
                        if (orientation == 90 || orientation == 270) {
                            int temp = outWidth;
                            outWidth = outHeight;
                            outHeight = temp;
                        }
                        float tw = (float) outWidth / (float) width;
                        float th = (float) outHeight / (float) height;
                        float t = tw < th ? tw : th;
                        if (shouldCut) {
                            if (tw < 1 || th < 1) {
                                t = 1;
                            }
                        }
                        String savePath = EnvConstants.imageRootPath
                                + File.separator + saveName;
                        if (bitmap != null && length > 0) {
                            Matrix matrix = new Matrix();
                            Bitmap tempBitmap = bitmap;
                            if (orientation > 0) {
                                matrix.setRotate(orientation);
                                bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                                        bitmap.getWidth(), bitmap.getHeight(),
                                        matrix, true);

                                if (tempBitmap != bitmap) {
                                    tempBitmap.recycle();
                                    tempBitmap = null;
                                }
                            }

                            if (shouldCut && t != 1) {
                                matrix = new Matrix();
                                matrix.setScale(1f / t, 1f / t);
                                tempBitmap = bitmap;
                                bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                                        bitmap.getWidth(), bitmap.getHeight(),
                                        matrix, true);

                                if (tempBitmap != bitmap) {
                                    tempBitmap.recycle();
                                    tempBitmap = null;
                                }
                            }

                            FileOutputStream fos = null;
                            File imageRoot = new File(
                                    EnvConstants.imageRootPath);
                            if (!imageRoot.exists()) {
                                imageRoot.mkdirs();
                            }
                            File file = new File(savePath);
                            if (!file.exists()) {
                                try {
                                    file.createNewFile();
                                } catch (IOException e) {
                                    // AUTO_TODO Auto-generated catch block
                                    Log.w(TAG, e);
                                }
                            } else {
                                file.delete();
                            }
                            try {
                                fos = new FileOutputStream(file);
                            } catch (FileNotFoundException e) {
                                // AUTO_TODO Auto-generated catch block
                                Log.w(TAG, e);
                            }
                            if (fos != null) {
                                Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
                                if (PNG.equals(type)) {
                                    format = Bitmap.CompressFormat.PNG;
                                }
                                if (bitmap.compress(format, 60, fos)) {
                                    try {
                                        fos.flush();
                                        fos.close();
                                    } catch (IOException e) {
                                        // AUTO_TODO Auto-generated catch block
                                        Log.w(TAG, e);
                                    }
                                    bitmap.recycle();
                                    bitmap = null;
                                    bitmap = FileTools.readBitmap(savePath);
                                }
                            }
                        } else {
                            break;
                        }
                        return bitmap;
                    } else {
                        break;
                    }
                } catch (OutOfMemoryError oe) {
                    // EXCEPTION_TODO: handle exception
                    sampleSize++;
                } finally {
                    try {
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException e) {
                        // AUTO_TODO Auto-generated catch block
                        Log.w(TAG, e);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            Log.w(TAG, e);
        } catch (IOException e) {
            Log.w(TAG, e);
        } catch (OutOfMemoryError e) {
            Log.w(TAG, e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
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
}
