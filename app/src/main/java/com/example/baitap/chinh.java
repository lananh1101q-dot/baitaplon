package com.example.baitap;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class chinh extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chinh);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        bottomNavigationView.setSelectedItemId(R.id.menu_muctieu);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.menu_muctieu) {
                return true;
            } else if (id == R.id.menu_tapluyen) {
                startActivity(new Intent(this, tapluyen.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.menu_thongke) {
                startActivity(new Intent(this, thongke.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.menu_uongnuoc) {
                startActivity(new Intent(this, UongNuocActivity.class));
                overridePendingTransition(0, 0);
                return true;

        } else if (id == R.id.menu_thucan) {
            startActivity(new Intent(this, UongNuocActivity.class));
            overridePendingTransition(0, 0);
            return true;
        }
            return false;
        });
        // Tạo instance của database
        database dbHelper = new database(this);

        // Tự động tạo dữ liệu mẫu nếu database trống

    }
}
