package com.jacintochen.currencyexchange;

import android.content.Intent;
import android.net.ParseException;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
    private EditText mBank_Rate;
    private DecimalFormat mNumberFormatter;
    private boolean decimalPresent;
    private boolean legalRateInput;
    private boolean equalWasPressed;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mStart_Currency_TextView = (TextView) rootView.findViewById(R.id.start_currency_textview);
        mEnd_Currency_TextView = (TextView) rootView.findViewById(R.id.end_currency_textview);
        decimalPresent = false;
        // TODO: Once I finished the savedState, legalRateInput might need to be changed to a different location
        legalRateInput = false;
        equalWasPressed = false;

        // TODO: I might not need this decimalFormat if I am not using it for the start_currency
        mNumberFormatter = new DecimalFormat("#,###.##");

        initializeButtonListener(rootView);

        // TODO: Figure out how to remove focus after done is pressed. Probably use a dummy layout that is focusable
        mBank_Rate = (EditText) rootView.findViewById(R.id.bank_rate_edittext);
        mBank_Rate.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                updateRatePercentage();
            }
        });

        EditText market_rate = (EditText) rootView.findViewById(R.id.market_rate_edittext);
        market_rate.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.menu_sell:
                Intent intent = new Intent(getActivity(), SellActivity.class);
                // TODO: more advance would be to take the current currency and put it in the sell fragment
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
            // TODO: Finish function for switch
            case (R.id.button_switch):
                break;
            case (R.id.button_equal):
                equal();
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
        equalWasPressed = false;
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
        // After equal has been pressed, the next button should reset the calculator
        if (equalWasPressed){clear();}

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

    public void equal(){
        if (!equalWasPressed){
            String result = "ERROR";

            if (legalRateInput){
                try {
                    DecimalFormat formatter = new DecimalFormat("#.######");

                    Double start = formatter.parse(mStart_Currency_TextView.getText().toString()).doubleValue();
                    Double rate = formatter.parse(mBank_Rate.getText().toString()).doubleValue();
                    Double end = start * rate;

                    // TODO: Need to make a tester that prevent string that are too large for screen or change the decimal to use E
                    formatter.applyPattern("#,###.##");
                    result = formatter.format(end);
                    equalWasPressed = true;

                } catch (ParseException par){
                    Log.v(LOG_TAG, "Parsing error during calculating the end_currency. " + par);
                } catch (Exception ex){
                    // TODO: The equal exception is too generic. Change it more specific
                    Log.v(LOG_TAG, "An error with equal. " + ex);
                }

            } else {
                // equalWasPressed is false in this case because the user probably wouldn't like
                // to reset what they've enter in the start_currency
                result = "Enter Rate";
            }

            mEnd_Currency_TextView.setText(result);
        }
    }

    public void updateRatePercentage(){
        // Text that appears when either bank or market is empty
        String result = "---";
        int textColor = getResources().getColor(R.color.blue);
        legalRateInput = false;

        EditText market_rate = (EditText) getActivity().findViewById(R.id.market_rate_edittext);
        TextView rate_textView = (TextView) getActivity().findViewById(R.id.exchange_percentage);

        String bank = mBank_Rate.getText().toString();
        String market = market_rate.getText().toString();

        // Make sure there are no illegal input such as empty or just a decimal point
        if (bank.length() == 0 || market.length() == 0){

        } else if (bank.equals(".") || market.equals(".")){

        } else {

            try {
                DecimalFormat rate_formatter = new DecimalFormat("#.######");
                Double bank_double = rate_formatter.parse(bank).doubleValue();
                Double market_double = rate_formatter.parse(market).doubleValue();

                // Make sure both numbers are greater than zero
                if (bank_double > 0 && market_double > 0){
                    // Calculate the percent difference in the exchange rate between bank and market
                    Double rate_difference = (bank_double/market_double) - 1;

                    // Determine the text color
                    if (rate_difference >= 0 ){
                        textColor = getResources().getColor(R.color.green);
                    } else if (rate_difference < 0){
                        textColor = getResources().getColor(R.color.red);
                    }

                    // Set maximum significant digits to 2 and minimum to 1 while converting it to percent
                    rate_formatter.applyPattern("@#%");

                    // TODO: find a way to break out of the try once high and low is determine.
                    if (rate_difference > 0.994){
                        result = "HIGH";
                    } else if (rate_difference < -0.994){
                        result = "LOW";
                    } else {
                        // Set maximum significant digits to 2 and minimum to 1 while converting it to percent
                        rate_formatter.applyPattern("@#%");

                        result = rate_formatter.format(rate_difference);
                    }
                    legalRateInput = true;
                }

            } catch (ParseException par) {
                Log.v(LOG_TAG, "Parsing error. " + par);
            } catch (Exception ex){
                // TODO: The exception is too generic. Change it more specific
                Log.v(LOG_TAG, "An error with updateRatePercentage. " + ex);
            }
        }

        // Set text and textColor to exchange_percentage textview
        rate_textView.setText(result);
        rate_textView.setTextColor(textColor);

    }

}
