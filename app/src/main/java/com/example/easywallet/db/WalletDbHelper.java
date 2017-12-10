package com.example.easywallet.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by GUNNER on 10/12/2560.
 */

public class WalletDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "wallet.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "wallet";
    public static final String COL_ID = "_id";
    public static final String COL_PICTURE = "picture";
    public static final String COL_DES = "des";
    public static final String COL_COST = "cost";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_PICTURE + " TEXT, "
            + COL_DES + " TEXT, "
            + COL_COST + " INTEGER)";

    public WalletDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        insertInitialData(db);
    }

    private void insertInitialData(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(COL_PICTURE, "ic_income.png");
        cv.put(COL_DES, "คุณพ่อให้เงิน");
        cv.put(COL_COST, 8000);
        db.insert(TABLE_NAME, null, cv);

        cv = new ContentValues();
        cv.put(COL_PICTURE, "ic_expense.png");
        cv.put(COL_DES, "จ่ายค่าหอ");
        cv.put(COL_COST, 2500);
        db.insert(TABLE_NAME, null, cv);

        cv = new ContentValues();
        cv.put(COL_PICTURE, "ic_expense.png");
        cv.put(COL_DES, "ซื้อล็อตเตอรี่ 1 ชุด");
        cv.put(COL_COST, 700);
        db.insert(TABLE_NAME, null, cv);

        cv = new ContentValues();
        cv.put(COL_PICTURE, "ic_income.png");
        cv.put(COL_DES, "ถูกล็อตเตอรี่รางวัลที่ 1");
        cv.put(COL_COST, 30000000);
        db.insert(TABLE_NAME, null, cv);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
