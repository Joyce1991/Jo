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
    private static final String DOUBAN_BASE_URL = "https://api.douban.com/v2/book/isbn/";
    private static final String BASE_URL = "https://leancloud.cn";
    public static final String FILE_URL = "/1.1/files/";

    private static final String HEADER_APPLICATION_ID = "X-AVOSCloud-Application-Id";
    private static final String HEADER_APPLICATION_KEY = "X-AVOSCloud-Application-Key";

    private static AsyncHttpClient client = new AsyncHttpClient();

    // 添加application id和key到请求头
    static {
        client.addHeader(HEADER_APPLICATION_ID, "lgt86x4nela39ip0w9sual23hwubpgp1d5qhcl7k3jbkl9hv");
        client.addHeader(HEADER_APPLICATION_KEY, "ujnmqih2olni35gb7774ocd4jdosvbd0s725gdzjhoaqpx4z");
    }


    /**
     * GET方式请求
     * @param url   相对路径url
     * @param params    请求参数
     * @param responseHandler   网络请求响应处理类
     */
    public static void getFromDouban(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(DOUBAN_BASE_URL+url, params, responseHandler);
    }
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

    public static AsyncHttpClient getClient(){
        return client;
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
