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

public class HoaDonCuaTungPhongActivity extends AppCompatActivity {

    final String DATABASE_NAME = "TroNewNew.sqlite";
    SQLiteDatabase database;
    ListView lsvHoaDonCuaTungPhong;
    ArrayList<ChiTietHoaDon> list;
    AdapterHoaDonCuaTungPhong adapter;
    int maphong =-1;
    FloatingActionButton ftbTrangChu, ftbHoaDon, ftbPhong, ftbBangGia;
    Animation tren, trai, xeo,back_trai,back_tren,back_xeo;
    boolean trove = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa_don_cua_tung_phong);
        ThaoTac();
        AnhXa();
        ReadData();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.hoadon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_addHoaDon:
                Intent intent1 = getIntent();
                maphong = intent1.getIntExtra("MaPhong",-1);
                Cursor cursor =  database.rawQuery("select * from Phong where MaPhong = ?",new String[]{maphong+""});
                cursor.moveToFirst();
                String TT = cursor.getString(7);
                if(TT.equals("1"))
                {
                    Intent intent = new Intent(HoaDonCuaTungPhongActivity.this, LapHoaDonActivity.class);
                    intent.putExtra("MaPhong",maphong);
                    startActivity(intent);
                    //finish();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(HoaDonCuaTungPhongActivity.this);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Phòng còn trống nên không thể lập hóa đơn");

                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dialog =builder.create();
                    dialog.show();
                }
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
                Intent intent = new Intent(HoaDonCuaTungPhongActivity.this,DanhSachPhongActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ftbHoaDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HoaDonCuaTungPhongActivity.this, DanhSachHoaDonActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ftbBangGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HoaDonCuaTungPhongActivity.this, CapNhapGiaDienNuocActivity.class);
                startActivity(intent);
                finish();
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
    private void AnhXa()
    {
        lsvHoaDonCuaTungPhong = (ListView) findViewById(R.id.lsvHoaDonCuaTungPhong);
        list = new ArrayList<>();
        adapter = new AdapterHoaDonCuaTungPhong(this,list);
        lsvHoaDonCuaTungPhong.setAdapter(adapter);

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
    private void ReadData()
    {
        Intent intent1 = getIntent();
        maphong = intent1.getIntExtra("MaPhong",-1);
        database = Database.initDatabase(this,DATABASE_NAME);
        Cursor cursor =  database.rawQuery("select * from ChiTietHoaDon where MaPhong = ?",new String[]{maphong+""});
        list.clear();
        for(int i = 0 ; i< cursor.getCount(); i++)
        {
            cursor.moveToPosition(i);
            int maCTHD = cursor.getInt(0);
            String tienPhong = cursor.getString(1);
            String tienDien = cursor.getString(2);
            String tineNuoc = cursor.getString(3);
            String tienInternet = cursor.getString(4);
            String tienDVKhac = cursor.getString(5);
            String ngayLapHoaDOn = cursor.getString(6);
            String trangThai = cursor.getString(7);
            int maPhong = cursor.getInt(8);
            String tongTien = cursor.getString(9);
            String soDien = cursor.getString(10);
            String soNuoc = cursor.getString(11);
            String lidothu=cursor.getString(12);
            list.add(new ChiTietHoaDon(maCTHD,tienPhong,tienDien,tineNuoc,tienInternet,
                    tienDVKhac,ngayLapHoaDOn,trangThai,maPhong,tongTien,soDien,soNuoc,lidothu));
        }
        adapter.notifyDataSetChanged();
    }
}
