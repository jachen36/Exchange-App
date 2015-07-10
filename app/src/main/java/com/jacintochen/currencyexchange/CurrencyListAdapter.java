package com.jacintochen.currencyexchange;

import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jacintochen.currencyexchange.data.ExchangeContract;

/**
 * Created by Bokii on 7/8/2015.
 */
public class CurrencyListAdapter extends RecyclerView.Adapter<CurrencyListAdapter.ViewHolder>{
    Cursor cursor;
    //TODO: add project/index column so it is faster
    int COLUMN_ID;
    int COLUMN_CURRENCY_ONE;
    int COLUMN_CURRENCY_TWO;

    String[][] dummy= new String[][]{
            {"USD" , "Peso"},
            {"USD" , "EURO"},
            {"USA BigMac", "JPN BigMac"},
            {"Honey", "Bear"}
    };


    public void swapCursor(Cursor c){
//        cursor = c;
//        if (c != null){
//            COLUMN_CURRENCY_ONE = c.getColumnIndex(ExchangeContract.COLUMN_CURRENCY_ONE);
//            COLUMN_CURRENCY_TWO = c.getColumnIndex(ExchangeContract.COLUMN_CURRENCY_TWO);
//            COLUMN_ID = c.getColumnIndex(ExchangeContract._ID);
//        }
//        notifyDataSetChanged();

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
//        cursor.moveToPosition(position);
//        viewHolder.currency_1.setText(cursor.getString(COLUMN_CURRENCY_ONE));
//        viewHolder.currency_2.setText(cursor.getString(COLUMN_CURRENCY_TWO));

        viewHolder.currency_1.setText(dummy[position][0]);
        viewHolder.currency_2.setText(dummy[position][1]);

        //TODO: Add long pressed that will delete the cursor

    }

    @Override
    public long getItemId(int position){
        cursor.moveToPosition(position);
        return cursor.getLong(COLUMN_ID);
    }

    @Override
    public int getItemCount(){
//        return cursor != null ? cursor.getCount() : 0;
        return dummy.length;
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
