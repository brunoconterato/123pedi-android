package beer.happy_hour.drinking.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import beer.happy_hour.drinking.Constants;
import beer.happy_hour.drinking.R;
import beer.happy_hour.drinking.adapter.ShoppingCartAdapter;
import beer.happy_hour.drinking.async_http_client.CartGetterAPISync;
import beer.happy_hour.drinking.listener.TotalTextView;
import beer.happy_hour.drinking.load_stock_data.DownloadImageFragment;
import beer.happy_hour.drinking.load_stock_data.LoadStockFragment;
import beer.happy_hour.drinking.model.List_Item.ListItem;
import beer.happy_hour.drinking.model.List_Item.ShoppingCart;

public class CartActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
                                                                LoadStockFragment.TaskCallbacks,
                                                                DownloadImageFragment.TaskCallbacks  {

    private String TOTAL_PREFIX = "TOTAL: R$ ";
    private TotalTextView total_text_view;
    private ListView cartListView;
    private ShoppingCartAdapter shoppingCartAdapter;
    private ShoppingCart cart;

    private LoadStockFragment loadStockFragment;
    private DownloadImageFragment downloadImageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        FragmentManager fm = getFragmentManager();
        loadStockFragment = (LoadStockFragment) fm.findFragmentByTag(Constants.TAG_TASK_FRAGMENT);
        downloadImageFragment = (DownloadImageFragment) fm.findFragmentByTag(Constants.TAG_DOWNLOAD_IMAGE_TASK_FRAGMENT);

        if (loadStockFragment == null) {
            loadStockFragment = new LoadStockFragment();
            fm.beginTransaction().add(loadStockFragment, Constants.TAG_TASK_FRAGMENT).commit();
        }

        if(downloadImageFragment == null) {
            downloadImageFragment = new DownloadImageFragment();
            fm.beginTransaction().add(downloadImageFragment, Constants.TAG_DOWNLOAD_IMAGE_TASK_FRAGMENT).commit();
        }

        cartListView = (ListView) findViewById(R.id.cart_list_view);
        cartListView.setOnItemClickListener(this);

        setupAdapter();

        cart = ShoppingCart.getInstance();

        total_text_view = (TotalTextView) findViewById(R.id.total_text_view);
        total_text_view.setText(TOTAL_PREFIX + Double.toString(cart.getTotal()));
        cart.setListener(total_text_view);
    }

    public void setupAdapter(){
        shoppingCartAdapter = new ShoppingCartAdapter(this);
        cartListView.setAdapter(shoppingCartAdapter);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SearchTabsActivity.class));
        finish();
    }

    public void goToFinalizeActivity(View view) {
        if(cart.getListItems().size() > 0) {
            for(ListItem listItem : cart.getListItems())
                new CartGetterAPISync(listItem.getItem().getId(),listItem.getQuantity(),"none","none").execute();
            startActivity(new Intent(this, OrderDetailsActivity.class));
        }
        else
            Toast.makeText(getApplicationContext(), "Você precisa retornar e adicionar items ao carrinho para prosseguir", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.d("Menu", "onCreateMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem cart = (MenuItem) menu.findItem(R.id.cart_menu_item);
        cart.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, SearchTabsActivity.class));
                return (true);

        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    }

    //Show listview
    @Override
    public void onError() {
        Toast.makeText(this, "Erro! Não foi possível recuperar dados", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onCancelled() {

    }

    @Override
    public void onPostExecute() {
        Log.d("onPostExecute", "SearchTabsActivity");
        setupAdapter();
    }

    @Override
    public void onPreExecuteImage() {

    }

    @Override
    public void onCancelledImage() {

    }

    @Override
    public void onPostExecuteImage() {
        Log.d("onPostExecuteImage", "SearchTabsActivity");
        setupAdapter();
    }

    @Override
    public void onErrorImage() {
        Toast.makeText(this, "Erro! Não foi possível recuperar imagens", Toast.LENGTH_LONG).show();
    }
}
