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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UongNuocActivity extends AppCompatActivity {

    private TextView tvMucTieu, tvTongUong, tvTienDo;
    private EditText edtNhapNuoc;
    private ProgressBar progressBar;
    private Button btnLuu, btnNhacNho;
    private ListView listLichSu;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> lichSu = new ArrayList<>();

    private int tongUong = 0;
    private int mucTieu = 2000; // mặc định 2 lít

    private MucTieuDAO mucTieuDAO;
    private UongNuocDAO uongNuocDAO;
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uongnuoc);

        // Ánh xạ view
        tvMucTieu = findViewById(R.id.tvMucTieu);
        tvTongUong = findViewById(R.id.tvTongUong);
        tvTienDo = findViewById(R.id.tvTienDo);
        edtNhapNuoc = findViewById(R.id.edtNhapNuoc);
        progressBar = findViewById(R.id.progressBar);
        btnLuu = findViewById(R.id.btnLuu);
        btnNhacNho = findViewById(R.id.btnNhacNho);
        listLichSu = findViewById(R.id.listLichSu);
        barChart = findViewById(R.id.barChart);

        mucTieuDAO = new MucTieuDAO(this);
        uongNuocDAO = new UongNuocDAO(this);

        // --- Lấy dữ liệu từ bảng mục tiêu ---
        muctieu mucTieuMoiNhat = mucTieuDAO.getLatest();
        if (mucTieuMoiNhat != null) {
            mucTieu = mucTieuMoiNhat.getLuongNuoc();
        } else {
            mucTieu = 2000; // fallback
        }

        // --- Lấy dữ liệu ban đầu ---
        tongUong = uongNuocDAO.getTongNuocHomNay();
        lichSu.addAll(uongNuocDAO.getLichSuHomNay());

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lichSu);
        listLichSu.setAdapter(adapter);

        capNhatUI();

        // Nút lưu lượng nước
        btnLuu.setOnClickListener(v -> {
            try {
                int ml = Integer.parseInt(edtNhapNuoc.getText().toString());
                if (ml <= 0) {
                    Toast.makeText(this, "Vui lòng nhập số ml hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                uongNuocDAO.themUongNuoc(ml, "");
                tongUong = uongNuocDAO.getTongNuocHomNay();
                lichSu.clear();
                lichSu.addAll(uongNuocDAO.getLichSuHomNay());
                adapter.notifyDataSetChanged();
                capNhatUI();
                edtNhapNuoc.setText("");

                Toast.makeText(this, "Đã lưu " + ml + "ml nước!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Lỗi nhập liệu!", Toast.LENGTH_SHORT).show();
            }
        });

        // Nút bật nhắc nhở
        btnNhacNho.setOnClickListener(v -> batNhacNho());

        // BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        bottomNavigationView.setSelectedItemId(R.id.menu_uongnuoc);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_uongnuoc) {
                    return true;
                } else if (id == R.id.menu_tapluyen) {
                    startActivity(new Intent(UongNuocActivity.this, tapluyen.class));
                } else if (id == R.id.menu_thongke) {
                    startActivity(new Intent(UongNuocActivity.this, thongke.class));
                } else if (id == R.id.menu_muctieu) {
                    startActivity(new Intent(UongNuocActivity.this, MucTieuActivity.class));
                }else if (id == R.id.menu_thucan) {
                    startActivity(new Intent(UongNuocActivity.this, thucan_activity.class));
                }
                return true;
            }
        });


    }

    private void capNhatUI() {
        tvMucTieu.setText("Mục tiêu hôm nay: " + mucTieu + "ml");
        tvTongUong.setText("Đã uống: " + tongUong + "ml");
        int tienDo = (int) ((tongUong / (float) mucTieu) * 100);
        if (tienDo > 100) tienDo = 100;
        tvTienDo.setText("Tiến độ: " + tienDo + "%");
        progressBar.setProgress(tienDo);

        // --- Vẽ biểu đồ 7 ngày gần nhất ---
        Map<String, Integer> data7Ngay = uongNuocDAO.getNuoc7NgayGanNhat();
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        int index = 0;
        for (Map.Entry<String, Integer> entry : data7Ngay.entrySet()) {
            entries.add(new BarEntry(index++, entry.getValue()));
            labels.add(entry.getKey().substring(5)); // chỉ lấy MM-dd
        }

        BarDataSet dataSet = new BarDataSet(entries, "7 ngày gần nhất");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.9f);
        barChart.setData(data);
        barChart.getDescription().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value >= 0 && value < labels.size()) {
                    return labels.get((int) value);
                } else {
                    return "";
                }
            }
        });

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        barChart.getAxisRight().setEnabled(false);
        barChart.animateY(1000);
        barChart.invalidate();
    }

    private void batNhacNho() {
        Toast.makeText(this, "Đã bật nhắc nhở uống nước mỗi 2 giờ!", Toast.LENGTH_SHORT).show();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ThongBaoReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 7200000,
                7200000,
                pendingIntent
        );
    }
}
