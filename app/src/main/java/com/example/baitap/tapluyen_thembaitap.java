package com.example.baitap;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.*;

public class tapluyen_thembaitap extends AppCompatActivity {

    private Spinner spnTenBaiTap, spnThoiGian;
    private TextView txtCalo;
    private Button btnSave, btnCancel;
    private TapLuyenDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tapluyen_thembaitap);

        spnTenBaiTap = findViewById(R.id.spnTenBaiTap);
        spnThoiGian = findViewById(R.id.spnThoiGian);
        txtCalo = findViewById(R.id.edtCalo);
        btnSave = findViewById(R.id.btnLuu);
        btnCancel = findViewById(R.id.btnHuy);
        dao = new TapLuyenDAO(this);

        // Gán dữ liệu cho spinner
        spnTenBaiTap.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new ArrayList<>(tapluyen_DanhMucBaiTap.CALO_MAP.keySet())
        ));

        spnThoiGian.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                Arrays.asList(10, 20, 30, 40, 50, 60, 120, 180)
        ));

        // Tính calo khi chọn bài tập hoặc thời gian
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tenBaiTap = spnTenBaiTap.getSelectedItem().toString();
                int thoiGian = (int) spnThoiGian.getSelectedItem();
                int calo = tapluyen_DanhMucBaiTap.tinhCalo(tenBaiTap, thoiGian);
                txtCalo.setText("Calo tiêu thụ: " + calo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

        spnTenBaiTap.setOnItemSelectedListener(listener);
        spnThoiGian.setOnItemSelectedListener(listener);

        // Lưu bài tập
        btnSave.setOnClickListener(v -> {
            String tenBaiTap = spnTenBaiTap.getSelectedItem().toString();
            int thoiGian = (int) spnThoiGian.getSelectedItem();
            int calo = tapluyen_DanhMucBaiTap.tinhCalo(tenBaiTap, thoiGian);
            String ngay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            dao.insert(new tapluyen_employ(0, tenBaiTap, thoiGian, calo, ngay));
            Toast.makeText(this, "Đã lưu bài tập: " + tenBaiTap, Toast.LENGTH_SHORT).show();
            finish();
        });

        btnCancel.setOnClickListener(v -> finish());
    }
}
