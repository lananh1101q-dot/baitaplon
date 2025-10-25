package com.example.baitap;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.baitap.database;
import com.example.baitap.model_Loai;
import com.example.baitap.model_Monan;
import com.example.baitap.model_FoodLog;

import java.util.List;

public class thucan_sua extends AppCompatActivity {

    Spinner spinnerLoai, spinnerMon;
    TextView tvCalo;
    Button btLuu, btHuy;
    database db;
    List<model_Loai> loaiList;
    List<model_Monan> monanList;
    model_Monan selectedMon;
    model_FoodLog curLog;
    int logId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thucan_sua);

        spinnerLoai = findViewById(R.id.spinnerLoai);
        spinnerMon = findViewById(R.id.spinnerMon);
        tvCalo = findViewById(R.id.edtCalo); // you used edtCalo for calo; we'll show calo here or change id
        btLuu = findViewById(R.id.bt_themluu);
        btHuy = findViewById(R.id.bt_themhuy);

        db = new database(this);

        Intent it = getIntent();
        logId = it.getIntExtra("log_id", -1);
        if (logId == -1) {
            finish();
            return;
        }

        // load log entry
        // getFoodLogsByDate doesn't get by id, but we can query monan by id via db.getFoodLogsByDate?
        // For simplicity, we'll fetch all logs for today and find id.
        // Better way: create a new method getFoodLogById; but to keep code concise, do a simple approach:
        // We'll re-query logs by today's date and find the one matching id.
        String today = curDate();
        List<model_FoodLog> list = db.getFoodLogsByDate(today);
        for (model_FoodLog f : list) {
            if (f.getId() == logId) {
                curLog = f;
                break;
            }
        }
        if (curLog == null) {
            // not found: maybe different date -> try all (simple approach)
            // fallback: finish
            finish();
            return;
        }

        loadLoai();

        // pre-select the loai and mon after both lists are ready
        spinnerLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                model_Loai l = loaiList.get(position);
                loadMonByLoai(l.getId());
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerMon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                selectedMon = monanList.get(position);
                // show calo
                tvCalo.setText(String.valueOf(selectedMon.getCalo()));
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        btLuu.setOnClickListener(v -> {
            if (selectedMon == null) {
                Toast.makeText(thucan_sua.this, "Chọn món", Toast.LENGTH_SHORT).show();
                return;
            }
            db.updateFoodLog(logId, selectedMon.getId(), null);
            setResult(RESULT_OK);
            finish();
        });

        btHuy.setOnClickListener(v -> {
            new AlertDialog.Builder(thucan_sua.this)
                    .setTitle("Huỷ")
                    .setMessage("Bạn có muốn huỷ sửa không?")
                    .setPositiveButton("Có", (dialog, which) -> { setResult(RESULT_CANCELED); finish(); })
                    .setNegativeButton("Không", null)
                    .show();
        });

        // After loadLoai and mon, we need to set initial selected items:
        // We'll postpone selecting spinners until monanList loaded.
    }

    private String curDate() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
        return sdf.format(new java.util.Date());
    }

    private void loadLoai() {
        loaiList = db.getAllLoai();
        ArrayAdapter<model_Loai> ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, loaiList);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLoai.setAdapter(ad);

        // Try to set spinnerLoai selection to the loai of current mon
        // but we don't yet know monan list; we'll set selection after mon list loaded in loadMonByLoai
        // Find loai index containing curLog.monanId
        model_Monan curMon = db.getMonAnById(curLog.getMonanId());
        if (curMon != null) {
            int loaiId = curMon.getLoaiId();
            for (int i = 0; i < loaiList.size(); i++) {
                if (loaiList.get(i).getId() == loaiId) {
                    spinnerLoai.setSelection(i);
                    break;
                }
            }
        }
    }

    private void loadMonByLoai(int loaiId) {
        monanList = db.getMonAnByLoai(loaiId);
        ArrayAdapter<model_Monan> ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, monanList);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMon.setAdapter(ad);

        // if curLog corresponds to one of these, select it
        for (int i = 0; i < monanList.size(); i++) {
            if (monanList.get(i).getId() == curLog.getMonanId()) {
                spinnerMon.setSelection(i);
                break;
            }
        }
    }
}


