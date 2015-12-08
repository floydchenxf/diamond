package com.floyd.diamond.biz.manager;

import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.aync.AsyncJob;
import com.floyd.diamond.aync.Func;
import com.floyd.diamond.aync.HttpJobFactory;
import com.floyd.diamond.biz.func.StringFunc;
import com.floyd.diamond.biz.parser.AbstractJsonParser;
import com.floyd.diamond.channel.request.HttpMethod;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by floyd on 15-12-5.
 */
public class JsonHttpJobFactory {

    public static <T> AsyncJob<T> getJsonAsyncJob(String url, Map<String, String> params, HttpMethod httpMethod, final Type type) {
        return HttpJobFactory.createHttpJob(url, params, HttpMethod.POST).map(new StringFunc()).flatMap(new Func<String, AsyncJob<T>>() {
            @Override
            public AsyncJob<T> call(final String s) {
                return new AsyncJob<T>() {
                    @Override
                    public void start(ApiCallback<T> callback) {
                        new AbstractJsonParser<T>() {
                            @Override
                            protected T convert2Obj(String data) {
                                Gson gson = new Gson();
                                return gson.fromJson(data, type);
                            }
                        }.doParse(callback, s);
                    }
                };
            }
        });
    }
}
