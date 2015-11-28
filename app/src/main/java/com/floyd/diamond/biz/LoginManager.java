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

    public static AsyncJob<ApiResult<LoginVO>> createLoginJob(String phoneNum, String password, int loginType) {
        String url = APIConstants.HOST + APIConstants.API_USER_LOGIN;
        final Map<String, String> params = new HashMap<String, String>();
        params.put("phoneNumber", phoneNum);
        params.put("password", password);
        params.put("loginType", loginType + "");
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

        AsyncJob<ApiResult<LoginVO>> targetJob = new AsyncJob<ApiResult<LoginVO>>() {
            @Override
            public void start(final ApiCallback<ApiResult<LoginVO>> callback) {

                httpJob.start(new ApiCallback<String>() {
                    @Override
                    public void onError(int code, String errorInfo) {
                        callback.onError(code, errorInfo);
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (TextUtils.isEmpty(s)) {
                            callback.onError(APIError.API_CONTENT_EMPTY, "content is empty!");
                            return;
                        }

                        ApiResult<LoginVO> result = new ApiResult<LoginVO>();
                        try {
                            LoginVO loginVO = new LoginVO();
                            JSONObject j = new JSONObject(s);
                            boolean success = j.getBoolean("success");
                            if (success) {
                                JSONObject data = j.getJSONObject("data");
                                loginVO.userName = j.getString("userName");
                                loginVO.accessToken = j.getString("accessToken");
                                loginVO.accountType = j.getInt("accountType");
                                result.code = 200;
                                result.result = loginVO;
                                callback.onSuccess(result);
                            } else {
                                String msg = j.getString("msg");
                                result.msg = msg;
                                callback.onSuccess(result);
                            }
                        } catch (JSONException e) {
                            callback.onError(APIError.API_JSON_PARSE_ERROR, e.getMessage());
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

    public static AsyncJob<Boolean> fetchVerifyCodeJob(String phoneNumber) {
        String url = APIConstants.HOST + APIConstants.API_VERIFY_CODE;
        final Map<String, String> params = new HashMap<String, String>();
        params.put("phoneNumber", phoneNumber);
        final AsyncJob<String> httpJob = HttpJobFactory.createHttpJob(url, params, HttpMethod.GET).map(new Func<byte[], String>() {
            @Override
            public String call(byte[] bytes) {
                return new String(bytes);
            }
        });

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
        final AsyncJob<String> httpJob = HttpJobFactory.createHttpJob(url, params, HttpMethod.GET).map(new Func<byte[], String>() {
            @Override
            public String call(byte[] bytes) {
                return new String(bytes);
            }
        });

        AsyncJob<ApiResult<Boolean>> resultJob = new AsyncJob<ApiResult<Boolean>>() {
            @Override
            public void start(final ApiCallback<ApiResult<Boolean>> callback) {
                httpJob.start(new ApiCallback<String>() {
                    @Override
                    public void onError(int code, String errorInfo) {
                        callback.onError(code, errorInfo);
                    }

                    @Override
                    public void onSuccess(String s) {
                        ApiResult<Boolean> result = new ApiResult<Boolean>();
                        JSONObject j = null;
                        try {
                            j = new JSONObject(s);
                            boolean success = j.getBoolean("success");
                            if (success) {
                                result.code = 200;
                                result.result = Boolean.TRUE;
                            } else {
                                String msg = j.getString("msg");
                                result.msg = msg;
                            }
                        } catch (JSONException e) {
                            callback.onError(APIError.API_JSON_PARSE_ERROR, e.getMessage());
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

        return resultJob;

    }

    public static AsyncJob<ApiResult<Boolean>> fetchResetPwdJob(String phoneNumber, String newPwd, String smsCode) {
        String url = APIConstants.HOST + APIConstants.API_CHANGE_PASSWORD_VERIFY_CODE;
        Map<String, String> params = new HashMap<String, String>();
        params.put("phoneNumber", phoneNumber);
        params.put("new_password", newPwd);
        params.put("smsCode", smsCode);
        final AsyncJob<String> httpJob = HttpJobFactory.createHttpJob(url, params, HttpMethod.GET).map(new Func<byte[], String>() {
            @Override
            public String call(byte[] bytes) {
                return new String(bytes);
            }
        });

        AsyncJob<ApiResult<Boolean>> resultJob = new AsyncJob<ApiResult<Boolean>>() {
            @Override
            public void start(final ApiCallback<ApiResult<Boolean>> callback) {
                httpJob.start(new ApiCallback<String>() {
                    @Override
                    public void onError(int code, String errorInfo) {
                        callback.onError(code, errorInfo);
                    }

                    @Override
                    public void onSuccess(String s) {
                        ApiResult<Boolean> result = new ApiResult<Boolean>();
                        try {
                            JSONObject j = new JSONObject(s);
                            boolean success = j.getBoolean("success");
                            if (success) {
                                result.code = 200;
                                result.result = Boolean.TRUE;
                            } else {
                                result.msg = j.getString("msg");
                            }

                            callback.onSuccess(result);
                        } catch (JSONException e) {
                            callback.onError(APIError.API_JSON_PARSE_ERROR, e.getMessage());
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

        return resultJob;
    }

}

