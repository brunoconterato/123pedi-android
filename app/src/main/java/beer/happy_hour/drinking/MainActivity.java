package beer.happy_hour.drinking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity {

    private LoginButton b;
    private int backButtonCount;

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

    /**
     * Back button listener.
     * Will close the application if the back button pressed twice.
     */
    @Override
    public void onBackPressed() {
        if (backButtonCount >= 1) {
            backButtonCount = 0;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Pressione o botão voltar outra vez para fechar a aplicação", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
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
