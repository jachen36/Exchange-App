package com.jacintochen.currencyexchange.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Bokii on 7/2/2015.
 */
public final class ExchangeContract implements BaseColumns{

    // TODO: Put notes into noteable. Authority has to be the same as when you declare in manifest
    public static final String CONTENT_AUTHORITY = "com.jacintochen.currencyexchange.data.ExchangeProvider";

    public static final String PATH_EXCHANGE = "exchange";

    public static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY + "/" + PATH_EXCHANGE);

    public static final String TABLE_NAME = "exchange";



    // Database Columns
    public static final String COLUMN_CURRENCY_ONE = "currency_one";
    public static final String COLUMN_CURRENCY_TWO = "currency_two";
    public static final String COLUMN_BANK_RATE_ONE_TO_TWO = "bank_one_to_two";
    public static final String COLUMN_MARKET_RATE_ONE_TO_TWO = "market_one_to_two";
    public static final String COLUMN_BANK_RATE_TWO_TO_ONE = "bank_two_to_one";
    public static final String COLUMN_MARKET_RATE_TWO_TO_ONE = "market_two_to_one";


    // TODO: Not sure which MIME type I should use?
    // Types
    public static final String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXCHANGE;
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXCHANGE;

    // MIME type
    public static final String EXCHANGE_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.com.jacintochen.exchange.exchange";
    public static final String EXCHANGE_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.com.jacintochen.exchange.exchange";


    public static Uri buildExchangeUri(long id){
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
