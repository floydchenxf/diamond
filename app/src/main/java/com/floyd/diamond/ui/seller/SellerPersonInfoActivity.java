package com.floyd.diamond.ui.seller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.BitmapProcessor;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.biz.constants.EnvConstants;
import com.floyd.diamond.biz.manager.FileUploadManager;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.manager.SellerManager;
import com.floyd.diamond.biz.tools.DataBaseUtils;
import com.floyd.diamond.biz.tools.FileTools;
import com.floyd.diamond.biz.tools.ImageUtils;
import com.floyd.diamond.biz.tools.ThumbnailUtils;
import com.floyd.diamond.biz.vo.AreaDetailVO;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.mote.UserVO;
import com.floyd.diamond.biz.vo.seller.SellerInfoUpdateParams;
import com.floyd.diamond.biz.vo.seller.SellerInfoVO;
import com.floyd.diamond.ui.DialogCreator;
import com.floyd.diamond.ui.ImageLoaderFactory;
import com.floyd.diamond.ui.activity.ProfileAddressActivity;
import com.floyd.diamond.ui.graphic.CropImageActivity;
import com.floyd.diamond.ui.loading.DataLoadingView;
import com.floyd.diamond.ui.loading.DefaultDataLoadingView;
import com.floyd.diamond.ui.view.UIAlertDialog;
import com.floyd.diamond.ui.view.YWPopupWindow;
import com.floyd.pickview.popwindow.DatePickerPopWin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class SellerPersonInfoActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "PersonInfoActivity";
    private String tempImage = "image_temp";
    private String tempImageCompress = "image_tmp";
    private int avatorSize = 720;
    private String avatorTmp = "avator_tmp.jpg";
    private static final int TAKE_PICTURE = 1;
    private static final int CROP_PICTURE_REQUEST = 2;
    private static final int CODE_GALLERY_REQUEST = 3;
    private static final int CODE_ADDRESS_REQUEST = 4;
    private static final int CODE_ITEM_MOBILE_REQUEST = 5;
    private TextView rightView;
    private NetworkImageView personHeadView;

    private ImageView goodsAddressJiantou;

    private View returnItemMobileLayout;
    private View goodsAddressLayout;

    private EditText nickNameView;
    private EditText shopNameView;
    private EditText alipayView;
    private EditText weixinView;
    private EditText qqView;
    private EditText alipayname;

    private EditText returnItemMobileView;
    private TextView goodsCityView;
    private TextView goodsAddressView;
    private TextView takePhotoView;
    private TextView choosePhotoView;
    private TextView cancelView;

    private boolean isEditorMode;
    private ImageLoader mImageLoader;

    private YWPopupWindow ywPopupWindow;

    private SellerInfoVO sellerInfoVO;
    private UserVO userVO;

    private float oneDp;

    private DatePickerPopWin pickerPopWin;

    private InputMethodManager imm;

    private File tempFile;

    private Dialog dataLoadingDialog;
    private DataLoadingView dataLoadingView;

    private AreaDetailVO areaDetailVO;
    private LoginVO loginVO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_person_info);
        oneDp = this.getResources().getDimension(R.dimen.one_dp);
        mImageLoader = ImageLoaderFactory.createImageLoader();
        imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        dataLoadingDialog = DialogCreator.createDataLoadingDialog(this);
        dataLoadingView = new DefaultDataLoadingView();
        dataLoadingView.initView(findViewById(R.id.act_lsloading), this);
        findViewById(R.id.title_back).setOnClickListener(this);
        rightView = (TextView) findViewById(R.id.right);
        rightView.setVisibility(View.VISIBLE);
        rightView.setText("编辑");
        rightView.setOnClickListener(this);
        initView();
        loginVO = LoginManager.getLoginInfo(this);
        userVO = loginVO.user;
        if (userVO != null) {
            fillData(userVO);
        }
        loadData(false);
    }

    private void fillData(UserVO userVO) {

        if (userVO == null) {
            Toast.makeText(this, "登录用户出错", Toast.LENGTH_SHORT).show();
            this.finish();
        }

        sellerInfoVO = SellerManager.getSellerInfo(this);

        String avatarUrl = sellerInfoVO.getPreviewUrl();
        personHeadView.setDefaultImageResId(R.drawable.head);
        personHeadView.setImageUrl(avatarUrl, mImageLoader, new BitmapProcessor() {
            @Override
            public Bitmap processBitmpa(Bitmap bitmap) {
                return ImageUtils.getCircleBitmap(bitmap, 60 * oneDp);
            }
        });
        alipayView.setText(userVO.alipayId);
        alipayname.setText(userVO.alipayName);
        weixinView.setText(userVO.weixin);
        qqView.setText(userVO.qq);
        shopNameView.setText(sellerInfoVO.shopName);
        nickNameView.setText(sellerInfoVO.nickname);
        returnItemMobileView.setText(userVO.returnItemMobile);
        goodsCityView.setText(userVO.getAddressSummary());
        goodsAddressView.setText(userVO.address);
    }


    private void loadData(final boolean isFirst) {
//        if (isFirst) {
//            dataLoadingView.startLoading();
//        } else {
//            dataLoadingDialog.show();
//        }

        MoteManager.getUserInfo(loginVO.token).startUI(new ApiCallback<UserVO>() {
            @Override
            public void onError(int code, String errorInfo) {
//                if (isFirst) {
//                    dataLoadingView.loadFail();
//                } else {
//                    dataLoadingDialog.dismiss();
//                }

                Toast.makeText(SellerPersonInfoActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(UserVO userVO) {
//                if (isFirst) {
//                    dataLoadingView.loadSuccess();
//                } else {
//                    dataLoadingDialog.dismiss();
//                }

                fillData(userVO);
                UserVO lastUser = loginVO.user;
                userVO.type = lastUser.type;
                loginVO.user = userVO;
                LoginManager.saveLoginInfo(SellerPersonInfoActivity.this, loginVO);
            }

            @Override
            public void onProgress(int progress) {

            }
        });


    }

    private void initView() {
        goodsAddressJiantou = (ImageView) findViewById(R.id.goods_address_jiantou);
        personHeadView = (NetworkImageView) findViewById(R.id.touxiang_personinfo);

        goodsAddressLayout = findViewById(R.id.goods_address_layout);
        returnItemMobileLayout = findViewById(R.id.return_item_mobile_layout);

        nickNameView = (EditText) findViewById(R.id.nick_name_view);
        shopNameView = (EditText) findViewById(R.id.shop_name_view);
        alipayView = (EditText) findViewById(R.id.alipay_view);
        alipayname= ((EditText)findViewById(R.id.alipayname_shop));
        weixinView = (EditText) findViewById(R.id.weixin_view);
        qqView = (EditText) findViewById(R.id.qq_view);

        goodsAddressView = (TextView) findViewById(R.id.goods_address_view);
        goodsCityView = (TextView) findViewById(R.id.goods_city_view);
        returnItemMobileView = (EditText) findViewById(R.id.return_item_mobile_view);

        hiddenJiantou();
        removeClickListener();

        ywPopupWindow = new YWPopupWindow(this);
        ywPopupWindow.initView(personHeadView, R.layout.popup_edit_head, (int) (140 * oneDp), new YWPopupWindow.ViewInit() {
            @Override
            public void initView(View v) {
                takePhotoView = (TextView) v.findViewById(R.id.edit_head);
                takePhotoView.setOnClickListener(SellerPersonInfoActivity.this);
                takePhotoView.setText("拍照");
                choosePhotoView = (TextView) v.findViewById(R.id.edit_profile);
                choosePhotoView.setOnClickListener(SellerPersonInfoActivity.this);
                choosePhotoView.setText("从手机相册中选择");
                cancelView = (TextView) v.findViewById(R.id.cancel_button);
                cancelView.setOnClickListener(SellerPersonInfoActivity.this);
            }
        });
        personHeadView.setOnClickListener(this);

        disableEditable();
    }

    private void enableEditable() {
        weixinView.setEnabled(true);
        alipayView.setEnabled(true);
        alipayname.setEnabled(true);
        shopNameView.setEnabled(true);
        qqView.setEnabled(true);
        nickNameView.setEnabled(true);
        returnItemMobileView.setEnabled(true);

        weixinView.setOnClickListener(this);
        alipayView.setOnClickListener(this);
        alipayname.setOnClickListener(this);
        shopNameView.setOnClickListener(this);
        qqView.setOnClickListener(this);
        nickNameView.setOnClickListener(this);
        returnItemMobileView.setOnClickListener(this);
    }

    private void disableEditable() {
        weixinView.setEnabled(false);
        alipayView.setEnabled(false);
        alipayname.setEnabled(false);
        shopNameView.setEnabled(false);
        qqView.setEnabled(false);
        returnItemMobileView.setEnabled(false);
        nickNameView.setEnabled(false);
        returnItemMobileLayout.setEnabled(false);
    }

    private void hiddenPopup() {
        if (!SellerPersonInfoActivity.this.isFinishing()) {
            if (ywPopupWindow.isShowing()) {
                ywPopupWindow.hidePopUpWindow();
            }
        }
    }

    private void hiddenJiantou() {
        goodsAddressJiantou.setVisibility(View.GONE);
    }

    private void showJiantou() {
        goodsAddressJiantou.setVisibility(View.VISIBLE);
    }

    private void removeClickListener() {
        weixinView.setOnClickListener(null);
        alipayView.setOnClickListener(null);
        alipayname.setOnClickListener(null);
        shopNameView.setOnClickListener(null);
        returnItemMobileLayout.setOnClickListener(null);
        qqView.setOnClickListener(null);
        nickNameView.setOnClickListener(null);
        goodsAddressLayout.setOnClickListener(null);
    }

    private void addClickListener() {
        weixinView.setOnClickListener(this);
        alipayView.setOnClickListener(this);
        alipayname.setOnClickListener(this);
        shopNameView.setOnClickListener(this);
        returnItemMobileLayout.setOnClickListener(this);
        qqView.setOnClickListener(this);
        nickNameView.setOnClickListener(this);
        goodsAddressLayout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                if (isEditorMode) {
                    final UIAlertDialog.Builder builder = new UIAlertDialog.Builder(this);
                    SpannableString message = new SpannableString("亲, 您还未保存修改信息，是否保存？");
                    message.setSpan(new RelativeSizeSpan(2), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    message.setSpan(new ForegroundColorSpan(Color.parseColor("#d4377e")), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.setMessage(message).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            isEditorMode = false;
                            doSaveInfo();
                        }
                    }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            SellerPersonInfoActivity.this.finish();
                        }
                    });

                    builder.show();
                    return;
                }
                this.finish();
                break;
            case R.id.right:
                if (isEditorMode) {
                    isEditorMode = false;
                    doSaveInfo();
                } else {
                    isEditorMode = true;
                    showJiantou();
                    addClickListener();
                    enableEditable();
                    rightView.setText("保存");
                }
                break;
            case R.id.goods_address_layout:
                Intent addressIntent = new Intent(SellerPersonInfoActivity.this, ProfileAddressActivity.class);
                addressIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(addressIntent, CODE_ADDRESS_REQUEST);
                break;
            case R.id.edit_head:
                //拍照
                String status = Environment.getExternalStorageState();
                File saveFile = new File(EnvConstants.imageRootPath);

                if (!status.equals(Environment.MEDIA_MOUNTED)) {
                    Toast.makeText(this, R.string.insert_sdcard, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!saveFile.exists()) {
                    saveFile.mkdir();
                }

                tempFile = new File(saveFile.getAbsolutePath() + File.separator + UUID.randomUUID().toString());

                if (saveFile.exists()) {// 判断是否有SD卡
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri uri = Uri.fromFile(tempFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, TAKE_PICTURE);
                } else if (saveFile == null || !saveFile.exists()) {
                    Toast.makeText(this, R.string.insert_sdcard, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.edit_profile:
                //从手机相册中选择
                Intent intentFromGallery = new Intent();
                intentFromGallery.setType("image/*");
                intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
                break;
            case R.id.touxiang_personinfo:
                if (!this.isFinishing()) {
                    hiddenPopup();
                    ywPopupWindow.showPopUpWindow();
                }

                break;
            case R.id.cancel_button:
                hiddenPopup();
                break;
            case R.id.weixin_view:
                weixinView.requestFocus();
                imm.showSoftInput(weixinView, 0);
                break;
            case R.id.nickname_view:
                shopNameView.requestFocus();
                imm.showSoftInput(shopNameView, 0);
                break;
            case R.id.alipay_view:
                alipayView.requestFocus();
                imm.showSoftInput(alipayView, 0);
                break;
            case R.id.alipayname_shop:
                alipayname.requestFocus();
                imm.showSoftInput(alipayname,0);
                break;
            case R.id.shop_name_view:
                shopNameView.requestFocus();
                imm.showSoftInput(shopNameView, 0);
                break;
            case R.id.qq_view:
                qqView.requestFocus();
                imm.showSoftInput(qqView, 0);
                break;
            case R.id.return_item_mobile_view:
                returnItemMobileView.requestFocus();
                imm.showSoftInput(returnItemMobileView, 0);
                break;
            case R.id.act_lsloading:
                loadData(false);
                break;
        }

    }

    private void doSaveInfo() {
        hiddenJiantou();
        removeClickListener();
        disableEditable();
        rightView.setText("编辑");
        dataLoadingDialog.show();
        String weixin = weixinView.getText().toString();
        String alipayId = alipayView.getText().toString();
        String aliname=alipayname.getText().toString();
        String qq = qqView.getText().toString();
        final String nicknameStr = nickNameView.getText().toString();
        final String shopNameStr = shopNameView.getText().toString();
        String returnItemMobile = returnItemMobileView.getText().toString();

        if (areaDetailVO != null) {
            userVO.address = areaDetailVO.addressDetail;
            userVO.provinceId = areaDetailVO.provideId;
            userVO.cityId = areaDetailVO.cityId;
            userVO.districtId = areaDetailVO.districtId;
        }

        userVO.nickname = nicknameStr;
        userVO.weixin = weixin;
        userVO.alipayId = alipayId;
        userVO.alipayName=aliname;
        userVO.qq = qq;

        String token = LoginManager.getLoginInfo(this).token;
        SellerInfoUpdateParams sellerInfoUpdateParams = new SellerInfoUpdateParams();
        sellerInfoUpdateParams.token = token;
        if (areaDetailVO != null) {
            sellerInfoUpdateParams.address = areaDetailVO.addressDetail;
            sellerInfoUpdateParams.provineId = areaDetailVO.provideId;
            sellerInfoUpdateParams.cityId = areaDetailVO.cityId;
            sellerInfoUpdateParams.districtId = areaDetailVO.districtId;
        }
        sellerInfoUpdateParams.alipayId = alipayId;
        sellerInfoUpdateParams.alipayName=aliname;
        sellerInfoUpdateParams.weixin = weixin;
        sellerInfoUpdateParams.qq = qq;
        sellerInfoUpdateParams.nickname = nicknameStr;
        sellerInfoUpdateParams.shopName = shopNameStr;
        sellerInfoUpdateParams.returnItemMobile = returnItemMobile;

        SellerManager.updateSellerInfo(sellerInfoUpdateParams).startUI(new ApiCallback<SellerInfoUpdateParams>() {
            @Override
            public void onError(int code, String errorInfo) {
                if (!SellerPersonInfoActivity.this.isFinishing()) {
                    dataLoadingDialog.dismiss();
                    Toast.makeText(SellerPersonInfoActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSuccess(SellerInfoUpdateParams params) {
                if (!SellerPersonInfoActivity.this.isFinishing()) {
                    dataLoadingDialog.dismiss();
                }

                LoginVO loginVO = LoginManager.getLoginInfo(SellerPersonInfoActivity.this);
                loginVO.user.qq = params.qq;
                loginVO.user.nickname = params.nickname;
                loginVO.user.address = params.address;
                loginVO.user.provinceId = params.provineId;
                loginVO.user.cityId = params.cityId;
                loginVO.user.districtId = params.districtId;
                loginVO.user.alipayId = params.alipayId;
                loginVO.user.alipayName=params.alipayName;
                if (GlobalParams.isDebug){
                    Log.e("TAG",loginVO.user.alipayName+"");
                }
                loginVO.user.weixin = params.weixin;
                loginVO.user.returnItemMobile = params.returnItemMobile;
                LoginManager.saveLoginInfo(SellerPersonInfoActivity.this, loginVO);

                sellerInfoVO.nickname = params.nickname;
                sellerInfoVO.shopName = params.shopName;
                SellerManager.saveSellerInfo(SellerPersonInfoActivity.this, sellerInfoVO);
            }

            @Override
            public void onProgress(int progress) {

            }
        });
    }

    public void onBackPressed() {
        if (!this.isFinishing()) {
            hiddenPopup();

            if (isEditorMode) {
                final UIAlertDialog.Builder builder = new UIAlertDialog.Builder(this);
                SpannableString message = new SpannableString("亲, 您还未保存修改信息，是否保存？");
                message.setSpan(new RelativeSizeSpan(2), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                message.setSpan(new ForegroundColorSpan(Color.parseColor("#d4377e")), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setMessage(message).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        isEditorMode = false;
                        doSaveInfo();
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SellerPersonInfoActivity.this.finish();
                    }
                });

                builder.show();
                return;
            }

            this.finish();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        ywPopupWindow = null;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        hiddenPopup();
        if (requestCode == TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(this, CropImageActivity.class);
                intent.setDataAndType(Uri.fromFile(tempFile), "image/*");// 设置要裁剪的图片
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
                this.startActivityForResult(intent, CROP_PICTURE_REQUEST);
            }
        } else if (requestCode == CROP_PICTURE_REQUEST && resultCode == Activity.RESULT_OK) {
            final File newFile = new File(EnvConstants.imageRootPath, avatorTmp);
            dataLoadingDialog.show();
            final LoginVO loginVO = LoginManager.getLoginInfo(this);

            FileUploadManager.uploadFiles(loginVO.token, newFile).startUI(new ApiCallback<String>() {
                @Override
                public void onError(int code, String errorInfo) {
                    Toast.makeText(SellerPersonInfoActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                    hiddenPopup();
                    dataLoadingDialog.dismiss();
                }

                @Override
                public void onSuccess(String booleanApiResult) {
                    dataLoadingDialog.dismiss();
                    personHeadView.setImageUrl(booleanApiResult, mImageLoader, new BitmapProcessor() {
                        @Override
                        public Bitmap processBitmpa(Bitmap bitmap) {
                            return ImageUtils.getCircleBitmap(bitmap, 60 * oneDp);
                        }
                    });
                    LoginVO loginVo = LoginManager.getLoginInfo(SellerPersonInfoActivity.this);
                    loginVo.user.avartUrl = booleanApiResult;
                    LoginManager.saveLoginInfo(SellerPersonInfoActivity.this, loginVo);
                    sellerInfoVO.avartUrl = booleanApiResult;
                    SellerManager.saveSellerInfo(SellerPersonInfoActivity.this, sellerInfoVO);
                    hiddenPopup();
                    newFile.delete();
                }

                @Override
                public void onProgress(int progress) {

                }
            });

        } else if (requestCode == CODE_GALLERY_REQUEST) {
            if (!this.isFinishing()) {
                hiddenPopup();
            }

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
                            bitmap = BitmapFactory.decodeByteArray(tmpData, 0,
                                    tmpData.length);
                        } catch (OutOfMemoryError e) {
                            Log.e(TAG, e.getMessage(), e);
                            bitmap = BitmapFactory.decodeByteArray(tmpData, 0,
                                    tmpData.length);
                        }
                        FileTools.writeBitmap(EnvConstants.imageRootPath,
                                tempImage, bitmap);
                        File newFile = new File(EnvConstants.imageRootPath, tempImage);
                        bitmap = ThumbnailUtils.getImageThumbnail(newFile,
                                avatorSize, avatorSize, tempImageCompress,
                                false);
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
                    tmpData = null;

                    File tmpFile = new File(EnvConstants.imageRootPath+File.separator+tempImageCompress);
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
                    this.startActivityForResult(intent, CROP_PICTURE_REQUEST);
                }
            }
        } else if (requestCode == CODE_ADDRESS_REQUEST && resultCode == RESULT_OK) {
            areaDetailVO = data.getParcelableExtra(ProfileAddressActivity.AREA_DETAIL_INFO);
            String addressSummary = areaDetailVO.addressSummary;
            String addressDetail = areaDetailVO.addressDetail;
            goodsCityView.setText(addressSummary);
            goodsAddressView.setText(addressDetail);
        }
    }
}
