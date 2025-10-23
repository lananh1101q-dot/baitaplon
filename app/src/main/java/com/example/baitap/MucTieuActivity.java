package com.example.baitap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MucTieuActivity extends AppCompatActivity {
    TextView txtMucTieu, txtBmi, txtChieuCao, txtCanNang, txtNl, txtLuongNuoc, txtNgay;
    Button btnMucTieu;
    MucTieuDAO dao;
    int currentUserId = 1;

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

        dao = new MucTieuDAO(this);
        loadLatest();

        btnMucTieu.setOnClickListener(v -> startActivity(new Intent(this, muctieu_themmuctieu.class)));

        txtMucTieu.setOnClickListener(v -> {
            Intent i = new Intent(this, muctieu_muctieu.class);
            startActivity(i);
        });

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

    private void loadLatest() {
        muctieu m = dao.getCurrent(currentUserId);
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
