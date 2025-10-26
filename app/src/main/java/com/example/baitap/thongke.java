package com.example.baitap;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class thongke extends AppCompatActivity {

    TextView tvNgay, tvTieuThu, tvHapThu, tvNuocUong, tvChenhLech;
    Button btnNgay, btnTuan, btnThang;
    database db;
    MucTieuDAO mucTieuDAO;
    private String loaiThongKe = "ngay"; // mặc định là ngày
    private String ngayHienTai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongke);

        // ánh xạ
        tvNgay = findViewById(R.id.ngay);
        tvTieuThu = findViewById(R.id.tieuthu);
        tvHapThu = findViewById(R.id.hapthu);
        tvNuocUong = findViewById(R.id.nccannap);
        tvChenhLech = findViewById(R.id.nlcannap);

        btnNgay = findViewById(R.id.btnNgay);
        btnTuan = findViewById(R.id.btnTuan);
        btnThang = findViewById(R.id.btnThang);

        db = new database(this);
        // Tự động tạo dữ liệu mẫu nếu database trống
        db.autoSeedIfEmpty();

        mucTieuDAO = new MucTieuDAO(this);

        // Lấy ngày hôm nay
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        ngayHienTai = sdf.format(calendar.getTime());

        // Hiển thị thống kê theo ngày mặc định
        loadThongKeTheoNgay(ngayHienTai);

        // Sự kiện nút Ngày
        btnNgay.setOnClickListener(v -> {
            loaiThongKe = "ngay";
            hienThiDatePicker();
        });

        // Sự kiện nút Tuần
        btnTuan.setOnClickListener(v -> {
            loaiThongKe = "tuan";
            loadThongKeTheoTuan();
        });

        // Sự kiện nút Tháng
        btnThang.setOnClickListener(v -> {
            loaiThongKe = "thang";
            hienThiMonthPicker();
        });

        // Click vào TextView ngày để chọn ngày khác
        tvNgay.setOnClickListener(v -> {
            if (loaiThongKe.equals("ngay")) {
                hienThiDatePicker();
            } else if (loaiThongKe.equals("thang")) {
                hienThiMonthPicker();
            }
        });

        // bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        bottomNavigationView.setSelectedItemId(R.id.menu_thongke);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_muctieu) {
                startActivity(new Intent(this, MucTieuActivity.class));
                return true;
            } else if (id == R.id.menu_tapluyen) {
                startActivity(new Intent(this, tapluyen.class));
                return true;
            } else if (id == R.id.menu_thongke) {
                return true;
            }
        else if (id == R.id.menu_thucan) {
            startActivity(new Intent(this, thucan_activity.class));
            return true;
        }
            else if (id == R.id.menu_uongnuoc) {
                startActivity(new Intent(this, UongNuocActivity.class));
                return true;
            }
            return false;
        });
    }

    private void hienThiDatePicker() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                thongke.this,
                (view, y, m, d) -> {
                    String ngay = y + "-" + String.format("%02d", (m + 1)) + "-" + String.format("%02d", d);
                    loadThongKeTheoNgay(ngay);
                },
                year, month, day
        );
        dialog.show();
    }

    private void hienThiMonthPicker() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                thongke.this,
                (view, y, m, d) -> {
                    String thang = y + "-" + String.format("%02d", (m + 1));
                    loadThongKeTheoThang(thang);
                },
                year, month, 1
        );
        dialog.show();
    }

    private void loadThongKeTheoNgay(String ngay) {
        int caloTieuThu = db.getTongCaloTieuThuTheoNgay(ngay);
        int caloHapThu = db.getTongCaloHapThuTheoNgay(ngay);
        int nuocUong = db.getTongNuocUongTheoNgay(ngay);

        // Lấy mục tiêu

        muctieu mt = mucTieuDAO.getLatest();

        int mucTieuCalo = (mt != null) ? mt.getNangLuong() : 2000;
        int mucTieuNuoc = (mt != null) ? mt.getLuongNuoc() : 2000;

        // Tính chênh lệch (calo hấp thụ - calo tiêu thụ)
        int chenhLechCalo = mucTieuCalo-caloHapThu + caloTieuThu;
        int chenhLechNuoc = mucTieuNuoc - nuocUong;

        // Hiển thị
        tvNgay.setText("Ngày: " + formatNgay(ngay));
        tvTieuThu.setText(caloTieuThu + " kcal");
        tvHapThu.setText(caloHapThu + " kcal");
        tvNuocUong.setText(nuocUong + " ml (Cần thêm: " + (chenhLechNuoc > 0 ? chenhLechNuoc : 0) + " ml)");

        String ketQua = chenhLechCalo > 0
                ? "Thiếu " + chenhLechCalo + " kcal"
                : (chenhLechCalo < 0 ? "Dư " + Math.abs(chenhLechCalo) + " kcal" : "Đủ");
        tvChenhLech.setText(ketQua);
    }

    private void loadThongKeTheoTuan() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String ngayBatDau = sdf.format(cal.getTime());

        cal.add(Calendar.DAY_OF_MONTH, 6);
        String ngayKetThuc = sdf.format(cal.getTime());

        int caloTieuThu = db.getTongCaloTieuThuTheoTuan(ngayBatDau, ngayKetThuc);
        int caloHapThu = db.getTongCaloHapThuTheoTuan(ngayBatDau, ngayKetThuc);
        int nuocUong = db.getTongNuocUongTheoTuan(ngayBatDau, ngayKetThuc);

        // Mục tiêu tuần = mục tiêu ngày * 7

        muctieu mt = mucTieuDAO.getLatest();

        int mucTieuCalo = (mt != null) ? mt.getNangLuong() * 7 : 14000;
        int mucTieuNuoc = (mt != null) ? mt.getLuongNuoc() * 7 : 14000;

        int chenhLechCalo = mucTieuCalo-caloHapThu + caloTieuThu;
        int chenhLechNuoc = mucTieuNuoc - nuocUong;

        tvNgay.setText("Tuần: " + formatNgay(ngayBatDau) + " - " + formatNgay(ngayKetThuc));
        tvTieuThu.setText(caloTieuThu + " kcal");
        tvHapThu.setText(caloHapThu + " kcal");
        tvNuocUong.setText(nuocUong + " ml (Cần thêm: " + (chenhLechNuoc > 0 ? chenhLechNuoc : 0) + " ml)");

        String ketQua = chenhLechCalo > 0
                ? "Thiếu " + chenhLechCalo + " kcal"
                : (chenhLechCalo < 0 ? "Dư " + Math.abs(chenhLechCalo) + " kcal" : "Đủ");
        tvChenhLech.setText(ketQua);
    }

    private void loadThongKeTheoThang(String thang) {
        int caloTieuThu = db.getTongCaloTieuThuTheoThang(thang);
        int caloHapThu = db.getTongCaloHapThuTheoThang(thang);
        int nuocUong = db.getTongNuocUongTheoThang(thang);

        // Mục tiêu tháng = mục tiêu ngày * 30

        muctieu mt = mucTieuDAO.getLatest();

        int mucTieuCalo = (mt != null) ? mt.getNangLuong() * 30 : 60000;
        int mucTieuNuoc = (mt != null) ? mt.getLuongNuoc() * 30 : 60000;

        int chenhLechCalo = mucTieuCalo-caloHapThu + caloTieuThu;
        int chenhLechNuoc = mucTieuNuoc - nuocUong;

        tvNgay.setText("Tháng: " + thang);
        tvTieuThu.setText(caloTieuThu + " kcal");
        tvHapThu.setText(caloHapThu + " kcal");
        tvNuocUong.setText(nuocUong + " ml (Cần thêm: " + (chenhLechNuoc > 0 ? chenhLechNuoc : 0) + " ml)");

        String ketQua = chenhLechCalo > 0
                ? "Thiếu " + chenhLechCalo + " kcal"
                : (chenhLechCalo < 0 ? "Dư " + Math.abs(chenhLechCalo) + " kcal" : "Đủ");
        tvChenhLech.setText(ketQua);
    }

    private String formatNgay(String ngay) {
        try {
            SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat sdfOutput = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return sdfOutput.format(sdfInput.parse(ngay));
        } catch (Exception e) {
            return ngay;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh dữ liệu khi quay lại màn hình
        if (loaiThongKe.equals("ngay")) {
            loadThongKeTheoNgay(ngayHienTai);
        } else if (loaiThongKe.equals("tuan")) {
            loadThongKeTheoTuan();
        }
    }
}