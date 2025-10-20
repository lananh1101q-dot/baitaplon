package com.example.baitap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UongNuocDAO {
    private database dbHelper;

    public UongNuocDAO(Context context) {
        dbHelper = new database(context);
    }

    // Thêm lượng nước vừa uống
    public long insert(int soMl) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues v = new ContentValues();
        String ngay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String gio = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        v.put("ngay", ngay);
        v.put("gio", gio);
        v.put("luongnuoc", soMl);
        long id = db.insert("uongnuoc", null, v);
        db.close();
        return id;
    }

    // Lấy tổng lượng nước đã uống hôm nay
    public int getTongHomNay() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String ngay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Cursor c = db.rawQuery("SELECT SUM(luongnuoc) FROM uongnuoc WHERE ngay = ?", new String[]{ngay});
        int tong = 0;
        if (c.moveToFirst()) tong = c.getInt(0);
        c.close();
        db.close();
        return tong;
    }
}
