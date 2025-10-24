package com.example.baitap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class muctieu_muctieu extends AppCompatActivity {
    Button btnGiam, btnGiu, btnTang;
    int currentUserId = 1; // 🟢 Mặc định app chỉ có 1 người dùng
    MucTieuDAO dao;
    muctieu mucTieuHienTai; // mục tiêu hiện tại trong DB

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muctieu_muctieu);

        // Ánh xạ view
        btnGiam = findViewById(R.id.giamcan);
        btnGiu = findViewById(R.id.giucan);
        btnTang = findViewById(R.id.tangcan);

        dao = new MucTieuDAO(this);

        // 🟢 Lấy mục tiêu hiện tại của user mặc định (id = 1)
        mucTieuHienTai = dao.getCurrent(currentUserId);

        if (mucTieuHienTai == null) {
            Toast.makeText(this, "Chưa có dữ liệu mục tiêu trước đó!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 👉 Giảm cân
        btnGiam.setOnClickListener(v -> {
            mucTieuHienTai.setTenMucTieu("Giảm cân");
            mucTieuHienTai.setNangLuong(mucTieuHienTai.getNangLuong() - 300);
            capNhatVaChuyenManHinh();
        });

        // 👉 Giữ cân
        btnGiu.setOnClickListener(v -> {
            mucTieuHienTai.setTenMucTieu("Giữ cân");
            capNhatVaChuyenManHinh();
        });

        // 👉 Tăng cân
        btnTang.setOnClickListener(v -> {
            mucTieuHienTai.setTenMucTieu("Tăng cân");
            mucTieuHienTai.setNangLuong(mucTieuHienTai.getNangLuong() + 300);
            mucTieuHienTai.setLuongNuoc(mucTieuHienTai.getLuongNuoc() + 200);
            capNhatVaChuyenManHinh();
        });
    }

    private void capNhatVaChuyenManHinh() {
        // 🟢 Cập nhật dữ liệu trong DB
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
