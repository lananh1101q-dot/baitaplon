package com.example.baitap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MucTieuDAO {
    private database dbHelper;

    public MucTieuDAO(Context context) {
        dbHelper = new database(context);
    }

    // chèn mới, trả về id
    public long insert(muctieu m) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("tenmuctieu", m.getTenMucTieu());
        v.put("chieucao", m.getChieuCao());
        v.put("cannang", m.getCanNang());
        v.put("bmi", m.getBmi());
        v.put("nangluong", m.getNangLuong());
        v.put("luongnuoc", m.getLuongNuoc());
        // nếu ngày null thì gán ngày hôm nay dạng yyyy-MM-dd
        String ngay = m.getNgay();
        if (ngay == null || ngay.isEmpty()) {
            ngay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        }
        v.put("ngay", ngay);
        long id = db.insert("muctieu", null, v);
        db.close();
        return id;
    }

    // lấy mục tiêu mới nhất
    public muctieu getLatest() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM muctieu ORDER BY id DESC LIMIT 1", null);
        muctieu m = null;
        if (c.moveToFirst()) {
            int id = c.getInt(c.getColumnIndexOrThrow("id"));
            String ten = c.getString(c.getColumnIndexOrThrow("tenmuctieu"));
            double chieucao = c.getDouble(c.getColumnIndexOrThrow("chieucao"));
            double cannang = c.getDouble(c.getColumnIndexOrThrow("cannang"));
            double bmi = c.getDouble(c.getColumnIndexOrThrow("bmi"));
            int nangluong = c.getInt(c.getColumnIndexOrThrow("nangluong"));
            int luongnuoc = c.getInt(c.getColumnIndexOrThrow("luongnuoc"));
            String ngay = c.getString(c.getColumnIndexOrThrow("ngay"));
            m = new muctieu(id, ten, chieucao, cannang, bmi, nangluong, luongnuoc, ngay);
        }
        c.close();
        db.close();
        return m;
    }
}
