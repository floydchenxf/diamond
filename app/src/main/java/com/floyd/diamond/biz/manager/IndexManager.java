package com.floyd.diamond.biz.manager;

import com.floyd.diamond.aync.AsyncJob;
import com.floyd.diamond.aync.Func;
import com.floyd.diamond.biz.constants.APIConstants;
import com.floyd.diamond.biz.vo.AdvDetailVO;
import com.floyd.diamond.biz.vo.AdvVO;
import com.floyd.diamond.biz.vo.IndexVO;
import com.floyd.diamond.biz.vo.MoteInfoVO;
import com.floyd.diamond.channel.request.HttpMethod;
import com.google.gson.reflect.TypeToken;

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
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, new TypeToken<ArrayList<AdvVO>>() {
        }.getType());
    }

    /**
     * 获取首页模特信息
     *
     * @param moteType 1女模 2男模 3童摸
     * @param pageNo
     * @param pageSize
     * @return
     */
    public static AsyncJob<List<MoteInfoVO>> fetchMoteList(int moteType, int pageNo, int pageSize) {
        String url = APIConstants.HOST + APIConstants.API_GET_MOTE_LIST;
        final Map<String, String> params = new HashMap<String, String>();
        params.put("moteType", moteType + "");
        params.put("pageNo", pageNo + "");
        params.put("pageSize", pageSize + "");

        AsyncJob<Motes> jj = JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, Motes.class);
        return jj.map(new Func<Motes, List<MoteInfoVO>>() {
            @Override
            public List<MoteInfoVO> call(Motes motes) {
                return motes.moteVOs;
            }
        });
    }

    class Motes {
        public List<MoteInfoVO> moteVOs;
    }

    public static AsyncJob<AdvDetailVO> fetchAdvDetail(long advId) {
        String url = APIConstants.HOST + APIConstants.API_ADV_DETAIL_INFO;
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", advId+"");
        return JsonHttpJobFactory.getJsonAsyncJob(url,params, HttpMethod.POST,AdvDetailVO.class);
    }

    public static AsyncJob<IndexVO> getIndexInfoJob() {
        String url = APIConstants.HOST + APIConstants.API_INDEX_INFO;
        return JsonHttpJobFactory.getJsonAsyncJob(url, null, HttpMethod.POST, IndexVO.class);

    }

}

