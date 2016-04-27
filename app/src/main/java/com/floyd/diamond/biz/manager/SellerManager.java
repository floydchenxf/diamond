package com.floyd.diamond.biz.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.aync.AsyncJob;
import com.floyd.diamond.aync.Func;
import com.floyd.diamond.aync.HttpJobFactory;
import com.floyd.diamond.bean.CorrectTime;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.bean.PingPP;
import com.floyd.diamond.bean.VipDays;
import com.floyd.diamond.bean.VipInfos;
import com.floyd.diamond.bean.VipMoney;
import com.floyd.diamond.bean.VipObject;
import com.floyd.diamond.biz.constants.APIConstants;
import com.floyd.diamond.biz.constants.APIError;
import com.floyd.diamond.biz.constants.SellerTaskDetailStatus;
import com.floyd.diamond.biz.constants.SellerTaskStatus;
import com.floyd.diamond.biz.func.StringFunc;
import com.floyd.diamond.biz.tools.PrefsTools;
import com.floyd.diamond.biz.vo.AdvVO;
import com.floyd.diamond.biz.vo.mote.MoteTaskPicVO;
import com.floyd.diamond.biz.vo.mote.TaskPicsVO;
import com.floyd.diamond.biz.vo.seller.SellerInfoUpdateParams;
import com.floyd.diamond.biz.vo.seller.SellerInfoVO;
import com.floyd.diamond.biz.vo.seller.SellerTaskDetailVO;
import com.floyd.diamond.biz.vo.seller.SellerTaskVO;
import com.floyd.diamond.biz.vo.seller.SellerWalletSummaryVO;
import com.floyd.diamond.biz.vo.seller.SellerWalletVO;
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
    public static final String SELLER_INFO = "seller_info";

    public static SellerInfoVO getSellerInfo(Context context) {
        String sellerInfo = PrefsTools.getStringPrefs(context, SELLER_INFO, "");
        if (TextUtils.isEmpty(sellerInfo)) {
            return null;
        }

        Gson gson = new Gson();
        SellerInfoVO vo = gson.fromJson(sellerInfo, SellerInfoVO.class);
        return vo;
    }

    public static void saveSellerInfo(Context context, SellerInfoVO sellerInfoVO) {
        Gson gson = new Gson();
        String data = gson.toJson(sellerInfoVO);
        PrefsTools.setStringPrefs(context, SELLER_INFO, data);
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
                            saveSellerInfo(context, vo);
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
     * @param status   0全部 1进行中 2待确定 3已结束
     * @param pageNo
     * @param pageSize
     * @param token
     * @return
     */
    public static AsyncJob<List<SellerTaskDetailVO>> getSellerTaskDetailList(long taskId, SellerTaskDetailStatus status, int pageNo, int pageSize, String token) {
        String url = APIConstants.HOST + APIConstants.API_SELLER_TASK_LIST_DETAIL;
        Map<String, String> params = new HashMap<String, String>();
        params.put("taskId", taskId + "");
        params.put("type", status.code + "");
        params.put("pageNo", pageNo + "");
        params.put("pageSize", pageSize + "");
        params.put("token", token);
        Type classType = new TypeToken<ArrayList<SellerTaskDetailVO>>() {
        }.getType();
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, classType);
    }

    /**
     * 获取卖家任务图片
     *
     * @param pageNo
     * @param pageSize
     * @param token
     * @return
     */
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

    /**
     * 确认并评价任务
     *
     * @param moteTaskId   　taskId
     * @param satisfaction 1满意　２不满意
     * @param token
     * @return
     */
    public static AsyncJob<Boolean> finishAndApproveMoteTask(long moteTaskId, int satisfaction, String token) {
        String url = APIConstants.HOST + APIConstants.API_SLLER_FINISH_AND_APPROVE_TASK;
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", moteTaskId + "");
        params.put("satisfaction", satisfaction + "");
        params.put("token", token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, Boolean.class);
    }

    /**
     * 更新卖家信息
     * @param updateParams
     * @return
     */
    public static AsyncJob<SellerInfoUpdateParams> updateSellerInfo(SellerInfoUpdateParams updateParams) {
        String url = APIConstants.HOST + APIConstants.API_UPDATE_SELLER_INFO;
        Map<String, String> params = updateParams.convert2Map();
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, SellerInfoUpdateParams.class);
    }

    public static AsyncJob<SellerWalletVO> getSellerWallet(String token) {
        String url = APIConstants.HOST + APIConstants.API_GET_SELLER_WALLET;
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, SellerWalletVO.class);
    }

    /**
     * 获取商家金额明细
     * @param type
     * @param pageNo
     * @param pageSize
     * @param token
     * @return
     */
    public static AsyncJob<SellerWalletSummaryVO> getSellerWalletRecord(int type, int pageNo, int pageSize, String token) {
        String url = APIConstants.HOST + APIConstants.API_GET_FEE_CHANGE_FLOW;
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", type+"");
        params.put("pageSize", pageSize+"");
        params.put("pageNo", pageNo+"");
        params.put("token", token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, SellerWalletSummaryVO.class);
    }

    /**
     * 获取商家会员开通的类别
     */
    public static AsyncJob<List<VipObject.DataEntity>>getVipInfo(String token){
        String url=APIConstants.HOST+APIConstants.API_VIPINFO;
        Map<String,String>params=new HashMap<>();
        params.put("token",token);
        return JsonHttpJobFactory.getJsonAsyncJob(url,params,HttpMethod.POST,new TypeToken<List<VipObject.DataEntity>>() {
        }.getType());
    }


    /**
     * 商家开通会员资费信息
     */
    public static AsyncJob<List<VipMoney.DataEntity>> getVipMoney(String token,String vipTypeIds){
        String url=APIConstants.HOST+APIConstants.API_VIP_TYPEMONEY;
        Map<String,String>params=new HashMap<>();
        params.put("token",token);
        params.put("vipTypeIds",vipTypeIds);
        return JsonHttpJobFactory.getJsonAsyncJob(url,params,HttpMethod.POST,new TypeToken<List<VipMoney.DataEntity>>() {
        }.getType());
    }


    /**
     * 商家开通会员剩下的天数
     */
    public static AsyncJob<List<VipDays.DataEntity>> getVipDays(String token,long userId){
        String url=APIConstants.HOST+APIConstants.API_VIP_DAYS;
        Map<String,String>params=new HashMap<>();
        params.put("token",token);
        params.put("userId",userId+"");

        if (GlobalParams.isDebug){
            Log.e("TAG",token+"userid:"+userId);
        }
        return JsonHttpJobFactory.getJsonAsyncJob(url,params,HttpMethod.POST,new TypeToken<List<VipDays.DataEntity>>() {
        }.getType());
    }

    /**
     * 倒计时任务最后几分钟矫正时间
     */
    public static AsyncJob<CorrectTime>correctTime(String token,String taskId){
        String url=APIConstants.HOST+APIConstants.API_CORRENT_TIME;
        Map<String,String>params=new HashMap<>();
        params.put("token",token);
        params.put("taskId",taskId+"");
        return JsonHttpJobFactory.getJsonAsyncJob(url,params,HttpMethod.POST,CorrectTime.class);
    }

    /**
     * 商家开通会员充值
     */

//    public static AsyncJob<PingPP.DataEntity> getVipType(String token,String channel,String money,String vipTypeIds,String cycle){
//        String url=APIConstants.HOST+APIConstants.API_PINGPP;
//        Map<String,String>params=new HashMap<>();
//        params.put("token",token);
//        params.put("channel",channel);
//        params.put("money",0.1+"");
//        params.put("vipTypeIds",vipTypeIds);
//        params.put("cycle",cycle);
//
//        Log.e("TAG_P",channel+money+vipTypeIds+cycle);
//
//        return JsonHttpJobFactory.getJsonAsyncJob(url,params,HttpMethod.POST, PingPP.DataEntity.class);
//
//    }



}
