package com.example.baitap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class muctieu_muctieu extends AppCompatActivity {
    Button btnGiam, btnGiu, btnTang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muctieu_muctieu);

        btnGiam = findViewById(R.id.giamcan);
        btnGiu = findViewById(R.id.giucan);
        btnTang = findViewById(R.id.tangcan);

        btnGiam.setOnClickListener(v -> openThayDoi("Giảm cân"));
        btnGiu.setOnClickListener(v -> openThayDoi("Giữ cân"));
        btnTang.setOnClickListener(v -> openThayDoi("Tăng cân"));
    }

    private void openThayDoi(String tenMucTieu) {
        Intent i = new Intent(this, MucTieuActivity.class);
        i.putExtra("tenmuctieu", tenMucTieu);
        startActivity(i);
        finish();
    }
}
