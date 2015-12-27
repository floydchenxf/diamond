package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.floyd.diamond.R;
import com.floyd.diamond.bean.ImageLoader;
import com.floyd.diamond.bean.MyImageLoader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by Administrator on 2015/11/26.
 */
public class MessageItemActivity extends Activity {
    private ImageView imageView;
    private RequestQueue queue;
    private LinearLayout back;//返回

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messagesecond);

        init();

        setImg();

        //setData();
    }

    public void init() {
        imageView = ((ImageView) findViewById(R.id.image_message));
        queue = Volley.newRequestQueue(MessageItemActivity.this);
        back = ((LinearLayout) findViewById(R.id.back));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setImg() {

        String imgUrl = getIntent().getStringExtra("imgUrl");
        MyImageLoader loader = new MyImageLoader(queue, imgUrl, imageView, MessageItemActivity.this);

    }

//    public void setData(){
//        String url= URl.BASEURL+URl.TGDETAIL;
//        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                if (GlobalParams.isDebug){
//                    Log.e("TAG",response);
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(MessageItemActivity.this,"请检查网络连接...",Toast.LENGTH_SHORT).show();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() {
//                //在这里设置需要post的参数
//                Map<String, String> params = new HashMap<>();
//                params.put("id","1");
//                return params;
//            }
//        };
//        queue.add(request);
//    }

}
