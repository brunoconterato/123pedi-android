package beer.happy_hour.drinking;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import beer.happy_hour.drinking.adapter.ListItemAdapter;
import beer.happy_hour.drinking.adapter.ShoppingCartAdapter;
import beer.happy_hour.drinking.model.Item;
import beer.happy_hour.drinking.model.ListItem;
import beer.happy_hour.drinking.model.ShoppingCartSingleton;

public class SearchActivity extends Activity implements LoadStockJSONTask.Listener,
                                                        AdapterView.OnItemClickListener,
                                                        SearchView.OnQueryTextListener{

    //Show listview
    private ListView mListView;
    //public static final String URL = "https://api.learn2crack.com/android/jsonandroid/";
    public static final String URL = "http://happy-hour.beer/api/search/stocksearch";

    private List<ListItem> list_listItems;
    ListItemAdapter listItemAdapter;
    ShoppingCartAdapter shoppingCartAdapter;

    ShoppingCartSingleton cart;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Show listview
        mListView = (ListView) findViewById(R.id.items_list_view);
        mListView.setOnItemClickListener(this);

        if(savedInstanceState == null || !savedInstanceState.containsKey("key")) {
            // Get the intent, verify the action and get the query
//            Intent intent = getIntent();
//            if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//                String query = intent.getStringExtra(SearchManager.QUERY);
//                doSearch(query);
//            }

            list_listItems = new ArrayList<ListItem>();

            //Show listiew
            new LoadStockJSONTask(this).execute(URL);
        }
        else {
            Log.d("Entrou: ", "else");
            list_listItems = savedInstanceState.getParcelableArrayList("key");

//            Log.d("listItems: ", listItems.toString());

            loadListView();
        }

        cart = ShoppingCartSingleton.getInstance();

        listItemAdapter = new ListItemAdapter(this, list_listItems);
        shoppingCartAdapter = new ShoppingCartAdapter(this,cart);

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
            list_listItems.add(new ListItem(item));
        }

        loadListView();
    }

    //show listview
    private void loadListView() {

        Log.d("Entrou : ", "loadListView() Method");

        Log.d("list_listItems: ", list_listItems.toString());

        listItemAdapter = new ListItemAdapter(this, list_listItems);
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

//        Toast.makeText(this, mItemsHashMap.get(i).get(
//                KEY_NAME
//                KEY_PRICE
//        ),Toast.LENGTH_SHORT).show();
    }


    //TODO: salvar Adapter
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        List<ListItem> values = listItemAdapter.getListItems();
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
        mListView.setAdapter(shoppingCartAdapter);
    }
}
