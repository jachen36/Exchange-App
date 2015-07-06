package com.jacintochen.currencyexchange.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Bokii on 7/2/2015.
 */
public class ExchangeProvider extends ContentProvider {

    static final int list_exchange = 100;
    static final int item_exchange = 101;


    // The URI Matcher used by this content provider
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private ExchangeDbHelper mOpenHelper;


    private static UriMatcher buildUriMatcher(){
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(ExchangeContract.CONTENT_AUTHORITY, "exchange", 0);
        matcher.addURI(ExchangeContract.CONTENT_AUTHORITY, "exchange/#", 1);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new ExchangeDbHelper(getContext());
        //TODO: Should I add an if function to see if a writeable database is return?
        // TODO: Should I just return a writable database instead of the helper class?
        return true;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        // A new data entry should not have its own ID
        if (values.containsKey(ExchangeContract._ID)){
            throw new UnsupportedOperationException("Unsupported insert where ID is present");
        }

        long _id = db.insertOrThrow(ExchangeContract.TABLE_NAME, null, values);
        getContext().getContentResolver().notifyChange(uri, null);

        return ExchangeContract.buildExchangeUri(_id);
    }

    @Override
    public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3, String arg4){
        //TODO: Provider query
        return null;
    }

    @Override
    public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3){
        //TODO: Provider update
        return 0;
    }


    @Override
    public int delete(Uri arg0, String arg1, String[] arg2){
        //TODO: Provider delete
        //TODO: Find in sunshine app where delete is used. believe this is during update/referesh
        //TODO: also find when update is used in sunshine app.
        //TODO: find out what happens when sunshine weather updates. are the weather replaced since they have the same date.
        return 0;
    }

    @Override
    public String getType(Uri uri){
        // Use UriMatcher to determine what uri this is
        final int match = sUriMatcher.match(uri);

        switch(match){
            case list_exchange:
                return ExchangeContract.EXCHANGE_DIR_TYPE;
            case item_exchange:
                return ExchangeContract.EXCHANGE_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknow uri: " + uri);
        }
    }




}
