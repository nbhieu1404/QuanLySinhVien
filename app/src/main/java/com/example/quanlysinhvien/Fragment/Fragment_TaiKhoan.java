package com.example.quanlysinhvien.Fragment;

import static com.example.quanlysinhvien.MainActivity.MY_REQUEST_CODE;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.quanlysinhvien.MainActivity;
import com.example.quanlysinhvien.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Fragment_TaiKhoan extends Fragment {
    private View mView;


    //Fragment_TaiKhoan
    private Button btnThemHinhAnh, btnCapNhat, btnDoiMatKhau, btnChinhSuaThongTin;
    private EditText edtEmail, edtUserName;
    private ImageView imgAvatar;
    private Uri mUri;
    private MainActivity mMainActivity;
    private ProgressDialog progressDialog;

    //Dialog_doi_mat_khau
    private EditText edtMatKhauMoi, edtXacNhan_MatKhauMoi;
    private Button btnThoat, btnCapNhatMKMoi;

    //dialog_dang_nhap_lai
    private EditText edtEmail_GanDay,edtMatKhau_GanDay;
    private Button btnDangNhapLai;
    private TextView txtError_DNL;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_tai_khoan, container, false);

        anhXa();
        progressDialog = new ProgressDialog(getActivity());
        mMainActivity = (MainActivity) getActivity();
        btnCapNhat.setVisibility(View.INVISIBLE);
        btnDoiMatKhau.setVisibility(View.INVISIBLE);
        btnThemHinhAnh.setVisibility(View.INVISIBLE);
        edtUserName.setEnabled(false);
        edtEmail.setEnabled(false);

        chinhSuaThongTin();
        showThongTin();
        themHinhAnh();
        capNhatThongTin();
        doiMatKhau();
        return mView;
    }


    private void anhXa() {
        //dialog_tai_khoan
        imgAvatar = mView.findViewById(R.id.img_avatar);
        edtEmail = mView.findViewById(R.id.edtEmail);
        edtUserName = mView.findViewById(R.id.edtUserName);
        btnDoiMatKhau = mView.findViewById(R.id.btnDoiMatKhau);
        btnChinhSuaThongTin = mView.findViewById(R.id.btnChinhSuaThongTin);
        btnThemHinhAnh = mView.findViewById(R.id.btnThemHinhAnh);
        btnCapNhat = mView.findViewById(R.id.btnCapNhat);

    }

    private void showThongTin() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        edtUserName.setText(user.getDisplayName());
        edtEmail.setText(user.getEmail());
        Glide.with(getActivity()).load(user.getPhotoUrl()).error(R.drawable.ic_user2).into(imgAvatar);
    }

    private void themHinhAnh() {
        btnThemHinhAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choPhepYeuCau();
            }
        });
    }

    private void choPhepYeuCau() {
        if(mMainActivity == null){
            return;
        }
        //Check phiên bản android
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            mMainActivity.moBoSuuTap();
            return;
        }
        //Check sự cho phép của người dùng
        if(getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            mMainActivity.moBoSuuTap();
        }else{
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            getActivity().requestPermissions(permission, MY_REQUEST_CODE);
        }
    }

    public void setBitmapImageView(Bitmap bitmapImageView){
        imgAvatar.setImageBitmap(bitmapImageView);
    }

    public void setmUri(Uri mUri) {
        this.mUri = mUri;
    }

    private void capNhatThongTin() {
        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) return;
                progressDialog.show();

                String fullName = edtUserName.getText().toString().trim();
//                String Email = edtEmail.getText().toString().trim();

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(fullName)
                        .setPhotoUri(mUri)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
                                    mMainActivity.showUserInformation();
                                    btnCapNhat.setVisibility(View.INVISIBLE);
                                    btnDoiMatKhau.setVisibility(View.INVISIBLE);
                                    btnThemHinhAnh.setVisibility(View.INVISIBLE);
                                    edtUserName.setEnabled(false);
                                    edtEmail.setEnabled(false);
                                }
                            }
                        });
            }
        });
    }

    private void chinhSuaThongTin() {
        btnChinhSuaThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_dang_nhap_lai);
                dialog.setCanceledOnTouchOutside(true);

                edtEmail_GanDay = dialog.findViewById(R.id.edtEmail_GanDay);
                edtMatKhau_GanDay = dialog.findViewById(R.id.edtMatKhau_GanDay);
                btnDangNhapLai = dialog.findViewById(R.id.btnDangNhapLai);
                txtError_DNL = dialog.findViewById(R.id.txtError_DNL);

                txtError_DNL.setVisibility(View.INVISIBLE);

                edtEmail_GanDay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        txtError_DNL.setVisibility(View.INVISIBLE);
                    }
                });
                edtMatKhau_GanDay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        txtError_DNL.setVisibility(View.INVISIBLE);
                    }
                });

                btnDangNhapLai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String strE = edtEmail_GanDay.getText().toString().trim();
                        String strMK = edtMatKhau_GanDay.getText().toString().trim();
                        if(strE.length() == 0){
                            edtEmail_GanDay.setError("Email trống");
                            edtEmail_GanDay.requestFocus();
                        } else if(TextUtils.isEmpty(strMK)){
                            edtMatKhau_GanDay.setError("Mật khẩu trống");
                            edtMatKhau_GanDay.requestFocus();
                        }else{
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            progressDialog.show();
                            AuthCredential credential = EmailAuthProvider
                                    .getCredential(strE, strMK);
                            user.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(getActivity(), "Xác thực thành công", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                                progressDialog.dismiss();
                                                edtUserName.setEnabled(true);
                                                edtEmail.setEnabled(true);
                                                btnCapNhat.setVisibility(View.VISIBLE);
                                                btnDoiMatKhau.setVisibility(View.VISIBLE);
                                                btnThemHinhAnh.setVisibility(View.VISIBLE);
//                                        dialog_doi_mat_khau();
                                            }else{
                                                progressDialog.dismiss();
                                                txtError_DNL.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    });
                        }
                    }
                });
                dialog.show();

            }
        });
    }

    private void doiMatKhau() {
        btnDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_doi_mat_khau();
            }
        });
    }

    private void dialog_doi_mat_khau() {
        Dialog dialogDMK = new Dialog(getActivity());
        dialogDMK.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogDMK.setContentView(R.layout.dialog_doi_mat_khau);
        dialogDMK.setCanceledOnTouchOutside(true);

        edtMatKhauMoi = dialogDMK.findViewById(R.id.edtMatKhauMoi);
        edtXacNhan_MatKhauMoi = dialogDMK.findViewById(R.id.edtXacNhan_MatKhauMoi);

        btnCapNhatMKMoi = dialogDMK.findViewById(R.id.btnCapNhatMKMoi);
        btnThoat = dialogDMK.findViewById(R.id.btnThoat);

        btnCapNhatMKMoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strMKM = edtMatKhauMoi.getText().toString().trim();
                String strXNMKM = edtXacNhan_MatKhauMoi.getText().toString().trim();

                if(TextUtils.isEmpty(strMKM)){
                    edtMatKhauMoi.setError("Chưa nhập mật khẩu mới");
                    edtMatKhauMoi.requestFocus();
                }else if(strMKM.length() < 6){
                    edtMatKhauMoi.setError("Mật khẩu tối thiểu gồm 6 kí tự");
                    edtMatKhauMoi.requestFocus();
                }else if(!strXNMKM.equals(strMKM)){
                    edtXacNhan_MatKhauMoi.setError("Mật khẩu không trùng khớp");
                    edtXacNhan_MatKhauMoi.requestFocus();
                }else{
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    progressDialog.show();
                    user.updatePassword(strMKM)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                    dialogDMK.dismiss();
                }
            }
        });
        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDMK.dismiss();
            }
        });


        dialogDMK.show();
    }

}
