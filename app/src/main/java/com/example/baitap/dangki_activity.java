package com.example.baitap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class dangki_activity extends AppCompatActivity {

    EditText edtNewUser, edtNewPass, edtNewPass2;
    Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangki); // Giao diện bạn đã tạo ở trên

        edtNewUser = findViewById(R.id.edtNewUser);
        edtNewPass = findViewById(R.id.edtNewPass);
        edtNewPass2 = findViewById(R.id.edtNewPass2);
        btnCreate = findViewById(R.id.btnCreate);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        btnCreate.setOnClickListener(v -> {
            String user = edtNewUser.getText().toString().trim();
            String pass = edtNewPass.getText().toString().trim();
            String pass2 = edtNewPass2.getText().toString().trim();

            // Kiểm tra nhập trống
            if (user.isEmpty() || pass.isEmpty() || pass2.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra xác minh mật khẩu
            if (!pass.equals(pass2)) {
                Toast.makeText(this, "Mật khẩu xác nhận không khớp!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lưu thông tin tài khoản
            editor.putString("username", user);
            editor.putString("password", pass);
            editor.apply();

            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

            // Quay về màn hình đăng nhập
            startActivity(new Intent(this, dangnhap_activity.class));
            finish();
        });
    }
}
