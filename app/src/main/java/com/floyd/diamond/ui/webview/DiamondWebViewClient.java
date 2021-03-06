package com.floyd.diamond.ui.webview;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.floyd.diamond.bean.GlobalParams;

/**
 * Created by floyd on 16-1-9.
 */
public class DiamondWebViewClient extends WebViewClient {
    private Context mContext;
    private WebViewErrorListener mWebViewErrorListener;
    private WebViewPageCallback mWebViewPageCallback;
    private String token;

    public DiamondWebViewClient(Context context,String token) {
        this.mContext = context;
        this.token=token;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (mWebViewPageCallback != null) {
            mWebViewPageCallback.onPageStarted(view, url, favicon);
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        String webStr;
        if (token==null){
            webStr="javascript:var token = '"+token+")';" +
                    "window.localStorage.setItem('moteApp',true);";
        }else{
            webStr="javascript:var token = '"+token+")';" +
                    "window.localStorage.setItem('token',token);" +
                    "window.localStorage.setItem('moteApp',true);";
        }

        view.loadUrl(webStr);

        if (mWebViewPageCallback != null) {
            mWebViewPageCallback.onPageFinished(view, url);
        }
    }

    @Override
    public void onReceivedError(WebView view, int errorCode,
                                String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        if (mWebViewErrorListener != null) {
            mWebViewErrorListener.onReceivedError();
        }
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return super.shouldOverrideUrlLoading(view, url);
    }

    public void setWebViewErrorListener(
            WebViewErrorListener webViewErrorListener) {
        this.mWebViewErrorListener = webViewErrorListener;
    }

    public void setmWebViewPageCallback(WebViewPageCallback webViewPageCallback) {
        this.mWebViewPageCallback = webViewPageCallback;
    }

    public void onReceivedSslError(final WebView view, final SslErrorHandler handler,
                                   SslError error) {
        super.onReceivedSslError(view, handler, error);
        if (mWebViewErrorListener != null) {
            mWebViewErrorListener.onReceivedError();
        }
    }

    public interface WebViewErrorListener {
        void onReceivedError();
    }

    public interface WebViewPageCallback {
        void onPageStarted(WebView view, String url, Bitmap favicon);

        void onPageFinished(WebView view, String url);
    }
}
