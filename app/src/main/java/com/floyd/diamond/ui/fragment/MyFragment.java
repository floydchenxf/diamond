package com.floyd.diamond.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.internal.widget.DrawableUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.BitmapProcessor;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.bean.VipDays;
import com.floyd.diamond.biz.constants.EnvConstants;
import com.floyd.diamond.biz.manager.FileUploadManager;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.manager.SellerManager;
import com.floyd.diamond.biz.tools.DataBaseUtils;
import com.floyd.diamond.biz.tools.FileTools;
import com.floyd.diamond.biz.tools.ImageUtils;
import com.floyd.diamond.biz.tools.PrefsTools;
import com.floyd.diamond.biz.tools.ThumbnailUtils;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.mote.MoteInfoVO;
import com.floyd.diamond.biz.vo.mote.UserExtVO;
import com.floyd.diamond.biz.vo.seller.SellerInfoVO;
import com.floyd.diamond.ui.DialogCreator;
import com.floyd.diamond.ui.ImageLoaderFactory;
import com.floyd.diamond.ui.activity.CareActivity;
import com.floyd.diamond.ui.activity.MainActivity;
import com.floyd.diamond.ui.activity.MoteAuthActivity;
import com.floyd.diamond.ui.activity.MoteWalletSummaryActivity;
import com.floyd.diamond.ui.activity.MyPicActivity;
import com.floyd.diamond.ui.activity.MyTaskActivity;
import com.floyd.diamond.ui.activity.PersonInfoActivity;
import com.floyd.diamond.ui.activity.SettingPersonInfoActivity;
import com.floyd.diamond.ui.activity.VipOneActivity;
import com.floyd.diamond.ui.graphic.CropImageActivity;
import com.floyd.diamond.ui.loading.DataLoadingView;
import com.floyd.diamond.ui.loading.DefaultDataLoadingView;
import com.floyd.diamond.ui.seller.SellerPersonInfoActivity;
import com.floyd.diamond.ui.seller.SellerTaskActivity;
import com.floyd.diamond.ui.seller.SellerWalletActivity;
import com.floyd.diamond.ui.view.YWPopupWindow;
import com.floyd.diamond.utils.CommonUtil;
import com.zhy.android.percent.support.PercentLayoutHelper;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MyFragment extends BackHandledFragment implements View.OnClickListener {

    private static final String TAG = "MyFragment";

    private static final int CODE_GALLERY_REQUEST = 80;
    private static final int CROP_PICTURE_REQUEST = 81;

    private YWPopupWindow ywPopupWindow;

    private TextView editHeadButton;
    private TextView editProfileButton;
    private TextView cancelButton;
    private NetworkImageView headView;
    private NetworkImageView bgHeadView;

    private TextView careView;//我的关注
    private TextView taskView; //我的任务
    private TextView pictrueView; //我的图库
    private TextView volleyView; //我的钱包
    private TextView setView; //我的设置

    private TextView nicknameView;//别名
    private TextView shopView; //店铺数量
    private TextView qiangView; //抢单数量
    private TextView placeView; //地址
    private TextView vip_1;//买家秀会员
    private TextView vip_2;//测款测图会员
    private ImageView home_vip;

    private LoginVO loginVO;

    private View authLayout;
    private TextView authStatusView;

    private String tempImage = "image_temp";
    private String tempImageCompress = "image_tmp";
    private int avatorSize = 720;
    private String avatorTmp = "avator_tmp.jpg";
    private File newFile = new File(EnvConstants.imageRootPath, tempImage);
    private File tmpFile = new File(EnvConstants.imageRootPath, tempImageCompress);

    private ProgressDialog avatorDialog;

    private ImageLoader mImageLoader;

    private DataLoadingView dataLoadingView;
    private Dialog dataLoadingDialog;

    private PercentLinearLayout seller_cz;

    private boolean isVip=true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageLoader = ImageLoaderFactory.createImageLoader();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        dataLoadingView = new DefaultDataLoadingView();
        dataLoadingView.initView(view, this);
        dataLoadingDialog = DialogCreator.createDataLoadingDialog(this.getActivity());

        seller_cz= ((PercentLinearLayout) view.findViewById(R.id.cz_seller));
        vip_1= ((TextView) view.findViewById(R.id.vip_1));
        vip_2= ((TextView) view.findViewById(R.id.vip_2));
        vip_1.setOnClickListener(this);
        vip_2.setOnClickListener(this);
        home_vip= ((ImageView) view.findViewById(R.id.home_vip));

        careView = (TextView) view.findViewById(R.id.care);
        taskView = (TextView) view.findViewById(R.id.task);
        pictrueView = (TextView) view.findViewById(R.id.pictrue);
        volleyView = (TextView) view.findViewById(R.id.volley);
        setView = (TextView) view.findViewById(R.id.set);
        authLayout = view.findViewById(R.id.auth_view);
        authStatusView = (TextView) view.findViewById(R.id.auth_status_view);
        authLayout.setVisibility(View.GONE);

        careView.setOnClickListener(this);
        taskView.setOnClickListener(this);
        pictrueView.setOnClickListener(this);
        volleyView.setOnClickListener(this);
        setView.setOnClickListener(this);
        authLayout.setOnClickListener(this);


        headView = (NetworkImageView) view.findViewById(R.id.mine_touxiang);
        headView.setDefaultImageResId(R.drawable.head);
        bgHeadView = (NetworkImageView) view.findViewById(R.id.bg_head_lay);
        bgHeadView.setDefaultImageResId(R.drawable.head_lay);
        nicknameView = (TextView) view.findViewById(R.id.mine_name);
        ywPopupWindow = new YWPopupWindow(this.getActivity());
        float height = this.getActivity().getResources().getDimension(R.dimen.edit_head_bar_heigh);
        ywPopupWindow.initView(headView, R.layout.popup_edit_head, (int) height, new YWPopupWindow.ViewInit() {
            @Override
            public void initView(View v) {
                editHeadButton = (TextView) v.findViewById(R.id.edit_head);
                editHeadButton.setOnClickListener(MyFragment.this);
                editProfileButton = (TextView) v.findViewById(R.id.edit_profile);
                editProfileButton.setOnClickListener(MyFragment.this);
                cancelButton = (TextView) v.findViewById(R.id.cancel_button);
                cancelButton.setOnClickListener(MyFragment.this);
            }
        });
        headView.setOnClickListener(this);
        loginVO = LoginManager.getLoginInfo(this.getActivity());
        if (GlobalParams.isDebug) {
            Log.e("loginVo", loginVO.token + "");
        }
        if (loginVO.isModel()) {
            ViewStub stub = (ViewStub) view.findViewById(R.id.mote_summary_info_stub);
            stub.inflate();

            shopView = (TextView) view.findViewById(R.id.shop);
            qiangView = (TextView) view.findViewById(R.id.qiang);
            placeView = (TextView) view.findViewById(R.id.place);

        } else {
            ViewStub stub = (ViewStub) view.findViewById(R.id.seller_summary_info_stub);
            stub.inflate();

            shopView = (TextView) view.findViewById(R.id.shop);
            placeView = (TextView) view.findViewById(R.id.place);
//
        }
//        else {
//            //商家充值界面
//            seller_cz.setVisibility(View.VISIBLE);
//            home_vip.setVisibility(View.VISIBLE);
//            loadVip();
//        }

        loadData(true, true);
        return view;
    }

//    private void showNetworkImage() {
//        headView.setVisibility(View.VISIBLE);
//        bgHeadView.setVisibility(View.VISIBLE);
//    }
//
//    private void hiddenNetworkImage() {
//        headView.setVisibility(View.GONE);
//        bgHeadView.setVisibility(View.GONE);
//    }

    public void onResume() {
        super.onResume();
        loadData(false, false);
        loadVip();
    }

    public void loadVip(){
        SellerManager.getVipDays(loginVO.token,loginVO.user.id).startUI(new ApiCallback<List<VipDays.DataEntity>>() {
            @Override
            public void onError(int code, String errorInfo) {

            }

            @Override
            public void onSuccess(List<VipDays.DataEntity> dataEntities) {
                Drawable drawable1 = getResources().getDrawable(R.drawable.hasopen);
                Drawable drawable2 = getResources().getDrawable(R.drawable.notopen);
                drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight()); //设置边界
                drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight()); //设置边界
                if (GlobalParams.isDebug){
                    Log.e("TAG_size",dataEntities.size()+"");
                }

                if (dataEntities.size()!=0){
                    isVip=true;
                    home_vip.setImageResource(R.drawable.vip_home_light);

                    if (dataEntities.size()==1){
                        for (VipDays.DataEntity dataEntity:dataEntities){
                            if (dataEntity.getVipTypeId()==1){
                                vip_1.setCompoundDrawables(null,null,null,drawable1);
                                vip_2.setCompoundDrawables(null,null,null,drawable2);
                            }else{
                                vip_2.setCompoundDrawables(null,null,null,drawable1);
                                vip_1.setCompoundDrawables(null,null,null,drawable2);
                            }
                        }
                    }else if (dataEntities.size()==2){
                        vip_1.setCompoundDrawables(null,null,null,drawable1);
                        vip_2.setCompoundDrawables(null,null,null,drawable1);
                    }

                }else{
                    isVip=false;
                    home_vip.setImageResource(R.drawable.vip_home_gray);
                    vip_1.setCompoundDrawables(null,null,null,drawable2);
                    vip_2.setCompoundDrawables(null, null, null, drawable2);
                }

            }

            @Override
            public void onProgress(int progress) {

            }
        });
    }

    private void loadData(final boolean needDialog, final boolean isFirst) {
        if (needDialog) {
            if (isFirst) {
                dataLoadingView.startLoading();
            } else {
                dataLoadingDialog.show();
            }
        }
        if (loginVO.isModel()) {
            //模特儿
            MoteInfoVO moteInfoVO = MoteManager.getMoteInfo(this.getActivity());
            if (moteInfoVO != null) {
                shopView.setText(moteInfoVO.orderNum + "");
                qiangView.setText(moteInfoVO.goodeEvalRate + "%");
                placeView.setText(moteInfoVO.followNum + "");
                placeView.setText(moteInfoVO.fee + "");
                if (!TextUtils.isEmpty(moteInfoVO.getHeadUrl())) {
                    headView.setImageUrl(moteInfoVO.getHeadUrl(), mImageLoader, new BitmapProcessor() {
                        @Override
                        public Bitmap processBitmpa(Bitmap bitmap) {
                            return ImageUtils.getCircleBitmap(bitmap, MyFragment.this.getActivity().getResources().getDimension(R.dimen.cycle_head_image_size));
                        }
                    });
                    bgHeadView.setImageUrl(moteInfoVO.getDetailUrl(), mImageLoader, new BitmapProcessor() {
                        @Override
                        public Bitmap processBitmpa(Bitmap bitmap) {
                            return ImageUtils.fastBlur(bitmap, 12);
                        }
                    });
                }
            }

            MoteManager.fetchUserExtInfo(loginVO.token).startUI(new ApiCallback<UserExtVO>() {
                @Override
                public void onError(int code, String errorInfo) {
                   if (errorInfo.equals("TOKEN验证错误.")){
                       Toast.makeText(getActivity(),"密码错误，请重新登录",Toast.LENGTH_SHORT).show();
                       PrefsTools.setStringPrefs(getActivity(), LoginManager.LOGIN_INFO, "");
                       PrefsTools.setStringPrefs(getActivity(), SellerManager.SELLER_INFO, "");
                       PrefsTools.setStringPrefs(getActivity(), MoteManager.MOTE_INFO, "");
                       Intent it = new Intent(getActivity(), MainActivity.class);
                       it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                       startActivity(it);
                   }
                    if (needDialog) {
                        if (isFirst) {
                            dataLoadingView.loadFail();
                        } else {
                            dataLoadingDialog.dismiss();
                        }
                    }
                }

                @Override
                public void onSuccess(UserExtVO userExtVO) {
                    Log.i(TAG, "---" + userExtVO);
                    if (needDialog) {
                        if (isFirst) {
                            dataLoadingView.loadSuccess();
                        } else {
                            dataLoadingDialog.dismiss();
                        }
                    }

                    if (userExtVO.authenStatus == 0) {
                        authLayout.setVisibility(View.VISIBLE);
                        authStatusView.setText("审核中");
                    }else if (userExtVO.authenStatus == 1) {
                        authLayout.setVisibility(View.VISIBLE);
                        authStatusView.setText("未认证");
                    } else if (userExtVO.authenStatus == 2) {
                        authLayout.setVisibility(View.GONE);
                    } else if (userExtVO.authenStatus == 3){
                        authLayout.setVisibility(View.VISIBLE);
                        authStatusView.setText("未通过");
                    }
                    shopView.setText(userExtVO.orderNum + "");
                    qiangView.setText(userExtVO.goodeEvalRate + "%");
                    placeView.setText(userExtVO.followNum + "");
                    nicknameView.setText(userExtVO.nickname);
                    if (!TextUtils.isEmpty(userExtVO.getPreviewUrl())) {
                        headView.setImageUrl(userExtVO.getPreviewUrl(), mImageLoader, new BitmapProcessor() {
                            @Override
                            public Bitmap processBitmpa(Bitmap bitmap) {
                                return ImageUtils.getCircleBitmap(bitmap, MyFragment.this.getActivity().getResources().getDimension(R.dimen.cycle_head_image_size));
                            }
                        });
                        bgHeadView.setImageUrl(userExtVO.getDetailUrl(), mImageLoader, new BitmapProcessor() {
                            @Override
                            public Bitmap processBitmpa(Bitmap bitmap) {
                                return ImageUtils.fastBlur(bitmap, 12);
                            }
                        });
                    }
                }

                @Override
                public void onProgress(int progress) {

                }
            });

        } else {

            //店铺
            final SellerInfoVO sellerInfoVO = SellerManager.getSellerInfo(this.getActivity());
            if (sellerInfoVO != null) {
                shopView.setText(sellerInfoVO.shopName);
                nicknameView.setText(sellerInfoVO.nickname);
                placeView.setText(sellerInfoVO.area);
                if (!TextUtils.isEmpty(sellerInfoVO.avartUrl)) {
                    headView.setImageUrl(sellerInfoVO.avartUrl, mImageLoader, new BitmapProcessor() {
                        @Override
                        public Bitmap processBitmpa(Bitmap bitmap) {
                            return ImageUtils.getCircleBitmap(bitmap, MyFragment.this.getActivity().getResources().getDimension(R.dimen.cycle_head_image_size));
                        }
                    });
                    bgHeadView.setImageUrl(sellerInfoVO.avartUrl, mImageLoader, new BitmapProcessor() {
                        @Override
                        public Bitmap processBitmpa(Bitmap bitmap) {
                            return ImageUtils.fastBlur(bitmap, 12);
                        }
                    });
                }
            }

            SellerManager.fetchSellerInfoJob(this.getActivity(), loginVO.token).startUI(new ApiCallback<SellerInfoVO>() {
                @Override
                public void onError(int code, String errorInfo) {
                    if (needDialog) {
                        if (isFirst) {
                            dataLoadingView.loadFail();
                        } else {
                            dataLoadingDialog.dismiss();
                        }
                    }
                    Toast.makeText(MyFragment.this.getActivity(), errorInfo, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(SellerInfoVO vo) {
                    if (needDialog) {
                        if (isFirst) {
                            dataLoadingView.loadSuccess();
                        } else {
                            dataLoadingDialog.dismiss();
                        }
                    }
                    shopView.setText(vo.shopName);
                    nicknameView.setText(vo.nickname);
                    placeView.setText(vo.area);

                    if (!TextUtils.isEmpty(vo.avartUrl)) {
                        headView.setImageUrl(vo.avartUrl, mImageLoader, new BitmapProcessor() {
                            @Override
                            public Bitmap processBitmpa(Bitmap bitmap) {
                                return ImageUtils.getCircleBitmap(bitmap, MyFragment.this.getActivity().getResources().getDimension(R.dimen.cycle_head_image_size));
                            }
                        });
                        bgHeadView.setImageUrl(vo.avartUrl, mImageLoader, new BitmapProcessor() {
                            @Override
                            public Bitmap processBitmpa(Bitmap bitmap) {
                                return ImageUtils.fastBlur(bitmap, 12);
                            }
                        });
                    }
                }

                @Override
                public void onProgress(int progress) {

                }
            });

        }
    }

    @Override
    public boolean onBackPressed() {
        ywPopupWindow.hidePopUpWindow();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_touxiang:
                ywPopupWindow.showPopUpWindow();
                break;
            case R.id.cancel_button:
                ywPopupWindow.hidePopUpWindow();
                break;
            case R.id.edit_head:
                Intent intentFromGallery = new Intent();
                intentFromGallery.setType("image/*");
                intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
                break;
            case R.id.edit_profile:
                ywPopupWindow.hidePopUpWindow();
                Intent editProfileIntent = new Intent();
                if (loginVO.isModel()) {
                    editProfileIntent.setClass(this.getActivity(), PersonInfoActivity.class);
                } else {
                    editProfileIntent.setClass(this.getActivity(), SellerPersonInfoActivity.class);
                }
                startActivity(editProfileIntent);
                break;
            case R.id.set:
                Intent settingIntent = new Intent(this.getActivity(), SettingPersonInfoActivity.class);
                startActivity(settingIntent);
                break;
            case R.id.task:
                if (loginVO.isModel()) {
                    Intent intent = new Intent(this.getActivity(), MyTaskActivity.class);
                    startActivity(intent);
                } else {
                    Intent taskintent = new Intent(this.getActivity(), SellerTaskActivity.class);
                    startActivity(taskintent);
                }
                break;
            case R.id.care:
                Intent intent1 = new Intent(this.getActivity(), CareActivity.class);
                intent1.putExtra("num", 1);
                startActivity(intent1);
                break;
            case R.id.volley:
                if (loginVO.isModel()) {
                    //模特儿钱包
                    Intent volleyIntent = new Intent(this.getActivity(), MoteWalletSummaryActivity.class);
                    startActivity(volleyIntent);

                } else {
                    //商家钱包
                    Intent volleyIntent = new Intent(this.getActivity(), SellerWalletActivity.class);
                    startActivity(volleyIntent);
                }
                break;
            case R.id.act_ls_fail_layout:
                //加载失败刷新
                loadData(true, true);
                break;
            case R.id.pictrue:
                Log.i(TAG, "--------model type:" + loginVO.isModel());
                Intent it = new Intent(this.getActivity(), MyPicActivity.class);
                startActivity(it);
                break;
            case R.id.auth_view:
                if (authStatusView.getText().toString().equals("未认证")||authStatusView.getText().toString().equals("未通过")){
                    Intent authIntent = new Intent(this.getActivity(), MoteAuthActivity.class);
                    authIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(authIntent);
                }
                break;
            case R.id.vip_1:
                //跳转到会员开通界面
                startActivity(new Intent(getActivity(), VipOneActivity.class));
                break;
            case R.id.vip_2:
                //跳转到会员开通界面
                startActivity(new Intent(getActivity(), VipOneActivity.class));
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_GALLERY_REQUEST) {
            if (!this.getActivity().isFinishing()) {
                ywPopupWindow.hidePopUpWindow();
            }

            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                if (uri != null) {
                    InputStream in = null;
                    String type = "";
                    byte[] tmpData = null;
                    try {
                        in = this.getActivity().getContentResolver().openInputStream(uri);
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
                            in = this.getActivity().getContentResolver().openInputStream(
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
                        newFile = new File(EnvConstants.imageRootPath, tempImage);
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
                            cursor = DataBaseUtils.doContentResolverQueryWrapper(this.getActivity(), uri,
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

                        Bitmap bitmap = ThumbnailUtils.getImageThumbnailFromAlbum(this.getActivity(), uri, avatorSize, avatorSize, tempImageCompress, orientation);

                        if (bitmap != null) {
                            bitmap.recycle();
                            bitmap = null;
                        }
                    }
                    tmpData = null;

                    Intent intent = new Intent(this.getActivity(), CropImageActivity.class);
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

        } else if (requestCode == CROP_PICTURE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (avatorDialog == null) {
                Context context = this.getActivity();
                avatorDialog = ProgressDialog.show(context, context.getResources()
                        .getString(R.string.setting_hint), context.getResources()
                        .getString(R.string.setting_updateing_avator), true, true);
            }

            newFile = new File(EnvConstants.imageRootPath, avatorTmp);
            avatorDialog.show();

            FileUploadManager.uploadFiles(loginVO.token, newFile).startUI(new ApiCallback<String>() {
                @Override
                public void onError(int code, String errorInfo) {
                    Toast.makeText(MyFragment.this.getActivity(), errorInfo, Toast.LENGTH_SHORT).show();
                    avatorDialog.dismiss();
                }

                @Override
                public void onSuccess(String booleanApiResult) {
                    avatorDialog.dismiss();
                    Log.i(TAG, booleanApiResult);
                    headView.setImageUrl(CommonUtil.getImage_200(booleanApiResult), mImageLoader, new BitmapProcessor() {
                        @Override
                        public Bitmap processBitmpa(Bitmap bitmap) {
                            return ImageUtils.getCircleBitmap(bitmap, MyFragment.this.getActivity().getResources().getDimension(R.dimen.cycle_head_image_size));
                        }
                    });

                    bgHeadView.setImageUrl(CommonUtil.getImage_400(booleanApiResult), mImageLoader, new BitmapProcessor() {
                        @Override
                        public Bitmap processBitmpa(Bitmap bitmap) {
                            return ImageUtils.fastBlur(bitmap, 12);
                        }
                    });
                    newFile.delete();
                }

                @Override
                public void onProgress(int progress) {

                }
            });

        }
    }
}
