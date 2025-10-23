package com.example.baitap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class muctieu_muctieu extends AppCompatActivity {
    Button btnGiam, btnGiu, btnTang;
    int currentUserId = 1; // user hiện tại
    MucTieuDAO dao;
    muctieu mucTieuHienTai; // mục tiêu đang có trong DB

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muctieu_muctieu);

        btnGiam = findViewById(R.id.giamcan);
        btnGiu = findViewById(R.id.giucan);
        btnTang = findViewById(R.id.tangcan);

        dao = new MucTieuDAO(this);
        // ✅ Lấy mục tiêu hiện tại của user
        mucTieuHienTai = dao.getCurrent(1);  // nếu app nhiều user thì dùng dao.getCurrent(currentUserId)

        if (mucTieuHienTai == null) {
            Toast.makeText(this, "Chưa có dữ liệu mục tiêu trước đó!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 👉 Giảm cân
        btnGiam.setOnClickListener(v -> {
            mucTieuHienTai.setTenMucTieu("Giảm cân");
            mucTieuHienTai.setNangLuong(mucTieuHienTai.getNangLuong() - 300);
            mucTieuHienTai.setLuongNuoc(mucTieuHienTai.getLuongNuoc());
            capNhatVaChuyenManHinh();
        });

        // 👉 Giữ cân
        btnGiu.setOnClickListener(v -> {
            mucTieuHienTai.setTenMucTieu("Giữ cân");
            // Giữ nguyên năng lượng và nước
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
        // ✅ Cập nhật mục tiêu hiện tại (dựa theo id)
        boolean ok = dao.updatee(mucTieuHienTai);
        if (ok) {
            Toast.makeText(this, "Đã cập nhật mục tiêu: " + mucTieuHienTai.getTenMucTieu(), Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MucTieuActivity.class);
            startActivity(i);
            finish();
        } else {
            Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
