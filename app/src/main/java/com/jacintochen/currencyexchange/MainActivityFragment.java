package com.jacintochen.currencyexchange;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
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

    // Logistic
    private DecimalFormat mNumberFormatter;
    private boolean decimalPresent;
    private boolean legalRateInput;
    private boolean equalWasPressed;
    // TODO: Check if there are unnecessary change to rateWasChange after adding enableUpdate
    private boolean rateWasChanged;
    private boolean enableUpdate;
    // TODO: Check if maximum is reasonable
    // TODO: Use Integer value from values folder
    private int maximum_number_length = 25;
    private int maximum_decimal = 2;

    // Exchange data for the calculator
    private SharedPreferences mPref;
    private String mCurrency_Position;
    private long mId;
    private String mCurrency_One;
    private String mCurrency_Two;
    private String mBank_Rate_One_To_Two;
    private String mMarket_Rate_One_To_Two;
    private String mBank_Rate_Two_To_One;
    private String mMarket_Rate_Two_To_One;

    // TODO: See if there is a way to do default values
    // TODO: need to go back and find out if I used Double or double

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "onCreate called");
        mPref = getActivity().getSharedPreferences(getString(R.string.main_pref_values), Context.MODE_PRIVATE);
        updateValues();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        decimalPresent = false;
        equalWasPressed = false;

        // TODO: I might not need this decimalFormat if I am not using it for the start_currency
        mNumberFormatter = new DecimalFormat();
        // TODO: Double check if maximum numbers are reasonable
        mNumberFormatter.setMaximumFractionDigits(maximum_decimal);
        mNumberFormatter.setGroupingSize(3);


        // Views that can change
        mStart_Currency_Title = (TextView) rootView.findViewById(R.id.start_currency_title);
        mEnd_Currency_Title = (TextView) rootView.findViewById(R.id.end_currency_title);
        mStart_Currency_TextView = (TextView) rootView.findViewById(R.id.start_currency_textview);
        mEnd_Currency_TextView = (TextView) rootView.findViewById(R.id.end_currency_textview);
        mBank_Rate = (EditText) rootView.findViewById(R.id.bank_rate_edittext);
        mMarket_Rate = (EditText) rootView.findViewById(R.id.market_rate_edittext);
        mRate_TextView = (TextView) rootView.findViewById(R.id.exchange_percentage);

        // Calling refreshView before the listener prevent unnecessary calling
        // updateRatePercentage multiple times.
        refreshView();
        mBank_Rate.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (enableUpdate) {
                    updateRatePercentage();
                    updateBankRate();
                }
            }
        });
        mMarket_Rate.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (enableUpdate) {
                    updateRatePercentage();
                    updateMarketRate();
                }
            }
        });

        initializeButtonListener(rootView);
        return rootView;
    }

    // This active the number and function dial to the calculator
    private void initializeButtonListener(View v){
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

    // Refresh the view when changing exchange or switching between currency
    private void refreshView(){
        // Prevent edittext view from updating values
        enableUpdate = false;
        Log.v(LOG_TAG, "refreshView called");

        if (mCurrency_Position.equals(getString(R.string.position_one))){
            refreshViewHelper(mStart_Currency_Title, mCurrency_One);
            refreshViewHelper(mEnd_Currency_Title, mCurrency_Two);
            Log.v(LOG_TAG, "Bank rate set_ONE");
            mBank_Rate.setText(mBank_Rate_One_To_Two);
            Log.v(LOG_TAG, "Market rate set_ONE");
            mMarket_Rate.setText(mMarket_Rate_One_To_Two);
        }
        else if (mCurrency_Position.equals(getString(R.string.position_two))){
            refreshViewHelper(mStart_Currency_Title, mCurrency_Two);
            refreshViewHelper(mEnd_Currency_Title, mCurrency_One);
            Log.v(LOG_TAG, "Bank rate set_TWO");
            mBank_Rate.setText(mBank_Rate_Two_To_One);
            Log.v(LOG_TAG, "Market rate set_TWO");
            mMarket_Rate.setText(mMarket_Rate_Two_To_One);
        }
        // Allow edittext view to detect user input now
        enableUpdate = true;

        // Call updateRatePercentage once both bank and market are set
        updateRatePercentage();
    }

    // Set the currency title and determine text size
    private void refreshViewHelper(TextView textView, String title){
        if (title.length() <= 4){
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        } else if (title.length() <= 6){
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        } else {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        }
        textView.setText(title);
    }

    // Update the exchange data member variables for the calculator
    private void updateValues(){
        // Reset rateWasChange to false because user has yet to change the data
        rateWasChanged = false;

        // If data was not initialized, default value is set
        mId = mPref.getLong(ExchangeContract._ID, -1);

        mCurrency_Position = mPref.getString(getString(R.string.mcurrency_position),
                getString(R.string.currency_position_default));
        mCurrency_One = mPref.getString(ExchangeContract.COLUMN_CURRENCY_ONE,
                getString(R.string.currency_one_default));
        mCurrency_Two = mPref.getString(ExchangeContract.COLUMN_CURRENCY_TWO,
                getString(R.string.currency_two_default));
        mBank_Rate_One_To_Two = mPref.getString(ExchangeContract.COLUMN_BANK_RATE_ONE_TO_TWO,
                getString(R.string.bank_rate_one_to_two_default));
        mMarket_Rate_One_To_Two = mPref.getString(ExchangeContract.COLUMN_MARKET_RATE_ONE_TO_TWO,
                getString(R.string.market_rate_one_to_two_default));
        mBank_Rate_Two_To_One = mPref.getString(ExchangeContract.COLUMN_BANK_RATE_TWO_TO_ONE,
                getString(R.string.bank_rate_two_to_one_default));
        mMarket_Rate_Two_To_One = mPref.getString(ExchangeContract.COLUMN_MARKET_RATE_TWO_TO_ONE,
                getString(R.string.market_rate_two_to_one_default));

        printLog("updateValues");

    }

    private void printLog(String function){
        Log.v(LOG_TAG, "At "+ function +
                " = rateChanged: " + rateWasChanged +
                ", position: " + mCurrency_Position +
                ", one: " + mCurrency_One +
                ", two: " + mCurrency_Two +
                ", bank1: " + mBank_Rate_One_To_Two +
                ", market1: " + mMarket_Rate_One_To_Two +
                ", bank2: " + mBank_Rate_Two_To_One +
                ", market2: " + mMarket_Rate_Two_To_One);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == GET_EXCHANGE_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                // TODO: Find out what happens when textview text is set to null
                // TODO: Might have to change this back to intent instead because saving is going to be a pain.
                Log.v(LOG_TAG, "At onActivityResult, next cals are updateValues and refreshView");
                updateValues();
                refreshView();
            }
        }
    }

//    TODO: When making swtich make sure to save the currency_position
//    public switch


    @Override
    public void onPause(){
        super.onPause();
        Log.v(LOG_TAG, "onPaused was called");
        if (rateWasChanged){
            if (mId > -1){
                // TODO: Should this go into a loader/asynctask? No, because 2nd activity much distrube it.
                SharedPreferences.Editor editor = mPref.edit();
                editor.putString(ExchangeContract.COLUMN_BANK_RATE_ONE_TO_TWO,
                        mBank_Rate_One_To_Two);
                editor.putString(ExchangeContract.COLUMN_MARKET_RATE_ONE_TO_TWO,
                        mMarket_Rate_One_To_Two);
                editor.putString(ExchangeContract.COLUMN_BANK_RATE_TWO_TO_ONE,
                        mBank_Rate_Two_To_One);
                editor.putString(ExchangeContract.COLUMN_MARKET_RATE_TWO_TO_ONE,
                        mMarket_Rate_Two_To_One);
                editor.apply();

                // TODO: Check what happens when I make 1 legal change at bank then 1 illegal change to market. see if it will save it to database

                printLog("onPause saved to preference");

                // Update Exchange data in the database
                ContentValues values = new ContentValues();
                values.put(ExchangeContract.COLUMN_BANK_RATE_ONE_TO_TWO, mBank_Rate_One_To_Two);
                values.put(ExchangeContract.COLUMN_MARKET_RATE_ONE_TO_TWO, mMarket_Rate_One_To_Two);
                values.put(ExchangeContract.COLUMN_BANK_RATE_TWO_TO_ONE, mBank_Rate_Two_To_One);
                values.put(ExchangeContract.COLUMN_MARKET_RATE_TWO_TO_ONE, mMarket_Rate_Two_To_One);

                getActivity().getContentResolver().update(
                        ExchangeContract.buildExchangeUri(mId),
                        values, null, null);

                // New changes has been saved so reset rateWasChanged to false
                rateWasChanged = false;
            }
        }
    }

    @Override
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
                switchExchange();
                // Reset the calculator each time to switch
                clear();
                break;
            case (R.id.button_equal):
                equal();
                break;
            default:
                Log.e(LOG_TAG, "The button was not recognized on the onClick function. Button id " + v.getId());
                throw new UnsupportedOperationException("Button not supported");
        }
    }

    // Swap the start and end currency
    private void switchExchange(){
        if (mCurrency_Position.equals(getString(R.string.position_two))){
            mCurrency_Position = getString(R.string.position_one);
        } else {
            mCurrency_Position = getString(R.string.position_two);
        }
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(getString(R.string.mcurrency_position), mCurrency_Position);
        refreshView();
    }

    // Return both mStart_currency_textview and mEnd_Currency_textview to zero
    private void clear(){
        mStart_Currency_TextView.setText("0");
        mEnd_Currency_TextView.setText("0");
        decimalPresent = false;
        equalWasPressed = false;
    }

    // Remove the last number from mStart_Currency_textview
    // TODO: Backspace does not reset comma placement
    private void backspace(){
        CharSequence textView = mStart_Currency_TextView.getText();
        int length = textView.length();
        if (length == 1){
            mStart_Currency_TextView.setText("0");
        } else if (decimalPresent) {
            mStart_Currency_TextView.setText(textView.subSequence(0, length-1));
            if (textView.charAt(length-1) == '.'){
                decimalPresent = false;
            }
        } else {
            String temp = textView.subSequence(0, length-1).toString();
            temp = temp.replace(",", "");
            try {
                mStart_Currency_TextView.setText(mNumberFormatter.format(Long.parseLong(temp)));
            } catch (NumberFormatException numb){
                Log.e(LOG_TAG, "Error parsing string in function backspace. " + numb);
            }

        }
    }

    // Insert the number pressed
    private void insert(String num){
        // After equal has been pressed, the next button should reset the calculator
        if (equalWasPressed){clear();}

        String textView = mStart_Currency_TextView.getText().toString();
        // When number reaches max length, no more number will be added
        if (textView.length() >= maximum_number_length){return;}

        if (textView.length() == 1 && textView.equals("0")){
            textView = num;
        } else if (decimalPresent){
            textView += num;
        } else {
            textView = textView.replace(",", "");
            textView = textView + num;
            try {
                textView = mNumberFormatter.format(Long.parseLong(textView));
            } catch (NumberFormatException numb){
                Log.e(LOG_TAG, "Error in function insert. Parsing error: " + num);
            }
        }
        mStart_Currency_TextView.setText(textView);
    }

    // Inserts the decimal point and change decimalPresent to true
    private void insertDot(){
        decimalPresent = true;
        mStart_Currency_TextView.setText(mStart_Currency_TextView.getText() + ".");
    }

    private void equal(){
        if (!equalWasPressed){
            String result = "Enter Rate";
            // equalWasPressed stay false if this function failed to display an output.
            // because User probably wouldn't like to retype number

            if (legalRateInput){
                try {
                    String start_string = mStart_Currency_TextView.getText().toString().replace(",", "");
                    double start = Double.parseDouble(start_string);
                    float rate = Float.parseFloat(mBank_Rate.getText().toString());
                    double end = start * rate;

                    // TODO: Need to make a tester that prevent string that are too large for screen or change the decimal to use E
                    // TODO: Change size of text when too large!
                    result = mNumberFormatter.format(end);
                    equalWasPressed = true;

                } catch (NumberFormatException numb){
                    Log.e(LOG_TAG, "Parsing error in function equal. " + numb);
                }
            }
            mEnd_Currency_TextView.setText(result);
        }
    }

    // TODO: Change the color or the rate
    private void updateRatePercentage(){
        Log.v(LOG_TAG, "updateRatePercentage called");

        // Text that appears when either bank or market is empty
        // TODO: Maybe I need a fool proof thing where if text was enter it will fail.
        String result = "---";
        int textColor = getResources().getColor(R.color.light_grey);
        legalRateInput = false;

        String bank = mBank_Rate.getText().toString();
        String market = mMarket_Rate.getText().toString();

        // Make sure there are no illegal input such as empty or just a decimal point
        if (bank.length() == 0 || market.length() == 0){

        } else if (bank.equals(".") || market.equals(".")){

        } else {

            try {
                float bank_float = Float.parseFloat(bank);
                float market_float = Float.parseFloat(market);

                // Make sure both numbers are greater than zero
                if (bank_float > 0 && market_float > 0){
                    // Calculate the percent difference in the exchange rate between bank and market
                    float rate_difference = (bank_float/market_float) - 1;

                    // Determine the text color
                    if (rate_difference >= 0 ){
                        textColor = getResources().getColor(R.color.green);
                    } else if (rate_difference < 0){
                        textColor = getResources().getColor(R.color.red);
                    }

                    // Determine the percent to display
                    if (rate_difference > 0.994){
                        result = "HIGH";
                    } else if (rate_difference < -0.994){
                        result = "LOW";
                    } else {
                        // Set maximum significant digits to 2 and minimum to 1 while converting it to percent
                        DecimalFormat rate_formatter = new DecimalFormat("@#%");

                        result = rate_formatter.format(rate_difference);
                    }
                    legalRateInput = true;
                }
            } catch (NumberFormatException numb) {
                Log.e(LOG_TAG, "Parsing error in function updateRatePercentage. " + numb);
            }
        }

        // Set text and textColor to exchange_percentage textview
        mRate_TextView.setText(result);
        mRate_TextView.setTextColor(textColor);

    }

    private void updateBankRate(){
        if (legalRateInput){
            if (mCurrency_Position.equals(getString(R.string.position_one))){
                mBank_Rate_One_To_Two = mBank_Rate.getText().toString();
            } else if (mCurrency_Position.equals(getString(R.string.position_two))){
                mBank_Rate_Two_To_One = mBank_Rate.getText().toString();
            }
            rateWasChanged = true;
        }
        printLog("updateBankRate");
    }

    private void updateMarketRate(){
        if (legalRateInput){
            if (mCurrency_Position.equals(getString(R.string.position_one))){
                mMarket_Rate_One_To_Two = mMarket_Rate.getText().toString();
            } else if (mCurrency_Position.equals(getString(R.string.position_two))){
                mMarket_Rate_Two_To_One = mMarket_Rate.getText().toString();
            }
            rateWasChanged = true;
        }
        printLog("updateMarketRate");
    }

}
