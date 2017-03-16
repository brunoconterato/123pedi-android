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

import beer.happy_hour.drinking.GPSTracker;
import beer.happy_hour.drinking.R;
import beer.happy_hour.drinking.model.DeliveryPlace;
import beer.happy_hour.drinking.model.User;
import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

public class FinalizeActivity extends AppCompatActivity {

    //Location Variables (Not Google Map API)
    Button get_location_button;
    // GPSTracker class
    GPSTracker gps;

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
}
