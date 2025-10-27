package com.example.baitap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class muctieu_themmuctieu extends AppCompatActivity {
    EditText edtChieuCao, edtCanNang, edtTuoi;
    RadioButton radNam, radNu;
    Button btnTiep, btnVe;
    MucTieuDAO dao;
    muctieu mucTieuHienTai;
    int currentUserId = 1; // giả định user hiện tại

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muctieu_thaydoi);

        edtChieuCao = findViewById(R.id.chieucao);
        edtCanNang = findViewById(R.id.cannang);
        edtTuoi = findViewById(R.id.tuoi);
        radNam = findViewById(R.id.nam);
        radNu = findViewById(R.id.nu);
        btnTiep = findViewById(R.id.tiep);
        btnVe = findViewById(R.id.ve);

        dao = new MucTieuDAO(this);
        mucTieuHienTai = dao.getCurrent(currentUserId);

        if (mucTieuHienTai != null) {
            edtChieuCao.setText(String.valueOf(mucTieuHienTai.getChieuCao()));
            edtCanNang.setText(String.valueOf(mucTieuHienTai.getCanNang()));
            edtTuoi.setText(String.valueOf(mucTieuHienTai.getTuoi()));
            if (mucTieuHienTai.isGioiTinhNam()) radNam.setChecked(true);
            else radNu.setChecked(true);
        }

        btnVe.setOnClickListener(v -> finish());

        btnTiep.setOnClickListener(v -> {
            String sCao = edtChieuCao.getText().toString().trim();
            String sNang = edtCanNang.getText().toString().trim();
            String sTuoi = edtTuoi.getText().toString().trim();
            // 🔹 Kiểm tra nhập trống
            if (sCao.isEmpty() || sNang.isEmpty() || sTuoi.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            double cao, nang;
            int tuoi;

            try {
                cao = Double.parseDouble(sCao);
                nang = Double.parseDouble(sNang);
                tuoi = Integer.parseInt(sTuoi);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Dữ liệu phải là số hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (cao < 100 || cao > 300) {
                Toast.makeText(this, "Chiều cao phải trong khoảng 100–300 cm", Toast.LENGTH_SHORT).show();
                return;
            }
            if (nang < 30 || nang > 300) {
                Toast.makeText(this, "Cân nặng phải trong khoảng 30–300 kg", Toast.LENGTH_SHORT).show();
                return;
            }
            if (tuoi < 5 || tuoi > 120) {
                Toast.makeText(this, "Tuổi phải trong khoảng 5–120", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isNam = radNam.isChecked();

            double bmr = isNam
                    ? 88.362 + (13.397 * nang) + (4.799 * cao) - (5.677 * tuoi)
                    : 447.593 + (9.247 * nang) + (3.098 * cao) - (4.330 * tuoi);
            double bmi = nang / Math.pow(cao / 100, 2);
            int nangLuong = (int) Math.round(bmr);
            int luongNuoc = (int) Math.round(nang * 35);
            String ngay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            // Nếu chưa có mục tiêu -> tạo mới và sang chọn chế độ
            if (mucTieuHienTai == null) {
                muctieu m = new muctieu(currentUserId, cao, nang, tuoi, isNam, bmi, nangLuong, luongNuoc, ngay);
                dao.insert(m);
                Intent i = new Intent(this, muctieu_muctieu.class);
                i.putExtra("chieu", cao);
                i.putExtra("can", nang);
                i.putExtra("bmi", bmi);
                i.putExtra("nangluong", nangLuong);
                i.putExtra("luongnuoc", luongNuoc);
                i.putExtra("ngayDb", ngay);
                startActivity(i);
            } else {
                // Nếu đã có mục tiêu -> cập nhật và quay lại xem
                mucTieuHienTai.update(cao, nang, tuoi, isNam, bmi, nangLuong, luongNuoc, ngay);
                dao.update(mucTieuHienTai);
                startActivity(new Intent(this, MucTieuActivity.class));
            }
            finish();
        });
    }
}
