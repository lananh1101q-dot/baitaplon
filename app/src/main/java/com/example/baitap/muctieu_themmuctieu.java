package com.example.baitap;

import android.content.Intent;
import android.content.SharedPreferences;
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
    int currentUserId;

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

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String currentUsername = prefs.getString("currentUser", null);
        if (currentUsername == null) {
            Toast.makeText(this, "Không tìm thấy người dùng!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        currentUserId = dao.getUserIdByUsername(currentUsername);
        if (currentUserId == -1) {
            Toast.makeText(this, "Không tìm thấy ID người dùng!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mucTieuHienTai = dao.getCurrent(currentUserId);

        if (mucTieuHienTai != null) {
            edtChieuCao.setText(String.valueOf(mucTieuHienTai.getChieuCao()));
            edtCanNang.setText(String.valueOf(mucTieuHienTai.getCanNang()));
            edtTuoi.setText(String.valueOf(mucTieuHienTai.getTuoi()));
            if ("Nam".equalsIgnoreCase(mucTieuHienTai.getGioiTinh())) radNam.setChecked(true);
            else radNu.setChecked(true);
        }

        btnVe.setOnClickListener(v -> finish());

        btnTiep.setOnClickListener(v -> {
            String sCao = edtChieuCao.getText().toString().trim();
            String sNang = edtCanNang.getText().toString().trim();
            String sTuoi = edtTuoi.getText().toString().trim();
            if (sCao.isEmpty() || sNang.isEmpty() || sTuoi.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            double cao = Double.parseDouble(sCao);
            double nang = Double.parseDouble(sNang);
            int tuoi = Integer.parseInt(sTuoi);
            boolean isNam = radNam.isChecked();
            String gioiTinh = isNam ? "Nam" : "Nữ";

            double bmr = isNam
                    ? 88.362 + (13.397 * nang) + (4.799 * cao) - (5.677 * tuoi)
                    : 447.593 + (9.247 * nang) + (3.098 * cao) - (4.330 * tuoi);
            double bmi = nang / Math.pow(cao / 100, 2);
            int nangLuong = (int) Math.round(bmr);
            int luongNuoc = (int) Math.round(nang * 35);
            String ngay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            if (mucTieuHienTai == null) {
                muctieu m = new muctieu(currentUserId, cao, nang, tuoi, gioiTinh,bmi, nangLuong, luongNuoc, ngay);
                dao.insert(m);
                Toast.makeText(this, "Đã lưu mục tiêu mới!", Toast.LENGTH_SHORT).show();
            } else {
                mucTieuHienTai.setChieuCao(cao);
                mucTieuHienTai.setCanNang(nang);
                mucTieuHienTai.setTuoi(tuoi);
                mucTieuHienTai.setGioiTinh(gioiTinh);
                mucTieuHienTai.setBmi(bmi);
                mucTieuHienTai.setNangLuong(nangLuong);
                mucTieuHienTai.setLuongNuoc(luongNuoc);
                mucTieuHienTai.setNgay(ngay);
                dao.update(mucTieuHienTai);
                Toast.makeText(this, "Đã cập nhật mục tiêu!", Toast.LENGTH_SHORT).show();
            }

            startActivity(new Intent(this, MucTieuActivity.class));
            finish();
        });
    }
}
