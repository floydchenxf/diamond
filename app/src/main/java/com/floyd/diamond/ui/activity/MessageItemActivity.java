package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
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
import com.floyd.diamond.bean.ImageLoader;
import com.floyd.diamond.bean.MessageItem;
import com.floyd.diamond.bean.MyImageLoader;
import com.floyd.diamond.biz.constants.APIConstants;
import com.floyd.diamond.ui.URl;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

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

//    public void setImg() {
//
//        String imgUrl = getIntent().getStringExtra("imgUrl");
//        MyImageLoader loader = new MyImageLoader(queue, imgUrl, imageView, MessageItemActivity.this);
//
//    }

    public void setData(){
        String url= URl.BASEURL+ APIConstants.API_ADV_DETAIL_INFO;
        id=getIntent().getLongExtra("id",0);
        if (GlobalParams.isDebug){
            Log.e("TAG", id+"");
        }
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (GlobalParams.isDebug){
                    Log.e("TAG", response);
                }
                Gson gson=new Gson();
                MessageItem messageItem=gson.fromJson(response,MessageItem.class);

                String content=messageItem.getData().getContent();
                String html = "<p><img src='http://wanzao2.b0.upaiyun.com/system/pictures/4837853/original/20131002140008.png' title='1451806618242088360.jpg' alt='模特推荐.jpg'/></p>";
                webView.loadDataWithBaseURL(null,html, "text/html",  "utf-8", null);
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
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MessageItemActivity.this,"请检查网络连接...", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> params = new HashMap<>();
                params.put("id",4+"");
                return params;
            }
        };
        queue.add(request);
    }

}
