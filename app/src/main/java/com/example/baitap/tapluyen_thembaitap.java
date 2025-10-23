package com.example.baitap;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class tapluyen_thembaitap extends AppCompatActivity {

    private TextView txtCalo;
    private Button btnSave, btnCancel;
    private TapLuyenDAO dao;
    Spinner spnTenBaiTap, spnThoiGian;

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

        // Danh sách bài tập
        ArrayAdapter<String> adapterBaiTap = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                tapluyen_DanhMucBaiTap.CALO_MAP.keySet().toArray(new String[0])
        );
        adapterBaiTap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTenBaiTap.setAdapter(adapterBaiTap);

        // Danh sách thời gian (phút)
        ArrayAdapter<Integer> adapterThoiGian = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                Arrays.asList(10,20, 30,40,50, 60,120,180)
        );
        adapterThoiGian.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnThoiGian.setAdapter(adapterThoiGian);

        // Khi người dùng chọn xong, hiển thị calo tạm tính
        spnTenBaiTap.setOnItemSelectedListener(new SimpleOnItemSelectedListener());
        spnThoiGian.setOnItemSelectedListener(new SimpleOnItemSelectedListener());

        // Khi nhấn Lưu
        btnSave.setOnClickListener(v -> {
            String tenBaiTap = spnTenBaiTap.getSelectedItem().toString();
            int thoiGian = (int) spnThoiGian.getSelectedItem();
            int calo = tapluyen_DanhMucBaiTap.tinhCalo(tenBaiTap, thoiGian);

            String ngay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(Calendar.getInstance().getTime());

            // Tạo đối tượng tapluyen_employ để lưu
            tapluyen_employ tl = new tapluyen_employ(0, tenBaiTap, thoiGian, calo, ngay);
            dao.insert(tl);

            txtCalo.setText("Calo tiêu thụ: " + calo);
            Toast.makeText(this, "Đã lưu bài tập: " + tenBaiTap, Toast.LENGTH_SHORT).show();
        });

        btnCancel.setOnClickListener(v -> finish());
    }

    private class SimpleOnItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
            if (spnTenBaiTap.getSelectedItem() != null && spnThoiGian.getSelectedItem() != null) {
                String tenBaiTap = spnTenBaiTap.getSelectedItem().toString();
                int thoiGian = (int) spnThoiGian.getSelectedItem();
                int calo = tapluyen_DanhMucBaiTap.tinhCalo(tenBaiTap, thoiGian);
                txtCalo.setText("Calo tiêu thụ: " + calo);
            }
        }

        @Override
        public void onNothingSelected(android.widget.AdapterView<?> parent) { }
    }

    private String getToday() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }
}
