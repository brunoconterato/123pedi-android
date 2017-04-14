package beer.happy_hour.drinking.fragment;

/**
 * Created by brcon on 24/03/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import beer.happy_hour.drinking.Constants;
import beer.happy_hour.drinking.R;
import beer.happy_hour.drinking.activity.BriefActivity;
import beer.happy_hour.drinking.activity.SearchTabsActivity;
import beer.happy_hour.drinking.adapter.ListItemAdapter;


public class CategoryFragment extends Fragment {
    public Activity activity;
    public Context context;

    private String searchQuery;

    private ListItemAdapter listItemAdapter;
    private ListView items_list_view;

    private boolean loadedFragment = false;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchQuery = getArguments().getString(Constants.QUERY_KEY_STRING);

        listItemAdapter = new ListItemAdapter(getActivity());
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        this.context = context;
        if (context instanceof Activity){
            activity = (Activity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_search, container, false);
        items_list_view = (ListView) view.findViewById(R.id.items_fragment_list_view);

        if(listItemAdapter == null)
            listItemAdapter = new ListItemAdapter(getActivity());

        setupAdapter();

        listItemAdapter.getFilter().filter(searchQuery);
        return view;
    }

    public void setupAdapter(){
        if(getActivity() != null) {
            listItemAdapter = new ListItemAdapter(context);

            listItemAdapter.setPopupShowListener((SearchTabsActivity)activity);

            items_list_view.setAdapter(listItemAdapter);
            listItemAdapter.getFilter().filter(searchQuery);
        }
    }

    public ListItemAdapter getAdapter(){
        return listItemAdapter;
    }
}