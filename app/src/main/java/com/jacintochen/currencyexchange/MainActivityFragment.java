package com.jacintochen.currencyexchange;

import android.app.Activity;
import android.content.Intent;
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

import com.jacintochen.currencyexchange.data.ExchangeContract;

import java.text.DecimalFormat;
import java.text.ParseException;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener {

    private String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private final int GET_EXCHANGE_REQUEST = 1;

    // Views that can change
    private TextView mStart_Currency_Title;
    private TextView mEnd_Currency_Title;
    private TextView mStart_Currency_TextView;
    private TextView mEnd_Currency_TextView;
    private EditText mBank_Rate;
    private EditText mMarket_Rate;
    private TextView mRate_TextView;

    private DecimalFormat mNumberFormatter;
    private boolean decimalPresent;
    private boolean legalRateInput;
    private boolean equalWasPressed;

    // Exchange information for the calculator
    private long mId;
    private String mCurrency_One;
    private String mCurrency_Two;
    private double mBank_Rate_One_To_Two;
    private double mMarket_Rate_One_To_Two;
    private double mBank_Rate_Two_To_One;
    private double mMarket_Rate_Two_To_One;

    // TODO: Need to transfer data from activity result and when the activity just started
    // TODO: See if there is a way to do default values
    // TODO: Need to display the data onto the screen aftered doing the saved state.

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null){
            // TODO: Optimize the default values. Maybe pass it into the preference
            mId = savedInstanceState.getLong(ExchangeContract._ID, -1);
            mCurrency_One = savedInstanceState.getString(ExchangeContract.COLUMN_CURRENCY_ONE, "USD");
            mCurrency_Two = savedInstanceState.getString(ExchangeContract.COLUMN_CURRENCY_TWO, "EURO");
            mBank_Rate_One_To_Two = savedInstanceState.getDouble(ExchangeContract.COLUMN_BANK_RATE_ONE_TO_TWO, 0.80);
            mMarket_Rate_One_To_Two = savedInstanceState.getDouble(ExchangeContract.COLUMN_MARKET_RATE_ONE_TO_TWO, 0.90);
            mBank_Rate_Two_To_One = savedInstanceState.getDouble(ExchangeContract.COLUMN_BANK_RATE_TWO_TO_ONE, 1.05);
            mMarket_Rate_Two_To_One = savedInstanceState.getDouble(ExchangeContract.COLUMN_MARKET_RATE_TWO_TO_ONE, 1.12);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        decimalPresent = false;
        // TODO: Once I finished the savedState, legalRateInput might need to be changed to a different location
        legalRateInput = false;
        equalWasPressed = false;

        // TODO: I might not need this decimalFormat if I am not using it for the start_currency
        mNumberFormatter = new DecimalFormat("#,###.##");

        initializeButtonListener(rootView);

        // Views that can change
        mStart_Currency_Title = (TextView) rootView.findViewById(R.id.start_currency_title);
        mEnd_Currency_Title = (TextView) rootView.findViewById(R.id.end_currency_title);
        mStart_Currency_TextView = (TextView) rootView.findViewById(R.id.start_currency_textview);
        mEnd_Currency_TextView = (TextView) rootView.findViewById(R.id.end_currency_textview);
        mBank_Rate = (EditText) rootView.findViewById(R.id.bank_rate_edittext);
        mMarket_Rate = (EditText) rootView.findViewById(R.id.market_rate_edittext);
        mRate_TextView = (TextView) rootView.findViewById(R.id.exchange_percentage);
        // TODO: Figure out how to remove focus after done is pressed. Probably use a dummy layout that is focusable
        // TODO: Make a helper class so I do not have so much overhead

        mBank_Rate.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                updateRatePercentage();
            }
        });

        mMarket_Rate.addTextChangedListener(new TextWatcher() {
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
                Intent sell = new Intent(getActivity(), SellActivity.class);
                // TODO: more advance would be to take the current currency and put it in the sell fragment
                startActivity(sell);
                return true;
            case R.id.menu_saved:
                Intent saved = new Intent(getActivity(), CurrencyListActivity.class);
                startActivityForResult(saved, GET_EXCHANGE_REQUEST);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putLong(ExchangeContract._ID, mId);
        outState.putString(ExchangeContract.COLUMN_CURRENCY_ONE, mCurrency_One);
        outState.putString(ExchangeContract.COLUMN_CURRENCY_TWO, mCurrency_Two);
        outState.putDouble(ExchangeContract.COLUMN_BANK_RATE_ONE_TO_TWO, mBank_Rate_One_To_Two);
        outState.putDouble(ExchangeContract.COLUMN_MARKET_RATE_ONE_TO_TWO, mMarket_Rate_One_To_Two);
        outState.putDouble(ExchangeContract.COLUMN_BANK_RATE_TWO_TO_ONE, mBank_Rate_Two_To_One);
        outState.putDouble(ExchangeContract.COLUMN_MARKET_RATE_TWO_TO_ONE, mMarket_Rate_Two_To_One);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == GET_EXCHANGE_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                // TODO: Find out what happens when textview text is set to null
                mId = data.getLongExtra(ExchangeContract._ID, -1);
                mCurrency_One = data.getStringExtra(ExchangeContract.COLUMN_CURRENCY_ONE);
                mCurrency_Two = data.getStringExtra(ExchangeContract.COLUMN_CURRENCY_TWO);

                try {
                    // TODO: Should i put default values if an error happens? so that the program will still run?
                    mBank_Rate_One_To_Two = Double.parseDouble(data.getStringExtra(ExchangeContract.COLUMN_BANK_RATE_ONE_TO_TWO));
                    mMarket_Rate_One_To_Two = Double.parseDouble(data.getStringExtra(ExchangeContract.COLUMN_MARKET_RATE_ONE_TO_TWO));
                    mBank_Rate_Two_To_One = Double.parseDouble(data.getStringExtra(ExchangeContract.COLUMN_BANK_RATE_TWO_TO_ONE));
                    mMarket_Rate_Two_To_One = Double.parseDouble(data.getStringExtra(ExchangeContract.COLUMN_MARKET_RATE_TWO_TO_ONE));

                } catch (NumberFormatException numb){
                    Log.v(LOG_TAG, "Failed to parse in onActivityResult. " + numb);
                }

            }
        }
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
                } catch (IllegalArgumentException arg){
                    Log.v(LOG_TAG, "Failed to format end result from equal. " + arg);
                }

            } else {
                // equalWasPressed is false in this case because the user probably wouldn't like
                // to reset what they've enter in the start_currency
                result = "Enter Rate";
            }

            mEnd_Currency_TextView.setText(result);
        }
    }

    // TODO: Change the color or the rate
    public void updateRatePercentage(){
        // Text that appears when either bank or market is empty
        String result = "---";
        int textColor = getResources().getColor(R.color.blue);
        legalRateInput = false;

        String bank = mBank_Rate.getText().toString();
        String market = mMarket_Rate.getText().toString();

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
            } catch (IllegalArgumentException arg){
                Log.v(LOG_TAG, "Failed to format the percentage from updateRatePercentage. " + arg);
            }
        }

        // Set text and textColor to exchange_percentage textview
        mRate_TextView.setText(result);
        mRate_TextView.setTextColor(textColor);

    }

}
