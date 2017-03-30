package beer.happy_hour.drinking.async_http_client;

/**
 * Created by brcon on 16/03/2017.
 */

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

public class RestApiHttpClient {
    private static final String BASE_URL = "";

//    private static AsyncHttpClient client = new AsyncHttpClient();
    private static SyncHttpClient client = new SyncHttpClient();

//    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
//        client.get(getAbsoluteUrl(url), params, responseHandler);
//    }
//
//    //Versao que funciona quando client é AsyncTask
//    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
//        client.post(getAbsoluteUrl(url), params, responseHandler);
//    }

    public static void get(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    //Versao que funciona quando client é AsyncTask
    public static void post(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}