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

    // ✅ Lấy user_id từ username
    public int getUserIdByUsername(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int userId = -1;
        Cursor c = db.rawQuery("SELECT id FROM taikhoan WHERE tendangnhap = ?", new String[]{username});
        if (c.moveToFirst()) {
            userId = c.getInt(0);
        }
        c.close();
        db.close();
        return userId;
    }

    // ✅ Lấy mục tiêu theo user_id
    public muctieu getCurrent(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        muctieu m = null;
        Cursor c = db.rawQuery("SELECT * FROM muctieu WHERE user_id = ? ORDER BY id DESC LIMIT 1", new String[]{String.valueOf(userId)});
        if (c.moveToFirst()) {
            m = new muctieu();
            m.setId(c.getInt(c.getColumnIndexOrThrow("id")));
            m.setUserId(userId);
            m.setTenMucTieu(c.getString(c.getColumnIndexOrThrow("tenmuctieu")));
            m.setGioiTinh(c.getString(c.getColumnIndexOrThrow("gioitinh")));
            m.setTuoi(c.getInt(c.getColumnIndexOrThrow("tuoi")));
            m.setChieuCao(c.getDouble(c.getColumnIndexOrThrow("chieucao")));
            m.setCanNang(c.getDouble(c.getColumnIndexOrThrow("cannang")));
            m.setBmi(c.getDouble(c.getColumnIndexOrThrow("bmi")));
            m.setNangLuong(c.getInt(c.getColumnIndexOrThrow("nangluong")));
            m.setLuongNuoc(c.getInt(c.getColumnIndexOrThrow("luongnuoc")));
            m.setNgay(c.getString(c.getColumnIndexOrThrow("ngay")));
        }
        c.close();
        db.close();
        return m;
    }

    // ✅ Lấy mục tiêu theo username
    public muctieu getCurrent(String username) {
        int userId = getUserIdByUsername(username);
        if (userId == -1) return null;
        return getCurrent(userId);
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

    // ✅ Cập nhật toàn bộ mục tiêu
    public int update(muctieu m) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("tenmuctieu", m.getTenMucTieu());
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

    // ✅ Cập nhật chế độ mục tiêu (giảm cân, giữ cân, tăng cân)
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
