package com.jacintochen.currencyexchange.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Bokii on 7/2/2015.
 */
public class ExchangeDbHelper extends SQLiteOpenHelper {

    private final String LOG_TAG = ExchangeDbHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "exchange.db";

    public ExchangeDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        final String SQL_CREATE_EXCHANGE_TABLE = "CREATE TABLE " + ExchangeContract.TABLE_NAME + " (" +
                ExchangeContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ExchangeContract.COLUMN_CURRENCY_ONE + " TEXT NOT NULL, " +
                ExchangeContract.COLUMN_CURRENCY_TWO + " TEXT NOT NULL, " +
                ExchangeContract.COLUMN_BANK_RATE_ONE_TO_TWO + " TEXT NOT NULL, " +
                ExchangeContract.COLUMN_MARKET_RATE_ONE_TO_TWO + " TEXT NOT NULL, " +
                ExchangeContract.COLUMN_BANK_RATE_TWO_TO_ONE + " TEXT NOT NULL, " +
                ExchangeContract.COLUMN_MARKET_RATE_TWO_TO_ONE + " TEXT NOT NULL);";

        // TODO: Make default values as constant text in string xml
        final String SQL_STARTING_USD_PESO = "INSERT INTO " + ExchangeContract.TABLE_NAME +
                " VALUES(" +
                "0, 'USD', 'PESO', '14', '15', '0.0714', '0.06667');";
        final String SQL_STARTING_USD_BIG_MAC = "INSERT INTO " + ExchangeContract.TABLE_NAME + " VALUES(" +
                "1, 'USD', 'BIG MAC', '3.99', '3.99', '0.2506', '0.2506');";

        db.execSQL(SQL_CREATE_EXCHANGE_TABLE);
        db.execSQL(SQL_STARTING_USD_PESO);
        db.execSQL(SQL_STARTING_USD_BIG_MAC);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // This method shouldn't be used at the moment
        throw new UnsupportedOperationException();
    }

}
