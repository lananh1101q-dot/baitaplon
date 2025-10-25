package com.example.baitap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDAO {
    private database dbHelper;

    public UserDAO(Context context) {
        dbHelper = new database(context);
    }

    // Đăng ký user mới
    public boolean dangKy(String username, String password, String email) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("username", username);
            values.put("password", password);
            values.put("email", email);
            long id = db.insert("users", null, values);
            return id != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (db != null) db.close();
        }
    }

    // Kiểm tra đăng nhập
    public boolean dangNhap(String username, String password) {
        SQLiteDatabase db = null;
        boolean success = false;
        try {
            db = dbHelper.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM users WHERE username=? AND password=?",
                    new String[]{username, password});
            success = c.moveToFirst();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return success;
    }

    // Quên mật khẩu theo email
    public String layMatKhau(String email) {
        SQLiteDatabase db = null;
        String pass = null;
        try {
            db = dbHelper.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT password FROM users WHERE email=?",
                    new String[]{email});
            if (c.moveToFirst()) {
                pass = c.getString(0);
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return pass;
    }

    // Kiểm tra user tồn tại
    public boolean userTonTai(String username) {
        SQLiteDatabase db = null;
        boolean exists = false;
        try {
            db = dbHelper.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT id FROM users WHERE username=?", new String[]{username});
            exists = c.moveToFirst();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return exists;
    }

    public boolean emailTonTai(String email) {
        SQLiteDatabase db = null;
        boolean exists = false;
        try {
            db = dbHelper.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT id FROM users WHERE email=?", new String[]{email});
            exists = c.moveToFirst();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return exists;
    }
}
