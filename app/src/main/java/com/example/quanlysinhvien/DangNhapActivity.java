package com.example.quanlysinhvien;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class DangNhapActivity extends AppCompatActivity {

    private TextView txtError;
    private LinearLayout layoutDangKy;
    private EditText edtEmail, edtPassword;
    private Button btnDangNhap;
    private FirebaseAuth thongTin;
    private ProgressDialog progressDialog;
    private static final String TAG = "LogInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        anhXa();
        edtEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtError.setVisibility(View.INVISIBLE);
            }
        });
        edtPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtError.setVisibility(View.INVISIBLE);
            }
        });
        txtError.setVisibility(View.INVISIBLE);
        nhapDangKi();
        nhapDangNhap();
    }
    private void anhXa() {
        txtError = findViewById(R.id.txtError);
        layoutDangKy = findViewById(R.id.layout_sign_up);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        thongTin = FirebaseAuth.getInstance();
    }



    @Override
    public void onBackPressed() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Thoát");
        b.setTitle("Bạn thực sự muốn thoát?");
        b.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        b.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        b.create().show();
    }
    private void nhapDangNhap() {
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtEmail = edtEmail.getText().toString().trim();
                String txtPassword = edtPassword.getText().toString().trim();

                if (TextUtils.isEmpty(txtEmail)) {
                    edtEmail.setError("Tài khoản không được để trống");
                    edtEmail.requestFocus();
                } else if (TextUtils.isEmpty(txtPassword)) {
                    edtPassword.setError("Mật khẩu không được để trống");
                    edtPassword.requestFocus();
                } else {
                    DangNhapNguoiDung(txtEmail, txtPassword);
                }
            }
        });
    }
    private void DangNhapNguoiDung(String txtEmail, String txtPassword) {

        thongTin.signInWithEmailAndPassword(txtEmail, txtPassword).addOnCompleteListener(DangNhapActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Intent intent = new Intent(DangNhapActivity.this, MainActivity.class);
                    Toast.makeText(DangNhapActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }else{
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        txtError.setText("Người dùng không tồn tại hoặc không còn giá trị. Vui lòng thử lại!");
                        txtError.setVisibility(View.VISIBLE);
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        txtError.setVisibility(View.VISIBLE);
                    }
                    catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(DangNhapActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    private void nhapDangKi() {
        layoutDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DangNhapActivity.this, DangKyActivity.class);
                startActivity(intent);
            }
        });
    }
}