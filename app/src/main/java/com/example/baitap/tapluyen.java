package com.example.baitap;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;



public class tapluyen extends AppCompatActivity {
    private ListView listview;
    private tapluyen_adapter adapter;
    private TapLuyenDAO dao;
    private ArrayList<tapluyen_employ> list;
    private Button btnThem;
    private database db;
    TextView txtNgay,tcalo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tapluyen);

        listview = findViewById(R.id.dstapluyen);
        btnThem = findViewById(R.id.thembaitap);
        tcalo = findViewById(R.id.txtTongCalo);


        db = new database(this);


        // Tự động tạo dữ liệu mẫu nếu database trống
//        database db = new database(this);
//        db.autoSeedIfEmpty();

        dao = new TapLuyenDAO(this);
        list = new ArrayList<>();


        adapter = new tapluyen_adapter(this, R.layout.activity_tapluyen_listitem, list, dao);
        listview = findViewById(R.id.dstapluyen);
        listview.setAdapter(adapter);
 //sự kiện trong lisview
//        listview.setOnItemClickListener((parent, view, position, id) -> {
//            tapluyen_employ item = list.get(position);
//            Toast.makeText(this, "Bài tập: " + item.getTenBaiTap(), Toast.LENGTH_SHORT).show();
//        });
//
//        listview.setOnItemLongClickListener((parent, view, position, id) -> {
//            tapluyen_employ item = list.get(position);
//            dao.delete(item.getId()); // Xóa trong database
//            list.remove(position);    // Xóa trong danh sách
//            adapter.notifyDataSetChanged(); // Cập nhật giao diện
//            Toast.makeText(this, "Đã xóa: " + item.getTenBaiTap(), Toast.LENGTH_SHORT).show();
//            return true; // Quan trọng: báo là sự kiện đã xử lý
//        });

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

         txtNgay = findViewById(R.id.txtNgay);
        txtNgay.setText(getToday());
        loadTodayData();
        refreshIfNewDay();


        // Nút thêm bài tập
        btnThem.setOnClickListener(v -> {
            Intent intent = new Intent(tapluyen.this, tapluyen_thembaitap.class);
            startActivity(intent);
        });

        // ✅ Nút chọn bài tập





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
            else if (id == R.id.menu_thucan) {
                startActivity(new Intent(this, thucan_activity.class));
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.refreshData(dao.getByDate(getToday()));
        updateTongCaloHomNay();
    }

    private String getToday() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    // ---- Load dữ liệu bài tập trong ngày ----
    private void loadTodayData() {
        updateTongCaloHomNay();
        String today = getToday();
        txtNgay.setText(today);
        list = dao.getByDate(today);
        adapter = new tapluyen_adapter(this, R.layout.activity_tapluyen_listitem, list, dao);
        listview.setAdapter(adapter);
        tcalo.setText("Tổng năng lượng hôm nay: 0 kcal");
    }
    // ---- Làm mới khi sang ngày mới ----
    private void refreshIfNewDay() {
        SharedPreferences prefs = getSharedPreferences("TapLuyenPrefs", MODE_PRIVATE);
        String last = prefs.getString("lastDate", "");
        String today = getToday();
        if (!today.equals(last)) {
            prefs.edit().putString("lastDate", today).apply();
            loadTodayData();
        } else {
            adapter.refreshData(dao.getByDate(today));
        }
    }
    public void updateTongCaloHomNay() {
        ArrayList<tapluyen_employ> listToday = dao.getByDate(getToday());
        int tong = 0;
        for (tapluyen_employ t : listToday) tong += t.getCaloTieuThu();
        tcalo.setText("Tổng năng lượng hôm nay: " + tong + " kcal");

    }

}