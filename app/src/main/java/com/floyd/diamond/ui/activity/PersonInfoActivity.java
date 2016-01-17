package com.floyd.diamond.ui.activity;

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
import com.floyd.diamond.biz.constants.EnvConstants;
import com.floyd.diamond.biz.manager.FileUploadManager;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.tools.DataBaseUtils;
import com.floyd.diamond.biz.tools.DateUtil;
import com.floyd.diamond.biz.tools.FileTools;
import com.floyd.diamond.biz.tools.ImageUtils;
import com.floyd.diamond.biz.tools.ThumbnailUtils;
import com.floyd.diamond.biz.vo.AreaDetailVO;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.mote.UserVO;
import com.floyd.diamond.ui.DialogCreator;
import com.floyd.diamond.ui.ImageLoaderFactory;
import com.floyd.diamond.ui.graphic.CropImageActivity;
import com.floyd.diamond.ui.loading.DataLoadingView;
import com.floyd.diamond.ui.loading.DefaultDataLoadingView;
import com.floyd.diamond.ui.view.UIAlertDialog;
import com.floyd.diamond.ui.view.YWPopupWindow;
import com.floyd.pickview.LoopListener;
import com.floyd.pickview.LoopView;
import com.floyd.pickview.popwindow.DatePickerPopWin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Administrator on 2015/11/28.
 */
public class PersonInfoActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "PersonInfoActivity";
    private String tempImage = "image_temp";
    private String tempImageCompress = "image_tmp";
    private int avatorSize = 720;
    private String avatorTmp = "avator_tmp.jpg";
    private static final int TAKE_PICTURE = 1;
    private static final int CROP_PICTURE_REQUEST = 2;
    private static final int CODE_GALLERY_REQUEST = 3;
    private static final int CODE_ADDRESS_REQUEST = 4;
    private TextView rightView;
    private NetworkImageView personHeadView;

    private ImageView genderJiantou;
    private ImageView birthdayJiantou;
    private ImageView heightTypeJiantou;
    private ImageView heightJiantou;
    private ImageView goodsAddressJiantou;
    private ImageView authJiantou;

    private View genderLayout;
    private View birthdayLayout;
    private View heightLayout;
    private View heightTypeLayout;
    private View goodsAddressLayout;
    private View authLayout;

    private TextView genderView;
    private TextView birthdayView;
    private TextView heightTypeView;
    private TextView heightView;
    private TextView goodsAddressView;
    private TextView goodsCityView;
    private EditText alipayView;
    private EditText weixinView;
    private TextView authView;
    private EditText nicknameView;

    private TextView takePhotoView;
    private TextView choosePhotoView;
    private TextView cancelView;

    private boolean isEditorMode;
    private ImageLoader mImageLoader;

    private YWPopupWindow ywPopupWindow;
    private YWPopupWindow genderPopupWindow;
    private YWPopupWindow heightTypePopupWindow;
    private YWPopupWindow heightPopupWindow;

    private TextView genderFemaleView;
    private TextView gendermaleView;

    private TextView heightTypeSmallView;
    private TextView heightTypeNormalView;
    private TextView heightTypeFatView;

    private UserVO userVO;

    private float oneDp;

    private DatePickerPopWin pickerPopWin;

    private InputMethodManager imm;

    private int heightType;
    private int genderType;

    private File tempFile;

    private Dialog dataLoadingDialog;
    private DataLoadingView dataLoadingView;

    private AreaDetailVO areaDetailVO;

    private  LoginVO loginVO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personinfo);
        if (!LoginManager.isLogin(this)) {
            Toast.makeText(this, "未登录用户", Toast.LENGTH_SHORT).show();
            this.finish();
        }

        oneDp = this.getResources().getDimension(R.dimen.one_dp);
        mImageLoader = ImageLoaderFactory.createImageLoader();
        imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        dataLoadingDialog = DialogCreator.createDataLoadingDialog(this);
        dataLoadingView = new DefaultDataLoadingView();
        dataLoadingView.initView(findViewById(R.id.act_lsloading), this);
        findViewById(R.id.title_back).setOnClickListener(this);
        rightView = (TextView) findViewById(R.id.right);
        rightView.setVisibility(View.VISIBLE);
        rightView.setOnClickListener(this);

        TextView titleNameView = (TextView)findViewById(R.id.title_name);
        titleNameView.setVisibility(View.VISIBLE);
        titleNameView.setText("个人资料");
        initView();

        loginVO = LoginManager.getLoginInfo(this);
        userVO = loginVO.user;
        if (userVO != null) {
            fillData(userVO);
        }
        loadData(true);
    }

    private void fillData(UserVO userVO) {
        String avatarUrl = userVO.getPreviewUrl();
        personHeadView.setDefaultImageResId(R.drawable.tupian);
        personHeadView.setImageUrl(avatarUrl, mImageLoader, new BitmapProcessor() {
            @Override
            public Bitmap processBitmpa(Bitmap bitmap) {
                return ImageUtils.getCircleBitmap(bitmap, 60 * oneDp);
            }
        });
        alipayView.setText(userVO.alipayId);
        heightView.setText(userVO.height + "");
        birthdayView.setText(DateUtil.getDateStr(userVO.birdthday));
        genderView.setText(userVO.getGender());
        weixinView.setText(userVO.weixin);
        nicknameView.setText(userVO.nickname);
        goodsCityView.setText(userVO.getAddressSummary());
        goodsAddressView.setText(userVO.address);
        if (userVO.shape == 1) {
            heightTypeView.setText("骨感");
        } else if (userVO.shape == 2) {
            heightTypeView.setText("标致");
        } else if (userVO.shape == 3) {
            heightTypeView.setText("丰满");
        }

        if (userVO.authenStatus == 2) {
            authView.setTextColor(Color.parseColor("#666666"));
            authView.setText("已认证");
        } else {
            authView.setText("未认证");
            authView.setTextColor(Color.RED);
        }
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

                Toast.makeText(PersonInfoActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
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
                LoginManager.saveLoginInfo(PersonInfoActivity.this, loginVO);
            }

            @Override
            public void onProgress(int progress) {

            }
        });


    }

    private void initView() {
        genderJiantou = (ImageView) findViewById(R.id.gender_jiantou);
        birthdayJiantou = (ImageView) findViewById(R.id.birthday_jiantou);
        heightTypeJiantou = (ImageView) findViewById(R.id.height_type_jiantou);
        heightJiantou = (ImageView) findViewById(R.id.height_jiantou);
        goodsAddressJiantou = (ImageView) findViewById(R.id.goods_address_jiantou);
        authJiantou = (ImageView) findViewById(R.id.auth_jiantou);

        personHeadView = (NetworkImageView) findViewById(R.id.touxiang_personinfo);
        genderLayout = findViewById(R.id.gender_layout);
        birthdayLayout = findViewById(R.id.birthday_layout);
        heightLayout = findViewById(R.id.height_layout);
        heightTypeLayout = findViewById(R.id.height_type_layout);
        goodsAddressLayout = findViewById(R.id.goods_address_layout);
        authLayout = findViewById(R.id.auth_layout);

        nicknameView = (EditText) findViewById(R.id.nickname_view);
        genderView = (TextView) findViewById(R.id.gender_view);
        birthdayView = (TextView) findViewById(R.id.birthday_view);
        heightTypeView = (TextView) findViewById(R.id.height_type_view);
        heightView = (TextView) findViewById(R.id.height_view);
        goodsAddressView = (TextView) findViewById(R.id.goods_address_view);
        goodsCityView = (TextView) findViewById(R.id.goods_city_view);
        alipayView = (EditText) findViewById(R.id.alipay_view);
        weixinView = (EditText) findViewById(R.id.weixin_view);
        authView = (TextView) findViewById(R.id.auth_view);
        authView.setOnClickListener(this);
        hiddenJiantou();
        removeClickListener();

        ywPopupWindow = new YWPopupWindow(this);
        ywPopupWindow.initView(personHeadView, R.layout.popup_edit_head, (int) (140 * oneDp), new YWPopupWindow.ViewInit() {
            @Override
            public void initView(View v) {
                takePhotoView = (TextView) v.findViewById(R.id.edit_head);
                takePhotoView.setOnClickListener(PersonInfoActivity.this);
                takePhotoView.setText("拍照");
                choosePhotoView = (TextView) v.findViewById(R.id.edit_profile);
                choosePhotoView.setOnClickListener(PersonInfoActivity.this);
                choosePhotoView.setText("从手机相册中选择");
                cancelView = (TextView) v.findViewById(R.id.cancel_button);
                cancelView.setOnClickListener(PersonInfoActivity.this);
            }
        });
        personHeadView.setOnClickListener(this);

        genderPopupWindow = new YWPopupWindow(this);
        genderPopupWindow.initView(genderView, R.layout.popup_gender, (int) (100 * oneDp), new YWPopupWindow.ViewInit() {
            @Override
            public void initView(View v) {
                genderFemaleView = (TextView) v.findViewById(R.id.gender_female);
                gendermaleView = (TextView) v.findViewById(R.id.gender_male);
                genderFemaleView.setOnClickListener(PersonInfoActivity.this);
                gendermaleView.setOnClickListener(PersonInfoActivity.this);
            }
        });

        heightTypePopupWindow = new YWPopupWindow(this);
        heightTypePopupWindow.initView(heightTypeView, R.layout.popup_height_type, (int) (180 * oneDp), new YWPopupWindow.ViewInit() {
            @Override
            public void initView(View v) {
                heightTypeSmallView = (TextView) v.findViewById(R.id.height_type_small);
                heightTypeNormalView = (TextView) v.findViewById(R.id.height_type_normal);
                heightTypeFatView = (TextView) v.findViewById(R.id.height_type_fat);
                heightTypeNormalView.setOnClickListener(PersonInfoActivity.this);
                heightTypeFatView.setOnClickListener(PersonInfoActivity.this);
                heightTypeSmallView.setOnClickListener(PersonInfoActivity.this);
                v.findViewById(R.id.cancel_button).setOnClickListener(PersonInfoActivity.this);
            }
        });

        pickerPopWin = new DatePickerPopWin(this, new DatePickerPopWin.OnDatePickedListener() {
            @Override
            public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                birthdayView.setText(dateDesc);
            }
        });

        heightPopupWindow = new YWPopupWindow(this);
        heightPopupWindow.initView(nicknameView, R.layout.popup_height_picker, (int) (210 * oneDp), new YWPopupWindow.ViewInit() {

            private int one;
            private int two;
            private int three;

            @Override
            public void initView(View v) {
                LoopView oneLoopView = (LoopView) v.findViewById(R.id.picker_one);
                LoopView twoLoopView = (LoopView) v.findViewById(R.id.picker_two);
                LoopView threeLoopView = (LoopView) v.findViewById(R.id.picker_three);

                oneLoopView.setNotLoop();
                twoLoopView.setNotLoop();
                threeLoopView.setNotLoop();

                oneLoopView.setTextSize(25);
                twoLoopView.setTextSize(25);
                threeLoopView.setTextSize(25);

                oneLoopView.setListener(new LoopListener() {
                    @Override
                    public void onItemSelect(int item) {
                        one = item;
                    }
                });
                twoLoopView.setListener(new LoopListener() {
                    @Override
                    public void onItemSelect(int item) {
                        two = item;
                    }
                });

                twoLoopView.setInitPosition(5);
                two = 5;
                threeLoopView.setListener(new LoopListener() {
                    @Override
                    public void onItemSelect(int item) {
                        three = item;
                    }
                });
                ArrayList<String> oneList = new ArrayList<String>();
                oneList.add("1");
                oneList.add("2");
                oneLoopView.setArrayList(oneList);
                ArrayList<String> twoList = new ArrayList<String>();
                for (int i = 0; i < 10; i++) {
                    twoList.add(i + "");
                }
                twoLoopView.setArrayList(twoList);
                threeLoopView.setArrayList(twoList);

                v.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        heightView.setText(((one + 1) * 100 + two * 10 + three) + "");
                        if (!PersonInfoActivity.this.isFinishing()) {
                            heightPopupWindow.hidePopUpWindow();
                        }
                    }
                });

                v.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!PersonInfoActivity.this.isFinishing()) {
                            heightPopupWindow.hidePopUpWindow();
                        }
                    }
                });
            }
        });

        disableEditable();
    }

    private void enableEditable() {
        weixinView.setEnabled(true);
        alipayView.setEnabled(true);
        nicknameView.setEnabled(true);
        weixinView.setOnClickListener(this);
        alipayView.setOnClickListener(this);
        nicknameView.setOnClickListener(this);

    }

    private void disableEditable() {
        weixinView.setEnabled(false);
        alipayView.setEnabled(false);
        nicknameView.setEnabled(false);
    }

    private void hiddenPopup() {
        if (!PersonInfoActivity.this.isFinishing()) {
            if (ywPopupWindow.isShowing()) {
                ywPopupWindow.hidePopUpWindow();
            }

            if (genderPopupWindow.isShowing()) {
                genderPopupWindow.hidePopUpWindow();
            }

            if (heightTypePopupWindow.isShowing()) {
                heightTypePopupWindow.hidePopUpWindow();
            }
        }
    }

    private void hiddenJiantou() {
        genderJiantou.setVisibility(View.GONE);
        birthdayJiantou.setVisibility(View.GONE);
        heightTypeJiantou.setVisibility(View.GONE);
        heightJiantou.setVisibility(View.GONE);
        goodsAddressJiantou.setVisibility(View.GONE);
        authJiantou.setVisibility(View.GONE);
    }

    private void showJiantou() {
        genderJiantou.setVisibility(View.VISIBLE);
        birthdayJiantou.setVisibility(View.VISIBLE);
        heightTypeJiantou.setVisibility(View.VISIBLE);
        heightJiantou.setVisibility(View.VISIBLE);
        goodsAddressJiantou.setVisibility(View.VISIBLE);
        authJiantou.setVisibility(View.VISIBLE);
    }

    private void removeClickListener() {
        genderLayout.setOnClickListener(null);
        birthdayLayout.setOnClickListener(null);
        heightLayout.setOnClickListener(null);
        heightTypeLayout.setOnClickListener(null);
        goodsAddressLayout.setOnClickListener(null);
        authLayout.setOnClickListener(null);
    }

    private void addClickListener() {
        genderLayout.setOnClickListener(this);
        birthdayLayout.setOnClickListener(this);
        heightLayout.setOnClickListener(this);
        heightTypeLayout.setOnClickListener(this);
        goodsAddressLayout.setOnClickListener(this);
        authLayout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                if (isEditorMode) {
                    final UIAlertDialog.Builder builder = new UIAlertDialog.Builder(this);
                    builder.setMessage("您还未保存修改信息，是否保存？").setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
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
                            PersonInfoActivity.this.finish();
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
            case R.id.gender_layout:
                if (!this.isFinishing()) {
                    hiddenPopup();
                    genderPopupWindow.showPopUpWindow();
                }
                break;
            case R.id.birthday_layout:
                pickerPopWin.showPopWin(this);
                break;
            case R.id.height_layout:
                heightPopupWindow.showPopUpWindow();
                break;
            case R.id.height_type_layout:
                if (!this.isFinishing()) {
                    hiddenPopup();
                    heightTypePopupWindow.showPopUpWindow();
                }
                break;
            case R.id.goods_address_layout:
                Intent addressIntent = new Intent(PersonInfoActivity.this, ProfileAddressActivity.class);
                addressIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(addressIntent, CODE_ADDRESS_REQUEST);
                break;
            case R.id.auth_layout:
                break;
            case R.id.edit_head:
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
                //拍照
                break;
            case R.id.edit_profile:
                //从手机相册中选择
                Intent intentFromGallery = new Intent();
                intentFromGallery.setType("image/*");
                intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
                break;
            case R.id.gender_female:
                genderType = 2;
                genderView.setText("女");
                if (!this.isFinishing()) {
                    hiddenPopup();
                }
                break;
            case R.id.gender_male:
                genderType = 1;
                genderView.setText("男");
                if (!this.isFinishing()) {
                    hiddenPopup();
                }
                break;
            case R.id.touxiang_personinfo:
                if (!this.isFinishing()) {
                    hiddenPopup();
                    ywPopupWindow.showPopUpWindow();
                }

                break;

            case R.id.height_type_small:
                heightType = 1;
                heightTypeView.setText("骨感");
                hiddenPopup();
                break;
            case R.id.height_type_normal:
                heightType = 2;
                heightTypeView.setText("标致");
                hiddenPopup();
                break;
            case R.id.height_type_fat:
                heightType = 3;
                heightTypeView.setText("丰满");
                hiddenPopup();
                break;
            case R.id.cancel_button:
                hiddenPopup();
                break;
            case R.id.weixin_view:
                weixinView.requestFocus();
                imm.showSoftInput(weixinView, 0);
                break;
            case R.id.nickname_view:
                nicknameView.requestFocus();
                imm.showSoftInput(nicknameView, 0);
                break;
            case R.id.alipay_view:
                alipayView.requestFocus();
                imm.showSoftInput(alipayView, 0);
                break;
            case R.id.auth_view:
                Intent authIntent = new Intent(this, MoteAuthActivity.class);
                authIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(authIntent);
                break;
            case R.id.act_lsloading:
                loadData(true);
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
        String birthdayStr = birthdayView.getText().toString();
        String nicknameStr = nicknameView.getText().toString();
        int gender = genderType;
        String height = heightView.getText().toString();


        userVO.gender = genderType;
        userVO.height = Integer.parseInt(height);
        userVO.shape = heightType;
        if (areaDetailVO != null) {
            userVO.address = areaDetailVO.addressDetail;
            userVO.provinceId = areaDetailVO.provideId;
            userVO.cityId = areaDetailVO.cityId;
            userVO.districtId = areaDetailVO.districtId;
        }

        userVO.nickname = nicknameStr;
        userVO.birdthdayStr = birthdayStr;
        userVO.weixin = weixin;
        userVO.alipayId = alipayId;

        String token = LoginManager.getLoginInfo(this).token;
        MoteManager.updateMoteInfo(userVO, token).startUI(new ApiCallback<UserVO>() {
            @Override
            public void onError(int code, String errorInfo) {
                if (!PersonInfoActivity.this.isFinishing()) {
                    dataLoadingDialog.dismiss();
                }
            }

            @Override
            public void onSuccess(UserVO userVO) {
                if (!PersonInfoActivity.this.isFinishing()) {
                    dataLoadingDialog.dismiss();
                }

                LoginVO loginVO = LoginManager.getLoginInfo(PersonInfoActivity.this);
                loginVO.user.gender = userVO.gender;
                loginVO.user.height = userVO.height;
                loginVO.user.shape = userVO.shape;
                loginVO.user.address = userVO.address;
                loginVO.user.provinceId = userVO.provinceId;
                loginVO.user.cityId = userVO.cityId;
                loginVO.user.districtId = userVO.districtId;
                loginVO.user.nickname = userVO.nickname;
                loginVO.user.birdthdayStr = userVO.birdthdayStr;
                loginVO.user.birdthday = userVO.birdthday;
                loginVO.user.weixin = userVO.weixin;
                loginVO.user.alipayId = userVO.alipayId;
                LoginManager.saveLoginInfo(PersonInfoActivity.this, loginVO);
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
                builder.setMessage("您还未保存修改信息，是否保存？").setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
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
                        PersonInfoActivity.this.finish();
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
        genderPopupWindow = null;
        heightTypePopupWindow = null;
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
                    Toast.makeText(PersonInfoActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
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
                    LoginVO loginVo = LoginManager.getLoginInfo(PersonInfoActivity.this);
                    loginVo.user.avartUrl = booleanApiResult;
                    LoginManager.saveLoginInfo(PersonInfoActivity.this, loginVo);
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
