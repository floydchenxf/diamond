package com.floyd.diamond.biz.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.aync.AsyncJob;
import com.floyd.diamond.aync.Func;
import com.floyd.diamond.aync.HttpJobFactory;
import com.floyd.diamond.bean.MoteDetail1;
import com.floyd.diamond.biz.constants.APIConstants;
import com.floyd.diamond.biz.constants.MoteTaskStatus;
import com.floyd.diamond.biz.func.StringFunc;
import com.floyd.diamond.biz.parser.AbstractJsonParser;
import com.floyd.diamond.biz.tools.PrefsTools;
import com.floyd.diamond.biz.vo.AreaVO;
import com.floyd.diamond.biz.vo.mote.MoteInfoVO;
import com.floyd.diamond.biz.vo.mote.MoteTaskPicVO;
import com.floyd.diamond.biz.vo.mote.MoteTaskVO;
import com.floyd.diamond.biz.vo.mote.MoteWalletPageVO;
import com.floyd.diamond.biz.vo.mote.MoteWalletVO;
import com.floyd.diamond.biz.vo.mote.TaskItemVO;
import com.floyd.diamond.biz.vo.mote.TaskPicsVO;
import com.floyd.diamond.biz.vo.mote.UnReadMsgVO;
import com.floyd.diamond.biz.vo.mote.UserVO;
import com.floyd.diamond.biz.vo.mote.UserExtVO;
import com.floyd.diamond.biz.vo.process.TaskProcessVO;
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

    public static final String MOTE_INFO = "mote_info";

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

    public static AsyncJob<MoteInfoVO> fetchMoteInfoJob(String accessToken) {
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
    public static AsyncJob<Integer> addFollow(long moteId, String token) {
        String url = APIConstants.HOST + APIConstants.API_ADD_FOLLOW;
        Map<String, String> params = new HashMap<String, String>();
        params.put("moteId", moteId + "");
        params.put("token", token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, Integer.class);
    }

    /**
     * 取消关注模特儿
     *
     * @param moteIds
     * @param token
     * @return
     */
    public static AsyncJob<Boolean> cancelFollow(List<Long> moteIds, String token) {
        String url = APIConstants.HOST + APIConstants.API_CANCEL_FOLLOW;
        Map<String, String> params = new HashMap<String, String>();
        params.put("moteIds", moteIds.toString().substring(1, moteIds.toString().length() - 1));
        params.put("token", token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, Boolean.class);
    }

    /**
     * 获取模特的详细信息
     *
     * @param moteId
     * @param token 可以为空
     * @return
     */
    public static AsyncJob<MoteDetail1> fetchMoteDetailInfo(long moteId, String token) {
        String url = APIConstants.HOST + APIConstants.API_MOTE_DETAIL_INFO;
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", moteId + "");
        params.put("token", token);

        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, MoteDetail1.class);

    }

    /**
     * 获取模特的任务图片
     *
     * @param moteId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public static AsyncJob<List<TaskPicsVO>> fetchMoteTaskPics(long moteId, int pageNo, int pageSize) {
        String url = APIConstants.HOST + APIConstants.API_MOTE_TASK_PICS;
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", moteId + "");
        params.put("pageNo", pageNo + "");
        params.put("pageSize", pageSize + "");
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
        params.put("itemType", moteType + "");
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
     * @param moteTaskId
     * @param orderNo
     * @param token
     * @return
     */
    public static AsyncJob<Boolean> addOrderNo(long moteTaskId, String orderNo, String token) {
        String url = APIConstants.HOST + APIConstants.API_ADD_ORDER_NO;
        Map<String, String> params = new HashMap<String, String>();
        params.put("moteTaskId", moteTaskId + "");
        params.put("orderNo", orderNo);
        params.put("token", token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, Boolean.class);
    }

    /**
     * 模特儿上传完成图片
     *
     * @param moteTaskId
     * @param token
     * @return
     */
    public static AsyncJob<Boolean> finishPicShow(long moteTaskId, String token) {
        String url = APIConstants.HOST + APIConstants.API_FINISH_SHOW_PIC;
        Map<String, String> params = new HashMap<String, String>();
        params.put("moteTaskId", moteTaskId + "");
        params.put("token", token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, Boolean.class);
    }

    /**
     * 模特儿退回商品
     *
     * @param moteTaskId       任务id
     * @param token            用户token
     * @param expressCompanyId 　快递公司Id
     * @param expressNo        　快递单号
     * @return
     */
    public static AsyncJob<Boolean> returnGoods(long moteTaskId, String token, String expressCompanyId, String expressNo) {
        String url = APIConstants.HOST + APIConstants.API_RETURN_ITEM;
        Map<String, String> params = new HashMap<String, String>();
        params.put("moteTaskId", moteTaskId + "");
        params.put("token", token);
        params.put("expressCompanyId", expressCompanyId);
        params.put("expressNo", expressNo);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, Boolean.class);
    }

    /**
     * 模特儿自购商品
     *
     * @param moteTaskId
     * @param token
     * @return
     */
    public static AsyncJob<Boolean> selfBuy(long moteTaskId, String token) {
        String url = APIConstants.HOST + APIConstants.API_SELF_BUY;
        Map<String, String> params = new HashMap<String, String>();
        params.put("moteTaskId", moteTaskId + "");
        params.put("token", token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, Boolean.class);
    }

    /**
     * 模特儿上传图片
     *
     * @param moteTaskId
     * @param pics
     * @param token
     * @return
     */
    public static AsyncJob<List<MoteTaskPicVO>> uploadPics(long moteTaskId, List<File> pics, String token) {
        String url = APIConstants.HOST + APIConstants.API_UPLOAD_IMAGE;
        Map<String, String> params = new HashMap<String, String>();
        params.put("moteTaskId", moteTaskId + "");
        params.put("token", token);
        Map<String, FileItem> files = new HashMap<String, FileItem>();
        if (pics != null && !pics.isEmpty()) {
            int idx = 1;
            for (File f : pics) {
                FileItem fileItem = new FileItem(f);
                files.put("image" + idx++, fileItem);
            }
        }

        return JsonHttpJobFactory.getJsonAsyncJob(url, params, files, HttpMethod.POST, new TypeToken<ArrayList<MoteTaskPicVO>>(){}.getType());
    }

    /**
     * 删除模特图片
     *
     * @param picIds
     * @param token
     * @return
     */
    public static AsyncJob<Boolean> reomveImageUrl(List<Long> picIds, String token) {
        String url = APIConstants.HOST + APIConstants.API_REMOVE_IMAGE_URL;
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        StringBuilder picsString = new StringBuilder();
        if (picIds != null && !picIds.isEmpty()) {
            for (Long picId : picIds) {
                picsString.append(picId).append(",");
            }

            String picIdsParams = picIds.toString().substring(1, picIds.toString().length() - 1);
            params.put("taskPicIds", picIdsParams);
        }
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, Boolean.class);
    }

    public static AsyncJob<TaskProcessVO> fetchTaskProcess(long moteTaskId, String token) {
        String url = APIConstants.HOST + APIConstants.API_TASK_PROCESS;
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("moteTaskId", moteTaskId + "");
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, TaskProcessVO.class);
    }

    /**
     * 放弃已接单任务
     *
     * @param taskId
     * @param token
     * @return
     */
    public static AsyncJob<Boolean> giveupTask(long taskId, String token) {
        String url = APIConstants.HOST + APIConstants.API_GIVE_UP_TASK;
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("taskId", taskId + "");
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, Boolean.class);
    }

    /**
     * 放弃接收任务
     *
     * @param taskId
     * @param token
     * @return
     */
    public static AsyncJob<Boolean> giveupUnAcceptTask(long taskId, String token) {
        String url = APIConstants.HOST + APIConstants.API_GIVE_UP_UNACCEPT_TASK;
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("taskId", taskId + "");
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, Boolean.class);
    }

    /**
     * 获取地区信息
     *
     * @param pid
     * @return
     */
    public static AsyncJob<List<AreaVO>> getAreaListByPid(long pid) {
        String url = APIConstants.HOST + APIConstants.API_AREA_LEST_BY_PID;
        Map<String, String> params = new HashMap<String, String>();
        params.put("pid", pid + "");
        Type type = new TypeToken<ArrayList<AreaVO>>() {
        }.getType();
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, type);
    }

    /**
     * 更新模特信息
     *
     * @param userVO
     * @param token
     * @return
     */
    public static AsyncJob<UserVO> updateMoteInfo(UserVO userVO, String token) {
        String url = APIConstants.HOST + APIConstants.API_UPDATE_MOTE;
        Map<String, String> params = new HashMap<String, String>();
        params.put("avartUrl", userVO.avartUrl);
        params.put("nickname", userVO.nickname);
        params.put("gender", userVO.gender + "");
        params.put("birdthdayStr", userVO.birdthdayStr);
        params.put("shape", userVO.shape + "");
        params.put("height", userVO.height + "");
        params.put("provinceId", userVO.provinceId + "");
        params.put("cityId", userVO.cityId + "");
        params.put("districtId", userVO.districtId + "");
        params.put("address", userVO.address);
        params.put("weixin", userVO.weixin);
        params.put("alipayId", userVO.alipayId);
        params.put("msgSwitch", userVO.msgSwitch + "");
        params.put("token", token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, UserVO.class);
    }

    /**
     * 图片点赞
     *
     * @param picId
     * @param token
     * @return
     */
    public static AsyncJob<Boolean> picUpVote(long picId, String token) {
        String url = APIConstants.HOST + APIConstants.API_PIC_UPVOTE;
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", picId + "");
        params.put("token", token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, Boolean.class);
    }

    /**
     * 取消图片点赞
     *
     * @param picId
     * @param token
     * @return
     */
    public static AsyncJob<Boolean> cancelPicUpVote(long picId, String token) {
        String url = APIConstants.HOST + APIConstants.API_CANCEL_PIC_UPVOTE;
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", picId + "");
        params.put("token", token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, Boolean.class);
    }

    /**
     * 更新模特儿验证信息
     *
     * @param userVO
     * @param token
     * @return
     */
    public static AsyncJob<Boolean> updateMoteAuthenInfo(UserVO userVO, String token) {
        String url = APIConstants.HOST + APIConstants.API_UPDATE_MOTE_AUTHEN_INFO;
        Map<String, String> params = new HashMap<String, String>();
        params.put("authenPic1", userVO.authenPic1);
        params.put("authenPic2", userVO.authenPic2);
        params.put("authenPic3", userVO.authenPic3);
        params.put("idcardPic", userVO.idcardPic);
        params.put("realName", userVO.realName);
        params.put("idNumber", userVO.idNumber);
        params.put("token", token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, Boolean.class);
    }

    /**
     * 上传图片通用接口
     * @param file
     * @param token
     * @return
     */
    public static AsyncJob<String> uploadCommonFile(File file, String token) {
        String url = APIConstants.HOST + APIConstants.API_COMMON_UPLOAD;
        Map<String, FileItem> fileParams = new HashMap<String, FileItem>();
        fileParams.put("image", new FileItem(file));
        Map<String, String> params = new HashMap<String, String>();
        params.put("token",token);
        return HttpJobFactory.createFileJob(url, params, fileParams, HttpMethod.POST).map(new StringFunc()).flatMap(new Func<String, AsyncJob<String>>() {
            @Override
            public AsyncJob<String> call(final String s) {
                return new AsyncJob<String>() {
                    @Override
                    public void start(ApiCallback<String> callback) {
                        new AbstractJsonParser<String>() {
                            @Override
                            protected String convert2Obj(String data) {
                                return data;
                            }
                        }.doParse(callback, s);
                    }
                };
            }
        });
    }

    /**
     * 获取模特儿钱包
     * @param token
     * @return
     */
    public static AsyncJob<MoteWalletVO> getMoteWallet(String token) {
        String url = APIConstants.HOST + APIConstants.API_MOTE_WALLET;
        Map<String, String> params = new HashMap<String, String>();
        params.put("token",token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, MoteWalletVO.class);
    }

    /**
     * 申请提现
     * @param money
     * @param smsCode
     * @param password
     * @param token
     * @return
     */
    public static AsyncJob<Boolean> applyReduceCash(String money, String smsCode, String password,String token) {
        String url = APIConstants.HOST + APIConstants.API_REDUCE_CASH_APPLY;
        Map<String, String> params = new HashMap<String, String>();
        params.put("money", money);
        params.put("smsCode", smsCode);
        params.put("password", password);
        params.put("token",token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, Boolean.class);
    }

    /**
     * 获取模特提现记录
     * @param pageNo
     * @param pageSize
     * @param token
     * @return
     */
    public static AsyncJob<MoteWalletPageVO> fetchWalletRecords(int pageNo, int pageSize, String token) {
        String url = APIConstants.HOST + APIConstants.API_QUERY_APPLY_LIST;
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNo", pageNo+"");
        params.put("pageSize", pageSize+"");
        params.put("token",token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, MoteWalletPageVO.class);
    }

    public static AsyncJob<Boolean> addSuggestion(String content, String token) {
        String url = APIConstants.HOST + APIConstants.API_USER_SUGGESTION;
        Map<String, String> params = new HashMap<String, String>();
        params.put("content", content);
        params.put("token",token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, Boolean.class);
    }

    /**
     * 获取任务详情
     * @param taskId
     * @param token　可空，空为不可接单
     * @return
     */
    public static AsyncJob<TaskItemVO> getDetailByTaskId(long taskId, String token) {
        String url = APIConstants.HOST + APIConstants.API_GET_DETAIL_BY_TASKID;
        Map<String, String> params = new HashMap<String, String>();
        params.put("taskId", taskId + "");
        params.put("token",token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, TaskItemVO.class);
    }

    /**
     * 获取用户信息
     * @param token
     * @return
     */
    public static AsyncJob<UserVO> getUserInfo(String token) {
        String url = APIConstants.HOST +  APIConstants.API_GET_USER_INFO;
        Map<String, String> params = new HashMap<String, String>();
        params.put("token",token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, UserVO.class);
    }

    /**
     * 获取图片详情
     * @param id
     * @return
     */
    public static AsyncJob<MoteTaskPicVO> fetchMoteTaskPicDetail(long id) {
        String url = APIConstants.HOST +  APIConstants.API_TASK_PIC_DETAIL;
        Map<String, String> params = new HashMap<String, String>();
        params.put("id",id+"");
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, MoteTaskPicVO.class);
    }

    public static AsyncJob<UserExtVO> fetchUserExtInfo(final String token) {
        AsyncJob<UserVO> userJob = getUserInfo(token);
        AsyncJob<UserExtVO> aa = userJob.flatMap(new Func<UserVO, AsyncJob<UserExtVO>>() {
            @Override
            public AsyncJob<UserExtVO> call(final UserVO userVO) {
                AsyncJob<UserExtVO> bb = MoteManager.fetchMoteInfoJob(token).flatMap(new Func<MoteInfoVO, AsyncJob<UserExtVO>>() {
                    @Override
                    public AsyncJob<UserExtVO> call(final MoteInfoVO moteInfoVO) {
                        return new AsyncJob<UserExtVO>() {
                            @Override
                            public void start(ApiCallback<UserExtVO> callback) {
                                UserExtVO userExtVO = new UserExtVO();
                                userExtVO.fenNum = moteInfoVO.fenNum;
                                userExtVO.followNum = moteInfoVO.followNum;
                                userExtVO.nickname = moteInfoVO.nickname;
                                userExtVO.goodeEvalRate = moteInfoVO.goodeEvalRate;
                                userExtVO.orderNum = moteInfoVO.orderNum;
                                userExtVO.address = userVO.address;
                                userExtVO.alipayId = userVO.alipayId;
                                userExtVO.areaId = userVO.areaId;
                                userExtVO.authenPic1 = userVO.authenPic1;
                                userExtVO.authenPic2 = userVO.authenPic2;
                                userExtVO.authenPic3 = userVO.authenPic3;
                                userExtVO.authenStatus = userVO.authenStatus;
                                userExtVO.avartUrl = userVO.avartUrl;
                                userExtVO.birdthday = userVO.birdthday;
                                userExtVO.birdthdayStr = userVO.birdthdayStr;
                                userExtVO.idcardPic = userVO.idcardPic;
                                callback.onSuccess(userExtVO);
                            }
                        };
                    }
                });

                return bb;
            }
        });
        return aa;
    }


    /**
     * 获取未读消息数接口
     * @return
     */
    public static AsyncJob<UnReadMsgVO> fetchUnReadMsgs() {
        String url = APIConstants.HOST + APIConstants.API_UNREAD_MSG_NUM;
        return JsonHttpJobFactory.getJsonAsyncJob(url, null, HttpMethod.POST, UnReadMsgVO.class);
    }

    /**
     * 设置已读
     * @param token 用户信息
     * @param type　1女装2男装3童装4综合
     * @return
     */
    public static AsyncJob<Boolean> hasReadMsg(String token, int type) {
        String url = APIConstants.HOST + APIConstants.API_HAS_READ_MSG;
        Map<String, String> params = new HashMap<String, String>();
        params.put("msgType", type+"");
        params.put("token", token);
        return JsonHttpJobFactory.getJsonAsyncJob(url, params, HttpMethod.POST, Boolean.class);
    }
}
