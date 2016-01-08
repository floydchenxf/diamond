package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.floyd.diamond.R;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.bean.MessageItem;
import com.floyd.diamond.biz.constants.APIConstants;
import com.floyd.diamond.ui.URl;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/26.
 */
public class MessageItemActivity extends Activity {
    private WebView webView;
    private RequestQueue queue;
    private LinearLayout back;//返回
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messagesecond);

        init();

        setData();
    }

    public void init() {
        webView = ((WebView) findViewById(R.id.webView_message));
        queue = Volley.newRequestQueue(MessageItemActivity.this);
        back = ((LinearLayout) findViewById(R.id.back));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setData() {

        id = getIntent().getLongExtra("id", 0);
        if (GlobalParams.isDebug) {
            Log.e("TAG", id + "");
        }
//<<<<<<< HEAD
//=======
//        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                if (GlobalParams.isDebug){
//                    Log.e("TAG", response);
//                }
//
//                if (TextUtils.isEmpty(response)) {
//                    Toast.makeText(MessageItemActivity.this, "请求输入为空！", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                Gson gson=new Gson();
//                MessageItem messageItem=gson.fromJson(response,MessageItem.class);
//
//                String content=messageItem.getData().getContent();
//                String html = "<p><img src='http://wanzao2.b0.upaiyun.com/system/pictures/4837853/original/20131002140008.png' title='1451806618242088360.jpg' alt='模特推荐.jpg'/></p>";
//                webView.loadDataWithBaseURL(null,html, "text/html",  "utf-8", null);
//                // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
//                webView.setWebViewClient(new WebViewClient() {
//                    @Override
//                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                        // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
//                        view.loadUrl(url);
//                        return true;
//                    }
//                });
//>>>>>>> dde800801524fb32b9e3bf9d5591587bc1621368

        String url = URl.BASEURL + APIConstants.API_ADV_DETAIL_INFO+"?id="+id;
        String html = "<p><img src='http://wanzao2.b0.upaiyun.com/system/pictures/4837853/original/20131002140008.png' title='1451806618242088360.jpg' alt='模特推荐.jpg'/></p>";
//        webView.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
        webView.loadUrl(url);
        // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });

    }
}
