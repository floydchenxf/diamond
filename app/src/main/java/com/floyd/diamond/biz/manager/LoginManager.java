package com.floyd.diamond.biz.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.aync.AsyncJob;
import com.floyd.diamond.aync.HttpJobFactory;
import com.floyd.diamond.biz.ApiResult;
import com.floyd.diamond.biz.constants.APIConstants;
import com.floyd.diamond.biz.constants.APIError;
import com.floyd.diamond.biz.constants.AccountType;
import com.floyd.diamond.biz.func.AbstractJsonApiCallback;
import com.floyd.diamond.biz.func.StringFunc;
import com.floyd.diamond.biz.tools.PrefsTools;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.channel.request.HttpMethod;

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

    public static LoginVO getLoginInfo(Context context) {
        String loginInfo = PrefsTools.getStringPrefs(context, LOGIN_INFO, "");
        if (TextUtils.isEmpty(loginInfo)) {
            return null;
        }

        LoginVO loginVO = new LoginVO();
        try {
            JSONObject json = new JSONObject(loginInfo);
            JSONObject data = json.getJSONObject("data");
            loginVO.accessToken = data.getString("token");
            JSONObject userJson = data.getJSONObject("user");
            loginVO.accountType = userJson.getInt("type");
            loginVO.avartUrl = userJson.getString("avartUrl");
            loginVO.phoneNumber = userJson.getString("phoneNumber");
            loginVO.id = userJson.getLong("id");
        } catch (JSONException e) {
            Log.e(TAG, "parse json cause error:", e);
            return null;
        }
        return loginVO;
    }

    public static void saveLoginInfo(Context context, String loginInfoJson) {
        PrefsTools.setStringPrefs(context, LOGIN_INFO, loginInfoJson);
    }

    public static AsyncJob<ApiResult<LoginVO>> createLoginJob(final Context context, String phoneNum, String password, int loginType) {
        String url = APIConstants.HOST + APIConstants.API_USER_LOGIN;
        final Map<String, String> params = new HashMap<String, String>();
        params.put("phoneNumber", phoneNum);
        params.put("password", password);
        params.put("loginType", loginType + "");
        final AsyncJob<String> httpJob = HttpJobFactory.createHttpJob(url, params, HttpMethod.POST).map(new StringFunc());

        AsyncJob<ApiResult<LoginVO>> targetJob = new AsyncJob<ApiResult<LoginVO>>() {
            @Override
            public void start(final ApiCallback<ApiResult<LoginVO>> callback) {

                httpJob.start(new AbstractJsonApiCallback<LoginVO>(callback) {
                    @Override
                    protected LoginVO convert2Obj(String s, JSONObject data) throws JSONException {
                        LoginVO loginVO = new LoginVO();
                        JSONObject userJson = data.getJSONObject("user");
                        loginVO.accessToken = data.getString("token");
                        loginVO.accountType = userJson.getInt("type");
                        loginVO.avartUrl = userJson.getString("avartUrl");
                        loginVO.phoneNumber = userJson.getString("phoneNumber");
                        loginVO.id = userJson.getLong("id");
                        PrefsTools.setStringPrefs(context, LOGIN_INFO, s);
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

    public static AsyncJob<ApiResult<Boolean>> regUserJob(String phoneNum, String username, String password, AccountType accountType, String smsCode) {
        String url = APIConstants.HOST + APIConstants.API_REG_USER;
        Map<String, String> params = new HashMap<String, String>();
        params.put("phoneNumber", phoneNum);
        params.put("nickname", username);
        params.put("password", password);
        params.put("type", accountType.code + "");
        params.put("smsCode", smsCode);
        final AsyncJob<String> httpJob = HttpJobFactory.createHttpJob(url, params, HttpMethod.POST).map(new StringFunc());

        AsyncJob<ApiResult<Boolean>> resultJob = new AsyncJob<ApiResult<Boolean>>() {
            @Override
            public void start(final ApiCallback<ApiResult<Boolean>> callback) {
                httpJob.start(new AbstractJsonApiCallback<Boolean>(callback) {
                    @Override
                    protected Boolean convert2Obj(String s, JSONObject data) throws JSONException {
                        return Boolean.TRUE;
                    }
                });

            }
        };

        return resultJob;

    }

    public static AsyncJob<ApiResult<Boolean>> fetchResetPwdJob(String phoneNumber, String newPwd, String smsCode) {
        String url = APIConstants.HOST + APIConstants.API_CHANGE_PASSWORD_VERIFY_CODE;
        Map<String, String> params = new HashMap<String, String>();
        params.put("phoneNumber", phoneNumber);
        params.put("password", newPwd);
        params.put("smsCode", smsCode);
        final AsyncJob<String> httpJob = HttpJobFactory.createHttpJob(url, params, HttpMethod.POST).map(new StringFunc());

        AsyncJob<ApiResult<Boolean>> resultJob = new AsyncJob<ApiResult<Boolean>>() {
            @Override
            public void start(final ApiCallback<ApiResult<Boolean>> callback) {
                httpJob.start(new AbstractJsonApiCallback<Boolean>(callback) {
                    @Override
                    protected Boolean convert2Obj(String s, JSONObject data) throws JSONException {
                        return Boolean.TRUE;
                    }
                });
            }
        };

        return resultJob;
    }

}
