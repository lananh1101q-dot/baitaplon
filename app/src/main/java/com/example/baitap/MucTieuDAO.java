package com.example.baitap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MucTieuDAO {
    private database dbHelper;

    public MucTieuDAO(Context context) {
        dbHelper = new database(context);
    }

    // ✅ Thêm mục tiêu mới
    public long insert(muctieu m) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("user_id", m.getUserId());
        v.put("tenmuctieu", m.getTenMucTieu());
        v.put("gioitinh", m.getGioiTinh());
        v.put("tuoi", m.getTuoi());
        v.put("chieucao", m.getChieuCao());
        v.put("cannang", m.getCanNang());
        v.put("bmi", m.getBmi());
        v.put("nangluong", m.getNangLuong());
        v.put("luongnuoc", m.getLuongNuoc());
        v.put("ngay", m.getNgay());
        long id = db.insert("muctieu", null, v);
        db.close();
        return id;
    }

    // ✅ Cập nhật mục tiêu hiện tại
    public int update(muctieu m) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("gioitinh", m.getGioiTinh());
        v.put("tuoi", m.getTuoi());
        v.put("chieucao", m.getChieuCao());
        v.put("cannang", m.getCanNang());
        v.put("bmi", m.getBmi());
        v.put("nangluong", m.getNangLuong());
        v.put("luongnuoc", m.getLuongNuoc());
        v.put("ngay", m.getNgay());
        int result = db.update("muctieu", v, "id=?", new String[]{String.valueOf(m.getId())});
        db.close();
        return result;
    }

    // ✅ Lấy mục tiêu hiện tại (nếu có)
    public muctieu getCurrent(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        muctieu m = null;
        Cursor c = db.rawQuery("SELECT * FROM muctieu WHERE user_id=? ORDER BY id DESC LIMIT 1",
                new String[]{String.valueOf(userId)});
        if (c.moveToFirst()) {
            m = mapCursor(c);
        }
        c.close();
        db.close();
        return m;
    }

    // ✅ Lấy mục tiêu mới nhất (phục vụ hiển thị)
    public muctieu getLatest() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        muctieu m = null;
        Cursor c = db.rawQuery("SELECT * FROM muctieu ORDER BY id DESC LIMIT 1", null);
        if (c.moveToFirst()) {
            m = mapCursor(c);
        }
        c.close();
        db.close();
        return m;
    }

    private muctieu mapCursor(Cursor c) {
        muctieu m = new muctieu();
        m.setId(c.getInt(c.getColumnIndexOrThrow("id")));
        m.setUserId(c.getInt(c.getColumnIndexOrThrow("user_id")));
        m.setTenMucTieu(c.getString(c.getColumnIndexOrThrow("tenmuctieu")));
        m.setGioiTinh(c.getString(c.getColumnIndexOrThrow("gioitinh")));
        m.setTuoi(c.getInt(c.getColumnIndexOrThrow("tuoi")));
        m.setChieuCao(c.getDouble(c.getColumnIndexOrThrow("chieucao")));
        m.setCanNang(c.getDouble(c.getColumnIndexOrThrow("cannang")));
        m.setBmi(c.getDouble(c.getColumnIndexOrThrow("bmi")));
        m.setNangLuong(c.getInt(c.getColumnIndexOrThrow("nangluong")));
        m.setLuongNuoc(c.getInt(c.getColumnIndexOrThrow("luongnuoc")));
        m.setNgay(c.getString(c.getColumnIndexOrThrow("ngay")));
        return m;

    }
    public boolean updatee(muctieu m) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("tenmuctieu", m.getTenMucTieu());
        v.put("nangluong", m.getNangLuong());
        v.put("luongnuoc", m.getLuongNuoc());
        int r = db.update("muctieu", v, "id=?", new String[]{String.valueOf(m.getId())});
        db.close();
        return r > 0;
    }
}
