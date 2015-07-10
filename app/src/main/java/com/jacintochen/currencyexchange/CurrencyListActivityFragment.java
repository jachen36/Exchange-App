package com.jacintochen.currencyexchange;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A placeholder fragment containing a simple view.
 */
public class CurrencyListActivityFragment extends Fragment {
    //TODO: Add the add feature to this activity as well
    RecyclerView mRecyclerView;
    CurrencyListAdapter mAdapter;


    public CurrencyListActivityFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mAdapter = new CurrencyListAdapter();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_currency_list, container, false);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler);
        mRecyclerView.setAdapter(mAdapter);
        // This optimizes creating the cards. Change if not true
        mRecyclerView.setHasFixedSize(true);
        // TODO: Figure out why layout manager is set dynamically
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return root;
    }
}
