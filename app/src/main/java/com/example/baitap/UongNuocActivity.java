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
import java.util.ArrayList;

public class UongNuocActivity extends AppCompatActivity {

    private TextView tvMucTieu, tvTongUong, tvTienDo;
    private EditText edtNhapNuoc;
    private ProgressBar progressBar;
    private Button btnLuu, btnNhacNho;
    private ListView listLichSu;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> lichSu = new ArrayList<>();
    private int tongUong = 0;
    private int mucTieu = 2000; // ml

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

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lichSu);
        listLichSu.setAdapter(adapter);

        capNhatUI();

        // Nút lưu lượng nước
        btnLuu.setOnClickListener(v -> {
            try {
                int ml = Integer.parseInt(edtNhapNuoc.getText().toString());
                tongUong += ml;
                lichSu.add("Uống " + ml + "ml");
                adapter.notifyDataSetChanged();
                capNhatUI();
                edtNhapNuoc.setText("");
            } catch (Exception e) {
                Toast.makeText(this, "Vui lòng nhập số ml hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });

        // Nút nhắc nhở
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
                }
                overridePendingTransition(0,0);
                return true;
            }
        });
    }

    private void capNhatUI() {
        tvMucTieu.setText("Mục tiêu hôm nay: " + mucTieu + "ml");
        tvTongUong.setText("Đã uống: " + tongUong + "ml");
        int tienDo = (int) ((tongUong / (float) mucTieu) * 100);
        tvTienDo.setText("Tiến độ: " + tienDo + "%");
        progressBar.setProgress(tienDo);
    }

    private void batNhacNho() {
        Toast.makeText(this, "Đã bật nhắc nhở uống nước mỗi 2 giờ!", Toast.LENGTH_SHORT).show();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ThongBaoReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 7200000, 7200000, pendingIntent);
    }
}
