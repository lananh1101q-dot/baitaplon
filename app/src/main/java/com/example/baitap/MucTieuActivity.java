package com.example.baitap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MucTieuActivity extends AppCompatActivity {
    TextView txtMucTieu, txtBmi, txtChieuCao, txtCanNang, txtNl, txtLuongNuoc, txtNgay;
    Button btnMucTieu, btnDangXuat;
    MucTieuDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muctieu);

        txtMucTieu = findViewById(R.id.muctieu);
        txtBmi = findViewById(R.id.bmi);
        txtChieuCao = findViewById(R.id.chieucao_mt);
        txtCanNang = findViewById(R.id.cannang_mt);
        txtNl = findViewById(R.id.nl);
        txtLuongNuoc = findViewById(R.id.luongnuoc);
        txtNgay = findViewById(R.id.ngay);
        btnMucTieu = findViewById(R.id.btmuctieu);
        btnDangXuat = findViewById(R.id.btnDangXuat);

        dao = new MucTieuDAO(this);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String currentUser = prefs.getString("currentUser", null);
        if (currentUser == null) {
            Toast.makeText(this, "Không tìm thấy người dùng!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadLatest(currentUser);

        btnMucTieu.setOnClickListener(v -> startActivity(new Intent(this, muctieu_themmuctieu.class)));

        btnDangXuat.setOnClickListener(v -> {
            prefs.edit().remove("currentUser").apply();
            Intent intent = new Intent(MucTieuActivity.this, dangnhap_activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        txtMucTieu.setOnClickListener(v -> startActivity(new Intent(this, muctieu_muctieu.class)));

        BottomNavigationView nav = findViewById(R.id.bottomNavigationView2);
        nav.setSelectedItemId(R.id.menu_muctieu);
        nav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menu_muctieu) return true;
            else if (item.getItemId() == R.id.menu_tapluyen) startActivity(new Intent(this, tapluyen.class));
            else if (item.getItemId() == R.id.menu_thongke) startActivity(new Intent(this, thongke.class));
            else if (item.getItemId() == R.id.menu_uongnuoc) startActivity(new Intent(this, UongNuocActivity.class));
            else if (item.getItemId() == R.id.menu_thucan) startActivity(new Intent(this, thucan_activity.class));
            return false;
        });
    }

    private void loadLatest(String username) {
        muctieu m = dao.getCurrent(username);
        if (m != null) {
            txtMucTieu.setText("Mục tiêu: " + m.getTenMucTieu());
            txtBmi.setText(String.format(Locale.getDefault(), "%.1f", m.getBmi()));
            txtChieuCao.setText(m.getChieuCao() + " cm");
            txtCanNang.setText(m.getCanNang() + " kg");
            txtNl.setText(m.getNangLuong() + " kcal/ngày");
            txtLuongNuoc.setText(m.getLuongNuoc() + " ml/ngày");

            try {
                Date parsed = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(m.getNgay());
                txtNgay.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(parsed));
            } catch (Exception e) {
                txtNgay.setText(m.getNgay());
            }
        } else {
            txtMucTieu.setText("-");
            txtBmi.setText("-");
            txtChieuCao.setText("-");
            txtCanNang.setText("-");
            txtNl.setText("-");
            txtLuongNuoc.setText("-");
            txtNgay.setText("-");
        }
    }
}
