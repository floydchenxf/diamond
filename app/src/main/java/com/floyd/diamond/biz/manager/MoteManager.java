package com.floyd.diamond.biz.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.aync.AsyncJob;
import com.floyd.diamond.aync.Func;
import com.floyd.diamond.aync.HttpJobFactory;
import com.floyd.diamond.biz.constants.APIConstants;
import com.floyd.diamond.biz.func.StringFunc;
import com.floyd.diamond.biz.parser.AbstractJsonParser;
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

    public static AsyncJob<MoteInfoVO> fetchMoteInfoJob(final Context context, String accessToken) {
        String url = APIConstants.HOST + APIConstants.API_MY_MOTE_INFO;
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", accessToken);
        return HttpJobFactory.createHttpJob(url, params, HttpMethod.POST).map(new StringFunc()).flatMap(new Func<String, AsyncJob<MoteInfoVO>>() {
            @Override
            public AsyncJob<MoteInfoVO> call(final String s) {
                return new AsyncJob<MoteInfoVO>() {
                    @Override
                    public void start(ApiCallback<MoteInfoVO> callback) {
                        new AbstractJsonParser<MoteInfoVO>() {
                            @Override
                            protected MoteInfoVO convert2Obj(String data) {
                                Gson gson = new Gson();
                                return gson.fromJson(data, MoteInfoVO.class);
                            }
                        }.doParse(callback, s);
                    }
                };
            }
        });
    }


}
