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
import com.floyd.diamond.biz.vo.MoteDetailInfoVO;
import com.floyd.diamond.biz.vo.MoteInfoVO;
import com.floyd.diamond.biz.vo.MoteTaskPicVO;
import com.floyd.diamond.biz.vo.MoteTaskVO;
import com.floyd.diamond.biz.vo.TaskPicsVO;
import com.floyd.diamond.channel.request.HttpMethod;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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

    public static AsyncJob<Boolean> addFollow(long moteId, String token) {
        String url = APIConstants.HOST + APIConstants.API_ADD_FOLLOW;
        Map<String, String> params = new HashMap<String, String>();
        params.put("moteId", moteId + "");
        params.put("token", token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, Boolean.class);
    }

    public static AsyncJob<Boolean> cancelFollow(long moteId, String token) {
        String url = APIConstants.HOST + APIConstants.API_ADD_FOLLOW;
        Map<String, String> params = new HashMap<String, String>();
        params.put("moteId", moteId + "");
        params.put("token", token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, Boolean.class);
    }

    public static AsyncJob<MoteDetailInfoVO> fetchMoteDetailInfo(long moteId, String token) {
        String url = APIConstants.HOST + APIConstants.API_MOTE_DETAIL_INFO;
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", moteId + "");
        params.put("token", token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, MoteDetailInfoVO.class);
    }

    public static AsyncJob<List<TaskPicsVO>> fetchMoteTaskPics(long moteId, int pageNo, int pageSize, String token) {
        String url = APIConstants.HOST + APIConstants.API_MOTE_TASK_PICS;
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", moteId + "");
        params.put("pageNo", pageNo + "");
        params.put("pageSize", pageSize + "");
        params.put("token", token);
        Type type = new TypeToken<ArrayList<LinkedHashMap<String, ArrayList<MoteTaskPicVO>>>>() {
        }.getType();
        AsyncJob<List<Map<String, List<MoteTaskPicVO>>>> job = JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, type);
        return job.map(new Func<List<Map<String, List<MoteTaskPicVO>>>, List<TaskPicsVO>>() {
            @Override
            public List<TaskPicsVO> call(List<Map<String, List<MoteTaskPicVO>>> taskPicList) {
                if (taskPicList.isEmpty()) {
                    return Collections.EMPTY_LIST;
                }

                List<TaskPicsVO> pics = new ArrayList<TaskPicsVO>();
                for (Map<String, List<MoteTaskPicVO>> ele : taskPicList) {
                    TaskPicsVO taskPicsVO = new TaskPicsVO();
                    if (ele != null) {
                        for (Map.Entry<String, List<MoteTaskPicVO>> ent : ele.entrySet()) {
                            taskPicsVO.dateTime = ent.getKey();
                            taskPicsVO.taskPics = ent.getValue();
                            pics.add(taskPicsVO);
                        }
                    }
                }
                return pics;
            }
        });
    }

    public static AsyncJob<MoteTaskVO> fetchTaskList(int moteType, int pageNo, int pageSize, String token) {
        String url = APIConstants.HOST + APIConstants.API_MOTE_TASK_SEARCH;
        Map<String, String> params = new HashMap<String, String>();
        params.put("moteType", moteType + "");
        params.put("pageNo", pageNo + "");
        params.put("pageSize", pageSize + "");
        params.put("token", token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, MoteTaskVO.class);
    }
}
