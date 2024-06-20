package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "finance.db";
    private static final int DATABASE_VERSION = 2; // 更新数据库版本
    private static final String TABLE_NAME = "transactions";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_NOTE = "note";
    private static final String COLUMN_CURRENCY = "currency"; // 新增货币列

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_AMOUNT + " TEXT, "
                + COLUMN_DATE + " TEXT, "
                + COLUMN_CATEGORY + " TEXT, "
                + COLUMN_NOTE + " TEXT, "
                + COLUMN_CURRENCY + " TEXT)"; // 添加货币列
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_CURRENCY + " TEXT");
        }
    }

    public void addTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AMOUNT, transaction.getAmount());
        values.put(COLUMN_DATE, transaction.getDate());
        values.put(COLUMN_CATEGORY, transaction.getCategory());
        values.put(COLUMN_NOTE, transaction.getNote());
        values.put(COLUMN_CURRENCY, transaction.getCurrency()); // 添加货币值
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void updateTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AMOUNT, transaction.getAmount());
        values.put(COLUMN_DATE, transaction.getDate());
        values.put(COLUMN_CATEGORY, transaction.getCategory());
        values.put(COLUMN_NOTE, transaction.getNote());
        values.put(COLUMN_CURRENCY, transaction.getCurrency()); // 更新货币值

        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(transaction.getId())});
        db.close();
    }

    public void deleteTransaction(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String amount = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY));
                String note = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE));
                String currency = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CURRENCY)); // 读取货币值

                Transaction transaction = new Transaction(id, amount, date, category, note, currency);
                transactions.add(transaction);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return transactions;
    }
}
