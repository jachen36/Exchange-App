package com.jacintochen.currencyexchange;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
    private int COLUMN_ID;
    private int COLUMN_CURRENCY_ONE;
    private int COLUMN_CURRENCY_TWO;
    private int COLUMN_BANK_RATE_ONE_TO_TWO;
    private int COLUMN_MARKET_RATE_ONE_TO_TWO;
    private int COLUMN_BANK_RATE_TWO_TO_ONE;
    private int COLUMN_MARKET_RATE_TWO_TO_ONE;


    public void swapCursor(Cursor c){
        cursor = c;
        if (c != null){
            COLUMN_ID = c.getColumnIndex(ExchangeContract._ID);
            COLUMN_CURRENCY_ONE = c.getColumnIndex(ExchangeContract.COLUMN_CURRENCY_ONE);
            COLUMN_CURRENCY_TWO = c.getColumnIndex(ExchangeContract.COLUMN_CURRENCY_TWO);
            COLUMN_BANK_RATE_ONE_TO_TWO = c.getColumnIndex(ExchangeContract.COLUMN_BANK_RATE_ONE_TO_TWO);
            COLUMN_MARKET_RATE_ONE_TO_TWO = c.getColumnIndex(ExchangeContract.COLUMN_MARKET_RATE_ONE_TO_TWO);
            COLUMN_BANK_RATE_TWO_TO_ONE = c.getColumnIndex(ExchangeContract.COLUMN_BANK_RATE_TWO_TO_ONE);
            COLUMN_MARKET_RATE_TWO_TO_ONE = c.getColumnIndex(ExchangeContract.COLUMN_MARKET_RATE_TWO_TO_ONE);
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
    public void onBindViewHolder(ViewHolder viewHolder, int position){
        final long id = getItemId(position);
        final Context context = viewHolder.currency_1.getContext();

        cursor.moveToPosition(position);
        viewHolder.currency_1.setText(cursor.getString(COLUMN_CURRENCY_ONE));
        viewHolder.currency_2.setText(cursor.getString(COLUMN_CURRENCY_TWO));

        // Clicking changes exchange for the calculator
        viewHolder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // TODO: Need to finish the click function
                Intent result = new Intent();
                result.putExtra(ExchangeContract._ID, cursor.getString(COLUMN_ID));
                result.putExtra(ExchangeContract.COLUMN_CURRENCY_ONE, cursor.getString(COLUMN_CURRENCY_ONE));
                result.putExtra(ExchangeContract.COLUMN_CURRENCY_TWO, cursor.getString(COLUMN_CURRENCY_TWO));
                result.putExtra(ExchangeContract.COLUMN_BANK_RATE_ONE_TO_TWO, cursor.getString(COLUMN_BANK_RATE_ONE_TO_TWO));
                result.putExtra(ExchangeContract.COLUMN_MARKET_RATE_ONE_TO_TWO, cursor.getString(COLUMN_MARKET_RATE_ONE_TO_TWO));
                result.putExtra(ExchangeContract.COLUMN_BANK_RATE_TWO_TO_ONE, cursor.getString(COLUMN_BANK_RATE_TWO_TO_ONE));
                result.putExtra(ExchangeContract.COLUMN_MARKET_RATE_TWO_TO_ONE, cursor.getString(COLUMN_MARKET_RATE_TWO_TO_ONE));

                // Send back the data to change the exchange of the calculator
                ((Activity) context).setResult(Activity.RESULT_OK, result);
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
        return cursor.getLong(COLUMN_ID);
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
