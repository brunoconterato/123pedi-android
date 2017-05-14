package beer.happy_hour.drinking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import beer.happy_hour.drinking.R;

public class OnOrderAcceptedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_order_accepted);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void returnToMainActivity(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void goToFeedBackActivity(View view) {
        startActivity(new Intent(this, FeedBackActivity.class));
    }
}
