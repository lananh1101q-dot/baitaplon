package com.example.baitap;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class thongke extends AppCompatActivity {

    TextView tvNgay, tvTieuThu, tvHapThu, tvNlCanNap, tvNcCanNap;
    database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongke);

        // ánh xạ
        tvNgay = findViewById(R.id.ngay);
        tvTieuThu = findViewById(R.id.tieuthu);
        tvHapThu = findViewById(R.id.hapthu);
        tvNlCanNap = findViewById(R.id.nlcannap);
        tvNcCanNap = findViewById(R.id.nccannap);

        db = new database(this);


        // Lấy thống kê mới nhất (ngày hôm nay)
//        Cursor c = db.layThongKeMoiNhat();
//        if (c.moveToFirst()) {
//            int caloNap = c.getInt(c.getColumnIndexOrThrow("calonap"));
//            int caloTieuThu = c.getInt(c.getColumnIndexOrThrow("calotieuthu"));
//            int nlCan = c.getInt(c.getColumnIndexOrThrow("nlcan"));
//            int lgCan = c.getInt(c.getColumnIndexOrThrow("lgcan"));
//            String ngayStr = c.getString(c.getColumnIndexOrThrow("ngay"));
//
//            tvHapThu.setText(String.valueOf(caloNap));
//            tvTieuThu.setText(String.valueOf(caloTieuThu));
//            tvNlCanNap.setText(String.valueOf(nlCan));
//            tvNcCanNap.setText(String.valueOf(lgCan));
//            tvNgay.setText(ngayStr);
//        }
//        c.close();
//
//        // hiển thị thống kê hôm nay
//        Calendar calendar = Calendar.getInstance();
//        int y = calendar.get(Calendar.YEAR);
//        int m = calendar.get(Calendar.MONTH) + 1;
//        int d = calendar.get(Calendar.DAY_OF_MONTH);
//        String ngayHomNay = y + "-" + m + "-" + d;
//        tvNgay.setText("Ngày: " + ngayHomNay);
//        loadThongKeTheoNgay(ngayHomNay);
//
//        // khi bấm vào chữ "ngày" thì mở DatePicker
//        tvNgay.setOnClickListener(v -> {
//            Calendar cal = Calendar.getInstance();
//            int year = cal.get(Calendar.YEAR);
//            int month = cal.get(Calendar.MONTH);
//            int day = cal.get(Calendar.DAY_OF_MONTH);
//
//            DatePickerDialog dialog = new DatePickerDialog(
//                    thongke.this,
//                    (DatePicker view, int y1, int m1, int d1) -> {
//                        String ngay = y1 + "-" + (m1 + 1) + "-" + d1;
//                        tvNgay.setText("Ngày: " + ngay);
//                        loadThongKeTheoNgay(ngay);
//                    },
//                    year, month, day
//            );
//            dialog.show();
//        });

        // bottom navigation nếu cần
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        bottomNavigationView.setSelectedItemId(R.id.menu_thongke);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_muctieu) {
                startActivity(new Intent(this, muctieu.class));
                return true;
            } else if (id == R.id.menu_tapluyen) {
                startActivity(new Intent(this, tapluyen.class));
                return true;
            }
            else if (id == R.id.menu_thongke) {

                return true;
            }
//            else if (id == R.id.menu_uongnuoc) {
//                startActivity(new Intent(this, UongNuocActivity.class));
//                return true;
//            }
            return false;
        });
    }

//    private void loadThongKeTheoNgay(String ngay) {
//        Cursor c = db.layThongKeTheoNgay(ngay);
//        if (c != null && c.moveToFirst()) {
//            int calotieuthu = c.getInt(c.getColumnIndexOrThrow("calotieuthu"));
//            int calonap = c.getInt(c.getColumnIndexOrThrow("calonap"));
//            int nlcan = c.getInt(c.getColumnIndexOrThrow("nlcan"));
//            int lgcan = c.getInt(c.getColumnIndexOrThrow("lgcan"));
//
//            tvTieuThu.setText(String.valueOf(calotieuthu));
//            tvHapThu.setText(String.valueOf(calonap));
//            tvNlCanNap.setText(String.valueOf(nlcan));
//            tvNcCanNap.setText(String.valueOf(lgcan));
//        } else {
//            tvTieuThu.setText("0");
//            tvHapThu.setText("0");
//            tvNlCanNap.setText("0");
//            tvNcCanNap.setText("0");
//        }
//        if (c != null) c.close();
//    }
}
