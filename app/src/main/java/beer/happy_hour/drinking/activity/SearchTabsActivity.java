package beer.happy_hour.drinking.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.Toast;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import beer.happy_hour.drinking.Constants;
import beer.happy_hour.drinking.LoadStockJSONTask;
import beer.happy_hour.drinking.R;
import beer.happy_hour.drinking.adapter.ViewPagerAdapter;
import beer.happy_hour.drinking.fragment.SearchFragment;
import beer.happy_hour.drinking.model.Item;
import beer.happy_hour.drinking.model.shopping_cart.ListItem;
import beer.happy_hour.drinking.repository.ListItemRepository;

public class SearchTabsActivity extends AppCompatActivity implements LoadStockJSONTask.LoadListener {
//        SearchView.OnQueryTextListener {
//                                                                        AdapterView.OnItemClickListener,
//                                                                        SearchView.OnQueryTextListener
    
    //Show listview
//    private ListView mListView;
    private boolean loadedListView = false;

    private ListItemRepository listItemRepository;
//    private ListItemAdapter listItemAdapter;

    private SearchView searchView;
    private String searchViewText = "Buscar produto";

//    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private SearchFragment searchFragment;
    private SearchFragment alcoolicsFragment;
    private SearchFragment nonAlcoolicsFragment;
    private SearchFragment cigarettesFragment;
    private SearchFragment snacksFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tabs);

        //Show listview
//        mListView = (ListView) findViewById(R.id.items_list_view);
//        mListView.setOnItemClickListener(this);

        listItemRepository = ListItemRepository.getInstance();

//        if ((savedInstanceState == null || !savedInstanceState.containsKey("key"))
//                && listItemRepository.isEmpty()) {
//
//            //Show listiew
////            new LoadStockJSONTask(this).execute(Constants.BASE_STOCK_URL);
//        }
//        else {
//            Log.d("Entrou: ", "else");
//
//            loadSearchFragment();
//        }

//        listItemAdapter = new ListItemAdapter(this);

        //Initializing Fragments
        alcoolicsFragment = loadNewFragmentInstance(Constants.SEARCH_CATEGORY_HASH + "alcoolicos");
        nonAlcoolicsFragment = loadNewFragmentInstance(Constants.SEARCH_CATEGORY_HASH + "nao_alcoolicos");
        cigarettesFragment = loadNewFragmentInstance(Constants.SEARCH_CATEGORY_HASH + "cigarros");
        snacksFragment = loadNewFragmentInstance(Constants.SEARCH_CATEGORY_HASH + "outros");
        searchFragment = loadNewFragmentInstance("");

        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);

        searchView = (SearchView) findViewById(R.id.search_view);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(searchFragment);

        LoadStockJSONTask loadStockJSONTask = LoadStockJSONTask.getInstance();
        loadStockJSONTask.setListener(this);

        if(listItemRepository.isLoaded() && !loadedListView)
            loadSearchFragment();
        if(listItemRepository.isDisconnected())
            Toast.makeText(this, "Erro! Não foi possível recuperar dados", Toast.LENGTH_LONG).show();

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.search_view_pager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.search_tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();
    }

    private void setupTabIcons() {
        ImageView tab1 = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_search_tab, null);
        tab1.findViewById(R.id.tabIcons).setBackgroundResource(R.drawable.ic_snack_sack);
        tabLayout.getTabAt(0).setCustomView(tab1);

        ImageView tab2 = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_search_tab, null);
        tab2.findViewById(R.id.tabIcons).setBackgroundResource(R.drawable.ic_movie_soda_clipart_free_clipart_images);
        tabLayout.getTabAt(1).setCustomView(tab2);

        ImageView tab3 = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_search_tab, null);
        tab3.findViewById(R.id.tabIcons).setBackgroundResource(R.drawable.ic_antu_drink_beer_svg);
        tabLayout.getTabAt(2).setCustomView(tab3);

        ImageView tab4 = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_search_tab, null);
        tab4.findViewById(R.id.tabIcons).setBackgroundResource(R.drawable.ic_cigarette_box);
        tabLayout.getTabAt(3).setCustomView(tab4);

        ImageView tab5 = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_search_tab, null);
        tab5.findViewById(R.id.tabIcons).setBackgroundResource(R.drawable.ic_magnifying_glass_icon);
        tabLayout.getTabAt(4).setCustomView(tab5);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(snacksFragment, "OUTROS");
        viewPagerAdapter.addFragment(nonAlcoolicsFragment, "NÃO ALCOÓLICOS");
        viewPagerAdapter.addFragment(alcoolicsFragment, "ALCOÓLICOS");
        viewPagerAdapter.addFragment(cigarettesFragment, "CIGARROS");
        viewPagerAdapter.addFragment(searchFragment, "BUSCA");
        viewPager.setAdapter(viewPagerAdapter);
    }

    //Show listview
    @Override
    public void onLoaded(List<Item> listItems) {
        Log.d("Entrou : ", "onLoaded");

        Log.d("listItems: ", listItems.toString());

//        for(Item item : listItems){
//            Log.d("ToString : ", item.toString());
//            listItemRepository.add(new ListItem(item));
//        }

        Log.d("Lista Normal: ", listItemRepository.getList().toString());

        loadSearchFragment();
    }

    //show listview
    private void loadSearchFragment() {
        if(!loadedListView) {
            Log.d("Entrou : ", "loadSearchFragment() Method");

            Log.d("listItemRepository: ", listItemRepository.toString());

            alcoolicsFragment = loadNewFragmentInstance(Constants.SEARCH_CATEGORY_HASH + "alcoolicos");

            loadedListView = true;
        }
    }

    //Show listview
    @Override
    public void onError() {
        Toast.makeText(this, "Erro! Não foi possível recuperar dados", Toast.LENGTH_LONG).show();
    }

//    //Show listview
//    //Dando Pica
//    //TODO: Fazer Funcionar
//    @Override
//    public void onItemClick(AdapterView adapterView, View view, int i, long l) {
//        Toast.makeText(this.getApplicationContext(), "Item clicado", Toast.LENGTH_SHORT).show();
//    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        List<ListItem> values = listItemRepository.getList();
        outState.putParcelableArrayList("key", (ArrayList<ListItem>) values);
    }

//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        return false;
//    }
//
//    @Override
//    public boolean onQueryTextChange(String newText) {
//        Log.d("Entrou:", "onQueryTextChange");
////        listItemAdapter.getFilter().filter(newText);
////        mListView.setAdapter(listItemAdapter);
//
//        return true;
//    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void searchSnackCategory(View view){
//        mListView.setAdapter(listItemAdapter);
//        listItemAdapter.getFilter().filter(Constants.SEARCH_CATEGORY_HASH + "outros");
    }

    public void searchSodaCategory(View view){
//        mListView.setAdapter(listItemAdapter);
//        listItemAdapter.getFilter().filter(Constants.SEARCH_CATEGORY_HASH + "nao_alcoolicos");
    }

    public void searchAlcoholCategory(View view){
//        mListView.setAdapter(listItemAdapter);
//        listItemAdapter.getFilter().filter(Constants.SEARCH_CATEGORY_HASH + "alcoolicos");
    }

    public void searchCigaretteCategory(View view){
//        mListView.setAdapter(listItemAdapter);
//        listItemAdapter.getFilter().filter(Constants.SEARCH_CATEGORY_HASH + "cigarros");
    }

    public void viewShoppingCart(View view){
        Intent intent = new Intent(this, CartActivity.class);
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

    public SearchFragment loadNewFragmentInstance(String querySearch) {
        SearchFragment searchFragment = new SearchFragment();

        Bundle args = new Bundle();
        args.putString(Constants.QUERY_KEY_STRING, querySearch);
        searchFragment.setArguments(args);
        return searchFragment;
    }

    public void changeTab(int tabIndex){
        if(tabIndex < tabLayout.getTabCount()) {
            TabLayout.Tab tab = tabLayout.getTabAt(tabIndex);
            tab.select();
        }
    }
}
