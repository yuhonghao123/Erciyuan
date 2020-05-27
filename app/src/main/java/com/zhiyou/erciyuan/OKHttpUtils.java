package com.zhiyou.erciyuan;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OKHttpUtils {

    private static final String URL_PREFIX = "http://192.168.0.104:8080";

    final static OkHttpClient client = new OkHttpClient.Builder().build();

    public static <T> Result<T> get(String path, Map<String, Object> params, Class<T> clazz) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(URL_PREFIX + path)
                .newBuilder();
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (entry.getValue() != null) {
                    String value = entry.getValue().toString();
                    String encodedValue = URLEncoder.encode(value);
                    urlBuilder.addEncodedQueryParameter(entry.getKey(), encodedValue);
                }
            }
        }

        Request request = new Request.Builder().url(urlBuilder.build()).build();
        try {
            Response response = client.newCall(request).execute();
            String ret = response.body().string();

            Gson gson = new Gson();

            Map map = gson.fromJson(ret, Map.class);

            Result<T> result = new Result<T>();
            result.setSuccess((boolean) map.get("success"));
            result.setData(gson.fromJson(gson.toJson(map.get("data")), clazz));

            return result;

        } catch (IOException e) {
            return null;
        }
    }

    public static <T> Result<T> post(String path, Map<String, Object> params, Object data, Class<T> clazz) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(URL_PREFIX + path)
                .newBuilder();
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (entry.getValue() != null) {
                    String value = entry.getValue().toString();
                    String encodedValue = URLEncoder.encode(value);
                    urlBuilder.addEncodedQueryParameter(entry.getKey(), encodedValue);
                }
            }
        }

        Gson gson = new Gson();


        RequestBody postData = RequestBody.create(MediaType.parse("application/json"), gson.toJson(data));
        Request request = new Request.Builder().url(urlBuilder.build())
                .post(postData)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String ret = response.body().string();

            Map map = gson.fromJson(ret, Map.class);

            Result<T> result = new Result<T>();
            result.setSuccess((boolean) map.get("success"));
            result.setData(gson.fromJson(gson.toJson(map.get("data")), clazz));

            return result;

        } catch (IOException  e) {
            Log.d("POST", urlBuilder.toString(), e);
            return null;
        }
    }

}
