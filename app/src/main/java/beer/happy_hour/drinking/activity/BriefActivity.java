package beer.happy_hour.drinking.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import beer.happy_hour.drinking.R;
import beer.happy_hour.drinking.adapter.BriefItemsAdapter;
import beer.happy_hour.drinking.async_http_client.RestApiHttpClient;
import beer.happy_hour.drinking.model.DeliveryPlace;
import beer.happy_hour.drinking.model.Item;
import beer.happy_hour.drinking.model.ListItem;
import beer.happy_hour.drinking.model.ShoppingCartSingleton;
import beer.happy_hour.drinking.model.User;
import cz.msebera.android.httpclient.Header;

public class BriefActivity extends AppCompatActivity {

    private User user;
    private DeliveryPlace deliveryPlace;
    private ShoppingCartSingleton cart;

    private TextView contact_brief_text_view;
    private TextView address_brief_text_view;

    private ListView items_brief_list_view;
    private ArrayAdapter<ListItem> adapter;

    private CheckBox majority_check_box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brief);

        user = User.getInstance();
        deliveryPlace = DeliveryPlace.getInstance();
        cart = ShoppingCartSingleton.getInstance();

        contact_brief_text_view = (TextView) findViewById(R.id.contact_brief_text_view);
        address_brief_text_view = (TextView) findViewById(R.id.address_brief_text_view);

        contact_brief_text_view.setText(""
                + "Nome: " + user.getName()
                + "\nTelefone: " + user.getPhone()
                + "\nEmail: " + user.getEmail()
        );

        address_brief_text_view.setText(""
                + "Endereço: " + deliveryPlace.getAdress()
                + "\nCidade: " + deliveryPlace.getCityState()
                + "\nPaís: " + deliveryPlace.getCountryName()
        );

        items_brief_list_view = (ListView) findViewById(R.id.brief_items_list_view);
        adapter = new BriefItemsAdapter(this.getApplicationContext());
        items_brief_list_view.setAdapter(adapter);
        items_brief_list_view.setScrollBarSize(cart.getItemsQuantity());

        majority_check_box = (CheckBox) findViewById(R.id.majority_confirmation_checkBox);
    }

    private void generateOrder(View view) {
        if (majority_check_box.isChecked()) {
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
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.d("Success!!", "Array");

                    JSONArray jsonArray = response;
                    Log.d("JSONArray", String.valueOf(jsonArray));
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
        } else {

        }
    }

    private void initiatePopUpWindow(View view) {

    }
}
