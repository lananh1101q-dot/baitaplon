package com.example.baitap;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class UongNuocActivity extends AppCompatActivity {

    private TextView tvMucTieu, tvTongUong;
    private EditText edtNhapNuoc;
    private Button btnLuu;
    private MucTieuDAO mucTieuDAO;
    private UongNuocDAO uongNuocDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uongnuoc);

        tvMucTieu = findViewById(R.id.tvMucTieu);
        tvTongUong = findViewById(R.id.tvTongUong);
        edtNhapNuoc = findViewById(R.id.edtNhapNuoc);
        btnLuu = findViewById(R.id.btnLuu);

        mucTieuDAO = new MucTieuDAO(this);
        uongNuocDAO = new UongNuocDAO(this);

        loadThongTin();
        setupThongBaoMoiGio();

        btnLuu.setOnClickListener(v -> {
            try {
                int ml = Integer.parseInt(edtNhapNuoc.getText().toString());
                if (ml <= 0) throw new NumberFormatException();
                uongNuocDAO.insert(ml);
                Toast.makeText(this, "Đã lưu " + ml + "ml!", Toast.LENGTH_SHORT).show();
                edtNhapNuoc.setText("");
                loadThongTin();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Nhập số ml hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadThongTin() {
        muctieu m = mucTieuDAO.getLatest();
        int tong = uongNuocDAO.getTongHomNay();

        if (m != null) {
            tvMucTieu.setText("Mục tiêu hôm nay: " + m.getLuongNuoc() + " ml");
            tvTongUong.setText("Đã uống: " + tong + " ml");
        } else {
            tvMucTieu.setText("Chưa có mục tiêu");
            tvTongUong.setText("");
        }
    }

    // Gửi thông báo mỗi giờ
    private void setupThongBaoMoiGio() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ThongBaoReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, 1); // sau 1h sẽ kích hoạt
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(),
                AlarmManager.INTERVAL_HOUR,
                pendingIntent
        );
    }
}
