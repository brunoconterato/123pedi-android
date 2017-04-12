package beer.happy_hour.drinking.async_http_client;

/**
 * Created by brcon on 11/04/2017.
 */

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

public class CartGetterAPISync extends AsyncTask<Void, Void, Void> {

    private int stock_item_id;
    private int quantity;
    private String latitude = "none";
    private String longitude = "none";

    public CartGetterAPISync(int stock_item_id, int quantity, String latitude, String longitude){
        this.stock_item_id = stock_item_id;
        this.quantity = quantity;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    protected Void doInBackground(Void... params) {

        RequestParams requestParams = new RequestParams();

        requestParams.put("stock_item_id", stock_item_id);
        requestParams.put("quantity", quantity);
        //TODO: Arrumar essa nhaca na API
        requestParams.put("latitude", latitude);
        requestParams.put("longitude", longitude);

        Log.d("Request Params", requestParams.toString());

        RestApiHttpClient.post(Constants.BASE_INFORMATION_CART_ITEM_URL, requestParams, new JsonHttpResponseHandler() {
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
