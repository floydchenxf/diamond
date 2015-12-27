package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.biz.constants.EnvConstants;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.tools.DataBaseUtils;
import com.floyd.diamond.biz.tools.FileTools;
import com.floyd.diamond.biz.tools.ImageUtils;
import com.floyd.diamond.biz.tools.ThumbnailUtils;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.mote.UserVO;
import com.floyd.diamond.ui.ImageLoaderFactory;
import com.floyd.diamond.ui.view.YWPopupWindow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class MoteAuthActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "MoteAuthActivity";

    private static final int TAKE_PICTURE_1 = 1;
    private static final int TAKE_PICTURE_2 = 2;
    private static final int TAKE_PICTURE_3 = 3;
    private static final int CHOOSE_PICTURE_1 = 4;
    private static final int CHOOSE_PICTURE_2 = 5;
    private static final int CHOOSE_PICTURE_3 = 6;


    private TextView nextStepButton;
    private NetworkImageView authPic1View;
    private NetworkImageView authPic2View;
    private NetworkImageView authPic3View;

    private View authPic1Mask;
    private View authPic2Mask;
    private View authPic3Mask;

    private ProgressDialog dataLoadingDialog;
    private ImageLoader mImageLoader;

    private YWPopupWindow ywPopupWindow;
    private TextView takePhotoView;
    private TextView choosePhotoView;
    private TextView cancelView;
    private LoginVO loginVO;
    private File tempFile;
    private int takePhotoRequestCode;
    private int choosePhotoRequestCode;
    private int avatorSize = 800;
    private String tempImage = "image_temp";
    private String tempImageCompress = "image_tmp.jpg";

    private int widthpixels, heightpixels;
    private float oneDp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mote_auth);
        mImageLoader = ImageLoaderFactory.createImageLoader();
        oneDp = this.getResources().getDimension(R.dimen.one_dp);
        widthpixels = this.getResources().getDisplayMetrics().widthPixels;
        heightpixels = (int) (this.getResources().getDisplayMetrics().heightPixels - 32 * this.getResources().getDisplayMetrics().density);
        initView();
        loginVO = LoginManager.getLoginInfo(this);
        fillData();
    }

    private void fillData() {
        UserVO userVO = loginVO.user;

        boolean existPic1 = !TextUtils.isEmpty(userVO.authenPic1);
        boolean existPic2 = !TextUtils.isEmpty(userVO.authenPic2);
        boolean existPic3 = !TextUtils.isEmpty(userVO.authenPic3);

        if (existPic1) {
            authPic1View.setImageUrl(userVO.getAuthPicPreview1(), mImageLoader);
            authPic1Mask.setVisibility(View.GONE);
        }

        if (existPic2) {
            authPic2View.setImageUrl(userVO.getAuthPicPreview2(), mImageLoader);
            authPic2Mask.setVisibility(View.GONE);
        }

        if (existPic3) {
            authPic3View.setImageUrl(userVO.getAuthPicPreview3(), mImageLoader);
            authPic3Mask.setVisibility(View.GONE);
        }

        if (existPic1 && existPic2 && existPic3) {
            nextStepButton.setText("下一步");
            nextStepButton.setTag(0);
        } else if ((existPic1 && existPic2) || (existPic1 && existPic3) || (existPic2 && existPic3)) {
            nextStepButton.setText("还差一张哦");
            nextStepButton.setTag(1);
        } else if (existPic1 || existPic2 || existPic3) {
            nextStepButton.setText("还差二张哦");
            nextStepButton.setTag(2);
        } else {
            nextStepButton.setText("上传三张自拍照");
            nextStepButton.setTag(3);
        }

        nextStepButton.setOnClickListener(this);

    }

    private void initView() {
        nextStepButton = (TextView) findViewById(R.id.next_step_button);

        ywPopupWindow = new YWPopupWindow(this);
        ywPopupWindow.initView(nextStepButton, R.layout.popup_edit_head, (int) (160 * oneDp), new YWPopupWindow.ViewInit() {
            @Override
            public void initView(View v) {
                takePhotoView = (TextView) v.findViewById(R.id.edit_head);
                takePhotoView.setText("拍照");
                choosePhotoView = (TextView) v.findViewById(R.id.edit_profile);
                choosePhotoView.setText("从手机相册中选择");
                cancelView = (TextView) v.findViewById(R.id.cancel_button);
                takePhotoView.setOnClickListener(MoteAuthActivity.this);
                choosePhotoView.setOnClickListener(MoteAuthActivity.this);
                cancelView.setOnClickListener(MoteAuthActivity.this);
            }
        });

        authPic1Mask = findViewById(R.id.auth_pic_1_mask);
        authPic2Mask = findViewById(R.id.auth_pic_2_mask);
        authPic3Mask = findViewById(R.id.auth_pic_3_mask);

        authPic1View = (NetworkImageView) findViewById(R.id.auth_pic_1);
        authPic2View = (NetworkImageView) findViewById(R.id.auth_pic_2);
        authPic3View = (NetworkImageView) findViewById(R.id.auth_pic_3);

        authPic1Mask.setOnClickListener(this);
        authPic2Mask.setOnClickListener(this);
        authPic3Mask.setOnClickListener(this);
        authPic1View.setOnClickListener(this);
        authPic2View.setOnClickListener(this);
        authPic3View.setOnClickListener(this);

        findViewById(R.id.left).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left:
                this.finish();
                break;
            case R.id.cancel_button:
                if (!this.isFinishing() && ywPopupWindow.isShowing()) {
                    ywPopupWindow.hidePopUpWindow();
                }
                break;
            case R.id.next_step_button:
                int pics = (Integer) v.getTag();
                if (pics == 0) {
                    //TODO next activity
                    Intent cardIntent = new Intent(this, MoteCardActivity.class);
                    cardIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(cardIntent);
                    this.finish();
                }
                break;
            case R.id.auth_pic_1_mask:
            case R.id.auth_pic_1:
                takePhotoRequestCode = TAKE_PICTURE_1;
                choosePhotoRequestCode = CHOOSE_PICTURE_1;
                if (!ywPopupWindow.isShowing()) {
                    ywPopupWindow.showPopUpWindow();
                }
                break;
            case R.id.auth_pic_2_mask:
            case R.id.auth_pic_2:
                takePhotoRequestCode = TAKE_PICTURE_2;
                choosePhotoRequestCode = CHOOSE_PICTURE_2;
                if (!ywPopupWindow.isShowing()) {
                    ywPopupWindow.showPopUpWindow();
                }
                break;
            case R.id.auth_pic_3_mask:
            case R.id.auth_pic_3:
                if (!ywPopupWindow.isShowing()) {
                    ywPopupWindow.showPopUpWindow();
                }
                takePhotoRequestCode = TAKE_PICTURE_3;
                choosePhotoRequestCode = CHOOSE_PICTURE_3;
                break;
            case R.id.edit_head:
                //拍照
                takePhoto(takePhotoRequestCode);
                break;
            case R.id.edit_profile:
                //从相册中选择
                Intent intentFromGallery = new Intent();
                intentFromGallery.setType("image/*");
                intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intentFromGallery, choosePhotoRequestCode);
                break;
        }

    }


    private void takePhoto(int requestCode) {
        String status = Environment.getExternalStorageState();
        File saveFile = new File(EnvConstants.imageRootPath);

        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, R.string.insert_sdcard, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!saveFile.exists()) {
            saveFile.mkdir();
        }

        tempFile = new File(saveFile.getAbsolutePath() + File.separator + UUID.randomUUID().toString() + ".jpg");

        if (saveFile.exists()) {// 判断是否有SD卡
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri uri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, requestCode);
        } else if (saveFile == null || !saveFile.exists()) {
            Toast.makeText(this, R.string.insert_sdcard, Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!this.isFinishing() && ywPopupWindow.isShowing()) {
            ywPopupWindow.hidePopUpWindow();
        }

        if (requestCode == TAKE_PICTURE_1 && resultCode == RESULT_OK) {
            String originPath = EnvConstants.imageRootPath + File.separator + UUID.randomUUID().toString() + ".jpg";
            int ori = ImageUtils.getOrientation(tempFile.getAbsolutePath(), this, null);
            Bitmap origin = ThumbnailUtils.compressFileAndRotateToBitmapThumb(tempFile.getAbsolutePath(), widthpixels, heightpixels, ori, originPath);
            if (origin == null) {
                return;
            }

            final File compressFile = new File(originPath);
            if (!compressFile.exists()) {
                return;
            }

            doUploadFile(compressFile, new FileUploadCallback() {
                @Override
                public void doCallback(String s) {
                    loginVO.user.authenPic1 = s;
                    LoginManager.saveLoginInfo(MoteAuthActivity.this, loginVO);
                    fillData();
                }
            });

        } else if (requestCode == TAKE_PICTURE_2 && resultCode == RESULT_OK) {
            String originPath = EnvConstants.imageRootPath + File.separator + UUID.randomUUID().toString() + ".jpg";
            int ori = ImageUtils.getOrientation(tempFile.getAbsolutePath(), this, null);
            Bitmap origin = ThumbnailUtils.compressFileAndRotateToBitmapThumb(tempFile.getAbsolutePath(), widthpixels, heightpixels, ori, originPath);
            if (origin == null) {
                return;
            }

            final File compressFile = new File(originPath);
            if (!compressFile.exists()) {
                return;
            }

            doUploadFile(compressFile, new FileUploadCallback() {
                @Override
                public void doCallback(String s) {
                    loginVO.user.authenPic2 = s;
                    LoginManager.saveLoginInfo(MoteAuthActivity.this, loginVO);
                    fillData();
                }
            });
        } else if (requestCode == TAKE_PICTURE_3 && resultCode == RESULT_OK) {
            String originPath = EnvConstants.imageRootPath + File.separator + UUID.randomUUID().toString() + ".jpg";
            int ori = ImageUtils.getOrientation(tempFile.getAbsolutePath(), this, null);
            Bitmap origin = ThumbnailUtils.compressFileAndRotateToBitmapThumb(tempFile.getAbsolutePath(), widthpixels, heightpixels, ori, originPath);
            if (origin == null) {
                return;
            }

            final File compressFile = new File(originPath);
            if (!compressFile.exists()) {
                return;
            }

            doUploadFile(compressFile, new FileUploadCallback() {
                @Override
                public void doCallback(String s) {
                    loginVO.user.authenPic3 = s;
                    LoginManager.saveLoginInfo(MoteAuthActivity.this, loginVO);
                    fillData();
                }
            });
        } else if (requestCode == CHOOSE_PICTURE_1 && resultCode == RESULT_OK) {
            final File chooseFile = new File(EnvConstants.imageRootPath + File.separator + tempImageCompress);
            if (chooseFile.exists()) {
                chooseFile.delete();
            }
            compressFile(data);
            doUploadFile(chooseFile, new FileUploadCallback() {
                @Override
                public void doCallback(String s) {
                    loginVO.user.authenPic1 = s;
                    LoginManager.saveLoginInfo(MoteAuthActivity.this, loginVO);
                    fillData();
                }
            });
        } else if (requestCode == CHOOSE_PICTURE_2 && resultCode == RESULT_OK) {
            final File chooseFile = new File(EnvConstants.imageRootPath + File.separator + tempImageCompress);
            if (chooseFile.exists()) {
                chooseFile.delete();
            }
            compressFile(data);
            doUploadFile(chooseFile, new FileUploadCallback() {
                @Override
                public void doCallback(String s) {
                    loginVO.user.authenPic2 = s;
                    LoginManager.saveLoginInfo(MoteAuthActivity.this, loginVO);
                    fillData();
                }
            });

        } else if (requestCode == CHOOSE_PICTURE_3 && resultCode == RESULT_OK) {
            final File chooseFile = new File(EnvConstants.imageRootPath + File.separator + tempImageCompress);
            if (chooseFile.exists()) {
                chooseFile.delete();
            }
            compressFile(data);
            doUploadFile(chooseFile, new FileUploadCallback() {
                @Override
                public void doCallback(String s) {
                    loginVO.user.authenPic3 = s;
                    LoginManager.saveLoginInfo(MoteAuthActivity.this, loginVO);
                    fillData();
                }
            });
        }
    }

    private void compressFile(Intent data) {
        Uri uri = data.getData();
        if (uri != null) {
            InputStream in = null;
            String type = "";
            byte[] tmpData = null;
            try {
                in = this.getContentResolver().openInputStream(uri);
                tmpData = new byte[10];
                in.read(tmpData);
                type = ThumbnailUtils.getType(tmpData);
            } catch (FileNotFoundException e) {
                Log.w(TAG, e);
                Log.w(TAG, e);
            } catch (IOException e) {
                Log.w(TAG, e);
                Log.w(TAG, e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        Log.w(TAG, e);
                    }
                }
            }

            if ("GIF".equals(type)) {
                try {
                    in = this.getContentResolver().openInputStream(
                            uri);
                    tmpData = new byte[in.available()];
                    in.read(tmpData);
                } catch (FileNotFoundException e) {
                    Log.w(TAG, e);
                    Log.w(TAG, e);
                } catch (IOException e) {
                    Log.w(TAG, e);
                    Log.w(TAG, e);
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            Log.w(TAG, e);
                        }
                    }
                }
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeByteArray(tmpData, 0, tmpData.length);
                } catch (OutOfMemoryError e) {
                    Log.e(TAG, e.getMessage(), e);
                    bitmap = BitmapFactory.decodeByteArray(tmpData, 0,
                            tmpData.length);
                }
                FileTools.writeBitmap(EnvConstants.imageRootPath, tempImage, bitmap);
                File newFile = new File(EnvConstants.imageRootPath, tempImage);
                bitmap = ThumbnailUtils.getImageThumbnail(newFile, avatorSize, avatorSize, tempImageCompress, false);
                if (bitmap != null) {
                    bitmap.recycle();
                    bitmap = null;
                }
            } else {
                // tempImageCompress 是tmpFile的文件名
                int orientation = 0;
                Cursor cursor = null;
                try {
                    cursor = DataBaseUtils.doContentResolverQueryWrapper(this, uri,
                            new String[]{MediaStore.Images.ImageColumns.ORIENTATION},
                            null, null, null);
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            orientation = cursor.getInt(0);
                        }
                    }
                } catch (RuntimeException e) {
                    Log.w(TAG, e);
                } finally {
                    if (cursor != null) {
                        cursor.close();
                        cursor = null;
                    }
                }

                Bitmap bitmap = ThumbnailUtils.getImageThumbnailFromAlbum(this, uri, avatorSize, avatorSize, tempImageCompress, orientation);

                if (bitmap != null) {
                    bitmap.recycle();
                    bitmap = null;
                }
            }

        }
    }

    private void doUploadFile(final File file, final FileUploadCallback callback) {
        if (dataLoadingDialog == null) {
            dataLoadingDialog = ProgressDialog.show(this, this.getResources()
                    .getString(R.string.setting_hint), this.getResources()
                    .getString(R.string.setting_updateing_avator), true, true);
        } else {
            dataLoadingDialog.show();
        }

        final File chooseFile = new File(tempImageCompress);
        MoteManager.uploadCommonFile(file, loginVO.token).startUI(new ApiCallback<String>() {
            @Override
            public void onError(int code, String errorInfo) {
                if (!MoteAuthActivity.this.isFinishing()) {
                    dataLoadingDialog.dismiss();
                    Toast.makeText(MoteAuthActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                    file.delete();
                }
            }

            @Override
            public void onSuccess(String s) {
                if (!MoteAuthActivity.this.isFinishing()) {
                    dataLoadingDialog.dismiss();
                    if (callback != null) {
                        callback.doCallback(s);
                    }
                    file.delete();
                }
            }

            @Override
            public void onProgress(int progress) {

            }
        });
    }

    public interface FileUploadCallback {
        void doCallback(String s);
    }


}
