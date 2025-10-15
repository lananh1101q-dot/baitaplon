package com.example.baitap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
public class MucTieuActivity extends AppCompatActivity{
    TextView txtMucTieu, txtBmi, txtChieuCao, txtCanNang, txtNl, txtLuongNuoc, txtNgay;
    Button btnMucTieu;
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

        dao = new MucTieuDAO(this);
        loadLatest();

        btnMucTieu.setOnClickListener(v -> {
            startActivity(new Intent(this, muctieu_themmuctieu.class));
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        bottomNavigationView.setSelectedItemId(R.id.menu_muctieu);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_muctieu) return true;
            else if (id == R.id.menu_tapluyen) {
                startActivity(new Intent(this, tapluyen.class));
                return true;
            } else if (id == R.id.menu_thongke) {
                startActivity(new Intent(this, thongke.class));
                return true;
            }
            else if (id == R.id.menu_uongnuoc) {
                startActivity(new Intent(this, UongNuocActivity.class));
                return true;
            }
            return false;
        });


    }

    private void loadLatest() {
        muctieu m = dao.getLatest();
        if (m != null) {
            txtMucTieu.setText("Mục tiêu: " + m.getTenMucTieu());
            txtBmi.setText(String.format(Locale.getDefault(), "%.1f", m.getBmi()));
            txtChieuCao.setText(String.valueOf(m.getChieuCao()) + " cm");
            txtCanNang.setText(String.valueOf(m.getCanNang()) + " kg");
            txtNl.setText(String.valueOf(m.getNangLuong()) + " kcal/ngày");
            txtLuongNuoc.setText(String.valueOf(m.getLuongNuoc()) + " ml/ngày");

            // chuyển định dạng ngày yyyy-MM-dd -> dd/MM/yyyy để hiển thị
            String d = m.getNgay();
            try {
                Date parsed = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(d);
                String show = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(parsed);
                txtNgay.setText(show);
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
