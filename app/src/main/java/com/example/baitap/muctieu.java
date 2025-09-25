package com.example.baitap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class muctieu extends AppCompatActivity {

    TextView txtNl, txtBmi, txtChieuCao, txtCanNang, txtLuongNuoc, txtMucTieu, txtNgay;
    Button btnMucTieu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muctieu); // layout3 bạn gửi

        // Ánh xạ view
        txtNl = findViewById(R.id.nl);
        txtBmi = findViewById(R.id.bmi);
        txtChieuCao = findViewById(R.id.chieucao_mt);
        txtCanNang = findViewById(R.id.cannang_mt);
        txtLuongNuoc = findViewById(R.id.luongnuoc);
        txtMucTieu = findViewById(R.id.muctieu);
        txtNgay = findViewById(R.id.ngay);
        btnMucTieu = findViewById(R.id.btmuctieu);

        // ✅ Hiển thị ngày/tháng/năm hiện tại
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = sdf.format(calendar.getTime());
        txtNgay.setText(currentDate);

        // ✅ Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        double chieuCao = intent.getDoubleExtra("chieucao", 0);
        double canNang = intent.getDoubleExtra("cannang", 0);
        double bmr = intent.getDoubleExtra("bmr", 0);
        String mucTieu = intent.getStringExtra("muctieu");

        // ✅ Hiển thị dữ liệu
        txtChieuCao.setText(chieuCao + " cm");
        txtCanNang.setText(canNang + " kg");
        txtNl.setText(Math.round(bmr) + " kcal/ngày");

        double bmi = canNang / Math.pow(chieuCao / 100, 2);
        txtBmi.setText(String.format(Locale.getDefault(), "%.1f", bmi));

        double nuoc = canNang * 35; // ml/ngày
        txtLuongNuoc.setText((int) nuoc + " ml/ngày");

        txtMucTieu.setText("Mục tiêu: " + mucTieu);

        // ✅ Nút quay về Layout1
        btnMucTieu.setOnClickListener(v -> {
            Intent i = new Intent(muctieu.this, muctieu_chiso.class);
            startActivity(i);
            finish();
        });
    }
}
