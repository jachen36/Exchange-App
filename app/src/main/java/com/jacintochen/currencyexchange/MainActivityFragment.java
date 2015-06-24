package com.jacintochen.currencyexchange;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private TextView mstart_currency_textView;
    private TextView mend_currency_textView;
    private DecimalFormat mnumberFormatter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mstart_currency_textView = (TextView) rootView.findViewById(R.id.start_currency_textview);
        mend_currency_textView = (TextView) rootView.findViewById(R.id.end_currency_textview);

        //TODO: Allow user to add more than 2 decimal points
        mnumberFormatter = new DecimalFormat("#,###.##");

        return rootView;
    }


    public void addEight(View v){
        appendNumberHelper("8");
    }

    public void appendNumberHelper(String num){
        String number = mstart_currency_textView.getText().toString();
        String result = null;
        try {
            number = Double.toString(mnumberFormatter.parse(number).doubleValue());
            number += num;
            Double d = Double.parseDouble(number);

            result = mnumberFormatter.format(d);
        } catch (Exception ex){
            Log.v(LOG_TAG, "The parsing caused a problem!");
            return;
        }

        if (result != null){
            mstart_currency_textView.setText(result);
        }
    }

}
