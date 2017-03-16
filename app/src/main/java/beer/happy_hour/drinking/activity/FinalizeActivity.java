package beer.happy_hour.drinking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import beer.happy_hour.drinking.GPSTracker;
import beer.happy_hour.drinking.R;
import beer.happy_hour.drinking.async_http_client.RestApiHttpClient;
import beer.happy_hour.drinking.model.DeliveryPlace;
import beer.happy_hour.drinking.model.Item;
import beer.happy_hour.drinking.model.ListItem;
import beer.happy_hour.drinking.model.ShoppingCartSingleton;
import beer.happy_hour.drinking.model.User;
import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;
import cz.msebera.android.httpclient.Header;

public class FinalizeActivity extends AppCompatActivity {

    //Location Variables (Not Google Map API)
    Button get_location_button;
    // GPSTracker class
    GPSTracker gps;

    ShoppingCartSingleton cart;
    DeliveryPlace deliveryPlace;
    User user;

    EditText name_edit_text;
    EditText phone_edit_text;
    EditText email_edit_text;

    TextView adress_text_view;
    TextView citystate_text_view;
    TextView country_text_view;

    EditText complement_edit_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalize);

        //Not from google mapFragment api
        get_location_button = (Button) findViewById(R.id.get_location_button);

        // show location button click event
        get_location_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // create class object
                gps = new GPSTracker(FinalizeActivity.this);

                // check if GPS enabled
                if (gps.canGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

            }
        });

        cart = ShoppingCartSingleton.getInstance();
        deliveryPlace = DeliveryPlace.getInstance();
        user = User.getInstance();

        name_edit_text = (EditText) findViewById(R.id.name_edit_text);
        phone_edit_text = (EditText) findViewById(R.id.phone_edit_text);
        email_edit_text = (EditText) findViewById(R.id.email_edit_text);

        name_edit_text.setText(user.getName());
        name_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                user.setName(name_edit_text.getText().toString());
                Log.d("Nome", user.getName());
            }
        });

        phone_edit_text.setText(user.getPhone());
        phone_edit_text.addTextChangedListener(new UserPhoneTextListener("(##) # ####-####", phone_edit_text));

        email_edit_text.setText(user.getEmail());
        email_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                user.setEmail(email_edit_text.getText().toString());
                Log.d("Email", user.getEmail());
            }
        });

//        MaskEditTextChangedListener maskPhone = new MaskEditTextChangedListener("(##) # ####-####", phone_edit_text);
//        phone_edit_text.addTextChangedListener(maskPhone);


        adress_text_view = (TextView) findViewById(R.id.adress_text_view);
        citystate_text_view = (TextView) findViewById(R.id.citystate_text_view);
        country_text_view = (TextView) findViewById(R.id.country_text_view);

        complement_edit_text = (EditText) findViewById(R.id.complement_edit_text);
        complement_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                deliveryPlace.setComplement(complement_edit_text.getText().toString());
                Log.d("Complement : ", "" + deliveryPlace.getComplement());
            }
        });

        adress_text_view.setText(deliveryPlace.getAdress());
        citystate_text_view.setText(deliveryPlace.getCityState());
        country_text_view.setText(deliveryPlace.getCountryName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                return (true);
            case R.id.reset:
                return (true);
            case R.id.about:
                Toast.makeText(this, "About Toast!", Toast.LENGTH_LONG).show();
                return (true);
            case R.id.exit:
                finish();
                return (true);

        }
        return (super.onOptionsItemSelected(item));
    }

    public void goToMapActivity(View view) {
        startActivity(new Intent(this, MapsFragmentActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, CheckoutActivity.class));
    }

    public void logValidatePhone(View view) {
        Log.d("isValidPhone", Boolean.toString(user.isValidPhone(phone_edit_text.getText().toString())));
    }

    public void logValidateEmail(View view) {
        Log.d("isValidEmail", Boolean.toString(user.isValidEmail(email_edit_text.getText().toString())));
    }

    public void finalizeOrder(View view) {
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

//        OrderRestClient.post(Constants.BASE_ORDER_URL, requestParams, new AsyncHttpResponseHandler() {
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

    }

    private class UserPhoneTextListener extends MaskEditTextChangedListener implements TextWatcher {

        User user;

        public UserPhoneTextListener(String mask, EditText editText) {
            super(mask, editText);

            user = User.getInstance();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            user.setPhone(phone_edit_text.getText().toString());
            Log.d("Phone", user.getPhone());
        }
    }

//    public class SendPostRequest extends AsyncTask<String, Void, String> {
//
//        protected void onPreExecute(){}
//
//        protected String doInBackground(String... arg0) {
//            try{
//                URL url = new URL(Constants.BASE_ORDER_URL);
//
//                Log.d("URL", url.toString());
//
//                JSONObject postDataParams = new JSONObject();
//
//                int i=-1;
//                for (ListItem listItem : cart.getListItems()) {
//                    i++;
//                    Item item = listItem.getItem();
//                    postDataParams.put("stockitems_id[" + Integer.toString(i) + "]", Integer.toString(item.getId()));
//                    postDataParams.put("stockitems_quantity[" + i + "]", Integer.toString(listItem.getQuantity()));
//                }
//
//                postDataParams.put("name", user.getName());
//                postDataParams.put("phone", user.getPhone());
//                postDataParams.put("street_adress", deliveryPlace.getThoroughfare());
//                postDataParams.put("adress_line2", deliveryPlace.getComplement());
//                postDataParams.put("neighborhood", deliveryPlace.getSubLocality());
//                postDataParams.put("city", deliveryPlace.getLocality());
//                postDataParams.put("state", deliveryPlace.getAdminArea());
//                postDataParams.put("zipcode", deliveryPlace.getZipCode());
//                postDataParams.put("email", user.getEmail());
//                postDataParams.put("lat_coordinate", Double.toString(deliveryPlace.getLatitude()));
//                postDataParams.put("long_coordinate", Double.toString(deliveryPlace.getLongitude()));
//                Log.e("params",postDataParams.toString());
//
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setReadTimeout(15000 /* milliseconds */);
//                conn.setConnectTimeout(15000 /* milliseconds */);
//                conn.setRequestMethod("POST");
//                conn.setDoInput(true);
//                conn.setDoOutput(true);
//
//                OutputStream os = conn.getOutputStream();
//                BufferedWriter writer = new BufferedWriter(
//                        new OutputStreamWriter(os, "UTF-8"));
//
//                Log.d("Str", getPostDataString(postDataParams));
//                writer.write(getPostDataString(postDataParams));
//
//                writer.flush();
//                writer.close();
//                os.close();
//
//                int responseCode=conn.getResponseCode();
//
//                if (responseCode == HttpsURLConnection.HTTP_OK) {
//
//                    BufferedReader in=new BufferedReader(
//                            new InputStreamReader(
//                                    conn.getInputStream()));
//                    StringBuffer sb = new StringBuffer("");
//                    String line="";
//
//                    while((line = in.readLine()) != null) {
//
//                        sb.append(line);
//                        break;
//                    }
//
//                    in.close();
//                    return sb.toString();
//
//                }
//                else {
//                    return new String("false : "+responseCode);
//                }
//            }
//            catch(Exception e){
//                return new String("Exception: " + e.getMessage());
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            Toast.makeText(getApplicationContext(), result,
//                    Toast.LENGTH_LONG).show();
//        }
//
//        public String getPostDataString(JSONObject params) throws Exception {
//
//            Log.d("Entrou", "getPostDataString");
//
//            StringBuilder result = new StringBuilder();
//            boolean first = true;
//
//            Iterator<String> itr = params.keys();
//
//            while(itr.hasNext()){
//
//                String key= itr.next();
//                Object value = params.get(key);
//
//                if (first)
//                    first = false;
//                else
//                    result.append("&");
//
//                result.append(URLEncoder.encode(key, "UTF-8"));
//                result.append("=");
//                result.append(URLEncoder.encode(value.toString(), "UTF-8"));
//
//            }
//
//
//            Log.d("Post Data String", result.toString());
//            return result.toString();
//        }
//    }
}
