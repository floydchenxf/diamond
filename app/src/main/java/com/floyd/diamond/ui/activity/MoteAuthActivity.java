package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
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
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.UserVO;
import com.floyd.diamond.ui.ImageLoaderFactory;

import java.io.File;
import java.util.UUID;

public class MoteAuthActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "MoteAuthActivity";

    private static final int TAKE_PICTURE_1 = 1;
    private static final int TAKE_PICTURE_2 = 2;
    private static final int TAKE_PICTURE_3 = 3;


    private TextView nextStepButton;
    private NetworkImageView authPic1View;
    private NetworkImageView authPic2View;
    private NetworkImageView authPic3View;

    private View authPic1Mask;
    private View authPic2Mask;
    private View authPic3Mask;

    private ProgressDialog dataLoadingDialog;
    private ImageLoader mImageLoader;

    private LoginVO loginVO;

    private File tempFile;

    private float oneDp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mote_auth);
        mImageLoader = ImageLoaderFactory.createImageLoader();
        initView();
        loginVO = LoginManager.getLoginInfo(this);
        oneDp = this.getResources().getDimension(R.dimen.one_dp);
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
                takePhoto(TAKE_PICTURE_1);
                break;
            case R.id.auth_pic_2_mask:
            case R.id.auth_pic_2:
                takePhoto(TAKE_PICTURE_2);
                break;
            case R.id.auth_pic_3_mask:
            case R.id.auth_pic_3:
                takePhoto(TAKE_PICTURE_3);
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

        if (requestCode == TAKE_PICTURE_1 && resultCode == RESULT_OK) {
            if (dataLoadingDialog == null) {
                dataLoadingDialog = ProgressDialog.show(this, this.getResources()
                        .getString(R.string.setting_hint), this.getResources()
                        .getString(R.string.setting_updateing_avator), true, true);
            } else {
                dataLoadingDialog.show();
            }
            MoteManager.uploadCommonFile(tempFile, loginVO.token).startUI(new ApiCallback<String>() {
                @Override
                public void onError(int code, String errorInfo) {
                    if (!MoteAuthActivity.this.isFinishing()) {
                        dataLoadingDialog.dismiss();
                        Toast.makeText(MoteAuthActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                        tempFile.delete();
                    }
                }

                @Override
                public void onSuccess(String s) {
                    if (!MoteAuthActivity.this.isFinishing()) {
                        dataLoadingDialog.dismiss();
                        loginVO.user.authenPic1 = s;
                        LoginManager.saveLoginInfo(MoteAuthActivity.this, loginVO);
                        fillData();
                        tempFile.delete();
                    }
                }

                @Override
                public void onProgress(int progress) {

                }
            });

        } else if (requestCode == TAKE_PICTURE_2 && resultCode == RESULT_OK) {
            if (dataLoadingDialog == null) {
                dataLoadingDialog = ProgressDialog.show(this, this.getResources()
                        .getString(R.string.setting_hint), this.getResources()
                        .getString(R.string.setting_updateing_avator), true, true);
            } else {
                dataLoadingDialog.show();
            }
            MoteManager.uploadCommonFile(tempFile, loginVO.token).startUI(new ApiCallback<String>() {
                @Override
                public void onError(int code, String errorInfo) {
                    if (!MoteAuthActivity.this.isFinishing()) {
                        dataLoadingDialog.dismiss();
                        Toast.makeText(MoteAuthActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                        tempFile.delete();
                    }
                }

                @Override
                public void onSuccess(String s) {
                    if (!MoteAuthActivity.this.isFinishing()) {
                        dataLoadingDialog.dismiss();
                        loginVO.user.authenPic2 = s;
                        LoginManager.saveLoginInfo(MoteAuthActivity.this, loginVO);
                        fillData();
                        tempFile.delete();
                    }
                }

                @Override
                public void onProgress(int progress) {

                }
            });

        } else if (requestCode == TAKE_PICTURE_3 && resultCode == RESULT_OK) {
            if (dataLoadingDialog == null) {
                dataLoadingDialog = ProgressDialog.show(this, this.getResources()
                        .getString(R.string.setting_hint), this.getResources()
                        .getString(R.string.setting_updateing_avator), true, true);
            } else {
                dataLoadingDialog.show();
            }
            MoteManager.uploadCommonFile(tempFile, loginVO.token).startUI(new ApiCallback<String>() {
                @Override
                public void onError(int code, String errorInfo) {
                    if (!MoteAuthActivity.this.isFinishing()) {
                        dataLoadingDialog.dismiss();
                        Toast.makeText(MoteAuthActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                        tempFile.delete();
                    }
                }

                @Override
                public void onSuccess(String s) {
                    if (!MoteAuthActivity.this.isFinishing()) {
                        dataLoadingDialog.dismiss();
                        loginVO.user.authenPic3 = s;
                        LoginManager.saveLoginInfo(MoteAuthActivity.this, loginVO);
                        fillData();
                        tempFile.delete();
                    }
                }

                @Override
                public void onProgress(int progress) {

                }
            });
        }

    }


}
