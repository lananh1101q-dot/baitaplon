package com.example.baitap;




import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

public class muctieu_chiso extends AppCompatActivity {

    EditText edtChieuCao, edtCanNang, edtTuoi;
    RadioButton radNam, radNu;
    Button btnTiep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muctieu_thaydoi);

        edtChieuCao = findViewById(R.id.chieucao);
        edtCanNang = findViewById(R.id.cannang);
        edtTuoi = findViewById(R.id.tuoi);
        radNam = findViewById(R.id.nam);
        radNu = findViewById(R.id.nu);
        btnTiep = findViewById(R.id.tiep);

        btnTiep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double chieuCao = Double.parseDouble(edtChieuCao.getText().toString());
                double canNang = Double.parseDouble(edtCanNang.getText().toString());
                int tuoi = Integer.parseInt(edtTuoi.getText().toString());

                // tính BMR
                double bmr;
                if (radNam.isChecked()) {
                    bmr = 88.362 + (13.397 * canNang) + (4.799 * chieuCao) - (5.677 * tuoi);
                } else {
                    bmr = 447.593 + (9.247 * canNang) + (3.098 * chieuCao) - (4.330 * tuoi);
                }

                // chuyển sang GoalActivity
                Intent intent = new Intent(muctieu_chiso.this, muctieu_muctieu.class);
                intent.putExtra("chieucao", chieuCao);
                intent.putExtra("cannang", canNang);
                intent.putExtra("tuoi", tuoi);
                intent.putExtra("bmr", bmr);
                startActivity(intent);
            }
        });
    }
}
