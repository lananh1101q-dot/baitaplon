package com.example.baitap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class TapLuyenDAO {
    private database dbHelper;

    public TapLuyenDAO(Context context) {
        dbHelper = new database(context);
    }

    public void insert(tapluyen_employ tl) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenbaitap", tl.getTenBaiTap());
        values.put("thoigian", tl.getThoiGian());
        values.put("calotieuthu", tl.getCaloTieuThu());
        values.put("ngay", tl.getNgay());
        db.insert("tapluyen", null, values);
        db.close();
    }
    public long insertAuto(String tenBaiTap, int thoiGianPhut, String ngay) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long result = -1;
        try {
            // Tính calo tự động
            int caloTieuThu = tapluyen_DanhMucBaiTap.tinhCalo(tenBaiTap, thoiGianPhut);

            ContentValues values = new ContentValues();
            values.put("tenbaitap", tenBaiTap);
            values.put("thoigian", thoiGianPhut);
            values.put("calotieuthu", caloTieuThu);
            values.put("ngay", ngay);

            result = db.insert("tapluyen", null, values);
        } finally {
            db.close();
        }
        return result;
    }

    public void update(tapluyen_employ tl) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenbaitap", tl.getTenBaiTap());
        values.put("thoigian", tl.getThoiGian());
        values.put("calotieuthu", tl.getCaloTieuThu());
        values.put("ngay", tl.getNgay());
        db.update("tapluyen", values, "id=?", new String[]{String.valueOf(tl.getId())});
        db.close();
    }

    public void delete(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("tapluyen", "id=?", new String[]{String.valueOf(id)});
        db.close();
    }
    public ArrayList<tapluyen_employ> getByDate(String ngay) {
        ArrayList<tapluyen_employ> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM tapluyen WHERE ngay=? ORDER BY id DESC", new String[]{ngay});
        while (c.moveToNext()) {
            list.add(new tapluyen_employ(
                    c.getInt(0),
                    c.getString(1),
                    c.getInt(2),
                    c.getInt(3),
                    c.getString(4)
            ));
        }
        c.close(); db.close();
        return list;
    }



    public ArrayList<tapluyen_employ> getAll() {
        ArrayList<tapluyen_employ> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM tapluyen ORDER BY ngay DESC", null);
        if (c.moveToFirst()) {
            do {
                list.add(new tapluyen_employ(
                        c.getInt(0),
                        c.getString(1),
                        c.getInt(2),
                        c.getInt(3),
                        c.getString(4)
                ));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return list;
    }
}