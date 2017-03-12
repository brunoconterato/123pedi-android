package beer.happy_hour.drinking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import beer.happy_hour.drinking.adapter.ShoppingCartAdapter;
import beer.happy_hour.drinking.model.ShoppingCartSingleton;

public class CheckoutActivity extends Activity implements AdapterView.OnItemClickListener {

    private ListView cartListView;
    private ShoppingCartAdapter shoppingCartAdapter;
    private ShoppingCartSingleton cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        cartListView = (ListView) findViewById(R.id.cart_list_view);
        cartListView.setOnItemClickListener(this);

        cart = ShoppingCartSingleton.getInstance();
        shoppingCartAdapter = new ShoppingCartAdapter(this, cart);
        cartListView.setAdapter(shoppingCartAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SearchActivity.class));
    }

    public void goToFinalizeActivity(View view) {
        startActivity(new Intent(this, FinalizeActivity.class));
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
}
