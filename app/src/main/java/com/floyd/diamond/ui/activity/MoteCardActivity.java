package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.floyd.diamond.biz.tools.ThumbnailUtils;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.event.AuthStatusEvent;
import com.floyd.diamond.ui.DialogCreator;
import com.floyd.diamond.ui.ImageLoaderFactory;
import com.floyd.diamond.ui.graphic.CropImageActivity;
import com.floyd.diamond.ui.view.UIAlertDialog;
import com.floyd.diamond.ui.view.YWPopupWindow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import de.greenrobot.event.EventBus;

public class MoteCardActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "";
    private static final int CODE_GALLERY_REQUEST = 1;
    private static final int CROP_PICTURE_REQUEST = 2;
    private static final int TAKE_PICTURE = 3;

    private TextView confirmButton;
    private EditText realNameView;
    private EditText idcardNumberView;
    private ImageView takePhoneImageView;
    private NetworkImageView idCardPicView;

    private YWPopupWindow ywPopupWindow;
    private ImageLoader mImageLoader;
    private float oneDp;

    private TextView takePhoneButton;
    private TextView choosePhoneButton;
    private TextView cancelButton;

    private String tempImage = "image_temp";
    private String tempImageCompress = "image_tmp";
    private int avatorSize = 1000;
    private String avatorTmp = "avator_tmp.jpg";
    private File newFile = new File(EnvConstants.imageRootPath, tempImage);
    private File tmpFile = new File(EnvConstants.imageRootPath, tempImageCompress);

    private ProgressDialog avatorDialog;
    private Dialog dataLoadingDialog;

    private LoginVO loginVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mote_card);
        mImageLoader = ImageLoaderFactory.createImageLoader();
        oneDp = this.getResources().getDimension(R.dimen.one_dp);
        loginVO = LoginManager.getLoginInfo(this);
        dataLoadingDialog = DialogCreator.createDataLoadingDialog(this);

        findViewById(R.id.title_back).setOnClickListener(this);

        confirmButton = (TextView) findViewById(R.id.confirm_button);
        realNameView = (EditText) findViewById(R.id.real_name_view);
        idcardNumberView = (EditText) findViewById(R.id.id_card_view);
        idCardPicView = (NetworkImageView) findViewById(R.id.id_card_photo);
        takePhoneImageView = (ImageView) findViewById(R.id.take_phone_view);

        ywPopupWindow = new YWPopupWindow(this);
        ywPopupWindow.initView(takePhoneImageView, R.layout.popup_edit_head, (int) (160 * oneDp), new YWPopupWindow.ViewInit() {
            @Override
            public void initView(View v) {
                takePhoneButton = (TextView) v.findViewById(R.id.edit_head);
                takePhoneButton.setText("拍照");
                choosePhoneButton = (TextView) v.findViewById(R.id.edit_profile);
                choosePhoneButton.setText("从手机相册中选择");
                cancelButton = (TextView) v.findViewById(R.id.cancel_button);
                takePhoneButton.setOnClickListener(MoteCardActivity.this);
                choosePhoneButton.setOnClickListener(MoteCardActivity.this);
                cancelButton.setOnClickListener(MoteCardActivity.this);
            }
        });

        takePhoneImageView.setOnClickListener(this);
        confirmButton.setOnClickListener(this);

        if (!TextUtils.isEmpty(loginVO.user.realName)) {
            realNameView.setText(loginVO.user.realName);
        }

        if (!TextUtils.isEmpty(loginVO.user.idNumber)) {
            idcardNumberView.setText(loginVO.user.idNumber);
        }

        if (!TextUtils.isEmpty(loginVO.user.idcardPic)) {
            idCardPicView.setImageUrl(loginVO.user.getIdCardPicPreview(), mImageLoader);
        }

    }

    public void onBackPressed() {
        super.onBackPressed();
        if (!this.isFinishing() && ywPopupWindow.isShowing()) {
            ywPopupWindow.hidePopUpWindow();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_profile:
                //从相册中选择
                Intent intentFromGallery = new Intent();
                intentFromGallery.setType("image/*");
                intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
                break;
            case R.id.edit_head:
                //拍照
                takePhoto(TAKE_PICTURE);
                break;
            case R.id.cancel_button:
                if (!this.isFinishing()) {
                    ywPopupWindow.hidePopUpWindow();
                }
                break;
            case R.id.title_back:
                this.finish();
                break;
            case R.id.take_phone_view:
                if (!this.isFinishing()) {
                    ywPopupWindow.showPopUpWindow();
                }
                break;
            case R.id.confirm_button:
                String realName = realNameView.getText().toString();
                if (TextUtils.isEmpty(realName)) {
                    Toast.makeText(this, "请输入真实姓名!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String idcardNumber = idcardNumberView.getText().toString();
                if (TextUtils.isEmpty(idcardNumber)) {
                    Toast.makeText(this, "请输入身份证号码!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(loginVO.user.idcardPic)) {
                    Toast.makeText(this, "请上传身份证图片!", Toast.LENGTH_SHORT).show();
                    return;
                }

                loginVO.user.realName = realName;
                loginVO.user.idNumber = idcardNumber;

                //弹框提示认证后性别，年龄不可修改
                UIAlertDialog.Builder builder = new UIAlertDialog.Builder(MoteCardActivity.this);
                SpannableString message = new SpannableString(" 亲,性别与出生年月认证后无法修改");
                message.setSpan(new RelativeSizeSpan(2), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                message.setSpan(new ForegroundColorSpan(Color.parseColor("#d4377e")), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setMessage(message)
                        .setCancelable(true)
                        .setPositiveButton(R.string.confirm,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        //确定后提交


                                        dataLoadingDialog.show();
                                        MoteManager.updateMoteAuthenInfo(loginVO.user, loginVO.token).startUI(new ApiCallback<Boolean>() {
                                            @Override
                                            public void onError(int code, String errorInfo) {
                                                if (!MoteCardActivity.this.isFinishing()) {
                                                    dataLoadingDialog.dismiss();
                                                    Toast.makeText(MoteCardActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                                                }


                                            }

                                            @Override
                                            public void onSuccess(Boolean aBoolean) {
                                                if (!MoteCardActivity.this.isFinishing()) {
                                                    dataLoadingDialog.dismiss();
                                                    if (loginVO != null && loginVO.user !=null) {
                                                        loginVO.user.authenStatus = 0;
                                                        LoginManager.saveLoginInfo(MoteCardActivity.this, loginVO);
                                                    }
                                                    EventBus.getDefault().post(new AuthStatusEvent());
                                                    UIAlertDialog.Builder builder = new UIAlertDialog.Builder(MoteCardActivity.this);
                                                    SpannableString message = new SpannableString("亲,您已提交成功，请等待审核!");
                                                    message.setSpan(new RelativeSizeSpan(2), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                    message.setSpan(new ForegroundColorSpan(Color.parseColor("#d4377e")), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                    builder.setMessage(message)
                                                            .setCancelable(true)
                                                            .setPositiveButton(R.string.confirm,
                                                                    new DialogInterface.OnClickListener() {
                                                                        public void onClick(DialogInterface dialog,
                                                                                            int id) {
                                                                            dialog.dismiss();
                                                                            MoteCardActivity.this.finish();
                                                                        }
                                                                    });
                                                    AlertDialog dialog = builder.create();
                                                    dialog.show();
                                                }
                                            }

                                            @Override
                                            public void onProgress(int progress) {

                                            }
                                        });
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //取消后弹框消失
                        dialog.dismiss();
                        MoteCardActivity.this.finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

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

        if (saveFile.exists()) {// 判断是否有SD卡
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri uri = Uri.fromFile(tmpFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, requestCode);
        } else if (saveFile == null || !saveFile.exists()) {
            Toast.makeText(this, R.string.insert_sdcard, Toast.LENGTH_SHORT).show();
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_GALLERY_REQUEST) {
            if (!this.isFinishing()) {
                ywPopupWindow.hidePopUpWindow();

                if (resultCode == Activity.RESULT_OK) {
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
                                in = this.getContentResolver().openInputStream(uri);
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
                                bitmap = BitmapFactory.decodeByteArray(tmpData, 0, tmpData.length);
                            }
                            FileTools.writeBitmap(EnvConstants.imageRootPath, tempImage, bitmap);
                            newFile = new File(EnvConstants.imageRootPath, tempImage);
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
                                cursor = DataBaseUtils.doContentResolverQueryWrapper(this, uri, new String[]{MediaStore.Images.ImageColumns.ORIENTATION},
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
                        tmpData = null;

                        Intent intent = new Intent(this, CropImageActivity.class);
                        intent.setDataAndType(Uri.fromFile(tmpFile), "image/*");// 设置要裁剪的图片
                        intent.putExtra("crop", "true");// crop=true
                        // 有这句才能出来最后的裁剪页面.
                        intent.putExtra("aspectX", 1);
                        intent.putExtra("aspectY", 1);
                        intent.putExtra("outputX", avatorSize);
                        intent.putExtra("outputY", avatorSize);
                        intent.putExtra("noFaceDetection", true);
                        intent.putExtra("return-data", true);
                        intent.putExtra("path", avatorTmp);
                        intent.putExtra("outputFormat", "JPEG");// 返回格式
                        intent.putExtra("needRotate", true);
                        this.startActivityForResult(intent, CROP_PICTURE_REQUEST);
                    }
                }
            }
        } else if (requestCode == CROP_PICTURE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (avatorDialog == null) {
                avatorDialog = ProgressDialog.show(this, this.getResources()
                        .getString(R.string.setting_hint), this.getResources()
                        .getString(R.string.setting_updateing_avator), true, true);
            }

            newFile = new File(EnvConstants.imageRootPath, avatorTmp);
            avatorDialog.show();

            MoteManager.uploadCommonFile(newFile, loginVO.token).startUI(new ApiCallback<String>() {
                @Override
                public void onError(int code, String errorInfo) {
                    if (!MoteCardActivity.this.isFinishing()) {
                        avatorDialog.dismiss();
                        Toast.makeText(MoteCardActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                        newFile.delete();
                    }
                }

                @Override
                public void onSuccess(String s) {
                    if (!MoteCardActivity.this.isFinishing()) {
                        avatorDialog.dismiss();
                        idCardPicView.setImageUrl(s, mImageLoader);
                        loginVO.user.idcardPic = s;
                        LoginManager.saveLoginInfo(MoteCardActivity.this, loginVO);
                        newFile.delete();
                    }
                }

                @Override
                public void onProgress(int progress) {

                }
            });
        } else if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
            if (!this.isFinishing()) {
                ywPopupWindow.hidePopUpWindow();
                Intent intent = new Intent(this, CropImageActivity.class);
                intent.setDataAndType(Uri.fromFile(tmpFile), "image/*");// 设置要裁剪的图片
                intent.putExtra("crop", "true");// crop=true
                // 有这句才能出来最后的裁剪页面.
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", avatorSize);
                intent.putExtra("outputY", avatorSize);
                intent.putExtra("noFaceDetection", true);
                intent.putExtra("return-data", true);
                intent.putExtra("path", avatorTmp);
                intent.putExtra("outputFormat", "JPEG");// 返回格式
                intent.putExtra("needRotate", true);
                this.startActivityForResult(intent, CROP_PICTURE_REQUEST);
            }
        }

    }
}
