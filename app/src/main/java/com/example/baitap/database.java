package com.example.baitap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "suckhoe.db";
    private static final int DATABASE_VERSION = 3; // tăng version nếu thay đổi schema

    public database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Bảng mục tiêu
        db.execSQL("CREATE TABLE IF NOT EXISTS muctieu (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tenmuctieu TEXT, " +
                "chieucao REAL, " +
                "cannang REAL, " +
                "bmi REAL, " +
                "nangluong INTEGER, " +
                "luongnuoc INTEGER, " +
                "ngay TEXT DEFAULT (date('now'))" +
                ")");

        // Bảng tập luyện
        db.execSQL("CREATE TABLE IF NOT EXISTS tapluyen (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tenbaitap TEXT, " +
                "thoigian INTEGER, " +
                "calotieuthu INTEGER, " +
                "ngay TEXT DEFAULT (date('now'))" +
                ")");

        // Bảng thức ăn
        db.execSQL("CREATE TABLE IF NOT EXISTS thucan (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tenthucan TEXT, " +
                "buaan TEXT, " +
                "calohapthu INTEGER, " +
                "ngay TEXT DEFAULT (date('now'))" +
                ")");

        // Bảng uống nước
        db.execSQL("CREATE TABLE IF NOT EXISTS uongnuoc (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "luongnuoc INTEGER, " +
                "ngay TEXT DEFAULT (date('now'))" +
                ")");

        // Bảng thống kê (lưu tổng theo ngày)
        db.execSQL("CREATE TABLE IF NOT EXISTS thongke (" +
                "ngay TEXT PRIMARY KEY, " +
                "tenmuctieu TEXT, " +
                "tongcalotieuthu INTEGER, " +
                "tongcalohapthu INTEGER, " +
                "tenthucan TEXT, " +
                "buaan TEXT" +
                ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS lichnhacnuoc (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "thoigian TEXT, " +
                "noidung TEXT, " +
                "kichhoat INTEGER DEFAULT 1" +
                ")");

        // Bảng lịch sử uống nước
        db.execSQL("CREATE TABLE IF NOT EXISTS lichsu_uongnuoc (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ngay TEXT DEFAULT (date('now')), " +
                "gio TEXT, " +
                "luong INTEGER, " +
                "ghichu TEXT" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // đơn giản: drop + tạo lại (dễ debug). Trước khi deploy thực tế, migrate dữ liệu nếu cần.
        db.execSQL("DROP TABLE IF EXISTS muctieu");
        db.execSQL("DROP TABLE IF EXISTS tapluyen");
        db.execSQL("DROP TABLE IF EXISTS thucan");
        db.execSQL("DROP TABLE IF EXISTS uongnuoc");
        db.execSQL("DROP TABLE IF EXISTS thongke");
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS muctieu");
        db.execSQL("DROP TABLE IF EXISTS tapluyen");
        db.execSQL("DROP TABLE IF EXISTS thucan");
        db.execSQL("DROP TABLE IF EXISTS uongnuoc");
        db.execSQL("DROP TABLE IF EXISTS thongke");
        db.execSQL("DROP TABLE IF EXISTS lichnhacnuoc");
        db.execSQL("DROP TABLE IF EXISTS lichsu_uongnuoc");
        onCreate(db);
    }

}
