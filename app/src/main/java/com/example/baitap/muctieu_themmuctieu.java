package com.example.baitap;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class muctieu_themmuctieu  extends AppCompatActivity {
    EditText edtChieuCao, edtCanNang, edtTuoi, edtMucTieu;
    RadioButton radNam, radNu;
    Button btnTiep,ve;
    MucTieuDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muctieu_thaydoi); // hoặc R.layout.activity_muctieu_thaydoi tuỳ bạn

        edtChieuCao = findViewById(R.id.chieucao);
        edtCanNang = findViewById(R.id.cannang);
        edtTuoi = findViewById(R.id.tuoi);

        radNam = findViewById(R.id.nam);
        radNu = findViewById(R.id.nu);
        btnTiep = findViewById(R.id.tiep);
        ve=findViewById(R.id.ve);

        // nếu intent có tenmuctieu từ màn chọn, gán vào edtMucTieu
        String tenPassed = getIntent().getStringExtra("tenmuctieu");
        if (tenPassed != null) {
            edtMucTieu.setText(tenPassed);
        }

        dao = new MucTieuDAO(this);
        ve.setOnClickListener(v -> finish());

        btnTiep.setOnClickListener(v -> {
            String chieuStr = edtChieuCao.getText().toString().trim();
            String canStr = edtCanNang.getText().toString().trim();
            String tuoiStr = edtTuoi.getText().toString().trim();


            if (chieuStr.isEmpty() || canStr.isEmpty() || tuoiStr.isEmpty() ) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            double chieu = Double.parseDouble(chieuStr);
            double can = Double.parseDouble(canStr);
            int tuoi = Integer.parseInt(tuoiStr);

            // tính BMR (Harris-Benedict)
            double bmr;
            if (radNam.isChecked()) {
                bmr = 88.362 + (13.397 * can) + (4.799 * chieu) - (5.677 * tuoi);
            } else {
                bmr = 447.593 + (9.247 * can) + (3.098 * chieu) - (4.330 * tuoi);
            }

            double bmi = can / Math.pow(chieu / 100.0, 2);
            int nangluong = (int) Math.round(bmr);
            int luongnuoc = (int) Math.round(can * 35); // ml

            // ngày lưu: yyyy-MM-dd (dễ query)
            String ngayDb = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            Intent intent = new Intent(this, muctieu_muctieu.class);
            intent.putExtra("chieu", chieu);
            intent.putExtra("can", can);
            intent.putExtra("bmi", bmi);
            intent.putExtra("nangluong", nangluong);
            intent.putExtra("luongnuoc", luongnuoc);
            intent.putExtra("ngayDb", ngayDb);
            startActivity(intent);
            finish();
        });
    }
}