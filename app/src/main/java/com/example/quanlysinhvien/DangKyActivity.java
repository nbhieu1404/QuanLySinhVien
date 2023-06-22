package com.example.quanlysinhvien;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DangKyActivity extends AppCompatActivity {
    private EditText edtTenND, edtEmail, edtMK, edtXacNhanMK;
    private Button btnDangKy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);

        anhXa();
        batSuKien();
    }

    private void anhXa() {
        edtTenND = findViewById(R.id.edtTenND);
        edtEmail = findViewById(R.id.edtEmal);
        edtMK = findViewById(R.id.edtMK);
        edtXacNhanMK = findViewById(R.id.edtXacNhanMK);
        btnDangKy = findViewById(R.id.btnDangky);
    }

    private void batSuKien() {
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmail = edtEmail.getText().toString().trim();
                String strMK = edtMK.getText().toString().trim();
                if(edtTenND.length() == 0) {
                    edtTenND.setError("Tên người dùng không được bỏ trống");
                    edtTenND.requestFocus();
                }else if(edtTenND.length() < 2){
                    edtTenND.setError("Tên người dùng không hợp lệ");
                    edtTenND.requestFocus();
                }else if(edtEmail.length() == 0) {
                    edtEmail.setError("Email không được bỏ trống");
                    edtEmail.requestFocus();
                } else if(edtMK.length() == 0) {
                    edtMK.setError("Mật khẩu không được bỏ trống");
                    edtMK.requestFocus();
                }else if(edtMK.length() < 6){
                    edtMK.setError("Mật khẩu tối thiểu gồm 6 kí tự");
                    edtMK.requestFocus();
                }else if(!edtXacNhanMK.getText().toString().trim().equals(strMK)){
                    edtXacNhanMK.setError("Mật khẩu không trùng khớp");
                    edtXacNhanMK.requestFocus();
                }else{
                    nhapDangKy(strEmail, strMK);
                }
            }
        });
    }

    private void nhapDangKy(String strEmail, String strMK) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(strEmail, strMK)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(DangKyActivity.this, DangNhapActivity.class);
                            Toast.makeText(DangKyActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finishAffinity(); // Đóng hết các Activity trước
                        } else {
                            Toast.makeText(DangKyActivity.this, "Đăng kí thất bại",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}