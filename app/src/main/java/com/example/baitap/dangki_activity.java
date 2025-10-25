package com.example.baitap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;

public class dangki_activity extends AppCompatActivity {

    private EditText edtUser, edtPass;
    private Button btnDangKy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangki);

        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        btnDangKy = findViewById(R.id.btnDangKy);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        btnDangKy.setOnClickListener(v -> {
            String username = edtUser.getText().toString().trim();
            String password = edtPass.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                String usersJson = prefs.getString("users", "{}");
                JSONObject users = new JSONObject(usersJson);

                if (users.has(username)) {
                    Toast.makeText(this, "Tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
                    return;
                }

                users.put(username, password);
                prefs.edit().putString("users", users.toString()).apply();

                Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(this, dangnhap_activity.class));
                finish();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi khi đăng ký!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
