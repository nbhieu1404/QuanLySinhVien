package com.example.quanlysinhvien;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.quanlysinhvien.Fragment.Fragment_LichSu;
import com.example.quanlysinhvien.Fragment.Fragment_TaiKhoan;
import com.example.quanlysinhvien.Fragment.Fragment_TrangChu;
import com.example.quanlysinhvien.Fragment.Fragment_VeChungToi;

import com.google.android.material.navigation.NavigationView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;

    private ProgressDialog progressDialog;


    public static final int MY_REQUEST_CODE = 100;
    private static final int FRAGMENT_TRANGCHU = 0;
    private static final int FRAGMENT_LICHSU = 1;
    private static final int FRAGMENT_TAIKHOAN = 2;
    private static final int FRAGMENT_VECHUNGTOI = 3;


    private int mCurrentFragment = FRAGMENT_TRANGCHU;
    final private Fragment_TaiKhoan mFragment_TaiKhoan = new Fragment_TaiKhoan();

    private NavigationView navigationView;
    private ImageView imgAvatar;
    private TextView txtUserName, txtEmail;

    private ActivityResultLauncher<Intent>  mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                Intent intent = result.getData();
                if(intent == null){
                    return;
                }
                Uri uri = intent.getData();
                mFragment_TaiKhoan.setmUri(uri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    mFragment_TaiKhoan.setBitmapImageView(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        anhXa();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Đóng mở ngăn kéo
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar
                , R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Bắt sự kiện khi click vào thanh ngang
        navigationView.setNavigationItemSelectedListener(this);

        //luôn mở trang chủ khi vào app
        replaceFragment(new Fragment_TrangChu());
        navigationView.getMenu().findItem(R.id.nav_trang_chu).setChecked(true);

        showUserInformation();
    }
    private void anhXa() {
        navigationView = findViewById(R.id.navigation_view);
        imgAvatar = navigationView.getHeaderView(0).findViewById(R.id.img_avatar);
        txtEmail = navigationView.getHeaderView(0).findViewById(R.id.txtEmail);
        txtUserName = navigationView.getHeaderView(0).findViewById(R.id.txtUserName);
    }

    //Show thông tin cơ bản trong header_nav
    public void showUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;

        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

        if(name == null){
            txtUserName.setVisibility(View.GONE);
        }else{
            txtUserName.setText(name);
        }
        txtEmail.setText(email);
        Glide.with(this).load(photoUrl).error(R.drawable.ic_user2).into(imgAvatar);

    }

    //Sự kiện bấm phím back
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer((GravityCompat.START));
        }else{
            super.onBackPressed();
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentFrame, fragment);
        transaction.commit();
    }


    private void openDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Thoát");
        b.setTitle("Bạn thực sự muốn đăng xuất?");
        b.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        b.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(MainActivity.this, DangNhapActivity.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(intent);
                finish();
            }
        });
        b.create().show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                moBoSuuTap();
            }else{
                Toast.makeText(this, "Vui lòng cấp quyền truy cập thư viện!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //Chọn ảnh
    public void moBoSuuTap() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Chọn ảnh "));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_trang_chu){
            if(mCurrentFragment != FRAGMENT_TRANGCHU){
                replaceFragment(new Fragment_TrangChu());
                mCurrentFragment = FRAGMENT_TRANGCHU;
            }
//        }else if(id == R.id.nav_lich_su){
//            if(mCurrentFragment != FRAGMENT_LICHSU){
//                replaceFragment(new Fragment_LichSu());
//                mCurrentFragment = FRAGMENT_LICHSU;
//            }
            //
        }else if(id == R.id.nav_tai_khoan){
            if(mCurrentFragment != FRAGMENT_TAIKHOAN){
                replaceFragment(new Fragment_TaiKhoan());
                mCurrentFragment = FRAGMENT_TAIKHOAN;
            }

//        }else if(id == R.id.nav_ve_chung_toi){
//            if(mCurrentFragment != FRAGMENT_VECHUNGTOI){
//                replaceFragment(new Fragment_VeChungToi());
//                mCurrentFragment = FRAGMENT_VECHUNGTOI;
//            }
        }else{
            openDialog();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}