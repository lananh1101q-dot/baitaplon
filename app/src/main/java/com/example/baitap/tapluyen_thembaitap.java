package com.example.baitap;



import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class tapluyen_thembaitap extends AppCompatActivity {

    private EditText edtTen, edtThoigian, edtCalo;
    private Button btnLuu, btnHuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tapluyen_thembaitap);

        edtTen = findViewById(R.id.edtTen);
        edtThoigian = findViewById(R.id.edtThoigian);
        edtCalo = findViewById(R.id.edtCalo);
        btnLuu = findViewById(R.id.btnLuu);
        btnHuy = findViewById(R.id.btnHuy);

        btnLuu.setOnClickListener(v -> {
            String ten = edtTen.getText().toString().trim();
            int thoigian = Integer.parseInt(edtThoigian.getText().toString().trim());
            int calo = Integer.parseInt(edtCalo.getText().toString().trim());

            Intent result = new Intent();
            result.putExtra("ten", ten);
            result.putExtra("thoigian", thoigian);
            result.putExtra("calo", calo);

            setResult(RESULT_OK, result);
            finish();
        });

        btnHuy.setOnClickListener(v -> finish());
    }
}
