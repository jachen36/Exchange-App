package com.jacintochen.currencyexchange;

import android.net.ParseException;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
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

        EditText bank_rate = (EditText) rootView.findViewById(R.id.bank_rate_edittext);
        bank_rate.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count){}
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            public void afterTextChanged(Editable s){
                updateRatePercentage();
            }
        });

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
                    insertDot();
                }
                break;
            case (R.id.button_clear):
                clear();
                break;
            case (R.id.button_back):
                backspace();
                break;
            // TODO: Finish function for equal and switch
            case (R.id.button_switch):
                break;
            case (R.id.button_equal):
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

    public void backspace(){
        CharSequence textView = mStart_Currency_TextView.getText();
        int length = textView.length();
        if (length == 1){
            mStart_Currency_TextView.setText("0");
        } else {
            mStart_Currency_TextView.setText(textView.subSequence(0, length-1));
            if (textView.charAt(length-1) == '.'){
                decimalPresent = false;
            }
        }
    }

    // Insert the number pressed
    public void insert(String num){
        String textView = mStart_Currency_TextView.getText().toString();
        if (textView.length() == 1 && textView.equals("0")){
            mStart_Currency_TextView.setText(num);
        } else {
            mStart_Currency_TextView.setText(mStart_Currency_TextView.getText() + num);
        }
    }

    // Inserts the decimal point and change decimalPresent to true
    public void insertDot(){
        decimalPresent = true;
        mStart_Currency_TextView.setText(mStart_Currency_TextView.getText() + ".");
    }

    public void updateRatePercentage(){
        EditText bank_rate = (EditText) getActivity().findViewById(R.id.bank_rate_edittext);
        EditText market_rate = (EditText) getActivity().findViewById(R.id.market_rate_edittext);

        String bank = bank_rate.getText().toString();
        String market = market_rate.getText().toString();

        // Make sure that the input is correct before parsing
        if (bank.length() == 0 || market.length() == 0){
            return;
        } else if (bank.equals(".") || market.equals(".")){
            return;
        }


        try {
            // TODO: I need to understand the pattern a little more.
            DecimalFormat rate_formatter = new DecimalFormat("@######");
            Double bank_double = rate_formatter.parse(bank).doubleValue();
            Double market_double = rate_formatter.parse(market).doubleValue();

            // Make sure that app is not dividing by zero by exiting function
            if (bank_double == 0 || market_double == 0){
                return;
            }

            // TODO: finish function by calculating percentage. Need a different Decimal Formatter. Maybe max two sig fig?



        } catch (ParseException par) {
            Log.v(LOG_TAG, "Parsing error. " + par);
        } catch (Exception ex){
            // TODO: The exception is too generic. Change it more specific
            Log.v(LOG_TAG, "An error with updateRatePercentage. " + ex);
        }

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
