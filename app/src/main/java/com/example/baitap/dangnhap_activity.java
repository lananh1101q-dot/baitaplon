package com.example.baitap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

        btnDangNhap.setOnClickListener(v -> {
            String user = edtUser.getText().toString().trim();
            String pass = edtPass.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = new database(this).getReadableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM taikhoan WHERE tendangnhap = ? AND matkhau = ?", new String[]{user, pass});
            if (c.moveToFirst()) {
                int userId = c.getInt(c.getColumnIndexOrThrow("id"));
                SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                prefs.edit()
                        .putString("currentUser", user)
                        .putInt("user_id", userId)
                        .apply();

                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MucTieuActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
            }
            c.close();
            db.close();
        });

        btnDangKy.setOnClickListener(v -> startActivity(new Intent(this, dangki_activity.class)));
    }
}
