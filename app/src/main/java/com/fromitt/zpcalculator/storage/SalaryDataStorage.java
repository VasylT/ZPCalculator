package com.fromitt.zpcalculator.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by Tkachov Vasyl on 08.11.2016.
 */

public class SalaryDataStorage {

    private static final String TAG                 = "SalaryDataStorage";

    private static final int DB_VERSION             = 1;
    private static final String DB_NAME             = "salary_data.db";
    private static final String TABLE_NAME          = "salary_data";

    private static final String KEY_ID              = "_id";
    private static final String KEY_DATE            = "date";
    private static final String KEY_SALARY          = "salary";
    private static final String KEY_EXCHANGE_BUY1   = "exchange_buy1";
    private static final String KEY_EXCHANGE_SALE1  = "exchange_sale1";
    private static final String KEY_MONEY_ON_CARD1  = "money_on_card1";
    private static final String KEY_EXCHANGE_BUY2   = "exchange_buy2";
    private static final String KEY_EXCHANGE_SALE2  = "exchange_sale2";
    private static final String KEY_MONEY_ON_CARD2  = "money_on_card2";

    private static final String CREATE_CRED_TABLE  = "CREATE TABLE " + TABLE_NAME + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_DATE + " INTEGER NOT NULL UNIQUE,"
            + KEY_SALARY + " FLOAT NOT NULL,"
            + KEY_EXCHANGE_BUY1 + " FLOAT NOT NULL,"
            + KEY_EXCHANGE_SALE1 + " FLOAT NOT NULL,"
            + KEY_MONEY_ON_CARD1 + " FLOAT NOT NULL,"
            + KEY_EXCHANGE_BUY2 + " FLOAT NOT NULL,"
            + KEY_EXCHANGE_SALE2 + " FLOAT NOT NULL,"
            + KEY_MONEY_ON_CARD2 + " FLOAT NOT NULL" + ")";

    private DBOpenHelper mOpenHelper;
    private SQLiteDatabase mDatabase;

    private static SalaryDataStorage mInstance;

    public static SalaryDataStorage getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SalaryDataStorage(context.getApplicationContext());
        }
        return mInstance;
    }

    private SalaryDataStorage(Context context) {
        mOpenHelper = new DBOpenHelper(context);
        mDatabase = mOpenHelper.getWritableDatabase();
    }

    private class DBOpenHelper extends SQLiteOpenHelper {

        DBOpenHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_CRED_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
        }
    }

    public boolean addItem(SalaryDataItem entry) {
        long cleanMillis = (entry.getDate().getTime() / 1000) * 1000;
        Log.d(TAG, "addItem(): " + cleanMillis);

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, cleanMillis);
        values.put(KEY_SALARY, entry.getSalary());
        values.put(KEY_EXCHANGE_BUY1, entry.getExchangeBuy1());
        values.put(KEY_EXCHANGE_SALE1, entry.getExchangeSale1());
        values.put(KEY_MONEY_ON_CARD1, entry.getMoneyOnCard1());
        values.put(KEY_EXCHANGE_BUY2, entry.getExchangeBuy2());
        values.put(KEY_EXCHANGE_SALE2, entry.getExchangeSale2());
        values.put(KEY_MONEY_ON_CARD2, entry.getMoneyOnCard2());

        long id = mDatabase.insert(TABLE_NAME, null, values);
        return id > 0;
    }

    public SalaryDataItem getSalaryDataEntry(long dateMillis) {
        long cleanMillis = (dateMillis / 1000) * 1000;

        Cursor c = mDatabase.query(TABLE_NAME,
                new String[]{KEY_ID, KEY_DATE, KEY_SALARY, KEY_EXCHANGE_BUY1, KEY_EXCHANGE_SALE1, KEY_MONEY_ON_CARD1, KEY_EXCHANGE_BUY2, KEY_EXCHANGE_SALE2, KEY_MONEY_ON_CARD2},
                KEY_DATE + " = ?",
                new String[]{String.valueOf(cleanMillis)},
                null, null, null);

        Log.d(TAG, "getSalaryDataEntry(): " + c.getCount() + " entries for " + cleanMillis);

        SalaryDataItem entry = null;
        if (c.getCount() > 0) {
            c.moveToFirst();
            entry = new SalaryDataItem(
                    new Date(c.getLong(c.getColumnIndex(KEY_DATE))),
                    c.getFloat(c.getColumnIndex(KEY_SALARY)),
                    c.getFloat(c.getColumnIndex(KEY_EXCHANGE_BUY1)),
                    c.getFloat(c.getColumnIndex(KEY_EXCHANGE_SALE1)),
                    c.getFloat(c.getColumnIndex(KEY_MONEY_ON_CARD1)),
                    c.getFloat(c.getColumnIndex(KEY_EXCHANGE_BUY2)),
                    c.getFloat(c.getColumnIndex(KEY_EXCHANGE_SALE2)),
                    c.getFloat(c.getColumnIndex(KEY_MONEY_ON_CARD2))
            );
        }

        c.close();
        return entry;
    }

    public void updateItem(SalaryDataItem entry) {
        String where = KEY_DATE + " = " + entry.getDate().getTime() ;
        ContentValues values = new ContentValues();
        values.put(KEY_SALARY, entry.getSalary());
        values.put(KEY_EXCHANGE_BUY1, entry.getExchangeBuy1());
        values.put(KEY_EXCHANGE_SALE1, entry.getExchangeSale1());
        values.put(KEY_MONEY_ON_CARD1, entry.getMoneyOnCard1());
        values.put(KEY_EXCHANGE_BUY2, entry.getExchangeBuy2());
        values.put(KEY_EXCHANGE_SALE2, entry.getExchangeSale2());
        values.put(KEY_MONEY_ON_CARD2, entry.getMoneyOnCard2());
        mDatabase.update(TABLE_NAME, values, where, null);
    }

    public List<SalaryDataItem> getItems() {
        Log.d(TAG, "getCredentials()");
        Cursor c = mDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + KEY_DATE + " DESC", null);

        List<SalaryDataItem> entries = new ArrayList<>();
        if (c.getCount() > 0) {
            c.moveToFirst();
            int dateColumnIndex = c.getColumnIndex(KEY_DATE);
            int salaryColumnIndex = c.getColumnIndex(KEY_SALARY);
            int exchangeBuyStartColumnIndex = c.getColumnIndex(KEY_EXCHANGE_BUY1);
            int exchangeSaleStartColumnIndex = c.getColumnIndex(KEY_EXCHANGE_SALE1);
            int cardStartColumnIndex = c.getColumnIndex(KEY_MONEY_ON_CARD1);
            int exchangeBuyEndColumnIndex = c.getColumnIndex(KEY_EXCHANGE_BUY2);
            int exchangeSaleEndColumnIndex = c.getColumnIndex(KEY_EXCHANGE_SALE2);
            int cardEndColumnIndex = c.getColumnIndex(KEY_MONEY_ON_CARD2);

            while (!c.isAfterLast()) {
                SalaryDataItem entry = new SalaryDataItem(
                        new Date(c.getLong(dateColumnIndex)),
                        c.getFloat(salaryColumnIndex),
                        c.getFloat(exchangeBuyStartColumnIndex),
                        c.getFloat(exchangeSaleStartColumnIndex),
                        c.getFloat(cardStartColumnIndex),
                        c.getFloat(exchangeBuyEndColumnIndex),
                        c.getFloat(exchangeSaleEndColumnIndex),
                        c.getFloat(cardEndColumnIndex)
                );
                entries.add(entry);
                c.moveToNext();
            }
        }

        c.close();
        return entries;
    }

    public boolean deleteItem(Date data) {
        long cleanMillis = (data.getTime() / 1000) * 1000;
        Log.d(TAG, "deleteItem(): " + cleanMillis);
        return mDatabase.delete(TABLE_NAME, KEY_DATE + " = ?",
                new String[]{String.valueOf(cleanMillis)}) > 0;
    }

    public void clearDatabase() {
        Log.d(TAG, "clearDatabase()");
        mDatabase.delete(TABLE_NAME, "1", null);
    }

    public long getDatabaseSize() {
        Log.d(TAG, "getDatabaseSize()");
        return DatabaseUtils.queryNumEntries(mDatabase, TABLE_NAME);
    }
}