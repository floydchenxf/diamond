package com.floyd.diamond.biz.manager;

import android.util.Log;

import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.aync.AsyncJob;
import com.floyd.diamond.aync.HttpJobFactory;
import com.floyd.diamond.biz.ApiResult;
import com.floyd.diamond.biz.constants.APIConstants;
import com.floyd.diamond.biz.func.AbstractJsonApiCallback;
import com.floyd.diamond.biz.func.StringFunc;
import com.floyd.diamond.biz.vo.MoteInfoVO;
import com.floyd.diamond.channel.request.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by floyd on 15-11-28.
 */
public class MoteManager {
    private static final String TAG = "MoteManager";

    public static AsyncJob<ApiResult<MoteInfoVO>> fetchMoteInfoJob(String accessToken) {
        String url = APIConstants.HOST + APIConstants.API_MY_MOTE_INFO;
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", accessToken);
        final AsyncJob<String> httpJob = HttpJobFactory.createHttpJob(url, params, HttpMethod.POST).map(new StringFunc());


        return new AsyncJob<ApiResult<MoteInfoVO>>() {
            @Override
            public void start(ApiCallback<ApiResult<MoteInfoVO>> callback) {
                httpJob.start(new AbstractJsonApiCallback<MoteInfoVO>(callback) {
                    @Override
                    protected MoteInfoVO convert2Obj(String s, JSONObject data) throws JSONException {
                        Log.i(TAG, "---fetchMoteInfoJob:" + data.toString());
                        MoteInfoVO vo = new MoteInfoVO();
                        vo.nickname = data.getString("nickname");
                        vo.age = data.getInt("age");
                        vo.area = data.getString("area");
                        vo.authenPic1 = data.getString("authenPic1");
                        vo.authenPic2 = data.getString("authenPic2");
                        vo.authenPic3 = data.getString("authenPic3");
                        vo.authenStatus = data.getString("authenStatus");
                        return vo;
                    }
                });
            }
        };
    }


}