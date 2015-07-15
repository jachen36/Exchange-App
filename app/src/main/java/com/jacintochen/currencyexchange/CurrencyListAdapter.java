package com.jacintochen.currencyexchange;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jacintochen.currencyexchange.data.ExchangeContract;

/**
 * Created by Bokii on 7/8/2015.
 */
public class CurrencyListAdapter extends RecyclerView.Adapter<CurrencyListAdapter.ViewHolder>{
    private String LOG_TAG = CurrencyListAdapter.class.getSimpleName();

    private Cursor cursor;
    //TODO: add project/index column so it is faster
    private int INDEX_ID;
    private int INDEX_CURRENCY_ONE;
    private int INDEX_CURRENCY_TWO;
    private int INDEX_BANK_RATE_ONE_TO_TWO;
    private int INDEX_MARKET_RATE_ONE_TO_TWO;
    private int INDEX_BANK_RATE_TWO_TO_ONE;
    private int INDEX_MARKET_RATE_TWO_TO_ONE;


    public void swapCursor(Cursor c){
        cursor = c;
        // Once I have projection I do not need this
        if (c != null){
            // TODO: Remove this when i have the projections
            INDEX_ID = c.getColumnIndex(ExchangeContract._ID);
            INDEX_CURRENCY_ONE = c.getColumnIndex(ExchangeContract.COLUMN_CURRENCY_ONE);
            INDEX_CURRENCY_TWO = c.getColumnIndex(ExchangeContract.COLUMN_CURRENCY_TWO);
            INDEX_BANK_RATE_ONE_TO_TWO = c.getColumnIndex(ExchangeContract.COLUMN_BANK_RATE_ONE_TO_TWO);
            INDEX_MARKET_RATE_ONE_TO_TWO = c.getColumnIndex(ExchangeContract.COLUMN_MARKET_RATE_ONE_TO_TWO);
            INDEX_BANK_RATE_TWO_TO_ONE = c.getColumnIndex(ExchangeContract.COLUMN_BANK_RATE_TWO_TO_ONE);
            INDEX_MARKET_RATE_TWO_TO_ONE = c.getColumnIndex(ExchangeContract.COLUMN_MARKET_RATE_TWO_TO_ONE);

        }
        notifyDataSetChanged();

    }

    //TODO: If possible use the viewType to disable the current displayed currency exchange
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_list_item, parent, false);

        // Wrap it with a view holder
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position){
        final long id = getItemId(position);
        final Context context = viewHolder.currency_1.getContext();

        cursor.moveToPosition(position);
        viewHolder.currency_1.setText(cursor.getString(INDEX_CURRENCY_ONE));
        viewHolder.currency_2.setText(cursor.getString(INDEX_CURRENCY_TWO));

        // Clicking changes exchange for the calculator
        viewHolder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // TODO: cursor could be optimize like saving the cursor to the view, but require more memory but fast? IDK
                cursor.moveToPosition(position);
                Intent result = new Intent();
//                result.putExtra(ExchangeContract._ID, cursor.getLong(INDEX_ID));
//                result.putExtra(ExchangeContract.INDEX_CURRENCY_ONE, cursor.getString(INDEX_CURRENCY_ONE));
//                result.putExtra(ExchangeContract.INDEX_CURRENCY_TWO, cursor.getString(INDEX_CURRENCY_TWO));
//                result.putExtra(ExchangeContract.INDEX_BANK_RATE_ONE_TO_TWO, cursor.getString(INDEX_BANK_RATE_ONE_TO_TWO));
//                result.putExtra(ExchangeContract.INDEX_MARKET_RATE_ONE_TO_TWO, cursor.getString(INDEX_MARKET_RATE_ONE_TO_TWO));
//                result.putExtra(ExchangeContract.INDEX_BANK_RATE_TWO_TO_ONE, cursor.getString(INDEX_BANK_RATE_TWO_TO_ONE));
//                result.putExtra(ExchangeContract.INDEX_MARKET_RATE_TWO_TO_ONE, cursor.getString(INDEX_MARKET_RATE_TWO_TO_ONE));

                // TODO: I might send the update to database here!

                SharedPreferences.Editor editor = context.getSharedPreferences(
                        context.getString(R.string.main_pref_values), Context.MODE_PRIVATE).edit();

                // The currency position is reset to one every time
                editor.putString(context.getString(R.string.mcurrency_position),
                        context.getString(R.string.currency_position_default));
                editor.putLong(ExchangeContract._ID, cursor.getLong(INDEX_ID));
                editor.putString(ExchangeContract.COLUMN_CURRENCY_ONE,
                        cursor.getString(INDEX_CURRENCY_ONE));
                editor.putString(ExchangeContract.COLUMN_CURRENCY_TWO,
                        cursor.getString(INDEX_CURRENCY_TWO));
                editor.putString(ExchangeContract.COLUMN_BANK_RATE_ONE_TO_TWO,
                        cursor.getString(INDEX_BANK_RATE_ONE_TO_TWO));
                editor.putString(ExchangeContract.COLUMN_MARKET_RATE_ONE_TO_TWO,
                        cursor.getString(INDEX_MARKET_RATE_ONE_TO_TWO));
                editor.putString(ExchangeContract.COLUMN_BANK_RATE_TWO_TO_ONE,
                        cursor.getString(INDEX_BANK_RATE_TWO_TO_ONE));
                editor.putString(ExchangeContract.COLUMN_MARKET_RATE_TWO_TO_ONE,
                        cursor.getString(INDEX_MARKET_RATE_TWO_TO_ONE));

                editor.apply();

                Log.v(LOG_TAG, "At Adapter onClick.");
                Log.v(LOG_TAG, "Column index are: " +
                                INDEX_ID + " " +
                                INDEX_CURRENCY_ONE + " " +
                                INDEX_CURRENCY_TWO + " " +
                                INDEX_BANK_RATE_ONE_TO_TWO + " " +
                                INDEX_MARKET_RATE_ONE_TO_TWO + " " +
                                INDEX_BANK_RATE_TWO_TO_ONE + " " +
                                INDEX_MARKET_RATE_TWO_TO_ONE);
                Log.v(LOG_TAG, "Cursor values input to preference are: " +
                                cursor.getLong(INDEX_ID) + " : " +
                                cursor.getString(INDEX_CURRENCY_ONE) + " : " +
                                cursor.getString(INDEX_CURRENCY_TWO) + " : " +
                                cursor.getString(INDEX_BANK_RATE_ONE_TO_TWO) + " : " +
                                cursor.getString(INDEX_MARKET_RATE_ONE_TO_TWO) + " : " +
                                cursor.getString(INDEX_BANK_RATE_TWO_TO_ONE) + " : " +
                                cursor.getString(INDEX_MARKET_RATE_TWO_TO_ONE));
                Log.v(LOG_TAG, "Exit Adapter");

                // Send back the data to change the exchange of the calculator
                // TODO: Test if I can have null for intent. Save some cpu.
                ((Activity) context).setResult(Activity.RESULT_OK, result);

                // Return to main once data is set into preference
                ((Activity) context).finish();
            }
        });

        // Long click activates the delete alert dialog
        viewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
           public boolean onLongClick(View view){
               AlertDialog.Builder builder = new AlertDialog.Builder(context);
               builder.setTitle(R.string.alertdialog_delete)
                       .setCancelable(true)
                       .setNegativeButton(android.R.string.cancel, null)
                       .setPositiveButton(R.string.delete,
                               new DialogInterface.OnClickListener(){
                                   @Override
                                   public void onClick(DialogInterface dialogInterface, int i){
                                       deleteExchange(context, id);
                                   }
                               })
                       .show();
               return true;
           }
        });
    }

    private void deleteExchange(Context context, long id){
        context.getContentResolver().delete(ExchangeContract.buildExchangeUri(id), null, null);
    }

    @Override
    public long getItemId(int position){
        cursor.moveToPosition(position);
        return cursor.getLong(INDEX_ID);
    }

    @Override
    public int getItemCount(){
        return cursor != null ? cursor.getCount() : 0;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView currency_1;
        TextView currency_2;


        public ViewHolder(CardView itemView){
            super(itemView);
            cardView = itemView;
            currency_1 = (TextView) itemView.findViewById(R.id.card_view_currency_1);
            currency_2 = (TextView) itemView.findViewById(R.id.card_view_currency_2);

        }

    }
}
