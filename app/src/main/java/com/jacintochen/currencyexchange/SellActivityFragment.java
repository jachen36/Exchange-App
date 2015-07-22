package com.jacintochen.currencyexchange;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.ParseException;


/**
 * A placeholder fragment containing a simple view.
 */
public class SellActivityFragment extends Fragment {
    private final String LOG_TAG = SellActivityFragment.class.getSimpleName();

    private EditText mCurrency_One;
    private EditText mBuy_Rate;
    private EditText mSell_Rate;
    private TextView mCurrency_Two;
    private TextView mCost;

    public SellActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sell, container, false);

        mCurrency_One = (EditText) rootView.findViewById(R.id.currency_1_edittext);
        mBuy_Rate = (EditText) rootView.findViewById(R.id.buy_rate_edittext);
        mSell_Rate = (EditText) rootView.findViewById(R.id.sell_rate_edittext);
        mCurrency_Two = (TextView) rootView.findViewById(R.id.currency_2_textview);
        mCost = (TextView) rootView.findViewById(R.id.cost);

        Button calculate = (Button) rootView.findViewById(R.id.sell_calculate_button);
        calculate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                calculateResult();
            }
        });

        return rootView;
    }


    private void calculateResult(){
        String currency_one = mCurrency_One.getText().toString();
        String buy_rate = mBuy_Rate.getText().toString();
        String sell_rate = mSell_Rate.getText().toString();
        boolean oneIsLegal;
        boolean buyIsLegal;
        boolean sellIsLegal;

        oneIsLegal = isLegal(mCurrency_One, currency_one, getString(R.string.sell_foreign_error));
        buyIsLegal = isLegal(mBuy_Rate, buy_rate, getString(R.string.sell_buy_error));
        sellIsLegal = isLegal(mSell_Rate, sell_rate, getString(R.string.sell_sell_error));

        if (oneIsLegal && buyIsLegal && sellIsLegal){
            // TODO: the parsing formatter needs to be optimized
            DecimalFormat formatter = new DecimalFormat("#.########");
            try {
                Double dCurrency_One = formatter.parse(currency_one).doubleValue();
                Double dBuy_Rate = formatter.parse(buy_rate).doubleValue();
                Double dSell_Rate = formatter.parse(sell_rate).doubleValue();

                Double dCurrency_Two = dCurrency_One/dSell_Rate;
                Double dCost = dCurrency_Two - (dCurrency_One/dBuy_Rate);

                formatter.applyPattern("#,###.00");
                mCurrency_Two.setText(formatter.format(dCurrency_Two));
                mCost.setText(formatter.format(dCost));

            } catch (ParseException parse){
                Log.v(LOG_TAG, "Parsing error when calculating sell cost");
            } catch (IllegalArgumentException arg){
                Log.v(LOG_TAG, "Failed to format calculated result");
            }
        } else {
            mCost.setText(getString(R.string.temp_zero));
            mCurrency_Two.setText(getString(R.string.temp_zero));
        }
    }

    private boolean isLegal(EditText edit, String text, String error){

        if (text.length() == 0 || text == "." || text == "0"){
            edit.setError(error);
            return false;
        }
        // TODO: Got to undo the error
        return true;
    }
}
