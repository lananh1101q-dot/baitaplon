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