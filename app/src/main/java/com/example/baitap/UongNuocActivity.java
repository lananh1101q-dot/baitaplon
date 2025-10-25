package com.example.baitap;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.*;

public class UongNuocActivity extends AppCompatActivity {

    private TextView tvMucTieu, tvTongUong, tvTienDo;
    private EditText edtNhapNuoc;
    private ProgressBar progressBar;
    private Button btnLuu, btnNhacNho;
    private ListView listLichSu;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> lichSu = new ArrayList<>();
    private int tongUong = 0;
    private int mucTieu = 2000;

    private UongNuocDAO uongNuocDAO;
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uongnuoc);

        // Ánh xạ
        tvMucTieu = findViewById(R.id.tvMucTieu);
        tvTongUong = findViewById(R.id.tvTongUong);
        tvTienDo = findViewById(R.id.tvTienDo);
        edtNhapNuoc = findViewById(R.id.edtNhapNuoc);
        progressBar = findViewById(R.id.progressBar);
        btnLuu = findViewById(R.id.btnLuu);
        btnNhacNho = findViewById(R.id.btnNhacNho);
        listLichSu = findViewById(R.id.listLichSu);
        barChart = findViewById(R.id.barChart);

        uongNuocDAO = new UongNuocDAO(this);
        mucTieu = uongNuocDAO.getMucTieuNuoc();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lichSu);
        listLichSu.setAdapter(adapter);

        // Lấy dữ liệu lúc mở
        capNhatUI();
        capNhatBieuDo();
        capNhatLichSu();

        btnLuu.setOnClickListener(v -> {
            String input = edtNhapNuoc.getText().toString().trim();
            if (input.isEmpty()) {
                Toast.makeText(this, "Nhập số ml", Toast.LENGTH_SHORT).show();
                return;
            }
            int ml;
            try {
                ml = Integer.parseInt(input);
            } catch (Exception e) {
                Toast.makeText(this, "Nhập số hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            long id = uongNuocDAO.themUongNuoc(ml, "");
            if (id != -1) {
                Toast.makeText(this, "Lưu thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Lưu thất bại", Toast.LENGTH_SHORT).show();
            }

            capNhatUI();
            capNhatBieuDo();
            capNhatLichSu();

            edtNhapNuoc.setText("");
        });

        btnNhacNho.setOnClickListener(v -> batNhacNho());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        bottomNavigationView.setSelectedItemId(R.id.menu_uongnuoc);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_tapluyen) {
                startActivity(new Intent(this, tapluyen.class));
            } else if (id == R.id.menu_thongke) {
                startActivity(new Intent(this, thongke.class));
            } else if (id == R.id.menu_muctieu) {
                startActivity(new Intent(this, MucTieuActivity.class));
            }
            overridePendingTransition(0,0);
            return true;
        });
    }

    private void capNhatUI() {
        tongUong = uongNuocDAO.getTongNuocHomNay();
        tvMucTieu.setText("Mục tiêu hôm nay: " + mucTieu + " ml");
        tvTongUong.setText("Đã uống: " + tongUong + " ml");
        int percent = (int) ((tongUong * 100f) / mucTieu);
        tvTienDo.setText("Tiến độ: " + percent + "%");
        progressBar.setProgress(percent);
    }

    private void capNhatLichSu() {
        lichSu.clear();
        lichSu.addAll(uongNuocDAO.getLichSuHomNay());
        adapter.notifyDataSetChanged();
    }

    private void capNhatBieuDo() {
        Map<String, Integer> map = uongNuocDAO.getNuoc7NgayGanNhat();
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, Integer> e : map.entrySet()) {
            entries.add(new BarEntry(i, e.getValue()));
            labels.add(e.getKey().substring(5)); // MM-dd
            i++;
        }

        BarDataSet dataSet = new BarDataSet(entries, "Nước uống (ml)");
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);

        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setEnabled(false);

        barChart.getDescription().setEnabled(false);
        barChart.invalidate();
    }

    private void batNhacNho() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 1); // ví dụ 1 phút sau
        Intent intent = new Intent(this, ThongBaoReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_MUTABLE);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Toast.makeText(this, "Đã bật nhắc nhở 1 phút sau", Toast.LENGTH_SHORT).show();
    }
}
