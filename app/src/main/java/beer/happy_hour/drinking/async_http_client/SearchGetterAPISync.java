package beer.happy_hour.drinking.async_http_client;

/**
 * Created by brcon on 11/04/2017.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import beer.happy_hour.drinking.Constants;
import cz.msebera.android.httpclient.Header;

/**
 * Created by brcon on 21/03/2017.
 */

public class SearchGetterAPISync extends AsyncTask<Void, Void, Void> {

    private String search_term = "";
    private String latitude = "none";
    private String longitude = "none";

    public SearchGetterAPISync(String search_term, String latitude, String longitude){
        this.search_term = search_term;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    protected Void doInBackground(Void... params) {

        RequestParams requestParams = new RequestParams();

        requestParams.put("search_term", search_term);
        //TODO: Arrumar essa nhaca na API
        requestParams.put("latutude", latitude);
        requestParams.put("longitude", longitude);

        Log.d("Request Params", requestParams.toString());

        RestApiHttpClient.post(Constants.BASE_INFORMATION_SEARCH_URL, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("SuccessAPI!!", "SuccessAPI");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("SuccessAPI!!", "SuccessAPI");

                JSONArray jsonArray = response;
                Log.d("JSONArray", String.valueOf(jsonArray));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String str, Throwable throwable){
                Log.d("Failure code:", Integer.toString(statusCode));
                Log.e("MyApp", "Caught error", throwable);
            }
        });

        return null;
    }
}
