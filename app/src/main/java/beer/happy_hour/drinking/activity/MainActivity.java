package beer.happy_hour.drinking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.facebook.login.widget.LoginButton;

import beer.happy_hour.drinking.R;
import beer.happy_hour.drinking.model.ShoppingCartSingleton;

public class MainActivity extends AppCompatActivity {

    private LoginButton b;
    private int backButtonCount;

    private ShoppingCartSingleton cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cart = ShoppingCartSingleton.getInstance();

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

    public void goToCheckoutActivity(View view) {
        if (cart.getItemsQuantity() > 0)
            startActivity(new Intent(this, CartActivity.class));
        else
            Toast.makeText(this.getApplicationContext(), "Carrinho vazio!", Toast.LENGTH_SHORT).show();
    }
}
