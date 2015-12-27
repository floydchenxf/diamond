package com.floyd.diamond.ui.seller;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.floyd.diamond.R;
import com.floyd.diamond.ui.ImageLoaderFactory;

public class SellerInfoActivity extends Activity implements View.OnClickListener {

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

    private boolean isEditorMode;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_info);

        mImageLoader = ImageLoaderFactory.createImageLoader();

        findViewById(R.id.title_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                this.finish();
                break;
        }
    }
}