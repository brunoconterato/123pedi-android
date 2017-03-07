package beer.happy_hour.drinking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.*;

import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity {

    private LoginButton b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        b = (LoginButton) findViewById(R.id.login_button);
//
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(intent);
//            }
//        });

    }

    public void login(View view){
        //Pode haver problema nesse this
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void goToSearchActivity(View view){
        //Pode haver problema nesse this
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
}
