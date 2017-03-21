package beer.happy_hour.drinking.generate_order_async;

import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import beer.happy_hour.drinking.async_http_client.RestApiHttpClient;
import beer.happy_hour.drinking.model.DeliveryPlace;
import beer.happy_hour.drinking.model.Item;
import beer.happy_hour.drinking.model.ListItem;
import beer.happy_hour.drinking.model.ShoppingCartSingleton;
import beer.happy_hour.drinking.model.User;
import cz.msebera.android.httpclient.Header;

/**
 * Created by brcon on 21/03/2017.
 */

public class GenerateOrderAPIAsync extends AsyncTask<Void, Void, Boolean> {

    boolean succesInTransaction = false;

    private User user;
    private DeliveryPlace deliveryPlace;
    private ShoppingCartSingleton cart;

    @Override
    protected Boolean doInBackground(Void... params) {
        user = User.getInstance();
        deliveryPlace = DeliveryPlace.getInstance();
        cart = ShoppingCartSingleton.getInstance();

        RequestParams requestParams = new RequestParams();

        int i = -1;
        for (ListItem listItem : cart.getListItems()) {
            i++;
            Item item = listItem.getItem();
            requestParams.put("stockitems_id[" + Integer.toString(i) + "]", Integer.toString(item.getId()));
            requestParams.put("stockitems_quantity[" + i + "]", Integer.toString(listItem.getQuantity()));
        }

        requestParams.put("retailer_id", "1");
        requestParams.put("name", user.getName());
        requestParams.put("phone", user.getPhone());
        requestParams.put("street_adress", deliveryPlace.getThoroughfare());
        requestParams.put("adress_line_2", deliveryPlace.getComplement());
        requestParams.put("neighborhood", deliveryPlace.getSubLocality());
        requestParams.put("city", deliveryPlace.getLocality());
        requestParams.put("state", deliveryPlace.getAdminArea());
        requestParams.put("zipcode", deliveryPlace.getZipCode());
        requestParams.put("email", user.getEmail());
        requestParams.put("lat_coordinate", Double.toString(deliveryPlace.getLatitude()));
        requestParams.put("long_coordinate", Double.toString(deliveryPlace.getLongitude()));

        Log.d("Request Params", requestParams.toString());

        RestApiHttpClient.post("http://happy-hour.beer/api/unregistered/orders", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("Success!!", "Object");

                succesInTransaction = true;
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("Success!!", "Array");

                JSONArray jsonArray = response;
                Log.d("JSONArray", String.valueOf(jsonArray));

                succesInTransaction = true;
            }
        });

        //    OrderRestClient.post(Constants.BASE_ORDER_URL, requestParams, new AsyncHttpResponseHandler() {
        //            @Override
        //            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        ////                Log.d("Request Params", requestParams.toString());
        //                Log.d("Order", "Success!");
        //            }
        //
        //            @Override
        //            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        //                Log.d("Failed: ", "" + statusCode);
        //                Log.d("Error : ", "" + error);
        //                Log.d("Headers : ", "" + headers.toString());
        //            }
        //        });


        return succesInTransaction;
    }

    public interface Listener {

        void onGeneratedAPIOrder();

    }
}
