package com.example.quanlyphongtro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class XemThanhVienActivity extends AppCompatActivity {

    final String DATABASE_NAME = "TroNewNew.sqlite";
    SQLiteDatabase database;
    ListView lsvDanhsachthanhvien;
    int maphong = -1;
    ArrayList<KhachThue> list;
    AdapterKhachThue adapter;
    FloatingActionButton ftbTrangChu, ftbHoaDon, ftbPhong, ftbBangGia;
    Animation tren, trai, xeo,back_trai,back_tren,back_xeo;
    boolean trove = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_thanh_vien);
        addControl();
        readData();
        AnhXa();
        ThaoTac();
    }
    private void readData()
    {
        Intent intent = getIntent();
        maphong = intent.getIntExtra("MaPhong",-1);
        database = Database.initDatabase(this,DATABASE_NAME);
        Cursor cursor = database.rawQuery("select * from KhachThue where MaPhong = ?",new String[]{maphong+""});
        list.clear();
        for(int i = 0 ; i< cursor.getCount(); i++)
        {
            cursor.moveToPosition(i);
            int makhach = cursor.getInt(0);

            String tenkhach = cursor.getString(2);
            String gioitinh = cursor.getString(3);
            String ngaysinh = cursor.getString(4);
            String cmnd = cursor.getString(5);
            String sdt = cursor.getString(6);
            String ngaythue = cursor.getString(7);
            byte[]anhDD=cursor.getBlob(1);
            byte[]anhTruoc=cursor.getBlob(8);
            byte[]anhSau=cursor.getBlob(9);
            int maphong = cursor.getInt(10);
            list.add(new KhachThue(makhach,anhDD,tenkhach,gioitinh,ngaysinh,cmnd,sdt,ngaythue,anhTruoc,anhSau,maphong));
        }
        adapter.notifyDataSetChanged();
    }

    private void addControl()
    {
        lsvDanhsachthanhvien = findViewById(R.id.lsvDanhsachthanhvien);
        list = new ArrayList<>();
        adapter = new AdapterKhachThue(this,list);
        lsvDanhsachthanhvien.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_themnguoithue, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_addKhach:
                Intent intent1 = getIntent();
                maphong = intent1.getIntExtra("MaPhong",-1);
                Intent intent = new Intent(XemThanhVienActivity.this, ThemNguoiThueActivity.class);
                intent.putExtra("MaPhong1",maphong);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void ThaoTac()

    {
        ftbPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(XemThanhVienActivity.this,DanhSachPhongActivity.class);
                startActivity(intent);
                //finish();
            }
        });
        ftbBangGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(XemThanhVienActivity.this, CapNhapGiaDienNuocActivity.class);
                startActivity(intent);
                //finish();
            }
        });
        ftbHoaDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(XemThanhVienActivity.this, DanhSachHoaDonActivity.class);
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
        paramsXeo.bottomMargin -= (int) (ftbHoaDon.getWidth() * 1);
        paramsXeo.rightMargin -= (int) (ftbHoaDon.getWidth() * 1);
        ftbHoaDon.setLayoutParams(paramsXeo);
        ftbHoaDon.startAnimation(back_xeo);
    }
}

