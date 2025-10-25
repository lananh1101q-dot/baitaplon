package com.example.baitap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword, edtEmail;
    private Button btnLogin, btnRegister, btnForgot;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtEmail = findViewById(R.id.edtEmail);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnForgot = findViewById(R.id.btnForgot);

        userDAO = new UserDAO(this);

        btnLogin.setOnClickListener(v -> {
            String u = edtUsername.getText().toString().trim();
            String p = edtPassword.getText().toString().trim();
            if (u.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập username và password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (userDAO.dangNhap(u, p)) {
                Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, UongNuocActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Sai username hoặc password", Toast.LENGTH_SHORT).show();
            }
        });

        btnRegister.setOnClickListener(v -> {
            String u = edtUsername.getText().toString().trim();
            String p = edtPassword.getText().toString().trim();
            String e = edtEmail.getText().toString().trim();
            if (u.isEmpty() || p.isEmpty() || e.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            if (userDAO.userTonTai(u)) {
                Toast.makeText(this, "Username đã tồn tại", Toast.LENGTH_SHORT).show();
                return;
            }
            if (userDAO.emailTonTai(e)) {
                Toast.makeText(this, "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                return;
            }
            if (userDAO.dangKy(u, p, e)) {
                Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        btnForgot.setOnClickListener(v -> {
            String e = edtEmail.getText().toString().trim();
            if (e.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                return;
            }
            String pass = userDAO.layMatKhau(e);
            if (pass != null) {
                Toast.makeText(this, "Mật khẩu: " + pass, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Email không tồn tại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
