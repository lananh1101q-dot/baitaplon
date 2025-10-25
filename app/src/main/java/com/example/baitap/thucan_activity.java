package com.example.baitap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.baitap.thucan_adapter;
import com.example.baitap.database;
import com.example.baitap.model_FoodLog;

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

        today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        tvNgay.setText("Hôm nay: " + today);

        loadData();

        btnThemMon.setOnClickListener(v -> {
            Intent it = new Intent(thucan_activity.this, thucan_them.class);
            startActivityForResult(it, REQ_ADD);
        });

        // click -> edit
        lv.setOnItemClickListener((parent, view, position, id) -> {
            model_FoodLog f = logs.get(position);
            Intent it = new Intent(thucan_activity.this, thucan_sua.class);
            it.putExtra("log_id", f.getId());
            startActivityForResult(it, REQ_EDIT);
        });

        // long click -> delete
        lv.setOnItemLongClickListener((parent, view, position, id) -> {
            model_FoodLog f = logs.get(position);
            new AlertDialog.Builder(thucan_activity.this)
                    .setTitle("Xoá")
                    .setMessage("Bạn có chắc chắn muốn xoá \"" + f.getTenMon() + "\"?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        db.deleteFoodLog(f.getId());
                        loadData();
                    })
                    .setNegativeButton("Không", null)
                    .show();
            return true;
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
        tvTongCalo.setText("Tổng: " + tong + " kcal");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // after add/edit/delete refresh
        if (resultCode == RESULT_OK) {
            loadData();
        }
    }
}


