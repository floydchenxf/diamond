package com.floyd.diamond.biz.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.aync.AsyncJob;
import com.floyd.diamond.aync.Func;
import com.floyd.diamond.aync.HttpJobFactory;
import com.floyd.diamond.biz.constants.APIConstants;
import com.floyd.diamond.biz.constants.APIError;
import com.floyd.diamond.biz.constants.SellerTaskStatus;
import com.floyd.diamond.biz.func.StringFunc;
import com.floyd.diamond.biz.tools.PrefsTools;
import com.floyd.diamond.biz.vo.mote.MoteTaskPicVO;
import com.floyd.diamond.biz.vo.mote.TaskPicsVO;
import com.floyd.diamond.biz.vo.seller.SellerInfoVO;
import com.floyd.diamond.biz.vo.seller.SellerTaskDetailVO;
import com.floyd.diamond.biz.vo.seller.SellerTaskVO;
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
        } catch (JSONException e) {
            Log.e(TAG, "parse json cause error:", e);
            return null;
        }
        return vo;
    }

    /**
     * 获取卖家信息
     *
     * @param context
     * @param accessToken
     * @return
     */
    public static AsyncJob<SellerInfoVO> fetchSellerInfoJob(final Context context, String accessToken) {
        String url = APIConstants.HOST + APIConstants.API_MY_SELLER_INFO;
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", accessToken);

        return HttpJobFactory.createHttpJob(url, params, HttpMethod.POST).map(new StringFunc()).flatMap(new Func<String, AsyncJob<SellerInfoVO>>() {
            @Override
            public AsyncJob<SellerInfoVO> call(final String s) {
                return new AsyncJob<SellerInfoVO>() {
                    @Override
                    public void start(ApiCallback<SellerInfoVO> callback) {
                        JSONObject json = null;
                        try {
                            json = new JSONObject(s);
                            String data = json.getString("data");
                            Gson gson = new Gson();
                            SellerInfoVO vo = gson.fromJson(data, SellerInfoVO.class);
                            PrefsTools.setStringPrefs(context, SELLER_INFO, s);
                            callback.onSuccess(vo);
                        } catch (JSONException e) {
                            callback.onError(APIError.API_JSON_PARSE_ERROR, e.getMessage());
                            return;
                        }
                    }
                };
            }
        });
    }

    /**
     * 获取商家任务
     *
     * @param status
     * @param pageNo
     * @param pageSize
     * @param token
     * @return
     */
    public static AsyncJob<SellerTaskVO> getSellerTaskList(SellerTaskStatus status, int pageNo, int pageSize, String token) {
        String url = APIConstants.HOST + APIConstants.API_SELLER_TASK_LIST;
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", status.code + "");
        params.put("pageNo", pageNo + "");
        params.put("pageSize", pageSize + "");
        params.put("token", token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, SellerTaskVO.class);
    }

    /**
     * 获取商家任务详情
     *
     * @param taskId
     * @param type 0全部 1进行中 2待确定 3已结束
     * @param pageNo
     * @param pageSize
     * @param token
     * @return
     */
    public static AsyncJob<SellerTaskDetailVO> getSellerTaskDetailList(long taskId, int type, int pageNo, int pageSize, String token) {
        String url = APIConstants.HOST + APIConstants.API_SELLER_TASK_LIST_DETAIL;
        Map<String, String> params = new HashMap<String, String>();
        params.put("taskId", taskId + "");
        params.put("type", type + "");
        params.put("pageNo", pageNo + "");
        params.put("pageSize", pageSize + "");
        params.put("token", token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, SellerTaskVO.class);
    }

    public static AsyncJob<List<TaskPicsVO>> getSellerTaskPics(int pageNo, int pageSize, String token) {
        String url = APIConstants.HOST + APIConstants.API_SELLER_TASK_PICS;
        Map<String, String> params = new HashMap<String, String>();
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


}
