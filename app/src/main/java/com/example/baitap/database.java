package com.example.baitap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "suckhoe.db";
    private static final int DATABASE_VERSION = 4; // tăng version nếu thay đổi schema
    public static final String TBL_LOAI = "loai";
    public static final String TBL_MONAN = "monan";
    public static final String TBL_FOODLOG = "food_log";

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
        // Loai
        db.execSQL("CREATE TABLE " + TBL_LOAI + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ten TEXT NOT NULL)");

        // MonAn (each mon has a loai_id)
        db.execSQL("CREATE TABLE " + TBL_MONAN + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ten TEXT NOT NULL, " +
                "calo INTEGER NOT NULL, " +
                "loai_id INTEGER NOT NULL, " +
                "FOREIGN KEY(loai_id) REFERENCES " + TBL_LOAI + "(id) ON DELETE CASCADE)");

        // Food log (each saved entry references monan)
        db.execSQL("CREATE TABLE " + TBL_FOODLOG + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "monan_id INTEGER NOT NULL, " +
                "ngay TEXT NOT NULL, " + // format dd/MM/yyyy
                "luot INTEGER DEFAULT 1, " + // optional count if you want multiples
                "note TEXT, " +
                "FOREIGN KEY(monan_id) REFERENCES " + TBL_MONAN + "(id) ON DELETE CASCADE)");

        // Insert default categories and sample foods

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


    // seed dữ liệu mặc định
    seedInitialData(db);
}

@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + TBL_FOODLOG);
    db.execSQL("DROP TABLE IF EXISTS " + TBL_MONAN);
    db.execSQL("DROP TABLE IF EXISTS " + TBL_LOAI);
    db.execSQL("DROP TABLE IF EXISTS muctieu");
    db.execSQL("DROP TABLE IF EXISTS tapluyen");
    db.execSQL("DROP TABLE IF EXISTS uongnuoc");
    db.execSQL("DROP TABLE IF EXISTS thongke");
    db.execSQL("DROP TABLE IF EXISTS lichnhacnuoc");
    db.execSQL("DROP TABLE IF EXISTS lichsu_uongnuoc");
    onCreate(db);
}

// ==========================================
// ========== HÀM TẠO DỮ LIỆU MẪU ==========
// ==========================================

private void seedInitialData(SQLiteDatabase db) {
    // Loại
    long idChay = insertLoai(db, "Món ăn chay");
    long idNhieu = insertLoai(db, "Món ăn nhiều calo");
    long idIt = insertLoai(db, "Món ăn ít calo");
    long idBinh = insertLoai(db, "Món ăn bình thường");

    // Món ăn
    insertMonAn(db, "Sườn chay", 150, (int) idChay);
    insertMonAn(db, "Đậu hũ chiên", 180, (int) idChay);
    insertMonAn(db, "Giò chay", 200, (int) idChay);
    insertMonAn(db, "Cơm gà chiên", 600, (int) idNhieu);
    insertMonAn(db, "Bánh mì bơ đường", 320, (int) idNhieu);
    insertMonAn(db, "Salad rau", 80, (int) idIt);
    insertMonAn(db, "Sữa chua không đường", 90, (int) idIt);
    insertMonAn(db, "Bánh mì", 200, (int) idBinh);
    insertMonAn(db, "Phở bò", 350, (int) idBinh);
}

private long insertLoai(SQLiteDatabase db, String ten) {
    ContentValues v = new ContentValues();
    v.put("ten", ten);
    return db.insert(TBL_LOAI, null, v);
}

private long insertMonAn(SQLiteDatabase db, String ten, int calo, int loaiId) {
    ContentValues v = new ContentValues();
    v.put("ten", ten);
    v.put("calo", calo);
    v.put("loai_id", loaiId);
    return db.insert(TBL_MONAN, null, v);
}

// ==========================================
// =========== HÀM TRUY VẤN DỮ LIỆU =========
// ==========================================

public List<model_Loai> getAllLoai() {
    List<model_Loai> list = new ArrayList<>();
    SQLiteDatabase db = getReadableDatabase();
    Cursor c = db.rawQuery("SELECT id, ten FROM " + TBL_LOAI, null);
    while (c.moveToNext()) {
        list.add(new model_Loai(c.getInt(0), c.getString(1)));
    }
    c.close();
    return list;
}

public List<model_Monan> getMonAnByLoai(int loaiId) {
    List<model_Monan> list = new ArrayList<>();
    SQLiteDatabase db = getReadableDatabase();
    Cursor c = db.rawQuery("SELECT id, ten, calo, loai_id FROM " + TBL_MONAN + " WHERE loai_id = ?",
            new String[]{String.valueOf(loaiId)});
    while (c.moveToNext()) {
        list.add(new model_Monan(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3)));
    }
    c.close();
    return list;
}

public model_Monan getMonAnById(int id) {
    SQLiteDatabase db = getReadableDatabase();
    Cursor c = db.rawQuery("SELECT id, ten, calo, loai_id FROM " + TBL_MONAN + " WHERE id = ?",
            new String[]{String.valueOf(id)});
    model_Monan m = null;
    if (c.moveToFirst()) {
        m = new model_Monan(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3));
    }
    c.close();
    return m;
}

// ==========================================
// =========== NHẬT KÝ ĂN UỐNG =============
// ==========================================

public long insertFoodLog(int monanId, String ngay, String note) {
    SQLiteDatabase db = getWritableDatabase();
    ContentValues v = new ContentValues();
    v.put("monan_id", monanId);
    v.put("ngay", ngay);
    v.put("note", note);
    return db.insert(TBL_FOODLOG, null, v);
}

public List<model_FoodLog> getFoodLogsByDate(String ngay) {
    List<model_FoodLog> list = new ArrayList<>();
    SQLiteDatabase db = getReadableDatabase();
    String sql = "SELECT fl.id, fl.monan_id, m.ten, m.calo, fl.ngay, fl.note " +
            "FROM " + TBL_FOODLOG + " fl " +
            "JOIN " + TBL_MONAN + " m ON fl.monan_id = m.id " +
            "WHERE fl.ngay = ? ORDER BY fl.id DESC";
    Cursor c = db.rawQuery(sql, new String[]{ngay});
    while (c.moveToNext()) {
        list.add(new model_FoodLog(
                c.getInt(0),
                c.getInt(1),
                c.getString(2),
                c.getInt(3),
                c.getString(4),
                c.getString(5)
        ));
    }
    c.close();
    return list;
}

public int updateFoodLog(int id, int monanId, String note) {
    SQLiteDatabase db = getWritableDatabase();
    ContentValues v = new ContentValues();
    v.put("monan_id", monanId);
    v.put("note", note);
    return db.update(TBL_FOODLOG, v, "id = ?", new String[]{String.valueOf(id)});
}

public int deleteFoodLog(int id) {
    SQLiteDatabase db = getWritableDatabase();
    return db.delete(TBL_FOODLOG, "id = ?", new String[]{String.valueOf(id)});
}

// Tổng calo trong ngày
public int getTongCaloNgay(String ngay) {
    SQLiteDatabase db = getReadableDatabase();
    Cursor c = db.rawQuery(
            "SELECT SUM(m.calo) FROM " + TBL_FOODLOG + " f " +
                    "JOIN " + TBL_MONAN + " m ON f.monan_id = m.id " +
                    "WHERE f.ngay = ?",
            new String[]{ngay});
    int tong = 0;
    if (c.moveToFirst()) tong = c.getInt(0);
    c.close();
    return tong;
}


    public boolean isDatabaseEmpty() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM tapluyen", null);
        int count = 0;
        if (c.moveToFirst()) {
            count = c.getInt(0);
        }
        c.close();
        return count == 0;
    }


    public int getTongCaloTieuThuTheoNgay(String ngay) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(calotieuthu) FROM tapluyen WHERE ngay = ?", new String[]{ngay});
        int tong = 0;
        if (c.moveToFirst()) {
            tong = c.getInt(0);
        }
        c.close();
        return tong;
    }


    public int getTongCaloHapThuTheoNgay(String ngay) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(calohapthu) FROM thucan WHERE ngay = ?", new String[]{ngay});
        int tong = 0;
        if (c.moveToFirst()) {
            tong = c.getInt(0);
        }
        c.close();
        return tong;
    }


    public int getTongNuocUongTheoNgay(String ngay) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(luongnuoc) FROM uongnuoc WHERE ngay = ?", new String[]{ngay});
        int tong = 0;
        if (c.moveToFirst()) {
            tong = c.getInt(0);
        }
        c.close();
        return tong;
    }


    public int getTongCaloTieuThuTheoTuan(String ngayBatDau, String ngayKetThuc) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(calotieuthu) FROM tapluyen WHERE ngay BETWEEN ? AND ?",
            new String[]{ngayBatDau, ngayKetThuc});
        int tong = 0;
        if (c.moveToFirst()) {
            tong = c.getInt(0);
        }
        c.close();
        return tong;
    }


    public int getTongCaloHapThuTheoTuan(String ngayBatDau, String ngayKetThuc) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(calohapthu) FROM thucan WHERE ngay BETWEEN ? AND ?",
            new String[]{ngayBatDau, ngayKetThuc});
        int tong = 0;
        if (c.moveToFirst()) {
            tong = c.getInt(0);
        }
        c.close();
        return tong;
    }


    public int getTongNuocUongTheoTuan(String ngayBatDau, String ngayKetThuc) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(luongnuoc) FROM uongnuoc WHERE ngay BETWEEN ? AND ?",
            new String[]{ngayBatDau, ngayKetThuc});
        int tong = 0;
        if (c.moveToFirst()) {
            tong = c.getInt(0);
        }
        c.close();
        return tong;
    }

    public int getTongCaloTieuThuTheoThang(String thang) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(calotieuthu) FROM tapluyen WHERE strftime('%Y-%m', ngay) = ?",
            new String[]{thang});
        int tong = 0;
        if (c.moveToFirst()) {
            tong = c.getInt(0);
        }
        c.close();
        return tong;
    }


    public int getTongCaloHapThuTheoThang(String thang) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(calohapthu) FROM thucan WHERE strftime('%Y-%m', ngay) = ?",
            new String[]{thang});
        int tong = 0;
        if (c.moveToFirst()) {
            tong = c.getInt(0);
        }
        c.close();
        return tong;
    }

    // Lấy tổng nước uống theo tháng
    public int getTongNuocUongTheoThang(String thang) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(luongnuoc) FROM uongnuoc WHERE strftime('%Y-%m', ngay) = ?",
            new String[]{thang});
        int tong = 0;
        if (c.moveToFirst()) {
            tong = c.getInt(0);
        }
        c.close();
        return tong;
    }

    // ===== SEED DATA - TẠO DỮ LIỆU MẪU TỰ ĐỘNG =====

    // Kiểm tra xem đã có dữ liệu chưa


    // Tạo dữ liệu mẫu cho 90 ngày (3 tháng)
    public void seedData() {
        SQLiteDatabase db = getWritableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Random random = new Random();

        // 1. Thêm mục tiêu (nếu chưa có)
        Cursor cMucTieu = db.rawQuery("SELECT COUNT(*) FROM muctieu", null);
        int countMucTieu = 0;
        if (cMucTieu.moveToFirst()) {
            countMucTieu = cMucTieu.getInt(0);
        }
        cMucTieu.close();

        if (countMucTieu == 0) {
            ContentValues cvMucTieu = new ContentValues();
            cvMucTieu.put("tenmuctieu", "Giảm cân");
            cvMucTieu.put("chieucao", 170.0);
            cvMucTieu.put("cannang", 70.0);
            cvMucTieu.put("bmi", 24.2);
            cvMucTieu.put("nangluong", 2000);
            cvMucTieu.put("luongnuoc", 2000);
            cvMucTieu.put("ngay", sdf.format(Calendar.getInstance().getTime()));
            db.insert("muctieu", null, cvMucTieu);
        }

        // 2. Tạo dữ liệu cho 90 ngày gần nhất (3 tháng)
//        String[] tenBaiTap = {
//            "Chạy bộ", "Đạp xe", "Hít đất", "Gập bụng", "Plank", "Yoga", "Bơi lội",
//            "Nhảy dây", "Squat", "Lunges", "Đẩy tạ", "Kéo xà", "Nâng tạ", "Aerobic",
//            "Zumba", "Kickboxing", "Leo núi", "Tennis", "Cầu lông", "Đá bóng"
//        };
//        int[] thoiGianBaiTap = {
//            30, 45, 20, 15, 10, 60, 40, 25, 20, 25, 35, 30, 40, 50,
//            45, 55, 90, 60, 45, 60
//        };
//        int[] caloBaiTap = {
//            250, 400, 150, 120, 80, 200, 350, 300, 180, 200, 280, 250, 320, 380,
//            420, 450, 500, 450, 350, 480
//        };


        int[] luongNuocOptions = {200, 250, 300, 350, 400, 450, 500, 600};

        for (int i = 0; i < 90; i++) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -i);
            String ngay = sdf.format(cal.getTime());

            // Kiểm tra xem ngày này đã có dữ liệu chưa
            Cursor cCheck = db.rawQuery("SELECT COUNT(*) FROM tapluyen WHERE ngay = ?", new String[]{ngay});
            int existingData = 0;
            if (cCheck.moveToFirst()) {
                existingData = cCheck.getInt(0);
            }
            cCheck.close();

            if (existingData > 0) {
                continue; // Đã có dữ liệu, bỏ qua ngày này
            }

            // A. Thêm bài tập luyện (3-5 bài mỗi ngày)
            int soBaiTap = 3 + random.nextInt(3); // 3-5 bài
//            for (int j = 0; j < soBaiTap; j++) {
//                int idx = random.nextInt(tenBaiTap.length);
//                ContentValues cvTapLuyen = new ContentValues();
//                cvTapLuyen.put("tenbaitap", tenBaiTap[idx]);
//                cvTapLuyen.put("thoigian", thoiGianBaiTap[idx] + random.nextInt(10) - 5); // +/- 5 phút
//                cvTapLuyen.put("calotieuthu", caloBaiTap[idx] + random.nextInt(50) - 25); // +/- 25 calo
//                cvTapLuyen.put("ngay", ngay);
//                db.insert("tapluyen", null, cvTapLuyen);
//            }



            // C. Thêm lượng nước uống (5-8 lần mỗi ngày)
            int soLanUong = 5 + random.nextInt(4); // 5-8 lần
            for (int j = 0; j < soLanUong; j++) {
                ContentValues cvUongNuoc = new ContentValues();
                cvUongNuoc.put("luongnuoc", luongNuocOptions[random.nextInt(luongNuocOptions.length)]);
                cvUongNuoc.put("ngay", ngay);
                db.insert("uongnuoc", null, cvUongNuoc);
            }
        }

        db.close();
    }

    // Gọi hàm này để tự động seed dữ liệu khi cần
    public void autoSeedIfEmpty() {
        if (isDatabaseEmpty()) {
            seedData();
        }
    }
}
