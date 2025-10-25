package com.example.baitap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

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
        String[] tenBaiTap = {
            "Chạy bộ", "Đạp xe", "Hít đất", "Gập bụng", "Plank", "Yoga", "Bơi lội",
            "Nhảy dây", "Squat", "Lunges", "Đẩy tạ", "Kéo xà", "Nâng tạ", "Aerobic",
            "Zumba", "Kickboxing", "Leo núi", "Tennis", "Cầu lông", "Đá bóng"
        };
        int[] thoiGianBaiTap = {
            30, 45, 20, 15, 10, 60, 40, 25, 20, 25, 35, 30, 40, 50,
            45, 55, 90, 60, 45, 60
        };
        int[] caloBaiTap = {
            250, 400, 150, 120, 80, 200, 350, 300, 180, 200, 280, 250, 320, 380,
            420, 450, 500, 450, 350, 480
        };

        String[][] thucAn = {
            // Sáng (0-9)
            {"Phở bò", "Sáng", "350"},
            {"Bánh mì", "Sáng", "280"},
            {"Xôi gà", "Sáng", "320"},
            {"Bún bò", "Sáng", "380"},
            {"Hủ tiếu", "Sáng", "340"},
            {"Bánh cuốn", "Sáng", "260"},
            {"Cháo sườn", "Sáng", "300"},
            {"Bánh bao", "Sáng", "240"},
            {"Mì trứng", "Sáng", "330"},
            {"Cơm tấm", "Sáng", "400"},

            // Trưa (10-24)
            {"Cơm rang", "Trưa", "450"},
            {"Cơm gà", "Trưa", "500"},
            {"Bún chả", "Trưa", "420"},
            {"Cơm sườn", "Trưa", "480"},
            {"Mì xào", "Trưa", "400"},
            {"Cơm chiên Dương Châu", "Trưa", "520"},
            {"Bún bò Huế", "Trưa", "460"},
            {"Phở xào", "Trưa", "440"},
            {"Cơm cá", "Trưa", "490"},
            {"Bánh canh", "Trưa", "380"},
            {"Bún riêu cua", "Trưa", "430"},
            {"Mì Quảng", "Trưa", "450"},
            {"Cơm gà xối mỡ", "Trưa", "510"},
            {"Bún thịt nướng", "Trưa", "470"},
            {"Cơm rang hải sản", "Trưa", "530"},

            // Tối (25-39)
            {"Salad", "Tối", "200"},
            {"Cơm canh", "Tối", "380"},
            {"Phở gà", "Tối", "340"},
            {"Cháo dinh dưỡng", "Tối", "250"},
            {"Bún riêu", "Tối", "360"},
            {"Súp rau", "Tối", "180"},
            {"Canh chua", "Tối", "220"},
            {"Gỏi cuốn", "Tối", "240"},
            {"Bánh xèo", "Tối", "320"},
            {"Cơm hến", "Tối", "350"},
            {"Bún mắm", "Tối", "390"},
            {"Cháo lòng", "Tối", "310"},
            {"Súp hải sản", "Tối", "280"},
            {"Cơm chiên", "Tối", "420"},
            {"Mì gói xào", "Tối", "330"}
        };

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
            for (int j = 0; j < soBaiTap; j++) {
                int idx = random.nextInt(tenBaiTap.length);
                ContentValues cvTapLuyen = new ContentValues();
                cvTapLuyen.put("tenbaitap", tenBaiTap[idx]);
                cvTapLuyen.put("thoigian", thoiGianBaiTap[idx] + random.nextInt(10) - 5); // +/- 5 phút
                cvTapLuyen.put("calotieuthu", caloBaiTap[idx] + random.nextInt(50) - 25); // +/- 25 calo
                cvTapLuyen.put("ngay", ngay);
                db.insert("tapluyen", null, cvTapLuyen);
            }

            // B. Thêm thức ăn (3-4 bữa mỗi ngày: sáng, trưa, tối, và đôi khi ăn vặt)
            // Bữa sáng
            int idxSang = random.nextInt(10); // 0-9: các món sáng
            ContentValues cvSang = new ContentValues();
            cvSang.put("tenthucan", thucAn[idxSang][0]);
            cvSang.put("buaan", "Sáng");
            cvSang.put("calohapthu", Integer.parseInt(thucAn[idxSang][2]) + random.nextInt(40) - 20);
            cvSang.put("ngay", ngay);
            db.insert("thucan", null, cvSang);

            // Bữa trưa
            int idxTrua = 10 + random.nextInt(15); // 10-24: các món trưa
            ContentValues cvTrua = new ContentValues();
            cvTrua.put("tenthucan", thucAn[idxTrua][0]);
            cvTrua.put("buaan", "Trưa");
            cvTrua.put("calohapthu", Integer.parseInt(thucAn[idxTrua][2]) + random.nextInt(50) - 25);
            cvTrua.put("ngay", ngay);
            db.insert("thucan", null, cvTrua);

            // Bữa tối
            int idxToi = 25 + random.nextInt(15); // 25-39: các món tối
            ContentValues cvToi = new ContentValues();
            cvToi.put("tenthucan", thucAn[idxToi][0]);
            cvToi.put("buaan", "Tối");
            cvToi.put("calohapthu", Integer.parseInt(thucAn[idxToi][2]) + random.nextInt(40) - 20);
            cvToi.put("ngay", ngay);
            db.insert("thucan", null, cvToi);

            // Đôi khi thêm bữa phụ (30% xác suất)
            if (random.nextInt(10) < 3) {
                ContentValues cvPhu = new ContentValues();
                cvPhu.put("tenthucan", "Ăn vặt");
                cvPhu.put("buaan", "Phụ");
                cvPhu.put("calohapthu", 150 + random.nextInt(100)); // 150-250 calo
                cvPhu.put("ngay", ngay);
                db.insert("thucan", null, cvPhu);
            }

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
