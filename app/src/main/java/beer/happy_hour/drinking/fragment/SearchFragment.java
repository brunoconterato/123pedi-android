package beer.happy_hour.drinking.fragment;

/**
 * Created by brcon on 24/03/2017.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import beer.happy_hour.drinking.Constants;
import beer.happy_hour.drinking.R;
import beer.happy_hour.drinking.activity.SearchTabsActivity;
import beer.happy_hour.drinking.adapter.ListItemAdapter;


public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {

    private String searchQuery;

    private ListItemAdapter listItemAdapter;
    private ListView mListView;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listItemAdapter = new ListItemAdapter(getActivity());
        listItemAdapter.getFilter().filter(searchQuery);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();

        searchQuery = getArguments().getString(Constants.QUERY_KEY_STRING, Constants.SEARCH_CATEGORY_HASH + "alcoolicos");

        View view =  lf.inflate(R.layout.fragment_search, container, false);
        mListView = (ListView) view.findViewById(R.id.items_fragment_list_view);
        mListView.setAdapter(listItemAdapter);

        listItemAdapter = new ListItemAdapter(getActivity());
        listItemAdapter.getFilter().filter(searchQuery);

        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newQuery) {
        Log.d("Entrou:", "onQueryTextChange");

        SearchTabsActivity activity = (SearchTabsActivity) getActivity();
        activity.changeTab(4);

        listItemAdapter.getFilter().filter(newQuery);
        mListView.setAdapter(listItemAdapter);

        return true;
    }
}