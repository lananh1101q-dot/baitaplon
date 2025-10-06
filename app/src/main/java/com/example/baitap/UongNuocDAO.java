package com.example.baitap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class UongNuocDAO {
    private database dbHelper;

    public UongNuocDAO(Context context) {
        dbHelper = new database(context);
    }

    // Thêm bản ghi
    public void insert(UongNuocEmploy un) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("luongnuoc", un.getLuongNuoc());
        values.put("ngay", un.getNgay());
        db.insert("uongnuoc", null, values);
        db.close();
    }

    // Cập nhật
    public void update(UongNuocEmploy un) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("luongnuoc", un.getLuongNuoc());
        values.put("ngay", un.getNgay());
        db.update("uongnuoc", values, "id=?", new String[]{String.valueOf(un.getId())});
        db.close();
    }

    // Xóa
    public void delete(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("uongnuoc", "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Lấy tất cả
    public ArrayList<UongNuocEmploy> getAll() {
        ArrayList<UongNuocEmploy> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM uongnuoc ORDER BY ngay DESC", null);
        if (c.moveToFirst()) {
            do {
                list.add(new UongNuocEmploy(
                        c.getInt(0),
                        c.getInt(1),
                        c.getString(2)
                ));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return list;
    }

    // Tính tổng lượng nước đã uống trong 1 ngày
    public int getTongLuongNuocTrongNgay(String ngay) {
        int tong = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(luongnuoc) FROM uongnuoc WHERE ngay=?", new String[]{ngay});
        if (c.moveToFirst()) {
            tong = c.getInt(0);
        }
        c.close();
        db.close();
        return tong;
    }
}

