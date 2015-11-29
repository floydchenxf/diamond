package com.floyd.diamond.biz.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.aync.AsyncJob;
import com.floyd.diamond.aync.HttpJobFactory;
import com.floyd.diamond.biz.ApiResult;
import com.floyd.diamond.biz.constants.APIConstants;
import com.floyd.diamond.biz.func.AbstractJsonApiCallback;
import com.floyd.diamond.biz.func.StringFunc;
import com.floyd.diamond.biz.tools.PrefsTools;
import com.floyd.diamond.biz.vo.MoteInfoVO;
import com.floyd.diamond.channel.request.HttpMethod;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by floyd on 15-11-28.
 */
public class MoteManager {
    private static final String TAG = "MoteManager";

    private static final String MOTE_INFO = "mote_info";

    public static MoteInfoVO getMoteInfo(Context context) {
        String moteInfo = PrefsTools.getStringPrefs(context, MOTE_INFO, "");
        if (TextUtils.isEmpty(moteInfo)) {
            return null;
        }

        MoteInfoVO vo = new MoteInfoVO();
        try {
            JSONObject json = new JSONObject(moteInfo);
            String data = json.getString("data");
            Gson gson = new Gson();
            vo = gson.fromJson(data, MoteInfoVO.class);
//            vo.nickname = data.getString("nickname");
//            vo.orderNum = data.getInt("orderNum");
//            vo.authenPic1 = data.getString("authenPic1");
//            vo.authenPic2 = data.getString("authenPic2");
//            vo.authenPic3 = data.getString("authenPic3");
//            vo.authenStatus = data.getString("authenStatus");
        } catch (JSONException e) {
            Log.e(TAG, "parse json cause error:", e);
            return null;
        }
        return vo;
    }

    public static AsyncJob<ApiResult<MoteInfoVO>> fetchMoteInfoJob(final Context context, String accessToken) {
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
                        Gson gson = new Gson();
                        MoteInfoVO vo = gson.fromJson(data.toString(), MoteInfoVO.class);
//                        vo.nickname = data.getString("nickname");
//                        vo.orderNum = data.getInt("orderNum");
//                        vo.authenPic1 = data.getString("authenPic1");
//                        vo.authenPic2 = data.getString("authenPic2");
//                        vo.authenPic3 = data.getString("authenPic3");
//                        vo.authenStatus = data.getString("authenStatus");
                        PrefsTools.setStringPrefs(context, MOTE_INFO, s);
                        return vo;
                    }
                });
            }
        };
    }


}
