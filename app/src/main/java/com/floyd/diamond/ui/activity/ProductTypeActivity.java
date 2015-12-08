package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.floyd.diamond.IMChannel;
import com.floyd.diamond.IMImageCache;
import com.floyd.diamond.R;
import com.floyd.diamond.biz.constants.EnvConstants;
import com.floyd.diamond.ui.adapter.ProductTypeAdapter;
import com.floyd.pullrefresh.widget.PullToRefreshListView;

public class ProductTypeActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "ProductTypeActivity";

    public static final int FEMALE_PRODUCT_TYPE = 1;
    public static final int MALE_PRODUCT_TYPE = 2;
    public static final int BABY_PRODUCT_TYPE = 3;
    public static final int MULTI_PRODUCT_TYPE = 4;
    public static final String PRODUCT_TYPE_KEY = "PRODUCT_TYPE_KEY";

    private PullToRefreshListView mPullToRefreshListView;

    private ListView mListView;

    private TextView productTypeNameView;
    private Dialog loadingDialog;
    private ImageLoader mImageLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_type);
        findViewById(R.id.title_back).setOnClickListener(this);

        RequestQueue mQueue = Volley.newRequestQueue(this);
        IMImageCache wxImageCache = IMImageCache.findOrCreateCache(
                IMChannel.getApplication(), EnvConstants.imageRootPath);
        this.mImageLoader = new ImageLoader(mQueue, wxImageCache);
        mImageLoader.setBatchedResponseDelay(0);

        loadingDialog = new Dialog(this, R.style.data_load_dialog);
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.product_type_list);
        productTypeNameView = (TextView) findViewById(R.id.product_type);
        int type = getIntent().getIntExtra("PRODUCT_TYPE_KEY", 1);
        if (type == 1) {
            productTypeNameView.setText(R.string.female_type);
        } else if (type == 2) {
            productTypeNameView.setText(R.string.male_type);
        } else if (type == 3) {
            productTypeNameView.setText(R.string.baby_type);
        } else if (type == 4) {
            productTypeNameView.setText(R.string.multi_type);
        }

        mListView = mPullToRefreshListView.getRefreshableView();
        ProductTypeAdapter adapter = new ProductTypeAdapter(this, mImageLoader, new ProductTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

            }
        });

        loadingDialog.show();
        loadData();
    }

    private void loadData() {
        this.loadingDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                this.finish();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
