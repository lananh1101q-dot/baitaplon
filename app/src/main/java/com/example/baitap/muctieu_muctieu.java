package com.example.baitap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class muctieu_muctieu extends AppCompatActivity {
    Button btnGiam, btnGiu, btnTang;
    MucTieuDAO dao;
    muctieu mucTieuHienTai;
    int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muctieu_muctieu);

        btnGiam = findViewById(R.id.giamcan);
        btnGiu = findViewById(R.id.giucan);
        btnTang = findViewById(R.id.tangcan);

        dao = new MucTieuDAO(this);

        // ✅ Lấy username từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String currentUsername = prefs.getString("currentUser", null);
        if (currentUsername == null) {
            Toast.makeText(this, "Không tìm thấy người dùng!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // ✅ Lấy userId từ username
        currentUserId = dao.getUserIdByUsername(currentUsername);
        if (currentUserId == -1) {
            Toast.makeText(this, "Không tìm thấy ID người dùng!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // ✅ Lấy mục tiêu hiện tại
        mucTieuHienTai = dao.getCurrent(currentUserId);
        if (mucTieuHienTai == null) {
            Toast.makeText(this, "Chưa có dữ liệu mục tiêu trước đó!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnGiam.setOnClickListener(v -> {
            mucTieuHienTai.setTenMucTieu("Giảm cân");
            mucTieuHienTai.setNangLuong(mucTieuHienTai.getNangLuong() - 300);
            capNhatVaChuyenManHinh();
        });

        btnGiu.setOnClickListener(v -> {
            mucTieuHienTai.setTenMucTieu("Giữ cân");
            capNhatVaChuyenManHinh();
        });

        btnTang.setOnClickListener(v -> {
            mucTieuHienTai.setTenMucTieu("Tăng cân");
            mucTieuHienTai.setNangLuong(mucTieuHienTai.getNangLuong() + 300);
            mucTieuHienTai.setLuongNuoc(mucTieuHienTai.getLuongNuoc() + 200);
            capNhatVaChuyenManHinh();
        });
    }

    private void capNhatVaChuyenManHinh() {
        boolean ok = dao.updatee(mucTieuHienTai);
        if (ok) {
            Toast.makeText(this, "Đã cập nhật mục tiêu: " + mucTieuHienTai.getTenMucTieu(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MucTieuActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
