package beer.happy_hour.drinking;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import beer.happy_hour.drinking.adapter.ListItemAdapter;
import beer.happy_hour.drinking.model.Item;
import beer.happy_hour.drinking.model.ListItem;

public class SearchActivity extends Activity implements LoadStockJSONTask.Listener, AdapterView.OnItemClickListener {

    //Show listview
    private ListView mListView;
    //public static final String URL = "https://api.learn2crack.com/android/jsonandroid/";
    public static final String URL = "http://happy-hour.beer/api/search/stocksearch";

    private List<ListItem> list_listItems;
    ListItemAdapter adapter;

//    private List<HashMap<String, String>> mItemsHashMap = new ArrayList<>();
//    private static final String KEY_PRICE = "price";
//    private static final String KEY_NAME = "name";
//    private static final String KEY_BRAND = "brand";
//    private static final String KEY_MANUFACTURER = "manufacturer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Show listview
        mListView = (ListView) findViewById(R.id.items_list_view);
        mListView.setOnItemClickListener(this);

        if(savedInstanceState == null || !savedInstanceState.containsKey("key")) {
            // Get the intent, verify the action and get the query
            Intent intent = getIntent();
            if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
                String query = intent.getStringExtra(SearchManager.QUERY);
                doSearch(query);
            }

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
    }

    //Show listview
    @Override
    public void onLoaded(List<Item> listItems) {

//        this.listItems = listItems;

        Log.d("Entrou : ", "onLoaded");

        Log.d("listItems: ", listItems.toString());

        for(Item item : listItems){
            Log.d("ToString : ", item.toString());
            list_listItems.add(new ListItem(item));
        }


//        for (Item item : listItem) {
//
//            HashMap<String, String> map = new HashMap<>();
//
//            map.put(KEY_PRICE, Double.toString(item.getPrice()));
//            map.put(KEY_NAME, item.getProduct().getName());
//            map.put(KEY_BRAND, item.getProduct().getBrand());
//            map.put(KEY_MANUFACTURER, item.getProduct().getManufacturer());
//
//            Log.d("PRICE : ", Double.toString(item.getPrice()));
//            Log.d("JSON : ", item.getProduct().getName());
//            Log.d("JSON : ", item.getProduct().getBrand());
//            Log.d("JSON : ", item.getProduct().getManufacturer());
//
//            mItemsHashMap.add(map);
//
//        }

        loadListView();
    }

    //show listview
    private void loadListView() {

        Log.d("Entrou : ", "loadListView() Method");

//        Log.d("listItems: ", listItems.toString());

        Log.d("list_listItems: ", list_listItems.toString());

        adapter = new ListItemAdapter(this, list_listItems);
        mListView.setAdapter(adapter);

//        ListAdapter adapter = new SimpleAdapter(
//                SearchActivity.this,
//                mItemsHashMap,
//                R.layout.list_item,
//                new String[] {KEY_PRICE, KEY_NAME, KEY_BRAND, KEY_MANUFACTURER},
//                new int[] { R.id.price, R.id.name, R.id.brand, R.id.manufacturer});
//
//        mListView.setAdapter(adapter);
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

        List<ListItem> values = adapter.getListItems();
        outState.putParcelableArrayList("key", (ArrayList<ListItem>) values);
    }


    //search
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    //search
    public void onListItemClick(ListView l,
                                View v, int position, long id) {
        // call detail activity for clicked entry
    }

    //search
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query =
                    intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        }
    }

    //search
    private void doSearch(String queryStr) {
        // get a Cursor, prepare the ListAdapter
        // and set it
    }
}
