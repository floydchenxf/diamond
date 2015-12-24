package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.MessageQueue;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.floyd.diamond.R;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.bean.ImageLoader;
import com.floyd.diamond.bean.MyImageLoader;
import com.floyd.diamond.ui.URl;

import java.util.HashMap;
import java.util.Map;

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

       // setData();
    }

    public void init(){
        imageView= ((ImageView) findViewById(R.id.image_message));
        queue= Volley.newRequestQueue(MessageItemActivity.this);
        back= ((LinearLayout) findViewById(R.id.back));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setImg(){
        String imgUrl=getIntent().getStringExtra("imgUrl");
        MyImageLoader loader=new MyImageLoader(queue,imgUrl,imageView,MessageItemActivity.this);
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
