package beer.happy_hour.drinking.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import beer.happy_hour.drinking.Constants;
import beer.happy_hour.drinking.R;
import beer.happy_hour.drinking.activity.SearchTabsActivity;
import beer.happy_hour.drinking.adapter.ListItemAdapter;

/**
 * Created by brcon on 27/03/2017.
 */

public class SearchResultsFragment extends Fragment {
    public Activity activity;

    private ListItemAdapter listItemAdapter;
    private ListView items_list_view;

    private boolean loadedFragment = false;
    private Context context;

    public SearchResultsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

//        if(listItemAdapter == null)
//            listItemAdapter = new ListItemAdapter(getActivity());
//        items_list_view.setAdapter(listItemAdapter);
        setupAdapter();

        return view;
    }

    public ListItemAdapter getAdapter(){
        return listItemAdapter;
    }

    public void setupAdapter(){
        if(getActivity() != null) {
            listItemAdapter = new ListItemAdapter(context);

            listItemAdapter.setPopupShowListener((SearchTabsActivity)activity);

            items_list_view.setAdapter(listItemAdapter);

            listItemAdapter.getFilter().filter("");
        }
    }
}
