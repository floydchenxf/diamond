package com.floyd.diamond.biz.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.aync.AsyncJob;
import com.floyd.diamond.aync.HttpJobFactory;
import com.floyd.diamond.biz.constants.APIConstants;
import com.floyd.diamond.biz.func.AbstractJsonApiCallback;
import com.floyd.diamond.biz.func.StringFunc;
import com.floyd.diamond.biz.tools.PrefsTools;
import com.floyd.diamond.biz.vo.SellerInfoVO;
import com.floyd.diamond.channel.request.HttpMethod;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by floyd on 15-11-29.
 */
public class SellerManager {
    private static final String TAG = "SellerManager";
    private static final String SELLER_INFO = "seller_info";

    public static SellerInfoVO getSellerInfo(Context context) {
        String sellerInfo = PrefsTools.getStringPrefs(context, SELLER_INFO, "");
        if (TextUtils.isEmpty(sellerInfo)) {
            return null;
        }

        SellerInfoVO vo = new SellerInfoVO();
        try {
            JSONObject json = new JSONObject(sellerInfo);
            String data = json.getString("data");
            Gson gson = new Gson();
            vo = gson.fromJson(data, SellerInfoVO.class);
//            vo.nickname = data.getString("nickname");
//            vo.orderNum = data.getInt("orderNum");
//            vo.avartUrl = data.getString("avartUrl");
//            vo.credit = data.getString("credit");
//            vo.area = data.getString("area");
//            vo.shopName = data.getString("shopName");

        } catch (JSONException e) {
            Log.e(TAG, "parse json cause error:", e);
            return null;
        }
        return vo;
    }

    public static AsyncJob<SellerInfoVO> fetchSellerInfoJob(final Context context, String accessToken) {
        String url = APIConstants.HOST + APIConstants.API_MY_SELLER_INFO;
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", accessToken);
        final AsyncJob<String> httpJob = HttpJobFactory.createHttpJob(url, params, HttpMethod.POST).map(new StringFunc());


        return new AsyncJob<SellerInfoVO>() {
            @Override
            public void start(ApiCallback<SellerInfoVO> callback) {
                httpJob.start(new AbstractJsonApiCallback<SellerInfoVO>(callback) {
                    @Override
                    protected SellerInfoVO convert2Obj(String s, String data) throws JSONException {
                        Log.i(TAG, "---fetchSellerInfoJob:" + data.toString());
                        SellerInfoVO vo = new SellerInfoVO();
                        Gson gson = new Gson();
                        vo = gson.fromJson(data.toString(), SellerInfoVO.class);
                        PrefsTools.setStringPrefs(context, SELLER_INFO, s);
                        return vo;
                    }
                });
            }
        };
    }





}
