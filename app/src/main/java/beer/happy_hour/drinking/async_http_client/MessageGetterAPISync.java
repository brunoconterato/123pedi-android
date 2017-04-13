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
import beer.happy_hour.drinking.model.DeliveryPlace;
import beer.happy_hour.drinking.model.User;
import cz.msebera.android.httpclient.Header;

/**
 * Created by brcon on 21/03/2017.
 */

public class MessageGetterAPISync extends AsyncTask<Void, Void, Void> {

    private DeliveryPlace deliveryPlace;
    private User user;

    private boolean getName;

    private String message = "";

    public MessageGetterAPISync(String message, boolean getName){
        this.message = message;
        this.getName = getName;

        this.user = User.getInstance();
        this.deliveryPlace = DeliveryPlace.getInstance();
    }

    @Override
    protected Void doInBackground(Void... params) {

        RequestParams requestParams = new RequestParams();

        requestParams.put("message", message);

        requestParams.put("latitude", deliveryPlace.getLatitude());
        requestParams.put("longitude", deliveryPlace.getLongitude());

        if(getName) {
            requestParams.put("name", user.getName());
            requestParams.put("phone", user.getPhone());
            requestParams.put("email", user.getEmail());
        }
        else{
            requestParams.put("name", "NoName");
            requestParams.put("phone", "NoPhone");
            requestParams.put("email", "NoEmail");
        }

        Log.d("Request Params", requestParams.toString());

        RestApiHttpClient.post(Constants.BASE_INFORMATION_MESSAGE_URL, requestParams, new JsonHttpResponseHandler() {
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
