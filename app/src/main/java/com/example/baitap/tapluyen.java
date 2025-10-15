package com.example.baitap;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;



public class tapluyen extends AppCompatActivity {
    private ListView listview;
    private tapluyen_adapter adapter;
    private TapLuyenDAO dao;
    private ArrayList<tapluyen_employ> list;
    private Button btnThem, btnChon;
    private database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tapluyen);

        listview = findViewById(R.id.dstapluyen);
        btnThem = findViewById(R.id.thembaitap);
        btnChon = findViewById(R.id.chon);

        db = new database(this);


        // Tự động tạo dữ liệu mẫu nếu database trống
        database db = new database(this);
        db.autoSeedIfEmpty();

        dao = new TapLuyenDAO(this);
        list = dao.getAll();

        adapter = new tapluyen_adapter(this, R.layout.activity_tapluyen_listitem, list, dao);
        listview = findViewById(R.id.dstapluyen);
        listview.setAdapter(adapter);

        SearchView timkiem = findViewById(R.id.timkiem);
        timkiem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<tapluyen_employ> filtered = dao.getAll();
                if (newText != null && !newText.isEmpty()) {
                    filtered.removeIf(t -> !t.getTenBaiTap().toLowerCase().contains(newText.toLowerCase()));
                }
                adapter.refreshData(new ArrayList<>(filtered));
                return true;
            }
        });

        TextView txtNgay = findViewById(R.id.txtNgay);
        txtNgay.setText(getToday());

        // Nút thêm bài tập
        btnThem.setOnClickListener(v -> {
            Intent intent = new Intent(tapluyen.this, tapluyen_thembaitap.class);
            startActivity(intent);
        });

        // ✅ Nút chọn bài tập
        btnChon.setOnClickListener(v -> {
            ArrayList<tapluyen_employ> selected = adapter.getSelectedList();
            if (selected.isEmpty()) {Toast.makeText(this, "Bạn chưa chọn bài tập nào!", Toast.LENGTH_SHORT).show();
                return;
            }

            int tongCalo = 0;
            for (tapluyen_employ t : selected) {
                tongCalo += t.getCaloTieuThu();
            }

            Toast.makeText(this, "Bạn đã tiêu hao tổng năng lượng: " + tongCalo + " kcal", Toast.LENGTH_LONG).show();

            // ✅ Ghi dữ liệu sang bảng thống kê (cộng dồn nếu đã có)

        });


        // Thanh điều hướng dưới
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        bottomNavigationView.setSelectedItemId(R.id.menu_tapluyen);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_muctieu) {
                startActivity(new Intent(this, MucTieuActivity.class));
                return true;
            } else if (id == R.id.menu_tapluyen) {
                return true;
            } else if (id == R.id.menu_thongke) {
                startActivity(new Intent(this, thongke.class));
                return true;
            }else if (id == R.id.menu_uongnuoc) {
                startActivity(new Intent(this, UongNuocActivity.class));
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.refreshData(dao.getAll());
    }

    private String getToday() {
        return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    }
}