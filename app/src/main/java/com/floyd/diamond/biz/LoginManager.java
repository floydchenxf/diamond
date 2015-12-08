package com.floyd.diamond.biz;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.aync.AsyncJob;
import com.floyd.diamond.aync.Func;
import com.floyd.diamond.aync.HttpJobFactory;
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

        LoginVO loginVo = new LoginVO();
        try {
            JSONObject json = new JSONObject(loginInfo);
            loginVo.userName = json.getString("userName");
            loginVo.accountType = json.getInt("accountType");
            loginVo.accessToken = json.getString("accessToken");
        } catch (JSONException e) {
            Log.e(TAG, "parse json cause error:", e);
            return null;
        }
        return loginVo;
    }

    public static void saveLoginInfo(Context context, String loginInfoJson) {
        PrefsTools.setStringPrefs(context, LOGIN_INFO, loginInfoJson);
    }

    public static AsyncJob<LoginVO> createLoginJob(String phoneNum, String password, AccountType accountType, int loginType) {
        String urlPrefix = "";
        String urlPath = "";
        if (accountType == AccountType.COMMON) {
            urlPath = "api/user/login";
        } else {
            urlPath = "api/user/login4Seller";
        }
        final String url = urlPrefix + urlPath;
        final Map<String, String> params = new HashMap<String, String>();
        params.put("phoneNumber", phoneNum);
        params.put("password", password);
        final AsyncJob<String> httpJob = HttpJobFactory.createHttpJob(url, params, HttpMethod.GET).map(new Func<byte[], String>() {
            @Override
            public String call(byte[] bytes) {
                return new String(bytes);
            }
        }).map(new Func<String, String>() {
            @Override
            public String call(String s) {
                return s;
            }
        });


        AsyncJob<LoginVO> targetJob = new AsyncJob<LoginVO>() {
            @Override
            public void start(final ApiCallback<LoginVO> callback) {

                httpJob.start(new ApiCallback<String>() {
                    @Override
                    public void onError(int code, String errorInfo) {
                        callback.onError(code, errorInfo);
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (TextUtils.isEmpty(s)) {
                            callback.onError(400, "content is empty!");
                            return;
                        }

                        try {
                            LoginVO loginVO = new LoginVO();
                            JSONObject j = new JSONObject(s);
                            loginVO.userName = j.getString("userName");
                            loginVO.accessToken = j.getString("accessToken");
                            loginVO.accountType = j.getInt("accountType");
                            callback.onSuccess(loginVO);
                        } catch (JSONException e) {
                            callback.onError(800, e.getMessage());
                            return;
                        }

                    }


                    @Override
                    public void onProgress(int progress) {
                        callback.onProgress(progress);
                    }
                });

            }
        };

        return targetJob;

    }

    public static AsyncJob<String> fetchVerifyCodeJob(String phoneNumber) {
        String url = "api/user/sendVerifyCode";
        final Map<String, String> params = new HashMap<String, String>();
        params.put("phoneNumber", phoneNumber);
        AsyncJob<String> httpJob = HttpJobFactory.createHttpJob(url, params, HttpMethod.GET).map(new Func<byte[], String>() {
            @Override
            public String call(byte[] bytes) {
                return new String(bytes);
            }
        }).map(new Func<String, String>() {
            @Override
            public String call(String s) {
                return s;
            }
        });
        return httpJob;
    }

    public static AsyncJob<String> fetchResetPwdJob(String phoneNumber, String newPwd, String smsCode) {
        String url = "api/user/changePasswordByVerifyCode";
        Map<String, String> params = new HashMap<String, String>();
        params.put("phoneNumber", phoneNumber);
        params.put("new_password", newPwd);
        params.put("smsCode", smsCode);
        return HttpJobFactory.createHttpJob(url, params, HttpMethod.GET).map(new Func<byte[], String>() {
            @Override
            public String call(byte[] bytes) {
                return new String(bytes);
            }
        });
    }

}

