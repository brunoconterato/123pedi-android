package beer.happy_hour.drinking.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.login.widget.LoginButton;

import beer.happy_hour.drinking.Constants;
import beer.happy_hour.drinking.R;
import beer.happy_hour.drinking.load_stock.LoadStockFragment;
import beer.happy_hour.drinking.model.shopping_cart.ShoppingCart;

public class MainActivity extends AppCompatActivity implements LoadStockFragment.TaskCallbacks {

    private LoginButton b;
    private int backButtonCount;

    private ShoppingCart cart;

    private LoadStockFragment loadStockFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cart = ShoppingCart.getInstance();

        FragmentManager fm = getFragmentManager();
        loadStockFragment = (LoadStockFragment) fm.findFragmentByTag(Constants.TAG_TASK_FRAGMENT);

        if (loadStockFragment == null) {
            loadStockFragment = new LoadStockFragment();
            fm.beginTransaction().add(loadStockFragment, Constants.TAG_TASK_FRAGMENT).commit();
        }
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
        startActivity(new Intent(this, SearchTabsActivity.class));
    }

    public void goToCheckoutActivity(View view) {
        if (cart.getItemsQuantity() > 0)
            startActivity(new Intent(this, CartActivity.class));
        else
            Toast.makeText(this.getApplicationContext(), "Carrinho vazio!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError() {
        Toast.makeText(this, "Erro! Verifique sua conexão", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onCancelled() {

    }

    @Override
    public void onPostExecute() {
        Log.d("onPostExecute", "MainActivity");
    }
}
