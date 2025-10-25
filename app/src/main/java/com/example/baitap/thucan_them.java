package com.example.baitap;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class thucan_them extends AppCompatActivity {

    Spinner spinnerLoai, spinnerMon;
    TextView tvCalo;
    Button btLuu, btHuy;
    database db;
    List<model_Loai> loaiList;
    List<model_Monan> monanList;
    model_Monan selectedMon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thucan_them);

        spinnerLoai = findViewById(R.id.spinnerLoai);
        spinnerMon = findViewById(R.id.spinnerMon);
        tvCalo = findViewById(R.id.edtCalo);
        btLuu = findViewById(R.id.bt_themluu);
        btHuy = findViewById(R.id.bt_themhuy);

        db = new database(this);
        loadLoai();

        spinnerLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                model_Loai l = loaiList.get(position);
                loadMonByLoai(l.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        spinnerMon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                selectedMon = monanList.get(position);
                tvCalo.setText(selectedMon.getCalo() + " kcal");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedMon = null;
                tvCalo.setText("--");
            }
        });

        btLuu.setOnClickListener(v -> {
            if (selectedMon == null) {
                Toast.makeText(thucan_them.this, "Vui lòng chọn món", Toast.LENGTH_SHORT).show();
                return;
            }
            // Ghi ngày theo định dạng database
            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            db.insertFoodLog(selectedMon.getId(), today, null);

            Toast.makeText(this, "Đã lưu món ăn!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        });

        btHuy.setOnClickListener(v -> {
            new AlertDialog.Builder(thucan_them.this)
                    .setTitle("Huỷ thêm món")
                    .setMessage("Bạn có muốn huỷ thêm món không?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        setResult(RESULT_CANCELED);
                        finish();
                    })
                    .setNegativeButton("Không", null)
                    .show();
        });
    }

    private void loadLoai() {
        loaiList = db.getAllLoai();
        ArrayAdapter<model_Loai> ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, loaiList);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLoai.setAdapter(ad);
    }

    private void loadMonByLoai(int loaiId) {
        monanList = db.getMonAnByLoai(loaiId);
        if (monanList.isEmpty()) {
            spinnerMon.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"(Không có món)"}));
            tvCalo.setText("--");
            selectedMon = null;
            return;
        }
        ArrayAdapter<model_Monan> ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, monanList);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMon.setAdapter(ad);
    }
}
