package beer.happy_hour.drinking.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import beer.happy_hour.drinking.Constants;
import beer.happy_hour.drinking.LoadStockJSONTask;
import beer.happy_hour.drinking.R;
import beer.happy_hour.drinking.adapter.ListItemAdapter;
import beer.happy_hour.drinking.model.Item;
import beer.happy_hour.drinking.model.ListItem;
import beer.happy_hour.drinking.repository.ListItemRepositorySingleton;

public class SearchActivity extends AppCompatActivity implements LoadStockJSONTask.Listener,
                                                        AdapterView.OnItemClickListener,
                                                        SearchView.OnQueryTextListener{

    //public static final String URL = "https://api.learn2crack.com/android/jsonandroid/";
    public static final String URL = "http://happy-hour.beer/api/search/stocksearch";
    //Show listview
    private ListView mListView;
//    private List<ListItem> listItemRepository;
    private ListItemRepositorySingleton listItemRepository;
    private ListItemAdapter listItemAdapter;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Show listview
        mListView = (ListView) findViewById(R.id.items_list_view);
        mListView.setOnItemClickListener(this);

        listItemRepository = ListItemRepositorySingleton.getInstance();

        if ((savedInstanceState == null || !savedInstanceState.containsKey("key"))
                && listItemRepository.isEmpty()) {

            //Show listiew
            new LoadStockJSONTask(this).execute(URL);
        }
        else {
            Log.d("Entrou: ", "else");

            loadListView();
        }

        listItemAdapter = new ListItemAdapter(this);

        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);

        searchView = (SearchView) findViewById(R.id.search_view);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
    }

    //Show listview
    @Override
    public void onLoaded(List<Item> listItems) {
        Log.d("Entrou : ", "onLoaded");

        Log.d("listItems: ", listItems.toString());

        for(Item item : listItems){
            Log.d("ToString : ", item.toString());
            listItemRepository.add(new ListItem(item));
        }

        Log.d("Lista Normal: ", listItemRepository.getList().toString());

        loadListView();
    }

    //show listview
    private void loadListView() {

        Log.d("Entrou : ", "loadListView() Method");

        Log.d("listItemRepository: ", listItemRepository.toString());

        listItemAdapter = new ListItemAdapter(this);
        mListView.setAdapter(listItemAdapter);
    }

    //Show listview
    @Override
    public void onError() {

        Toast.makeText(this, "Error !", Toast.LENGTH_SHORT).show();
    }

    //Show listview
    //Dando Pica
    //TODO: Fazer Funcionar
    @Override
    public void onItemClick(AdapterView adapterView, View view, int i, long l) {
        Toast.makeText(this.getApplicationContext(), "Item clicado", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        List<ListItem> values = listItemRepository.getList();
        outState.putParcelableArrayList("key", (ArrayList<ListItem>) values);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d("Entrou:", "onQueryTextChange");
        listItemAdapter.getFilter().filter(newText);
        mListView.setAdapter(listItemAdapter);

        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void searchSnackCategory(View view){
        mListView.setAdapter(listItemAdapter);
        listItemAdapter.getFilter().filter(Constants.SEARCH_CATEGORY_HASH + "et");
    }

    public void searchSodaCategory(View view){
        mListView.setAdapter(listItemAdapter);
        listItemAdapter.getFilter().filter(Constants.SEARCH_CATEGORY_HASH + "Soda");
    }

    public void searchAlcoholCategory(View view){
        mListView.setAdapter(listItemAdapter);
        listItemAdapter.getFilter().filter(Constants.SEARCH_CATEGORY_HASH + "Alcohol");
    }

    public void searchCigaretteCategory(View view){
        mListView.setAdapter(listItemAdapter);
        listItemAdapter.getFilter().filter(Constants.SEARCH_CATEGORY_HASH + "Cigarette");
    }

    public void viewShoppingCart(View view){
        Intent intent = new Intent(this, CheckoutActivity.class);
        startActivity(intent);
        finish();
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
