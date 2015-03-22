package com.jalen.jo.http;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by jh on 2015/3/18.
 * <br/>
 *
 * 静态的httpclient
 *
 */
public class JoRestClient {
//    private static final String BASE_URL = "http://api.twitter.com/1/";
    private static final String BASE_URL = "https://api.douban.com/v2/book/isbn/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    /**
     * GET方式请求
     * @param url   相对路径url
     * @param params    请求参数
     * @param responseHandler   网络请求响应处理类
     */
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    /**
     * POST方式请求
     * @param url   相对url
     * @param params    请求参数
     * @param responseHandler   网络请求处理类
     */
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    /**
     * 获取绝对路径（BASE_URL + relative_url）
     * @param relativeUrl
     * @return
     */
    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
