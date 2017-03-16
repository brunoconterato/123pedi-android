package beer.happy_hour.drinking.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import beer.happy_hour.drinking.R;

public class MapsActivity extends AppCompatActivity {

    MapsFragmentActivity mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mapFragment = new MapsFragmentActivity();
    }


}
