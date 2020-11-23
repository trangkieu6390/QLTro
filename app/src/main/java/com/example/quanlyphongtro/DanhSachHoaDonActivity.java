package com.example.quanlyphongtro;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class DanhSachHoaDonActivity extends AppCompatActivity {

    final String DATABASE_NAME = "TroNewNew.sqlite";
    SQLiteDatabase database;
    ListView lsvDanhSachHoaDon;
    ArrayList<ChiTietHoaDon> list;
    AdapterHoaDon adapter;
    Spinner spnThangHoaDon, spnNamHoaDon;
    int maphong = -1;
    int position = 0;
    Calendar cal = Calendar.getInstance();

    int day = cal.get(Calendar.DAY_OF_MONTH);
    int month = cal.get(Calendar.MONTH) + 1;
    int year = cal.get(Calendar.YEAR);

    FloatingActionButton ftbTrangChu, ftbHoaDon, ftbPhong, ftbBangGia;
    Animation tren, trai, xeo,back_trai,back_tren,back_xeo;
    boolean trove = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_hoa_don);
        addControl();
        AnhXa();
        readData();
        ThaoTac();
    }
    private void ThaoTac()
    {
        ftbPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DanhSachHoaDonActivity.this,DanhSachPhongActivity.class);
                startActivity(intent);
                //finish();
            }
        });
        ftbHoaDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DanhSachHoaDonActivity.this, DanhSachHoaDonActivity.class);
                startActivity(intent);
               // finish();
            }
        });

        ftbBangGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DanhSachHoaDonActivity.this, CapNhapGiaDienNuocActivity.class);
                startActivity(intent);
                //finish();
            }
        });
        ftbTrangChu.setOnClickListener(new View.OnClickListener() {
            //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
    private void readData(){
    String dk = "01/"+spnThangHoaDon.getSelectedItem() + "/" + spnNamHoaDon.getSelectedItem();
    database = Database.initDatabase(this, DATABASE_NAME);
    Cursor cursor = database.rawQuery("select * from ChiTietHoaDon where NgayLapHoaDon = ?", new String[]{dk+""});
        list.clear();

        for (int i = 0; i < cursor.getCount(); i++) {
        cursor.moveToPosition(i);
        int maCTHD = cursor.getInt(0);
        String tienPhong = cursor.getString(1);
        String tienDien = cursor.getString(2);
        String tienNuoc = cursor.getString(3);
        String tienInternet = cursor.getString(4);
        String tienDVKhac = cursor.getString(5);
        String ngayLapHoaDOn = cursor.getString(6);
        String trangThai = cursor.getString(7);
        int maPhong = cursor.getInt(8);
        String tongTien = cursor.getString(9);
        String soDien = cursor.getString(10);
        String soNuoc = cursor.getString(11);
        String lidothu=cursor.getString(12);
        list.add(new ChiTietHoaDon(maCTHD, tienPhong, tienDien, tienNuoc, tienInternet,
                tienDVKhac,ngayLapHoaDOn, trangThai, maPhong, tongTien, soDien, soNuoc,lidothu));
    }
        adapter.notifyDataSetChanged();
}
    private void addControl() {
        lsvDanhSachHoaDon =  findViewById(R.id.lsvDanhSachHoaDon);
        list = new ArrayList<>();
        adapter = new AdapterHoaDon(DanhSachHoaDonActivity.this, list);
        lsvDanhSachHoaDon.setAdapter(adapter);

        spnThangHoaDon = findViewById(R.id.spnThangHoaDon);
        ArrayList<Integer> arrThang = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            arrThang.add(i);
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrThang);
        spnThangHoaDon.setAdapter(arrayAdapter);
        spnThangHoaDon.setSelection(arrayAdapter.getPosition(month));

        spnNamHoaDon = findViewById(R.id.spnNamHoaDon);
        ArrayList<Integer> arrNam = new ArrayList<>();
        for (int i = year; i > 2018; i--) {
            arrNam.add(i);
        }
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrNam);
        spnNamHoaDon.setAdapter(arrayAdapter1);
        spnNamHoaDon.setSelection(arrayAdapter1.getPosition(year));

        Button btnTim =  findViewById(R.id.btnTim);
        btnTim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XemPhong();
            }
        });
    }
    private void XemPhong()
    {
        String dk = "01/"+spnThangHoaDon.getSelectedItem() + "/" + spnNamHoaDon.getSelectedItem();
        database = Database.initDatabase(DanhSachHoaDonActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("select * from ChiTietHoaDon where NgayLapHoaDon = ?",new String[]{dk+""});
        list.clear();
        for (int m = 0; m < cursor.getCount(); m++) {
            cursor.moveToPosition(m);
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
            list.add(new ChiTietHoaDon(maCTHD, tienPhong, tienDien, tineNuoc, tienInternet,
                    tienDVKhac,ngayLapHoaDOn, trangThai, maPhong, tongTien, soDien, soNuoc,lidothu));
        }
        adapter.notifyDataSetChanged();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hoadon, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_Themhoadon:
                Intent intent = new Intent(DanhSachHoaDonActivity.this, DanhSachPhong_HoaDonActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.search_HD:
                final AlertDialog.Builder builder = new AlertDialog.Builder(DanhSachHoaDonActivity.this);
                final String[] list1 = {"Tất cả","Đã thu","Chưa thu"};
                builder.setTitle("Tìm kiếm");
                builder.setSingleChoiceItems(list1, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        position = i;
                        if (position == 0) {
                            readData();

                        } else if (position == 1)
                        {
                            String dk = "01/"+spnThangHoaDon.getSelectedItem()+"/"  + spnNamHoaDon.getSelectedItem();
                            database = Database.initDatabase(DanhSachHoaDonActivity.this, DATABASE_NAME);
                            Cursor cursor =
                                    database.rawQuery("select * from ChiTietHoaDon where TrangThai = ? and NgayLapHoaDon = ?",
                                            new String[]{1 + "",dk + ""});
                            list.clear();

                            for (int m = 0; m < cursor.getCount(); m++) {
                                cursor.moveToPosition(m);
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
                                list.add(new ChiTietHoaDon(maCTHD, tienPhong, tienDien, tineNuoc, tienInternet,
                                        tienDVKhac,ngayLapHoaDOn, trangThai, maPhong, tongTien, soDien, soNuoc,lidothu));
                            }
                            adapter.notifyDataSetChanged();

                        } else
                        {
                            String dk = "01/"+spnThangHoaDon.getSelectedItem() + "/"+ spnNamHoaDon.getSelectedItem();
                            database = Database.initDatabase(DanhSachHoaDonActivity.this, DATABASE_NAME);
                            Cursor cursor = database.rawQuery("select * from ChiTietHoaDon where TrangThai = ? and NgayLapHoaDon = ?", new String[]{0 + "",dk+""});
                            list.clear();

                            for (int m = 0; m < cursor.getCount(); m++) {
                                cursor.moveToPosition(m);
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
                                list.add(new ChiTietHoaDon(maCTHD, tienPhong, tienDien, tineNuoc, tienInternet,
                                        tienDVKhac,ngayLapHoaDOn, trangThai, maPhong, tongTien, soDien, soNuoc,lidothu));
                            }
                            adapter.notifyDataSetChanged();

                        }
                    }
                });

                builder.setNegativeButton("Chọn", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
