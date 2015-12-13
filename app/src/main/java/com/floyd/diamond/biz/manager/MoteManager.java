package com.floyd.diamond.biz.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.aync.AsyncJob;
import com.floyd.diamond.aync.Func;
import com.floyd.diamond.aync.HttpJobFactory;
import com.floyd.diamond.biz.constants.APIConstants;
import com.floyd.diamond.biz.constants.MoteTaskStatus;
import com.floyd.diamond.biz.func.StringFunc;
import com.floyd.diamond.biz.parser.AbstractJsonParser;
import com.floyd.diamond.biz.tools.PrefsTools;
import com.floyd.diamond.biz.vo.MoteDetailInfoVO;
import com.floyd.diamond.biz.vo.MoteInfoVO;
import com.floyd.diamond.biz.vo.MoteTaskPicVO;
import com.floyd.diamond.biz.vo.MoteTaskVO;
import com.floyd.diamond.biz.vo.TaskPicsVO;
import com.floyd.diamond.channel.request.FileItem;
import com.floyd.diamond.channel.request.HttpMethod;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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

    /**
     * 关注模特儿
     *
     * @param moteId
     * @param token
     * @return
     */
    public static AsyncJob<Boolean> addFollow(long moteId, String token) {
        String url = APIConstants.HOST + APIConstants.API_ADD_FOLLOW;
        Map<String, String> params = new HashMap<String, String>();
        params.put("moteId", moteId + "");
        params.put("token", token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, Boolean.class);
    }

    /**
     * 取消关注模特儿
     *
     * @param moteId
     * @param token
     * @return
     */
    public static AsyncJob<Boolean> cancelFollow(long moteId, String token) {
        String url = APIConstants.HOST + APIConstants.API_ADD_FOLLOW;
        Map<String, String> params = new HashMap<String, String>();
        params.put("moteId", moteId + "");
        params.put("token", token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, Boolean.class);
    }

    /**
     * 获取模特的详细信息
     *
     * @param moteId
     * @param token
     * @return
     */
    public static AsyncJob<MoteDetailInfoVO> fetchMoteDetailInfo(long moteId, String token) {
        String url = APIConstants.HOST + APIConstants.API_MOTE_DETAIL_INFO;
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", moteId + "");
        params.put("token", token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, MoteDetailInfoVO.class);
    }

    /**
     * 获取模特的任务图片
     *
     * @param moteId
     * @param pageNo
     * @param pageSize
     * @param token
     * @return
     */
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

    /**
     * 按照类型查询模特任务
     *
     * @param moteType
     * @param pageNo
     * @param pageSize
     * @param token
     * @return
     */
    public static AsyncJob<MoteTaskVO> fetchTaskList(int moteType, int pageNo, int pageSize, String token) {
        String url = APIConstants.HOST + APIConstants.API_MOTE_TASK_SEARCH;
        Map<String, String> params = new HashMap<String, String>();
        params.put("moteType", moteType + "");
        params.put("pageNo", pageNo + "");
        params.put("pageSize", pageSize + "");
        params.put("token", token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, MoteTaskVO.class);
    }

    /**
     * 模特儿接单
     *
     * @param taskId
     * @param token
     * @return
     */
    public static AsyncJob<Boolean> fetchNewTask(long taskId, String token) {
        String url = APIConstants.HOST + APIConstants.API_NEW_MOTE_TASK;
        Map<String, String> params = new HashMap<String, String>();
        params.put("taskId", taskId + "");
        params.put("token", token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, Boolean.class);
    }

    /**
     * 获取模特我的任务
     *
     * @param type     　类型1 全部 2进行中 3待确认 4 已经结束
     * @param pageNo
     * @param pageSize
     * @param token
     * @return
     */
    public static AsyncJob<MoteTaskVO> fetchMyTasks(MoteTaskStatus type, int pageNo, int pageSize, String token) {
        String url = APIConstants.HOST + APIConstants.API_MOTE_MY_TASK;
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", type.code + "");
        params.put("pageNo", pageNo + "");
        params.put("pageSize", pageSize + "");
        params.put("token", token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, MoteTaskVO.class);
    }

    /**
     * 模特录入订单号
     *
     * @param taskId
     * @param orderNo
     * @param token
     * @return
     */
    public static AsyncJob<Boolean> addOrderNo(long taskId, String orderNo, String token) {
        String url = APIConstants.HOST + APIConstants.API_ADD_ORDER_NO;
        Map<String, String> params = new HashMap<String, String>();
        params.put("moteTaskId", taskId + "");
        params.put("orderNo", orderNo);
        params.put("token", token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, Boolean.class);
    }

    /**
     * 模特儿上传完成图片
     * @param taskId
     * @param token
     * @return
     */
    public static AsyncJob<Boolean> finishPicShow(long taskId, String token) {
        String url = APIConstants.HOST + APIConstants.API_FINISH_SHOW_PIC;
        Map<String, String> params = new HashMap<String, String>();
        params.put("moteTaskId", taskId + "");
        params.put("token", token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, Boolean.class);
    }

    /**
     * 模特儿退回商品
     * @param taskId 任务id
     * @param token 用户token
     * @param expressCompanyId　快递公司Id
     * @param expressNo　快递单号
     * @return
     */
    public static AsyncJob<Boolean> returnGoods(long taskId, String token, long expressCompanyId, String expressNo) {
        String url = APIConstants.HOST + APIConstants.API_RETURN_ITEM;
        Map<String, String> params = new HashMap<String, String>();
        params.put("moteTaskId", taskId + "");
        params.put("token", token);
        params.put("expressCompanyId", expressCompanyId+"");
        params.put("expressNo", expressNo);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, Boolean.class);
    }

    /**
     *模特儿自购商品
     * @param taskId
     * @param token
     * @return
     */
    public static AsyncJob<Boolean> selfBuy(long taskId, String token) {
        String url = APIConstants.HOST + APIConstants.API_SELF_BUY;
        Map<String, String> params = new HashMap<String, String>();
        params.put("moteTaskId", taskId + "");
        params.put("token", token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, Boolean.class);
    }

    /**
     * 模特儿上传图片
     * @param taskId
     * @param pics
     * @param token
     * @return
     */
    public static AsyncJob<Boolean> uploadPics(long taskId, List<File> pics, String token) {
        String url = APIConstants.HOST + APIConstants.API_UPLOAD_IMAGE;
        Map<String, String> params = new HashMap<String, String>();
        params.put("moteTaskId", taskId + "");
        params.put("token", token);
        Map<String, FileItem> files = new HashMap<String, FileItem>();
        if (pics != null && !pics.isEmpty()) {
            int idx = 1;
            for (File f : pics) {
                FileItem fileItem = new FileItem(f);
                files.put("image" + idx++, fileItem);
            }
        }

        return JsonHttpJobFactory.getJsonAsyncJob(url, params, files, HttpMethod.POST, Boolean.class);
    }

    /**
     * 删除模特图片
     * @param picIds
     * @param token
     * @return
     */
    public static final AsyncJob<Boolean> reomveImageUrl(List<Long> picIds, String token) {
        String url = APIConstants.HOST + APIConstants.API_REMOVE_IMAGE_URL;
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        StringBuilder picsString = new StringBuilder();
        if (picIds != null && !picIds.isEmpty()) {
            for (Long picId : picIds) {
                picsString.append(picId).append(",");
            }

            String picIdsParams = picIds.toString().substring(0, picIds.toString().length() - 1);
            params.put("taskPicIds", picIdsParams);
        }
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, Boolean.class);
    }

}
