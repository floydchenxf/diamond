package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.BitmapProcessor;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.floyd.diamond.R;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.tools.ImageUtils;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.UserVO;
import com.floyd.diamond.ui.ImageLoaderFactory;
import com.floyd.diamond.ui.view.YWPopupWindow;

/**
 * Created by Administrator on 2015/11/28.
 */
public class PersonInfoActivity extends Activity implements View.OnClickListener {
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

    private TextView genderFemaleView;
    private TextView gendermaleView;

    private TextView heightTypeSmallView;
    private TextView heightTypeNormalView;
    private TextView heightTypeFatView;

    private UserVO userVO;

    private float oneDp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personinfo);
        oneDp = this.getResources().getDimension(R.dimen.one_dp);
        mImageLoader = ImageLoaderFactory.createImageLoader();
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
                return ImageUtils.getCircleBitmap(bitmap, 60*oneDp);
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


    //跳转到个人资料修改界面
    public void click(View view) {
        startActivity(new Intent(this, PersonModifyActivity.class));
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
                } else {
                    isEditorMode = true;
                    showJiantou();
                    addClickListener();
                }
                break;
            case R.id.gender_layout:
                if (!this.isFinishing()) {
                    hiddenPopup();
                    genderPopupWindow.showPopUpWindow();
                }
                break;
            case R.id.birthday_layout:
                break;
            case R.id.height_layout:
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
                //拍照
                break;
            case R.id.edit_profile:
                //从手机相册中选择
                break;
            case R.id.gender_female:
                userVO.gender = 2;
                genderView.setText("女");
                if (!this.isFinishing()) {
                    hiddenPopup();
                }
                break;
            case R.id.gender_male:
                userVO.gender = 1;
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
                break;
            case R.id.height_type_normal:
                break;
            case R.id.height_type_fat:
                break;
            case R.id.cancel_button:
                hiddenPopup();
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
}
