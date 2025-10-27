package com.example.baitap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.*;

public class UongNuocDAO {
    private database dbHelper;

    public UongNuocDAO(Context context) {
        dbHelper = new database(context);
    }

    // 🧊 Thêm một lần uống nước
    public long themUongNuoc(int soMl, String ghiChu) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String ngay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String gio = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        // 🔹 Kiểm tra xem ngày hôm nay đã có trong bảng uongnuoc chưa
        Cursor check = db.rawQuery("SELECT luongnuoc FROM uongnuoc WHERE ngay = ?", new String[]{ngay});

        if (check.moveToFirst()) {
            // 🔸 Nếu đã có → cộng thêm vào tổng lượng cũ
            int hienTai = check.getInt(0);
            int tongMoi = hienTai + soMl;

            ContentValues update = new ContentValues();
            update.put("luongnuoc", tongMoi);
            db.update("uongnuoc", update, "ngay = ?", new String[]{ngay});

        } else {
            // 🔸 Nếu chưa có → thêm mới dòng cho ngày đó
            ContentValues insert = new ContentValues();
            insert.put("luongnuoc", soMl);
            insert.put("ngay", ngay);
            db.insert("uongnuoc", null, insert);
        }
        check.close();

        // 🔹 Luôn ghi chi tiết vào bảng lịch sử
        ContentValues v2 = new ContentValues();
        v2.put("ngay", ngay);
        v2.put("gio", gio);
        v2.put("luong", soMl);
        v2.put("ghichu", ghiChu);
        long id = db.insert("lichsu_uongnuoc", null, v2);

        db.close();
        return id;
    }


    // 🧊 Lấy tổng nước uống trong ngày
    public int getTongNuocHomNay() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String ngay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Cursor c = db.rawQuery("SELECT SUM(luongnuoc) FROM uongnuoc WHERE ngay = ?", new String[]{ngay});
        int tong = 0;
        if (c.moveToFirst()) tong = c.getInt(0);
        c.close();
        db.close();
        return tong;
    }

    // 🧊 Lấy mục tiêu nước (từ bảng muctieu)
    public int getMucTieuNuoc() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT luongnuoc FROM muctieu ORDER BY id DESC LIMIT 1", null);
        int mucTieu = 2000;
        if (c.moveToFirst()) mucTieu = c.getInt(0);
        c.close();
        db.close();
        return mucTieu;
    }

    // 🧊 Lấy lịch sử uống nước hôm nay
    public List<String> getLichSuHomNay() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String ngay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Cursor c = db.rawQuery("SELECT gio, luong, ghichu FROM lichsu_uongnuoc WHERE ngay = ? ORDER BY gio DESC", new String[]{ngay});
        List<String> list = new ArrayList<>();
        while (c.moveToNext()) {
            String gio = c.getString(0);
            int ml = c.getInt(1);
            String note = c.getString(2);
            list.add(gio + " - " + ml + " ml" + (note != null && !note.isEmpty() ? " (" + note + ")" : ""));
        }
        c.close();
        db.close();
        return list;
    }

    // 🧊 Xóa lịch sử theo id
    public void xoaLichSu(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("lichsu_uongnuoc", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // 🧊 Lấy tổng lượng nước 7 ngày gần nhất (để vẽ biểu đồ)
    public Map<String, Integer> getNuoc7NgayGanNhat() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Map<String, Integer> map = new LinkedHashMap<>();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        for (int i = 6; i >= 0; i--) {
            Calendar temp = (Calendar) cal.clone();
            temp.add(Calendar.DAY_OF_MONTH, -i);
            String ngay = sdf.format(temp.getTime());
            Cursor c = db.rawQuery("SELECT SUM(luongnuoc) FROM uongnuoc WHERE ngay = ?", new String[]{ngay});
            int tong = 0;
            if (c.moveToFirst()) tong = c.getInt(0);
            c.close();
            map.put(ngay, tong);
        }
        db.close();
        return map;
    }


}
