package com.jacintochen.currencyexchange;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jacintochen.currencyexchange.data.ExchangeContract;


/**
 * A placeholder fragment containing a simple view.
 */
public class CurrencyListActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    //TODO: Add the add feature to this activity as well
    // TODO: Figure out what happens when the activity stop while the loader is finishing up.
    // TODO: Figure out what to do when there are zero entry on the list. what happens to the calculator

    private int LOADER_ID = 0;
    private RecyclerView mRecyclerView;
    private CurrencyListAdapter mAdapter;

    // TODO: Need to finish the projection
    private final String[] projection = {
            ExchangeContract._ID,
            ExchangeContract.COLUMN_CURRENCY_ONE,
            ExchangeContract.COLUMN_CURRENCY_TWO,
            ExchangeContract.COLUMN_BANK_RATE_ONE_TO_TWO,
            ExchangeContract.COLUMN_MARKET_RATE_ONE_TO_TWO,
            ExchangeContract.COLUMN_BANK_RATE_TWO_TO_ONE,
            ExchangeContract.COLUMN_MARKET_RATE_TWO_TO_ONE
    };

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

        // Allow user to add new exchange by starting the Add Activity
        Button add_exchange = (Button) root.findViewById(R.id.list_add_exchange_button);
        add_exchange.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent add_intent = new Intent(getActivity(), AddActivity.class);
                startActivity(add_intent);
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        getLoaderManager().initLoader(LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        return new CursorLoader(getActivity(),
                ExchangeContract.CONTENT_URI,
                projection, null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor){
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        mAdapter.swapCursor(null);
    }
}