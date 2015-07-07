package com.jacintochen.currencyexchange.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Bokii on 7/2/2015.
 */
public class ExchangeProvider extends ContentProvider {

    static final int LIST_EXCHANGE = 100;
    static final int ITEM_EXCHANGE = 101;


    // The URI Matcher used by this content provider
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private ExchangeDbHelper mOpenHelper;


    private static UriMatcher buildUriMatcher(){
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(ExchangeContract.CONTENT_AUTHORITY, "exchange", LIST_EXCHANGE);
        matcher.addURI(ExchangeContract.CONTENT_AUTHORITY, "exchange/#", ITEM_EXCHANGE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new ExchangeDbHelper(getContext());
        //TODO: Should I add an if function to see if a writeable database is return?
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
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
        //TODO: Provider query
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);

        Cursor retCursor;
        switch(match){
            case LIST_EXCHANGE:
                retCursor = db.query(
                        ExchangeContract.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, null, sortOrder);
                break;
            case ITEM_EXCHANGE:
                selection = ExchangeContract._ID + "=?";
                String[] arg = new String[]{ExchangeContract.getIdExchangeUri(uri)};

                retCursor =  db.query(
                        ExchangeContract.TABLE_NAME,
                        projection,
                        selection,
                        arg,
                        null, null,sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set the notification URI for this cursor. Our loader will
        // use this URI to watch for any change to our data,
        // and if there data change the loader will automatically refresh.
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public int update(Uri uri, ContentValues value, String selection, String[] selectionArgs){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        selection = ExchangeContract._ID + "=?";
        String[] arg = new String[]{ExchangeContract.getIdExchangeUri(uri)};

        int count = db.update(ExchangeContract.TABLE_NAME, value, selection, arg);

        if (count > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        selection = ExchangeContract._ID + "=?";
        String[] arg = new String[]{ExchangeContract.getIdExchangeUri(uri)};

        int count = db.delete(ExchangeContract.TABLE_NAME, selection, arg);

        if (count > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }

    @Override
    public String getType(Uri uri){
        // Use UriMatcher to determine what uri this is
        final int match = sUriMatcher.match(uri);

        switch(match){
            case LIST_EXCHANGE:
                return ExchangeContract.EXCHANGE_DIR_TYPE;
            case ITEM_EXCHANGE:
                return ExchangeContract.EXCHANGE_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknow uri: " + uri);
        }
    }


    //TODO: put shutdown method here
    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
