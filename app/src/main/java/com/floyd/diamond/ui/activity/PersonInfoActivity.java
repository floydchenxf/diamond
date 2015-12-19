package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.floyd.diamond.biz.tools.FileTools;
import com.floyd.diamond.biz.tools.ImageUtils;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.UserVO;
import com.floyd.diamond.ui.ImageLoaderFactory;
import com.floyd.diamond.ui.graphic.CropImageActivity;
import com.floyd.diamond.ui.view.YWPopupWindow;
import com.floyd.pickview.LoopListener;
import com.floyd.pickview.LoopView;
import com.floyd.pickview.popwindow.DatePickerPopWin;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Administrator on 2015/11/28.
 */
public class PersonInfoActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "PersonInfoActivity";
    private static final int TAKE_PICTURE = 1;
    private static final int CROP_PICTURE_REQUEST = 2;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personinfo);
        oneDp = this.getResources().getDimension(R.dimen.one_dp);
        mImageLoader = ImageLoaderFactory.createImageLoader();
        imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        dataLoadingDialog = new Dialog(this, R.style.data_load_dialog);
        findViewById(R.id.left).setOnClickListener(this);
        rightView = (TextView) findViewById(R.id.right);
        rightView.setOnClickListener(this);
        initView();
        fillData();
    }

    private void fillData() {
        LoginVO vo = LoginManager.getLoginInfo(this);
        userVO = vo.user;
        if (userVO == null) {
            Toast.makeText(this, "登录用户出错", Toast.LENGTH_SHORT).show();
            this.finish();
        }

        String avatarUrl = userVO.getPreviewUrl();
        personHeadView.setImageUrl(avatarUrl, mImageLoader, new BitmapProcessor() {
            @Override
            public Bitmap processBitmpa(Bitmap bitmap) {
                return ImageUtils.getCircleBitmap(bitmap, 60 * oneDp);
            }
        });
        alipayView.setText(userVO.alipayId);
        heightView.setText(userVO.height + "");
        birthdayView.setText(userVO.birdthdayStr);
        genderView.setText(userVO.getGender());
        weixinView.setText(userVO.weixin);
        nicknameView.setText(userVO.nickname);

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
            case R.id.left:
                this.finish();
                break;
            case R.id.right:
                if (isEditorMode) {
                    isEditorMode = false;
                    hiddenJiantou();
                    removeClickListener();
                    disableEditable();
                    rightView.setText("编辑");
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
        }

    }

    public void onBackPressed() {
        if (!this.isFinishing()) {
            hiddenPopup();
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
        if (requestCode == TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(this, CropImageActivity.class);
                intent.setDataAndType(Uri.fromFile(tempFile), "image/*");// 设置要裁剪的图片
                intent.putExtra("crop", "true");// crop=true
                // 有这句才能出来最后的裁剪页面.
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", 720);
                intent.putExtra("outputY", 720);
                intent.putExtra("noFaceDetection", true);
                intent.putExtra("return-data", true);
                intent.putExtra("path", "avator_tmp.jpg");
                intent.putExtra("outputFormat", "JPEG");// 返回格式
                this.startActivityForResult(intent, CROP_PICTURE_REQUEST);
            }
        } else if (requestCode == CROP_PICTURE_REQUEST && resultCode == Activity.RESULT_OK) {
        final File newFile = new File(EnvConstants.imageRootPath, "avator_tmp.jpg");
            dataLoadingDialog.show();
            final LoginVO loginVO = LoginManager.getLoginInfo(this);

        FileUploadManager.uploadFiles(loginVO.token, newFile).startUI(new ApiCallback<String>() {
            @Override
            public void onError(int code, String errorInfo) {
                Toast.makeText(PersonInfoActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                hiddenPopup();
                dataLoadingDialog.hide();
            }

            @Override
            public void onSuccess(String booleanApiResult) {
                dataLoadingDialog.hide();
                Bitmap bitmap = FileTools.readBitmap(newFile.getAbsolutePath());
                personHeadView.setImageBitmap(bitmap);
                loginVO.user.avartUrl = booleanApiResult;
                LoginManager.saveLoginInfo(loginVO);
                hiddenPopup();
                newFile.delete();
            }

            @Override
            public void onProgress(int progress) {

            }
        });

    }
    }
}
