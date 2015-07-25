package com.jacintochen.currencyexchange;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jacintochen.currencyexchange.data.ExchangeContract;


/**
 * A placeholder fragment containing a simple view.
 */
public class AddActivityFragment extends Fragment implements View.OnClickListener {

    private EditText mCurrency_One;
    private EditText mCurrency_Two;
    private EditText mBank_rate;
    private EditText mMarket_rate;

    public AddActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add, container, false);

        mCurrency_One = (EditText) rootView.findViewById(R.id.add_currency_1_edittext);
        mCurrency_Two = (EditText) rootView.findViewById(R.id.add_currency_2_edittext);
        mBank_rate = (EditText) rootView.findViewById(R.id.add_bank_rate_edittext);
        mMarket_rate = (EditText) rootView.findViewById(R.id.add_market_rate_edittext);

        Button submit = (Button) rootView.findViewById(R.id.add_submit_button);
        Button cancel = (Button) rootView.findViewById(R.id.add_cancel_button);

        submit.setOnClickListener(this);
        cancel.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.add_submit_button:
                save();
                break;
            case R.id.add_cancel_button:
                // Exit current activity
                getActivity().finish();
                break;
        }
    }

    private void save(){
        String currency_one = mCurrency_One.getText().toString();
        String currency_two = mCurrency_Two.getText().toString();
        String one_to_two_bank = mBank_rate.getText().toString();
        String one_to_two_market = mMarket_rate.getText().toString();

        // Since they are unseen until the calculator. Default is zero
        String two_to_one_bank = "0";
        String two_to_one_market = "0";

        if (isLegal(currency_one, currency_two, one_to_two_bank, one_to_two_market)){
            ContentValues values = new ContentValues();
            values.put(ExchangeContract.COLUMN_CURRENCY_ONE, currency_one);
            values.put(ExchangeContract.COLUMN_CURRENCY_TWO, currency_two);
            values.put(ExchangeContract.COLUMN_BANK_RATE_ONE_TO_TWO, one_to_two_bank);
            values.put(ExchangeContract.COLUMN_MARKET_RATE_ONE_TO_TWO,one_to_two_market);
            values.put(ExchangeContract.COLUMN_BANK_RATE_TWO_TO_ONE, two_to_one_bank);
            values.put(ExchangeContract.COLUMN_MARKET_RATE_TWO_TO_ONE, two_to_one_market);

            getActivity().getContentResolver().insert(ExchangeContract.CONTENT_URI, values);

            // Display a message to let user know the saved was successful
            Toast.makeText(getActivity(),
                    getString(R.string.add_saved_message),
                    Toast.LENGTH_SHORT).show();

            // The activity exit itself when the saved was successful
            getActivity().finish();
        }
    }

    private boolean isLegal(String one, String two, String bank, String market){
        boolean result = true;

        if (one.length() == 0){
            result = false;
            mCurrency_One.setError(getString(R.string.add_currency_error));
        }

        if (two.length() == 0){
            result = false;
            mCurrency_Two.setError(getString(R.string.add_currency_error));
        }

        if (bank.length() == 0 || bank.equals(".")){
            result = false;
            mBank_rate.setError(getString(R.string.add_bank_error));
        }

        if (market.length() == 0 || market.equals(".")){
            result = false;
            mMarket_rate.setError(getString(R.string.add_market_error));
        }

        return result;
    }

}
