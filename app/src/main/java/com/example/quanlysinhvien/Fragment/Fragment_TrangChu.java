package com.example.quanlysinhvien.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

//import com.example.quanlysinhvien.QLLHActivity;
//import com.example.quanlysinhvien.QLSVActivity;
import com.example.quanlysinhvien.R;

public class Fragment_TrangChu extends Fragment {
    private View mView;
//    private LinearLayout QLLH, QLSV;

    @SuppressLint("ResourceType")
    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_trang_chu, container, false);

//        anhXa();
//        suKien();
        return mView;
    }
//    private void anhXa() {
//        QLLH = mView.findViewById(R.id.QLLH);
//        QLSV = mView.findViewById(R.id.QLSV);
//
//    }
//    private void suKien() {
//        QLLH.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(requireContext(), QLLHActivity.class);
//                startActivity(intent);
//            }
//        });
//        QLSV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(requireContext(), QLSVActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
}
