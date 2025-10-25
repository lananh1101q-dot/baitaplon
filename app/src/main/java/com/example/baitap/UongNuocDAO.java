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

    // Thêm 1 lần uống nước
    public long themUongNuoc(int soMl, String ghiChu) {
        long id = -1;
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();

            String ngay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String gio = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

            // 1. Lưu vào lịch sử chi tiết
            ContentValues v = new ContentValues();
            v.put("ngay", ngay);
            v.put("gio", gio);
            v.put("luong", soMl);
            v.put("ghichu", ghiChu);
            id = db.insert("lichsu_uongnuoc", null, v);

            // 2. Tính tổng nước hôm nay
            int tongHienTai = getTongNuocHomNay(db) + soMl;

            // 3. Cập nhật bảng tổng uongnuoc
            ContentValues v1 = new ContentValues();
            v1.put("luongnuoc", tongHienTai);
            Cursor c = db.rawQuery("SELECT id FROM uongnuoc WHERE ngay = ?", new String[]{ngay});
            if (c.moveToFirst()) {
                int uid = c.getInt(0);
                db.update("uongnuoc", v1, "id = ?", new String[]{String.valueOf(uid)});
            } else {
                v1.put("ngay", ngay);
                db.insert("uongnuoc", null, v1);
            }
            c.close();

            // 4. Cập nhật biểu đồ
            luuBieuDo(db, ngay, tongHienTai);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return id;
    }

    // Lưu / cập nhật biểu đồ
    public void luuBieuDo(SQLiteDatabase db, String ngay, int tongML) {
        try {
            ContentValues values = new ContentValues();
            values.put("ngay", ngay);
            values.put("tongml", tongML);

            Cursor c = db.rawQuery("SELECT id FROM bieudo_uongnuoc WHERE ngay = ?", new String[]{ngay});
            if (c.moveToFirst()) {
                int id = c.getInt(0);
                db.update("bieudo_uongnuoc", values, "id = ?", new String[]{String.valueOf(id)});
            } else {
                db.insert("bieudo_uongnuoc", null, values);
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Tổng nước hôm nay từ lichsu_uongnuoc
    public int getTongNuocHomNay() {
        SQLiteDatabase db = null;
        int tong = 0;
        try {
            db = dbHelper.getReadableDatabase();
            tong = getTongNuocHomNay(db);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return tong;
    }

    // helper với db mở sẵn
    private int getTongNuocHomNay(SQLiteDatabase db) {
        int tong = 0;
        String ngay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Cursor c = db.rawQuery("SELECT SUM(luong) FROM lichsu_uongnuoc WHERE ngay = ?", new String[]{ngay});
        if (c.moveToFirst()) tong = c.getInt(0);
        c.close();
        return tong;
    }

    // Lấy lịch sử hôm nay
    public List<String> getLichSuHomNay() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getReadableDatabase();
            String ngay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            Cursor c = db.rawQuery(
                    "SELECT id, gio, luong, ghichu FROM lichsu_uongnuoc WHERE ngay = ? ORDER BY gio DESC",
                    new String[]{ngay});
            while (c.moveToNext()) {
                int id = c.getInt(0);
                String gio = c.getString(1);
                int ml = c.getInt(2);
                String note = c.getString(3);
                list.add(id + " - " + gio + " - " + ml + " ml" +
                        (note != null && !note.isEmpty() ? " (" + note + ")" : ""));
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return list;
    }

    // Tổng nước 7 ngày gần nhất
    public Map<String, Integer> getNuoc7NgayGanNhat() {
        Map<String, Integer> map = new LinkedHashMap<>();
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getReadableDatabase();
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            for (int i = 6; i >= 0; i--) {
                Calendar temp = (Calendar) cal.clone();
                temp.add(Calendar.DAY_OF_MONTH, -i);
                String ngay = sdf.format(temp.getTime());
                Cursor c = db.rawQuery("SELECT SUM(luong) FROM lichsu_uongnuoc WHERE ngay = ?", new String[]{ngay});
                int tong = 0;
                if (c.moveToFirst()) tong = c.getInt(0);
                c.close();
                map.put(ngay, tong);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return map;
    }

    // Lấy mục tiêu nước từ bảng muctieu
    public int getMucTieuNuoc() {
        int mucTieu = 2000;
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT luongnuoc FROM muctieu ORDER BY id DESC LIMIT 1", null);
            if (c.moveToFirst()) mucTieu = c.getInt(0);
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return mucTieu;
    }
}
