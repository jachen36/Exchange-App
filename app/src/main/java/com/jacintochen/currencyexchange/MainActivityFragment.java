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
public class MainActivityFragment extends Fragment implements View.OnClickListener {

    private String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private TextView mStart_Currency_TextView;
    private TextView mEnd_Currency_TextView;
    private DecimalFormat mNumberFormatter;
    private boolean decimalPresent;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mStart_Currency_TextView = (TextView) rootView.findViewById(R.id.start_currency_textview);
        mEnd_Currency_TextView = (TextView) rootView.findViewById(R.id.end_currency_textview);
        decimalPresent = false;

        //TODO: Allow user to add more than 2 decimal points
        mNumberFormatter = new DecimalFormat("#,###.##");

        initializeButtonListener(rootView);

        return rootView;
    }

    public void initializeButtonListener(View v){
        // Find the closest view to the buttons to increase performance
        View btn_parent = v.findViewById(R.id.button_LinearLayout);

        // btn is reused to reduce memory and increase performance
//        Button btn = (Button) btn_parent.findViewById(R.id.button_seven);
//        btn.setOnClickListener(this);
//        btn = (Button) btn_parent.findViewById(R.id.button_clear);
//        btn.setOnClickListener(this);

        // Less overhead but probably harder to read
        btn_parent.findViewById(R.id.button_one).setOnClickListener(this);
        btn_parent.findViewById(R.id.button_two).setOnClickListener(this);
        btn_parent.findViewById(R.id.button_three).setOnClickListener(this);
        btn_parent.findViewById(R.id.button_four).setOnClickListener(this);
        btn_parent.findViewById(R.id.button_five).setOnClickListener(this);
        btn_parent.findViewById(R.id.button_six).setOnClickListener(this);
        btn_parent.findViewById(R.id.button_seven).setOnClickListener(this);
        btn_parent.findViewById(R.id.button_eight).setOnClickListener(this);
        btn_parent.findViewById(R.id.button_nine).setOnClickListener(this);
        btn_parent.findViewById(R.id.button_zero).setOnClickListener(this);
        btn_parent.findViewById(R.id.button_decimal).setOnClickListener(this);
        btn_parent.findViewById(R.id.button_clear).setOnClickListener(this);
        btn_parent.findViewById(R.id.button_back).setOnClickListener(this);
        btn_parent.findViewById(R.id.button_switch).setOnClickListener(this);
        btn_parent.findViewById(R.id.button_equal).setOnClickListener(this);
    }


    public void onClick(View v){
        // TODO: Increase efficiency by using if statement that separate normal number from special operation. Instead of switch
        switch(v.getId()){
            case (R.id.button_one):
                insert("1");
                break;
            case (R.id.button_two):
                insert("2");
                break;
            case (R.id.button_three):
                insert("3");
                break;
            case (R.id.button_four):
                insert("4");
                break;
            case (R.id.button_five):
                insert("5");
                break;
            case (R.id.button_six):
                insert("6");
                break;
            case (R.id.button_seven):
                insert("7");
                break;
            case (R.id.button_eight):
                insert("8");
                break;
            case (R.id.button_nine):
                insert("9");
                break;
            case (R.id.button_zero):
                insert("0");
                break;
            case (R.id.button_decimal):
                if (!decimalPresent){
                    insert("A");
                    decimalPresent = true;
                }
                break;
            case (R.id.button_clear):
                clear();
                break;
            default:
                Log.v(LOG_TAG, "The button was not recognized on the onClick function. Button id " + v.getId());
                throw new UnsupportedOperationException("Button not supported");
        }
    }


    public void clear(){
        mStart_Currency_TextView.setText("0");
        mEnd_Currency_TextView.setText("0");
        decimalPresent = false;
    }

    public void insert(String num){
        // TODO: Have to remove the leading zero when it starts
        mStart_Currency_TextView.setText(mStart_Currency_TextView.getText() + num);
    }


    public void appendNumberHelper(String num){
        String number = mStart_Currency_TextView.getText().toString();
        String result;
        try {
            number = Double.toString(mNumberFormatter.parse(number).doubleValue());
            number += num;
            Double d = Double.parseDouble(number);

            result = mNumberFormatter.format(d);
        } catch (Exception ex){
            Log.v(LOG_TAG, "The parsing caused a problem!");
            return;
        }

        if (result != null){
            mStart_Currency_TextView.setText(result);
        }
    }

}
