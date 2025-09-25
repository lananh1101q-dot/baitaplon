package com.example.baitap;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class muctieu_muctieu extends AppCompatActivity {

    Button btnGiamCan, btnGiuCan, btnTangCan;

    double chieuCao, canNang, bmr;
    int tuoi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muctieu_muctieu);

        // nhận dữ liệu từ InputActivity
        Intent i = getIntent();
        chieuCao = i.getDoubleExtra("chieucao", 0);
        canNang = i.getDoubleExtra("cannang", 0);
        tuoi = i.getIntExtra("tuoi", 0);
        bmr = i.getDoubleExtra("bmr", 0);

        btnGiamCan = findViewById(R.id.giamcan);
        btnGiuCan = findViewById(R.id.giucan);
        btnTangCan = findViewById(R.id.tangcan);

        btnGiamCan.setOnClickListener(v -> chuyenTrang("Giảm cân"));
        btnGiuCan.setOnClickListener(v -> chuyenTrang("Giữ cân"));
        btnTangCan.setOnClickListener(v -> chuyenTrang("Tăng cân"));
    }

    private void chuyenTrang(String mucTieu) {
        Intent intent = new Intent(muctieu_muctieu.this, muctieu.class);
        intent.putExtra("chieucao", chieuCao);
        intent.putExtra("cannang", canNang);
        intent.putExtra("tuoi", tuoi);
        intent.putExtra("bmr", bmr);
        intent.putExtra("muctieu", mucTieu);
        startActivity(intent);
    }
}
