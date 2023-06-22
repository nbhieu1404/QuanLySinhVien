package com.example.quanlysinhvien;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Chuyển màn hình
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextActivity();
            }
        }, 1500);
    }

    //Check user đã đăng nhập hay chưa
    private void nextActivity() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            //Chưa login
            Intent intent = new Intent(this, DangNhapActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }
}