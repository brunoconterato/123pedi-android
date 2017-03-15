package beer.happy_hour.drinking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import beer.happy_hour.drinking.model.DeliveryPlace;
import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

public class FinalizeActivity extends AppCompatActivity {

    //Location Variables (Not Google Map API)
    Button get_location_button;
    // GPSTracker class
    GPSTracker gps;

    DeliveryPlace deliveryPlace;


    EditText name_edit_text;
    EditText phone_edit_text;
    EditText email_edit_text;

    TextView adress_text_view;
    TextView citystate_text_view;
    TextView country_text_view;

    EditText complement_edit_text;

    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

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

        name_edit_text = (EditText) findViewById(R.id.name_edit_text);
        phone_edit_text = (EditText) findViewById(R.id.phone_edit_text);
        email_edit_text = (EditText) findViewById(R.id.email_edit_text);

        MaskEditTextChangedListener maskPhone = new MaskEditTextChangedListener("(##) # ####-####", phone_edit_text);
        phone_edit_text.addTextChangedListener(maskPhone);

        deliveryPlace = DeliveryPlace.getInstance();

        adress_text_view = (TextView) findViewById(R.id.adress_text_view);
        citystate_text_view = (TextView) findViewById(R.id.citystate_text_view);
        country_text_view = (TextView) findViewById(R.id.country_text_view);

        complement_edit_text = (EditText) findViewById(R.id.complement_edit_text);

        adress_text_view.setText(deliveryPlace.getAdress());
        citystate_text_view.setText(deliveryPlace.getCityState());
        country_text_view.setText(deliveryPlace.getCountry());
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

    public boolean isValidPhone(String phone) {
        //retira todos os caracteres menos os numeros
        phone = phone.replace("(", "");
        phone = phone.replace(")", "");
        phone = phone.replace("-", "");
        phone = phone.replace(" ", "");

        //verifica se tem a qtde de numero correto
        if (phone.length() != 11) return false;

        //Se tiver 11 caracteres, verificar se começa com 9 o celular
        if (Integer.parseInt(phone.substring(2, 3)) != 9) return false;

        //DDDs validos
        int[] codigosDDD = {11, 12, 13, 14, 15, 16, 17, 18, 19,
                21, 22, 24, 27, 28, 31, 32, 33, 34,
                35, 37, 38, 41, 42, 43, 44, 45, 46,
                47, 48, 49, 51, 53, 54, 55, 61, 62,
                64, 63, 65, 66, 67, 68, 69, 71, 73,
                74, 75, 77, 79, 81, 82, 83, 84, 85,
                86, 87, 88, 89, 91, 92, 93, 94, 95,
                96, 97, 98, 99};

        ArrayList DDDs_validos = new ArrayList();
        for (int i : codigosDDD)
            DDDs_validos.add(i);

        //verifica se o DDD é valido (sim, da pra verificar rsrsrs)
        if (!DDDs_validos.contains(Integer.parseInt(phone.substring(0, 2)))) return false;

        //se passar por todas as validações acima, então está tudo certo
        return true;
    }

    public void logValidatePhone(View view) {
        Log.d("isValidPhone", Boolean.toString(isValidPhone(phone_edit_text.getText().toString())));
    }

    public void logValidateEmail(View view) {
        Log.d("isValidEmail", Boolean.toString(isValidEmail(email_edit_text.getText().toString())));
    }
}
