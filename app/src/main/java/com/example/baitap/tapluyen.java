//package com.example.baitap;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.SearchView;
//
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class tapluyen extends AppCompatActivity {
//    // Khai báo ở trên cùng (trong class TapLuyenActivity)
//
//
//
//    private RecyclerView recyclerView;
//    private tapluyen_adapter adapter;
//    private List<tapluyen_employ> danhSach;
//    private List<tapluyen_employ> danhSachGoc;
//    private Button btnThem;
//    private SearchView searchView;
//
//    private static final int REQUEST_THEM = 100;
//    private static final int REQUEST_SUA = 200;
//    private ActivityResultLauncher<Intent> launcherThem;
//    private ActivityResultLauncher<Intent> launcherSua;
//    private int viTriDangSua = -1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tapluyen);
//
//        // ánh xạ
//        recyclerView = findViewById(R.id.dstapluyen);
//        btnThem = findViewById(R.id.thembaitap);
//        searchView = findViewById(R.id.timkiem);
//
//        // dữ liệu mẫu
//        danhSach = new ArrayList<>();
//        danhSach.add(new tapluyen_employ("Chạy bộ", 30, 250));
//        danhSach.add(new tapluyen_employ("Đạp xe", 45, 400));
//        danhSach.add(new tapluyen_employ("Hít đất", 15, 100));
//        danhSachGoc = new ArrayList<>(danhSach);
//
//        // setup RecyclerView
//        adapter = new tapluyen_adapter(danhSach);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
//
//        // sự kiện thêm bài tập
//        launcherThem = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
//                        String ten = result.getData().getStringExtra("ten");
//                        int thoigian = result.getData().getIntExtra("thoigian", 0);
//                        int calo = result.getData().getIntExtra("calo", 0);
//
//                        tapluyen_employ bt = new tapluyen_employ(ten, thoigian, calo);
//                        danhSach.add(bt);
//                        danhSachGoc.add(bt);
//                        adapter.notifyItemInserted(danhSach.size() - 1);
//                    }
//                }
//        );
//
//        launcherSua = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
//                        String ten = result.getData().getStringExtra("ten");
//                        int thoigian = result.getData().getIntExtra("thoigian", 0);
//                        int calo = result.getData().getIntExtra("calo", 0);
//
//                        if (viTriDangSua != -1) {
//                            tapluyen_employ bt = danhSach.get(viTriDangSua);
//                            bt.setTen(ten);
//                            bt.setThoigian(thoigian);
//                            bt.setCalo(calo);
//
//                            adapter.notifyItemChanged(viTriDangSua);
//                            viTriDangSua = -1;
//                        }
//                    }
//                }
//        );
//
//        // ✅ Nút Thêm
//        btnThem.setOnClickListener(v -> {
//            Intent intent = new Intent(tapluyen.this, tapluyen_thembaitap.class);
//            launcherThem.launch(intent);
//        });
//
//        // ✅ Xử lý khi click Sửa trong adapter
//        adapter.setOnItemClickListener(new tapluyen_adapter.OnItemClickListener() {
//            @Override
//            public void onEdit(int position) {
//                viTriDangSua = position;
//                tapluyen_employ bt = danhSach.get(position);
//
//                Intent intent = new Intent(tapluyen.this, tapluyen_thembaitap.class);
//                intent.putExtra("ten", bt.getTen());
//                intent.putExtra("thoigian", bt.getThoigian());
//                intent.putExtra("calo", bt.getCalo());
//                launcherSua.launch(intent);
//            }
//
//            @Override
//            public void onDelete(int position) {
//                danhSach.remove(position);
//                adapter.notifyItemRemoved(position);
//            }
//        });
//        Button btnThongKe = findViewById(R.id.chon);
//        btnThongKe.setOnClickListener(v -> {
//            int tongCalo = 0;
//            for (tapluyen_employ bt : danhSach) {
//                if (bt.isSelected()) {
//                    tongCalo += bt.getCalo();
//                }
//            }
//
//            Intent intent = new Intent(tapluyen.this, thongke.class);
//            intent.putExtra("tongCalo", tongCalo);
//            startActivity(intent);
//        });
//
////        @Override
////        public void onDelete(int position) {
////            new AlertDialog.Builder(tapluyen.this)
////                    .setTitle("Xóa bài tập")
////                    .setMessage("Bạn có chắc chắn muốn xóa bài tập này không?")
////                    .setPositiveButton("Xóa", (dialog, which) -> {
////                        danhSach.remove(position);
////                        adapter.notifyItemRemoved(position);
////                    })
////                    .setNegativeButton("Hủy", null)
////                    .show();
////        }
//
//
//
//        // sự kiện tìm kiếm
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                timKiem(query);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                timKiem(newText);
//                return true;
//            }
//        });
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView2);
//
//        // Chọn menu hiện tại
//        bottomNavigationView.setSelectedItemId(R.id.menu_tapluyen);
//
//        bottomNavigationView.setOnItemSelectedListener(item -> {
//            int id = item.getItemId();
//            if (id == R.id.menu_muctieu) {
//                startActivity(new Intent(this, muctieu.class));
//                return true;
//            } else if (id == R.id.menu_thongke) {
//                startActivity(new Intent(this, thongke.class));
//                return true;
//            }
//            else if (id == R.id.menu_tapluyen) {
//
//                return true;
//            }
////            else if (id == R.id.menu_uongnuoc) {
////                startActivity(new Intent(this, UongNuocActivity.class));
////                return true;
////            }
//            return false;
//        });
//    }
//
//    // Hàm tìm kiếm
//    private void timKiem(String text) {
//        danhSach.clear();
//        if (text.isEmpty()) {
//            danhSach.addAll(danhSachGoc);
//        } else {
//            for (tapluyen_employ bt : danhSachGoc) {
//                if (bt.getTen().toLowerCase().contains(text.toLowerCase())) {
//                    danhSach.add(bt);
//                }
//            }
//        }
//        adapter.notifyDataSetChanged();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK && data != null) {
//            String ten = data.getStringExtra("ten");
//            int thoigian = data.getIntExtra("thoigian", 0);
//            int calo = data.getIntExtra("calo", 0);
//
//            if (requestCode == REQUEST_THEM) {
//                tapluyen_employ newBt = new tapluyen_employ(ten, thoigian, calo);
//                danhSach.add(newBt);
//                danhSachGoc.add(newBt);
//                adapter.notifyItemInserted(danhSach.size() - 1);
//            } else if (requestCode == REQUEST_SUA && viTriDangSua != -1) {
//                tapluyen_employ suaBt = danhSach.get(viTriDangSua);
//                suaBt.setTen(ten);
//                suaBt.setThoigian(thoigian);
//                suaBt.setCalo(calo);
//                adapter.notifyItemChanged(viTriDangSua);
//
//                // cập nhật danh sách gốc
//                danhSachGoc.set(viTriDangSua, suaBt);
//            }
//        }
////        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
////        bottomNavigationView.setSelectedItemId(R.id.menu_tapluyen);
////
////
////        bottomNavigationView.setOnItemSelectedListener(item -> {
////            int id = item.getItemId();
////            if (id == R.id.menu_muctieu) {
////                startActivity(new Intent(tapluyen.this, muctieu.class));
////                return true;
////            } else if (id == R.id.menu_thongke) {
////                startActivity(new Intent(tapluyen.this, thongke.class));
////                return true;
////            }
//////            else if (id == R.id.menu_thucan) {
//////                startActivity(new Intent(this, thucan.class));
//////            } else if (id == R.id.menu_uongnuoc) {
//////                startActivity(new Intent(this, MucTieuActivity.class));
//////            }
////            else {
////                return id == R.id.menu_tap;
////            }
////        });
//
//
//
//    }
//}
package com.example.baitap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SearchView;

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
    private RecyclerView recyclerView;
    private tapluyen_adapter adapter;
    private TapLuyenDAO dao;
    private ArrayList<tapluyen_employ> list;
    private Button btnThem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tapluyen);

        recyclerView = findViewById(R.id.dstapluyen);
        btnThem = findViewById(R.id.thembaitap);

        dao = new TapLuyenDAO(this);
        list = dao.getAll();

        adapter = new tapluyen_adapter(this, list, dao);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        SearchView timkiem = findViewById(R.id.timkiem);

        btnThem.setOnClickListener(v -> {
            Intent intent = new Intent(tapluyen.this, tapluyen_thembaitap.class);
            startActivity(intent);
        });

        timkiem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

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

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        bottomNavigationView.setSelectedItemId(R.id.menu_tapluyen);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_muctieu)  {startActivity(new Intent(this,MucTieuActivity.class));
            return true;}
            else if (id == R.id.menu_tapluyen) {
                return true;
            } else if (id == R.id.menu_thongke) {
                startActivity(new Intent(this, thongke.class));
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
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }
}
