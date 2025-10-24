package com.example.baitap;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class dangki_activity extends AppCompatActivity {
    EditText edtUser, edtPass;
    Button btnDangKy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangki);

        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        btnDangKy = findViewById(R.id.btnDangKy);

        btnDangKy.setOnClickListener(v -> {
            String user = edtUser.getText().toString().trim();
            String pass = edtPass.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = new database(this).getWritableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM taikhoan WHERE tendangnhap = ?", new String[]{user});
            if (c.moveToFirst()) {
                Toast.makeText(this, "Tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
                c.close();
                db.close();
                return;
            }
            c.close();

            ContentValues values = new ContentValues();
            values.put("tendangnhap", user);
            values.put("matkhau", pass);
            long newId = db.insert("taikhoan", null, values);
            db.close();

            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            prefs.edit()
                    .putString("currentUser", user)
                    .putInt("user_id", (int) newId)
                    .apply();

            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, muctieu_themmuctieu.class));
            finish();
        });
    }
}
