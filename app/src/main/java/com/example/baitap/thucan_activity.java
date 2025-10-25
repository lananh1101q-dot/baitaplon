package com.example.baitap;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class thucan_activity extends AppCompatActivity {

    private static final int REQ_ADD = 100;
    private static final int REQ_EDIT = 101;

    TextView tvNgay, tvTongCalo;
    Button btnThemMon;
    ListView lv;
    database db;
    thucan_adapter adapter;
    List<model_FoodLog> logs;
    String today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thucan_activity);

        db = new database(this);

        tvNgay = findViewById(R.id.tvNgay);
        tvTongCalo = findViewById(R.id.tvTongCalo);
        btnThemMon = findViewById(R.id.btnThemMon);
        lv = findViewById(R.id.lv);

        // Thống nhất format ngày để khớp với database
        today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        tvNgay.setText("Hôm nay: " + today);

        loadData();

        // Nút thêm món
        btnThemMon.setOnClickListener(v -> {
            Intent it = new Intent(thucan_activity.this, thucan_them.class);
            startActivityForResult(it, REQ_ADD);
        });

        // Click để sửa món
        lv.setOnItemClickListener((parent, view, position, id) -> {
            model_FoodLog f = logs.get(position);
            Intent it = new Intent(thucan_activity.this, thucan_sua.class);
            it.putExtra("log_id", f.getId());
            startActivityForResult(it, REQ_EDIT);
        });

        // Giữ lâu để xóa món
        lv.setOnItemLongClickListener((parent, view, position, id) -> {
            model_FoodLog f = logs.get(position);
            new AlertDialog.Builder(thucan_activity.this)
                    .setTitle("Xoá món ăn")
                    .setMessage("Bạn có chắc chắn muốn xoá \"" + f.getTenMon() + "\"?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        db.deleteFoodLog(f.getId());
                        loadData();
                    })
                    .setNegativeButton("Không", null)
                    .show();
            return true;
        });

        // Thanh điều hướng
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        bottomNavigationView.setSelectedItemId(R.id.menu_thucan);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_muctieu) {
                startActivity(new Intent(this, MucTieuActivity.class));
                return true;
            } else if (id == R.id.menu_tapluyen) {
                startActivity(new Intent(this, tapluyen.class));
                return true;
            } else if (id == R.id.menu_thongke) {
                startActivity(new Intent(this, thongke.class));
                return true;
            } else if (id == R.id.menu_uongnuoc) {
                startActivity(new Intent(this, UongNuocActivity.class));
                return true;
            } else if (id == R.id.menu_thucan) {
                return true;
            }
            return false;
        });
    }

    private void loadData() {
        logs = db.getFoodLogsByDate(today);
        if (adapter == null) {
            adapter = new thucan_adapter(this, logs);
            lv.setAdapter(adapter);
        } else {
            adapter.updateList(logs);
        }

        int tong = 0;
        for (model_FoodLog f : logs) tong += f.getCalo();
        tvTongCalo.setText("Tổng hấp thụ: " + tong + " kcal");
    }

    // Sau khi thêm hoặc sửa món ăn
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadData();
        }
    }
}
