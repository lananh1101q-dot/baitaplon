package com.example.baitap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class dangnhap_activity extends AppCompatActivity {

    EditText edtUser, edtPass;
    Button btnDangNhap, btnDangKy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangnhap);

        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        btnDangKy = findViewById(R.id.btnDangKy);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedUser = prefs.getString("username", null);
        String savedPass = prefs.getString("password", null);

        // 🟢 Nút Đăng nhập
        btnDangNhap.setOnClickListener(v -> {
            String user = edtUser.getText().toString().trim();
            String pass = edtPass.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (savedUser == null || savedPass == null) {
                Toast.makeText(this, "Chưa có tài khoản, vui lòng đăng ký!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (user.equals(savedUser) && pass.equals(savedPass)) {
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                // ✅ Chuyển sang màn hình chính (ví dụ tapluyen)
                Intent intent = new Intent(this, MucTieuActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Sai tên đăng nhập hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
            }
        });

        // 🟢 Nút Đăng ký
        btnDangKy.setOnClickListener(v -> {
            Intent intent = new Intent(this, dangki_activity.class);
            startActivity(intent);
        });
    }
}
