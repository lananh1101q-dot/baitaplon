package com.example.baitap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;

public class dangnhap_activity extends AppCompatActivity {

    private EditText edtUser, edtPass;
    private Button btnDangNhap, btnDangKy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangnhap);

        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        btnDangKy = findViewById(R.id.btnDangKy);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        btnDangNhap.setOnClickListener(v -> {
            String username = edtUser.getText().toString().trim();
            String password = edtPass.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                String usersJson = prefs.getString("users", "{}");
                JSONObject users = new JSONObject(usersJson);

                if (!users.has(username)) {
                    Toast.makeText(this, "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String savedPassword = users.getString(username);
                if (savedPassword.equals(password)) {
                    prefs.edit().putString("currentUser", username).apply();
                    Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(this, MucTieuActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Sai mật khẩu!", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi khi đăng nhập!", Toast.LENGTH_SHORT).show();
            }
        });

        btnDangKy.setOnClickListener(v -> {
            startActivity(new Intent(this, dangki_activity.class));
        });
    }
}
