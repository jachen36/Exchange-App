package com.jacintochen.currencyexchange;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jacintochen.currencyexchange.data.ExchangeContract;

/**
 * Created by Jacinto Chen on 7/8/2015.
 */
public class CurrencyListAdapter extends RecyclerView.Adapter<CurrencyListAdapter.ViewHolder>{
    private String LOG_TAG = CurrencyListAdapter.class.getSimpleName();

    private Cursor cursor;
    private final int INDEX_ID = 0;
    private final int INDEX_CURRENCY_ONE = 1;
    private final int INDEX_CURRENCY_TWO = 2;
    private final int INDEX_BANK_RATE_ONE_TO_TWO = 3;
    private final int INDEX_MARKET_RATE_ONE_TO_TWO = 4;
    private final int INDEX_BANK_RATE_TWO_TO_ONE = 5;
    private final int INDEX_MARKET_RATE_TWO_TO_ONE = 6;

    public void swapCursor(Cursor c){
        cursor = c;
        notifyDataSetChanged();
    }

    //TODO: If possible use the viewType to disable the current displayed currency exchange
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Log.v(LOG_TAG, "onCreateViewHolder called");
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
                cursor.moveToPosition(position);

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

                // Send back the data to change the exchange of the calculator
                ((Activity) context).setResult(Activity.RESULT_OK, null);

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
                                       deleteExchange(context, id, position);
                                   }
                               })
                       .show();
               return true;
           }
        });
    }

    private void deleteExchange(Context context, long id, int position){
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
