package com.example.quanlyphongtro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DanhSachPhong_HoaDonActivity extends AppCompatActivity {

    final String DATABASE_NAME="TroNewNew.sqlite";
    SQLiteDatabase database;
    ListView lsvDanhSachPhong;
    ArrayList<Phong>list;
    AdapterChiTietHoaDon adapter;

    int position = 0 ;
    FloatingActionButton ftbTrangChu, ftbHoaDon, ftbPhong, ftbBangGia;
    Animation tren, trai, xeo,back_trai,back_tren,back_xeo;
    boolean trove = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_phong__hoa_don);
        addControl();
        readData();
        AnhXa();
        ThaoTac();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.search_allhoadon,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.search_HD:

                final AlertDialog.Builder builder=new AlertDialog.Builder(this);
                final String[]list1={"Tất cả hóa đơn","Chưa thanh toán","Đã thanh toán"};
                builder.setTitle("Tìm kiếm");
                builder.setSingleChoiceItems(list1, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        position=which;
                        if(position==0) {
                            readData();
                        }
                        else if(position==1){

                        }
                            else{

                        }
                    }
                });
                builder.setNegativeButton("Chọn", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
                return true;

                default:return super.onOptionsItemSelected(item);
        }
    }
    private void readData() {
        database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("select * from Phong", null);
        list.clear();

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            int maphong = cursor.getInt(0);
            String tenphong = cursor.getString(1);
            String lau = cursor.getString(2);
            String tiencoc = cursor.getString(3);
            int sodien = cursor.getInt(4);
            int sonuoc = cursor.getInt(5);
            String trangthai = cursor.getString(6);
            list.add(new Phong(maphong, tenphong, lau, tiencoc, sodien, sonuoc, trangthai));
        }
        adapter.notifyDataSetChanged();
    }

    private void addControl() {
        lsvDanhSachPhong = findViewById(R.id.lsvdsphonghoadon);
        list = new ArrayList<>();
        adapter = new AdapterChiTietHoaDon(this, list);
        lsvDanhSachPhong.setAdapter(adapter);

    }
    private void AnhXa()
    {
        ftbTrangChu = findViewById(R.id.ftbTrangChu);
        ftbHoaDon = findViewById(R.id.ftbHoaDon);
        ftbPhong = findViewById(R.id.ftbPhong);
        ftbBangGia = findViewById(R.id.ftbBangGia);

        trai = AnimationUtils.loadAnimation(this, R.anim.trai);
        tren = AnimationUtils.loadAnimation(this, R.anim.phao);
        xeo = AnimationUtils.loadAnimation(this, R.anim.xeo);

        back_trai = AnimationUtils.loadAnimation(this, R.anim.back_trai);
        back_tren = AnimationUtils.loadAnimation(this, R.anim.back_phai);
        back_xeo = AnimationUtils.loadAnimation(this, R.anim.back_xeo);
    }

    private void move() {
        FrameLayout.LayoutParams paramsTrai = (FrameLayout.LayoutParams) ftbPhong.getLayoutParams();
        paramsTrai.rightMargin = (int) (ftbPhong.getWidth() * 1.7);
        ftbPhong.setLayoutParams(paramsTrai);
        ftbPhong.startAnimation(trai);

        FrameLayout.LayoutParams paramsTren = (FrameLayout.LayoutParams) ftbBangGia.getLayoutParams();
        paramsTren.bottomMargin = (int) (ftbBangGia.getWidth() * 1.7);
        ftbBangGia.setLayoutParams(paramsTren);
        ftbBangGia.startAnimation(tren);

        FrameLayout.LayoutParams paramsXeo = (FrameLayout.LayoutParams) ftbHoaDon.getLayoutParams();
        paramsXeo.bottomMargin = (int) (ftbHoaDon.getWidth() * 1.3);
        paramsXeo.rightMargin = (int) (ftbHoaDon.getWidth() * 1.3);
        ftbHoaDon.setLayoutParams(paramsXeo);
        ftbHoaDon.startAnimation(xeo);
    }
    private void Back()
    {
        FrameLayout.LayoutParams paramsTrai = (FrameLayout.LayoutParams) ftbPhong.getLayoutParams();
        paramsTrai.rightMargin -= (int) (ftbPhong.getWidth() * 1.4);
        ftbPhong.setLayoutParams(paramsTrai);
        ftbPhong.startAnimation(back_trai);

        FrameLayout.LayoutParams paramsTren = (FrameLayout.LayoutParams) ftbBangGia.getLayoutParams();
        paramsTren.bottomMargin -= (int) (ftbBangGia.getWidth() * 1.4);
        ftbBangGia.setLayoutParams(paramsTren);
        ftbBangGia.startAnimation(back_tren);

        FrameLayout.LayoutParams paramsXeo = (FrameLayout.LayoutParams) ftbHoaDon.getLayoutParams();
        paramsXeo.bottomMargin -= (ftbHoaDon.getWidth() * 1);
        paramsXeo.rightMargin -=  (ftbHoaDon.getWidth() * 1);
        ftbHoaDon.setLayoutParams(paramsXeo);
        ftbHoaDon.startAnimation(back_xeo);
    }
    private void ThaoTac()
    {
        ftbPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DanhSachPhong_HoaDonActivity.this,DanhSachPhongActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        ftbHoaDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DanhSachPhong_HoaDonActivity.this, DanhSachHoaDonActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        ftbBangGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DanhSachPhong_HoaDonActivity.this, CapNhapGiaDienNuocActivity.class);
                startActivity(intent);
               //finish();
            }
        });
        ftbTrangChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trove == false)
                {
                    move();
                    trove= true;
                }
                else
                {
                    Back();
                    trove=false;
                }
            }
        });
    }
}
