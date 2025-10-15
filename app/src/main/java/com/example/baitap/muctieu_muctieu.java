package com.example.baitap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class muctieu_muctieu extends AppCompatActivity {
    Button btnGiam, btnGiu, btnTang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muctieu_muctieu);
        double chieu = getIntent().getDoubleExtra("chieu", 0);
        double can = getIntent().getDoubleExtra("can", 0);
        double bmi = getIntent().getDoubleExtra("bmi", 0);
        int nangluong = getIntent().getIntExtra("nangluong", 0);
        int luongnuoc = getIntent().getIntExtra("luongnuoc", 0);
        String ngayDb = getIntent().getStringExtra("ngayDb");

        btnGiam = findViewById(R.id.giamcan);
        btnGiu = findViewById(R.id.giucan);
        btnTang = findViewById(R.id.tangcan);

        btnGiam.setOnClickListener(v -> saveMucTieu("Giảm cân", chieu, can, bmi, nangluong, luongnuoc, ngayDb));
        btnGiu.setOnClickListener(v -> saveMucTieu("Giữ cân", chieu, can, bmi, nangluong, luongnuoc, ngayDb));
        btnTang.setOnClickListener(v -> saveMucTieu("Tăng cân", chieu, can, bmi, nangluong, luongnuoc, ngayDb));
    }

    // ✅ Phương thức lưu dữ liệu vào database
    private void saveMucTieu(String tenMucTieu, double chieu, double can, double bmi,
                             int nangluong, int luongnuoc, String ngayDb) {
        MucTieuDAO dao = new MucTieuDAO(this);
        muctieu m = new muctieu(0,tenMucTieu, chieu, can, bmi, nangluong, luongnuoc, ngayDb );
        long id = dao.insert(m);
        if (id > 0) {
            Toast.makeText(this, "Đã lưu mục tiêu: " + tenMucTieu, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Lưu thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}