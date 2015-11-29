package com.floyd.diamond.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.biz.ApiResult;
import com.floyd.diamond.biz.constants.EnvConstants;
import com.floyd.diamond.biz.manager.FileUploadManager;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.manager.SellerManager;
import com.floyd.diamond.biz.tools.DataBaseUtils;
import com.floyd.diamond.biz.tools.FileTools;
import com.floyd.diamond.biz.tools.ThumbnailUtils;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.MoteInfoVO;
import com.floyd.diamond.biz.vo.SellerInfoVO;
import com.floyd.diamond.ui.graphic.CropImageActivity;
import com.floyd.diamond.ui.view.YWPopupWindow;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2015/11/25.
 */
public class MyFragment extends BackHandledFragment implements View.OnClickListener {

    private static final String TAG = "MyFragment";

    private static final int CODE_GALLERY_REQUEST = 80;
    private static final int CROP_PICTURE_REQUEST = 81;

    private YWPopupWindow ywPopupWindow;

    private TextView editHeadButton;
    private TextView editProfileButton;
    private TextView cancelButton;
    private ImageView headView;

    private TextView careView;//我的关注
    private TextView taskView; //我的任务
    private TextView pictrueView; //我的图库
    private TextView volleyView; //我的钱包
    private TextView setView; //我的设置

    private TextView nicknameView;//别名
    private TextView shopView; //店铺数量
    private TextView qiangView; //抢单数量
    private TextView placeView; //地址

    private ImageView shopPicView;
    private ImageView qiangPicView;
    private ImageView placePicView;

    private LoginVO loginVO;

    private String tempImage = "image_temp";
    private String tempImageCompress = "image_tmp";
    private int avatorSize = 720;
    private String avatorTmp = "avator_tmp.jpg";
    private File newFile = new File(EnvConstants.imageRootPath, tempImage);
    private File tmpFile = new File(EnvConstants.imageRootPath, tempImageCompress);

    private ProgressDialog avatorDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        careView = (TextView) view.findViewById(R.id.care);
        taskView = (TextView) view.findViewById(R.id.task);
        pictrueView = (TextView) view.findViewById(R.id.pictrue);
        volleyView = (TextView) view.findViewById(R.id.volley);
        setView = (TextView) view.findViewById(R.id.set);

        careView.setOnClickListener(this);
        taskView.setOnClickListener(this);
        pictrueView.setOnClickListener(this);
        volleyView.setOnClickListener(this);
        setView.setOnClickListener(this);


        headView = (ImageView) view.findViewById(R.id.mine_touxiang);
        nicknameView = (TextView) view.findViewById(R.id.mine_name);
        ywPopupWindow = new YWPopupWindow(this.getActivity());
        ywPopupWindow.initView(headView, R.layout.popup_edit_head, R.dimen.edit_head_bar_heigh, new YWPopupWindow.ViewInit() {
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

//        PrefsTools.setStringPrefs(this.getActivity(), LoginManager.LOGIN_INFO, "");
        loginVO = LoginManager.getLoginInfo(this.getActivity());
        if (loginVO.isModel()) {
            ViewStub stub = (ViewStub) view.findViewById(R.id.mote_summary_info_stub);
            stub.inflate();

            shopView = (TextView) view.findViewById(R.id.shop);
            qiangView = (TextView) view.findViewById(R.id.qiang);
            placeView = (TextView) view.findViewById(R.id.place);

            shopPicView = (ImageView) view.findViewById(R.id.shop_pic);
            qiangPicView = (ImageView) view.findViewById(R.id.qiang_pic);
            placePicView = (ImageView) view.findViewById(R.id.place_pic);
        } else {
            ViewStub stub = (ViewStub) view.findViewById(R.id.seller_summary_info_stub);
            stub.inflate();

            shopView = (TextView) view.findViewById(R.id.shop);
            qiangView = (TextView) view.findViewById(R.id.qiang);
            placeView = (TextView) view.findViewById(R.id.place);

            shopPicView = (ImageView) view.findViewById(R.id.shop_pic);
            qiangPicView = (ImageView) view.findViewById(R.id.qiang_pic);
            placePicView = (ImageView) view.findViewById(R.id.place_pic);
        }
        return view;
    }

    public void onResume() {
        super.onResume();

        if (loginVO.isModel()) {
            //模特儿
            MoteInfoVO moteInfoVO = MoteManager.getMoteInfo(this.getActivity());
            if (moteInfoVO != null) {
                shopView.setText(moteInfoVO.orderNum);
                nicknameView.setText(moteInfoVO.nickname);
                qiangView.setText(moteInfoVO.fenNum);
                placeView.setText(moteInfoVO.fee);
                if (!TextUtils.isEmpty(moteInfoVO.avatarUrl)) {
                    ImageLoader.getInstance().displayImage(moteInfoVO.avatarUrl, headView);
                }
            }

            MoteManager.fetchMoteInfoJob(this.getActivity(), loginVO.accessToken).startUI(new ApiCallback<ApiResult<MoteInfoVO>>() {
                @Override
                public void onError(int code, String errorInfo) {
                    Toast.makeText(MyFragment.this.getActivity(), errorInfo, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(ApiResult<MoteInfoVO> moteInfoVOApiResult) {
                    Log.i(TAG, "---" + moteInfoVOApiResult);
                    if (moteInfoVOApiResult.isSuccess()) {
                        MoteInfoVO moteInfoVO = moteInfoVOApiResult.result;
                        shopView.setText(moteInfoVO.orderNum);
                        qiangView.setText(moteInfoVO.fenNum);
                        placeView.setText(moteInfoVO.fee);
                        nicknameView.setText(moteInfoVO.nickname);
                        if (!TextUtils.isEmpty(moteInfoVO.avatarUrl)) {
                            ImageLoader.getInstance().displayImage(moteInfoVO.avatarUrl, headView);
                        }
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
                qiangView.setText(sellerInfoVO.orderNum + "");
                placeView.setText(sellerInfoVO.area);
                if (!TextUtils.isEmpty(sellerInfoVO.avatarUrl)) {
                    ImageLoader.getInstance().displayImage(sellerInfoVO.avatarUrl, headView);
                }
            }
            SellerManager.fetchSellerInfoJob(this.getActivity(), loginVO.accessToken).startUI(new ApiCallback<ApiResult<SellerInfoVO>>() {
                @Override
                public void onError(int code, String errorInfo) {
                    Toast.makeText(MyFragment.this.getActivity(), errorInfo, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(ApiResult<SellerInfoVO> sellerInfoVOApiResult) {
                    if (sellerInfoVOApiResult.isSuccess()) {
                        SellerInfoVO vo = sellerInfoVOApiResult.result;
                        shopView.setText(vo.shopName);
                        nicknameView.setText(vo.nickname);
                        qiangView.setText(vo.orderNum + "");
                        placeView.setText(vo.area);
                        if (!TextUtils.isEmpty(vo.avatarUrl)) {
                            ImageLoader.getInstance().displayImage(vo.avatarUrl, headView);
                        }
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
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {
            if (!this.getActivity().isFinishing()) {
                ywPopupWindow.hidePopUpWindow();
            }

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
                this.startActivityForResult(intent, CROP_PICTURE_REQUEST);
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

            FileUploadManager.uploadFiles(loginVO.accessToken, newFile).startUI(new ApiCallback<ApiResult<Boolean>>() {
                @Override
                public void onError(int code, String errorInfo) {
                    Toast.makeText(MyFragment.this.getActivity(), errorInfo, Toast.LENGTH_SHORT).show();
                    avatorDialog.hide();
                }

                @Override
                public void onSuccess(ApiResult<Boolean> booleanApiResult) {
                    avatorDialog.hide();
                    Bitmap bitmap = FileTools.readBitmap(newFile.getAbsolutePath());
                    headView.setImageBitmap(bitmap);
                }

                @Override
                public void onProgress(int progress) {

                }
            });

        }
    }
}
