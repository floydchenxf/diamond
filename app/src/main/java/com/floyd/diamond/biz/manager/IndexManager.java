package com.floyd.diamond.biz.manager;

import android.text.TextUtils;

import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.aync.AsyncJob;
import com.floyd.diamond.aync.HttpJobFactory;
import com.floyd.diamond.biz.constants.APIConstants;
import com.floyd.diamond.biz.func.AbstractJsonApiCallback;
import com.floyd.diamond.biz.func.StringFunc;
import com.floyd.diamond.biz.vo.AdvVO;
import com.floyd.diamond.biz.vo.MoteInfoVO;
import com.floyd.diamond.channel.request.HttpMethod;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by floyd on 15-12-3.
 */
public class IndexManager {

    public static AsyncJob<List<AdvVO>> fetchAdvLists(int count) {
        String url = APIConstants.HOST + APIConstants.API_GET_ADVERT_LIST;
        final Map<String, String> params = new HashMap<String, String>();
        params.put("count", count + "");
        final AsyncJob<String> httpJob = HttpJobFactory.createHttpJob(url, params, HttpMethod.POST).map(new StringFunc());

        AsyncJob<List<AdvVO>> targetJob = new AsyncJob<List<AdvVO>>() {
            @Override
            public void start(final ApiCallback<List<AdvVO>> callback) {

                httpJob.start(new AbstractJsonApiCallback<List<AdvVO>>(callback) {
                    @Override
                    protected List<AdvVO> convert2Obj(String s, String data) throws JSONException {
                        if (TextUtils.isEmpty(data)) {
                            return null;
                        }
                        Gson gson = new Gson();
                        List<AdvVO> rs = new ArrayList<AdvVO>();
                        Type type = new TypeToken<ArrayList<AdvVO>>() {
                        }.getType();

                        rs = gson.fromJson(data, type);
                        return rs;
                    }
                });

            }
        };

        return targetJob;
    }

    /**
     * 获取首页模特信息
     *
     * @param moteType     1女模 2男模 3童摸
     * @param pageNo
     * @param pageSize
     * @return
     */
    public static AsyncJob<List<MoteInfoVO>> fetchMoteList(int moteType, int pageNo, int pageSize) {
        String url = APIConstants.HOST + APIConstants.API_GET_MOTE_LIST;
        final Map<String, String> params = new HashMap<String, String>();
        params.put("moteType", moteType + "");
        params.put("pageNo", pageNo+"");
        params.put("pageSize", pageSize+"");

        final AsyncJob<String> httpJob = HttpJobFactory.createHttpJob(url, params, HttpMethod.POST).map(new StringFunc());

        AsyncJob<List<MoteInfoVO>> targetJob = new AsyncJob<List<MoteInfoVO>>() {
            @Override
            public void start(final ApiCallback<List<MoteInfoVO>> callback) {

                httpJob.start(new AbstractJsonApiCallback<List<MoteInfoVO>>(callback) {
                    @Override
                    protected List<MoteInfoVO> convert2Obj(String s, String data) throws JSONException {
                        if (TextUtils.isEmpty(data)) {
                            return null;
                        }
                        Gson gson = new Gson();
                        Motes rs = gson.fromJson(data, Motes.class);
                        return rs.moteVOs;
                    }
                });

            }
        };

        return targetJob;
    }

    class Motes {
        public List<MoteInfoVO> moteVOs;
    }
}

