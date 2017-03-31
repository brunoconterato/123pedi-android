package beer.happy_hour.drinking.activity;

import android.app.FragmentManager;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import beer.happy_hour.drinking.Constants;
import beer.happy_hour.drinking.R;
import beer.happy_hour.drinking.adapter.ViewPagerAdapter;
import beer.happy_hour.drinking.fragment.CategoryFragment;
import beer.happy_hour.drinking.fragment.SearchResultsFragment;
import beer.happy_hour.drinking.load_stock.LoadStockFragment;
import beer.happy_hour.drinking.model.List_Item.ShoppingCart;

public class SearchTabsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
                                                                        LoadStockFragment.TaskCallbacks {

    private boolean loadedFragments = false;

    private SearchView searchView;

    private ShoppingCart cart;

//    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private SearchResultsFragment searchResultsFragment;
    private CategoryFragment alcoolicsFragment;
    private CategoryFragment nonAlcoolicsFragment;
    private CategoryFragment cigarettesFragment;
    private CategoryFragment snacksFragment;

    private LoadStockFragment loadStockFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tabs);

        FragmentManager fm = getFragmentManager();
        loadStockFragment = (LoadStockFragment) fm.findFragmentByTag(Constants.TAG_TASK_FRAGMENT);

        if (loadStockFragment == null) {
            loadStockFragment = new LoadStockFragment();
            fm.beginTransaction().add(loadStockFragment, Constants.TAG_TASK_FRAGMENT).commit();
        }

        loadFragments();
        loadedFragments = true;

        cart = ShoppingCart.getInstance();

        viewPager = (ViewPager) findViewById(R.id.search_view_pager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.search_tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() != 4) {
                    /**
                     * Sumindo teclado ao executar busca!
                     */
                    View focus = getCurrentFocus();
                    if (focus != null) {
                        InputMethodManager im = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        im.hideSoftInputFromWindow(focus.getApplicationWindowToken(), 0);
                    }

                    if (focus != null) {
                        focus.clearFocus();
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupTabIcons();

        TabLayout.Tab tab = tabLayout.getTabAt(2);
        tab.select();

        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);

        searchView = (SearchView) findViewById(R.id.search_view);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        Log.d("onCreate finished", "SearchTabsActivity");
    }

    private void loadFragments() {
        alcoolicsFragment = loadNewCategoryFragmentInstance(Constants.SEARCH_CATEGORY_HASH + "alcoolicos");
        nonAlcoolicsFragment = loadNewCategoryFragmentInstance(Constants.SEARCH_CATEGORY_HASH + "nao_alcoolicos");
        cigarettesFragment = loadNewCategoryFragmentInstance(Constants.SEARCH_CATEGORY_HASH + "cigarros");
        snacksFragment = loadNewCategoryFragmentInstance(Constants.SEARCH_CATEGORY_HASH + "outros");

        searchResultsFragment = new SearchResultsFragment();

        loadedFragments = true;
        Log.d("Loaded Fragments", Boolean.toString(loadedFragments));
    }

    private void setupCategoryAdapters() {
        alcoolicsFragment.setupAdapter();
        nonAlcoolicsFragment.setupAdapter();
        cigarettesFragment.setupAdapter();
        snacksFragment.setupAdapter();
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
        viewPagerAdapter.addFragment(searchResultsFragment, "BUSCA");
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        viewPager.setCurrentItem(4);
        searchResultsFragment.getAdapter().getFilter().filter(query);

        /**
         * Sumindo teclado ao executar busca!
         */
        View focus = getCurrentFocus();
        if (focus != null) {
            InputMethodManager im = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(focus.getApplicationWindowToken(), 0);
        }

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newQuery) {
        Log.d("Entrou:", "onQueryTextChange");

        viewPager.setCurrentItem(4);

        searchResultsFragment.getAdapter().getFilter().filter(newQuery);

        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void goToShoppingCart(View view){
        if(cart.getItemsQuantity() > 0)
            startActivity(new Intent(this, CartActivity.class));
        else
            Toast.makeText(this, "Aumente quantidades para acrescentar itens ao carrinho!", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart_menu_item:
                if(cart.getItemsQuantity() > 0)
                    startActivity(new Intent(this, CartActivity.class));
                else
                    Toast.makeText(this, "Aumente quantidades para acrescentar itens ao carrinho!", Toast.LENGTH_LONG).show();

                return (true);

            case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                return true;
        }
        return (super.onOptionsItemSelected(item));
    }

    public CategoryFragment loadNewCategoryFragmentInstance(String querySearch) {
        CategoryFragment searchFragment = new CategoryFragment();

        Bundle args = new Bundle();
        args.putString(Constants.QUERY_KEY_STRING, querySearch);
        searchFragment.setArguments(args);

        return searchFragment;
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
        setupCategoryAdapters();
    }

    public void goToCartActivity(View view) {
        if(cart.getItemsQuantity() > 0)
            startActivity(new Intent(this, CartActivity.class));
        else
            Toast.makeText(this, "Aumente quantidades para acrescentar itens ao carrinho!", Toast.LENGTH_LONG).show();
    }
}
