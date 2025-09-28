//package com.example.baitap;
//
//
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class tapluyen_thembaitap extends AppCompatActivity {
//
//    private EditText edtTen, edtThoigian, edtCalo;
//    private Button btnLuu, btnHuy;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tapluyen_thembaitap);
//
//        edtTen = findViewById(R.id.edtTen);
//        edtThoigian = findViewById(R.id.edtThoigian);
//        edtCalo = findViewById(R.id.edtCalo);
//        btnLuu = findViewById(R.id.btnLuu);
//        btnHuy = findViewById(R.id.btnHuy);
//
//        btnLuu.setOnClickListener(v -> {
//            String ten = edtTen.getText().toString().trim();
//            String thoigianStr = edtThoigian.getText().toString().trim();
//            String caloStr = edtCalo.getText().toString().trim();
//
//            if (ten.isEmpty() || thoigianStr.isEmpty() || caloStr.isEmpty()) {
//                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            int thoigian = Integer.parseInt(thoigianStr);
//            int calo = Integer.parseInt(caloStr);
//
//            tapluyen_employ tap = new tapluyen_employ(ten, thoigian, calo);
//
//            // ✅ lưu vào database
//            database db = new database(this);
//            db.themTapLuyen(tap);
//
//            Toast.makeText(this, "Đã lưu bài tập!", Toast.LENGTH_SHORT).show();
//
//            setResult(RESULT_OK); // chỉ báo thành công
//            finish();
//        });
//
//        btnHuy.setOnClickListener(v -> finish());
//    }
//}
package com.example.baitap;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class tapluyen_thembaitap extends AppCompatActivity {

    private EditText edtTen, edtTime, edtCalo;
    private Button btnSave, btnCancel;
    private TapLuyenDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tapluyen_thembaitap);

        edtTen = findViewById(R.id.edtTen);
        edtTime = findViewById(R.id.edtThoigian);
        edtCalo = findViewById(R.id.edtCalo);
        btnSave = findViewById(R.id.btnLuu);
        btnCancel = findViewById(R.id.btnHuy);

        dao = new TapLuyenDAO(this);

        btnSave.setOnClickListener(v -> {
            String ten = edtTen.getText().toString();
            int time = Integer.parseInt(edtTime.getText().toString());
            int calo = Integer.parseInt(edtCalo.getText().toString());
            String ngay = getToday();

            dao.insert(new tapluyen_employ(ten, time, calo, ngay));
            finish();
        });

        btnCancel.setOnClickListener(v -> finish());
    }

    private String getToday() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }
}
