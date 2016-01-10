package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.floyd.diamond.R;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.biz.constants.APIConstants;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.vo.LoginVO;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/20.
 */
public class CareDialogActivity extends Activity {

    private TextView cancle, sure;
    private LoginVO loginVO;
    private List<String> deleteModel;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout);

        init();
    }

    public void init() {
        queue = Volley.newRequestQueue(this);
        loginVO = LoginManager.getLoginInfo(this);
        cancle = ((TextView) findViewById(R.id.cancle));
        sure = ((TextView) findViewById(R.id.sure));

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteModel = getIntent().getStringArrayListExtra("deleteList");

                String url = APIConstants.HOST + APIConstants.API_CANCEL_FOLLOW;
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (GlobalParams.isDebug) {
                            Log.e("response_success", response);
                            Log.e("response_list", deleteModel.toString().substring(1,deleteModel.toString().length()-1).replaceAll(" +","").trim());
                            Log.e("kongge","1039,1100");
                        }


                        JSONObject object= null;
                        try {
                            object = new JSONObject(response);
                            boolean isSuccess= (boolean) object.get("data");
                            if (isSuccess){
                                //传递要删除的模特id的集合，返回我的关注取消关注之后的界面
                                Intent intent = new Intent(CareDialogActivity.this, CareActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                                Toast.makeText(CareDialogActivity.this,"删除成功...",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(CareDialogActivity.this,"删除失败...",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {

                        //在这里设置需要post的参数
                        Map<String, String> params = new HashMap<>();
                        params.put("moteIds", deleteModel.toString().substring(1,deleteModel.toString().length()-1).replaceAll(" +","").trim());
                        params.put("token", loginVO.token);
                        return params;
                    }
                };


                queue.add(request);
            }
        });
    }
}
