package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.floyd.diamond.R;
import com.floyd.diamond.ui.webview.DiamondWebViewClient;
import com.floyd.diamond.ui.webview.XBlinkUIModel;

public class H5Activity extends Activity implements View.OnClickListener, DiamondWebViewClient.WebViewErrorListener, DiamondWebViewClient.WebViewPageCallback {

    private static final String TAG = "H5Activity";

    private WebView webView;
    private TextView titlenameView;
    protected DiamondWebViewClient webViewClient;

    private ProgressBar progressbar;

    private View titleLayout;

    private XBlinkUIModel wvUIModel = null;

    private boolean showProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5);
        findViewById(R.id.title_back).setOnClickListener(this);
        titleLayout = findViewById(R.id.title);
        initWebView();
        titlenameView = (TextView) findViewById(R.id.title_name);
        wvUIModel = new XBlinkUIModel(this, webView);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, 6);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, -1);
        wvUIModel.setLoadingView(progressbar, params);
        wvUIModel.enableShowLoading();

        H5Data data = getIntent().getParcelableExtra(H5Data.H5_DATA);
        showProcess = data.showProcess;
        if (!TextUtils.isEmpty(data.title)) {
            titlenameView.setVisibility(View.VISIBLE);
            titlenameView.setText(data.title);
        }

        if (data.showNav) {
            titleLayout.setVisibility(View.VISIBLE);
        } else {
            titleLayout.setVisibility(View.GONE);
        }

        Log.i(TAG, "data is ======:" + data.data);
        if (data.isUrl()) {
            webView.loadUrl(data.data);
        } else {
            webView.loadDataWithBaseURL("", data.data, "text/html", "utf-8", null);
        }
    }

    private void initWebView() {
        webView = (WebView) findViewById(R.id.h5_web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setDefaultFontSize(18);
        webView.setVerticalScrollBarEnabled(false);
        webViewClient = new DiamondWebViewClient(this);
        webViewClient.setWebViewErrorListener(this);
        webViewClient.setmWebViewPageCallback(this);
        webView.setWebViewClient(webViewClient);
        webView.requestFocus();
        CookieManager.getInstance().setAcceptCookie(true);
        if (android.os.Build.VERSION.SDK_INT >= 19) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
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
    public void onReceivedError() {
        wvUIModel.loadErrorPage();
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (showProcess) {
            wvUIModel.showLoadingView();
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (showProcess) {
            wvUIModel.hideLoadingView();
        }
    }

    public static class H5Data implements Parcelable {
        public static final String H5_DATA = "__H5_DATA__";
        public static final int H5_DATA_TYPE_URL = 1;
        public static final int H5_DATA_TYPE_DATA = 2;
        public int dataType;
        public String title;
        public String data;
        public boolean showNav;
        public boolean showProcess;

        public H5Data() {

        }

        public H5Data(Parcel in) {
            dataType = in.readInt();
            title = in.readString();
            data = in.readString();
            showNav = in.readByte() != 0;
            showProcess = in.readByte() != 0;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(dataType);
            dest.writeString(title);
            dest.writeString(data);
            dest.writeByte((byte) (showNav ? 1 : 0));
            dest.writeByte((byte) (showProcess ? 1 : 0));
        }

        public static final Parcelable.Creator<H5Data> CREATOR = new Parcelable.Creator<H5Data>() {
            public H5Data createFromParcel(Parcel in) {
                return new H5Data(in);
            }

            public H5Data[] newArray(int size) {
                return new H5Data[size];
            }
        };

        public boolean isUrl() {
            return dataType == H5_DATA_TYPE_URL;
        }

    }
}
