package com.floyd.diamond.aync;

import com.floyd.diamond.IMChannel;
import com.floyd.diamond.biz.constants.APIError;
import com.floyd.diamond.channel.request.BaseRequest;
import com.floyd.diamond.channel.request.FileItem;
import com.floyd.diamond.channel.request.HttpMethod;
import com.floyd.diamond.channel.request.RequestCallback;
import com.floyd.diamond.utils.NetworkUtil;

import java.util.Map;

/**
 * Created by floyd on 15-11-22.
 */
public class HttpJobFactory {

    public static AsyncJob<byte[]> createHttpJob(final String url, final Map<String, String> params, final HttpMethod httpMethod) {
        return new AsyncJob<byte[]>() {
            @Override
            public void start(final ApiCallback<byte[]> callback) {

                boolean isNetworkAvailable = NetworkUtil.isNetworkAvailable(IMChannel.getApplication());
                if (!isNetworkAvailable) {
                    callback.onError(APIError.API_NETWORK_ERROR, "无网络，请检查网络设置．");
                    return;
                }

                new BaseRequest(url, params, httpMethod, new RequestCallback() {
                    @Override
                    public void onProgress(int progress) {
                        callback.onProgress(progress);
                    }

                    @Override
                    public <T> void onSuccess(T... result) {
                        if (result == null || result.length <= 0) {
                            callback.onError(APIError.API_CONTENT_EMPTY, "empty!");
                            return;
                        }

                        byte[] content = (byte[]) result[0];
                        callback.onSuccess(content);
                    }

                    @Override
                    public void onError(int code, String info) {
                        callback.onError(code, info);
                    }
                }).execute();

            }
        }.threadOn();
    }

    public static AsyncJob<byte[]> createFileJob(final String url, final Map<String, String> params, final Map<String, FileItem> files, final HttpMethod httpMethod) {
        return new AsyncJob<byte[]>() {
            @Override
            public void start(final ApiCallback<byte[]> callback) {
                new BaseRequest(url, params, files, httpMethod, new RequestCallback() {
                    @Override
                    public void onProgress(int progress) {
                        callback.onProgress(progress);
                    }

                    @Override
                    public <T> void onSuccess(T... result) {
                        if (result == null || result.length <= 0) {
                            callback.onError(APIError.API_CONTENT_EMPTY, "empty!");
                            return;
                        }

                        byte[] content = (byte[]) result[0];
                        callback.onSuccess(content);
                    }

                    @Override
                    public void onError(int code, String info) {
                        callback.onError(code, info);
                    }
                }).execute();

            }
        }.threadOn();
    }

}
