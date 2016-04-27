package com.floyd.diamond.biz.manager;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.aync.AsyncJob;
import com.floyd.diamond.aync.HttpJobFactory;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.bean.ModifyBean;
import com.floyd.diamond.biz.constants.APIConstants;
import com.floyd.diamond.biz.constants.APIError;
import com.floyd.diamond.biz.constants.AccountType;
import com.floyd.diamond.biz.func.AbstractJsonApiCallback;
import com.floyd.diamond.biz.func.StringFunc;
import com.floyd.diamond.biz.tools.PrefsTools;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.channel.request.HttpMethod;
import com.floyd.diamond.ui.activity.LoginActivity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by floyd on 15-11-22.
 */
public class LoginManager {

    private static final String TAG = "LoginManager";

    public static final String LOGIN_INFO = "LOGIN_INFO";

    public static final String MSG_READ_TIMES = "MSG_READ_TIMES";

    public static final String DEVICE_ID_KEY = "__device_id_key__";

    public static final String HAS_GUIDED = "__has_guide__";

    public static LoginVO getLoginInfo(Context context) {
        String data = PrefsTools.getStringPrefs(context, LOGIN_INFO, "");
        if (TextUtils.isEmpty(data)) {
            return null;
        }

        LoginVO loginVO = new LoginVO();
        Gson gson = new Gson();
        loginVO = gson.fromJson(data, LoginVO.class);
        return loginVO;
    }

    public static void saveLoginInfo(Context context, LoginVO vo) {
        Gson gson = new Gson();
        String data = gson.toJson(vo);
        PrefsTools.setStringPrefs(context, LOGIN_INFO, data);
    }

    public static AsyncJob<LoginVO> createLoginJob(final Context context, String phoneNum, String password, int loginType, String deviceId) {
        String url = APIConstants.HOST + APIConstants.API_USER_LOGIN;
        final Map<String, String> params = new HashMap<String, String>();
        params.put("phoneNumber", phoneNum);
        params.put("password", password);
        params.put("lastDeviceType", loginType + "");
        if (GlobalParams.isDebug){
            Log.e("TAG_deviceid",deviceId);
        }
        params.put("lastDeviceId", deviceId);
        final AsyncJob<String> httpJob = HttpJobFactory.createHttpJob(url, params, HttpMethod.POST).map(new StringFunc());

        AsyncJob<LoginVO> targetJob = new AsyncJob<LoginVO>() {
            @Override
            public void start(final ApiCallback<LoginVO> callback) {

                httpJob.start(new AbstractJsonApiCallback<LoginVO>(callback) {
                    @Override
                    protected LoginVO convert2Obj(String s, String data) throws JSONException {
                        Gson gson = new Gson();
                        LoginVO loginVO = gson.fromJson(data, LoginVO.class);
                        saveLoginInfo(context, loginVO);
                        return loginVO;
                    }
                });

            }
        };

        return targetJob;

    }

    public static AsyncJob<Boolean> fetchVerifyCodeJob(String phoneNumber) {
        String url = APIConstants.HOST + APIConstants.API_VERIFY_CODE;
        final Map<String, String> params = new HashMap<String, String>();
        params.put("phoneNumber", phoneNumber);
        final AsyncJob<String> httpJob = HttpJobFactory.createHttpJob(url, params, HttpMethod.POST).map(new StringFunc());

        AsyncJob<Boolean> result = new AsyncJob<Boolean>() {
            @Override
            public void start(final ApiCallback<Boolean> callback) {
                httpJob.start(new ApiCallback<String>() {
                    @Override
                    public void onError(int code, String errorInfo) {
                        callback.onError(code, errorInfo);
                    }

                    @Override
                    public void onSuccess(String s) {
                        boolean result = false;
                        JSONObject j = null;
                        try {
                            j = new JSONObject(s);
                            result = j.getBoolean("success");
                        } catch (JSONException e) {
                            callback.onError(APIError.API_JSON_PARSE_ERROR, "json解析出错");
                            return;
                        }

                        callback.onSuccess(result);
                    }

                    @Override
                    public void onProgress(int progress) {
                        callback.onProgress(progress);
                    }
                });

            }
        };
        return result;
    }

    public static AsyncJob<Boolean> regUserJob(String phoneNum, String username, String password, AccountType accountType, String smsCode) {
        String url = APIConstants.HOST + APIConstants.API_REG_USER;
        Map<String, String> params = new HashMap<String, String>();
        params.put("phoneNumber", phoneNum);
        params.put("nickname", username);
        params.put("password", password);
        params.put("type", accountType.code + "");
        params.put("smsCode", smsCode);
        final AsyncJob<String> httpJob = HttpJobFactory.createHttpJob(url, params, HttpMethod.POST).map(new StringFunc());

        AsyncJob<Boolean> resultJob = new AsyncJob<Boolean>() {
            @Override
            public void start(final ApiCallback<Boolean> callback) {
                httpJob.start(new AbstractJsonApiCallback<Boolean>(callback) {
                    @Override
                    protected Boolean convert2Obj(String s, String data) throws JSONException {
                        return Boolean.TRUE;
                    }
                });

            }
        };

        return resultJob;

    }

    /**
     * 重置密码
     * @param phoneNumber
     * @param newPwd
     * @param smsCode
     * @return
     */
    public static AsyncJob<Boolean> fetchResetPwdJob(String phoneNumber, String newPwd, String smsCode) {
        String url = APIConstants.HOST + APIConstants.API_CHANGE_PASSWORD_VERIFY_CODE;
        Map<String, String> params = new HashMap<String, String>();
        params.put("phoneNumber", phoneNumber);
        params.put("password", newPwd);
        params.put("smsCode", smsCode);
        final AsyncJob<String> httpJob = HttpJobFactory.createHttpJob(url, params, HttpMethod.POST).map(new StringFunc());

        AsyncJob<Boolean> resultJob = new AsyncJob<Boolean>() {
            @Override
            public void start(final ApiCallback<Boolean> callback) {
                httpJob.start(new AbstractJsonApiCallback<Boolean>(callback) {
                    @Override
                    protected Boolean convert2Obj(String s, String data) throws JSONException {

                        return Boolean.TRUE;
                    }
                });
            }
        };

        return resultJob;
    }

    /**
     * 修改账号和密码
     */
    public static AsyncJob<Boolean> modifyNum(String oldPhone,String oldPass,String oldCode,String newPhone,String newPass,String newCode,String token){
        String url=APIConstants.HOST+APIConstants.API_MODIFY_PASS;
        Map<String, String> params = new HashMap<String, String>();
        params.put("oldPhoneNumber",oldPhone);
        params.put("oldPassword",oldPass);
        params.put("oldSmsCode",oldCode);
        params.put("newPhoneNumber",newPhone);
        params.put("newPassword",newPass);
        params.put("newSmsCode",newCode);
        params.put("token",token);
        final AsyncJob<String> httpJob = HttpJobFactory.createHttpJob(url, params, HttpMethod.POST).map(new StringFunc());

        AsyncJob<Boolean> resultJob = new AsyncJob<Boolean>() {
            @Override
            public void start(final ApiCallback<Boolean> callback) {
                httpJob.start(new AbstractJsonApiCallback<Boolean>(callback) {
                    @Override
                    protected Boolean convert2Obj(String s, String data) throws JSONException {

                       //data为新的token值

                        return Boolean.TRUE;
                    }
                });
            }
        };

        return resultJob;
    }

    public static boolean isLogin(Context context) {
        LoginVO loginVO = LoginManager.getLoginInfo(context);
        if (loginVO == null) {
            Intent it = new Intent(context, LoginActivity.class);
            context.startActivity(it);
            return false;
        }

        return true;
    }

    public static long getMsgReadTimes(Context context) {
        long time = PrefsTools.getLongPrefs(context, MSG_READ_TIMES, 0l);
        return time;
    }

    public static void setMsgReadTimes(Context context, long time) {
        PrefsTools.setLongPrefs(context, MSG_READ_TIMES, time);
    }

    public static void saveDeviceId(Context context, String deviceId) {
        PrefsTools.setStringPrefs(context, DEVICE_ID_KEY, deviceId);
    }

    public static String getDeviceId(Context context) {
        String deviceId = PrefsTools.getStringPrefs(context, DEVICE_ID_KEY);
        return deviceId == null ? "" : deviceId;
    }

    public static boolean hasGuided(Context context) {
        boolean hasGuided = PrefsTools.getBooleanPrefs(context, HAS_GUIDED);
        return hasGuided;
    }

    public static void saveGuided(Context context, boolean guided) {
        PrefsTools.setBooleanPrefs(context, HAS_GUIDED, guided);
    }
}

