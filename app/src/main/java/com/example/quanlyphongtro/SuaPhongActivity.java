package com.example.quanlyphongtro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class SuaPhongActivity extends AppCompatActivity {

    final String DATABASE_NAME = "TroNewNew.sqlite";
    int maphong = -1;
    Button btnSua;
    EditText edtTenPhong,edtLau,edtTienCoc,edtSoDien,edtSoNuoc,edtTrangThai;
    SQLiteDatabase database;
    Spinner spnTrangThai;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_phong);
        AnhXa();
        initUI();
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sua();

            }
        });

    }
    private void AnhXa()
    {
        edtTenPhong = findViewById(R.id.edtTenPhong);
        edtLau = findViewById(R.id.edtLau);
        edtTienCoc = findViewById(R.id.edtTienCoc);
        edtSoDien = findViewById(R.id.edtSoDien);
        edtSoNuoc = findViewById(R.id.edtSoNuoc);


        btnSua = findViewById(R.id.btnLuuPhong);
        Intent intent = getIntent();
        maphong = intent.getIntExtra("aa",-1);
        spnTrangThai = findViewById(R.id.spnTrangThaiSua);
        ArrayList<String> arrTrangThai = new ArrayList<String>();
        arrTrangThai.add("Trống");
        arrTrangThai.add("Đã cho thuê");
        ArrayAdapter arrayAdapter= new ArrayAdapter(this,android.R.layout.simple_spinner_item,arrTrangThai);
        spnTrangThai.setAdapter(arrayAdapter);
    }
    private void initUI()
    {
        Intent intent = getIntent();
        maphong = intent.getIntExtra("aa",-1);
        SQLiteDatabase database= Database.initDatabase(SuaPhongActivity.this,DATABASE_NAME);
        Cursor cursor = database.rawQuery("select * from Phong where MaPhong = ?",new String[]{maphong+""});
        cursor.moveToFirst();
        String tenphong = cursor.getString(1);
        String lau = cursor.getString(2);
        String tiencoc = cursor.getString(3);
        String sodien = cursor.getString(4);
        String sonuoc = cursor.getString(5);
        String trangthai = cursor.getString(6);

        edtTenPhong.setText(tenphong);
        edtLau.setText(lau);
        edtTienCoc.setText(tiencoc);
        edtSoDien.setText(sodien);
        edtSoNuoc.setText(sonuoc);
        if(trangthai.equals("1"))
        {
            spnTrangThai.setSelection(1);
        }
        else
        {
            spnTrangThai.setSelection(0);
        }

    }
    private void Sua()
    {

        String tenphong = edtTenPhong.getText().toString();
        String lau = edtLau.getText().toString();
        String tiencoc = edtTienCoc.getText().toString();
        String sodien = edtSoDien.getText().toString();
        String sonuoc = edtSoNuoc.getText().toString();
        String trangthai = spnTrangThai.getSelectedItem().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put("TenPhong", tenphong);
        contentValues.put("Lau", lau);
        contentValues.put("TienCoc", tiencoc);
        contentValues.put("SoDien", sodien);
        contentValues.put("SoNuoc", sonuoc);
        if (trangthai.equals("Trống")) {
            contentValues.put("trangthai", 0);
        } else {
            contentValues.put("trangthai", 1);
        }
        Intent intent = getIntent();
        maphong = intent.getIntExtra("aa", -1);

        SQLiteDatabase database = Database.initDatabase(SuaPhongActivity.this, DATABASE_NAME);
        database.update("Phong", contentValues, "MaPhong=?", new String[]{maphong + ""});
        Intent intent1 = new Intent(SuaPhongActivity.this, DanhSachPhongActivity.class);
        startActivity(intent1);
        Toast.makeText(SuaPhongActivity.this, "Sửa phòng thành công", Toast.LENGTH_LONG).show();
        onPause();
    }



    protected void onPause(){
        super.onPause();
        finish();
    }
    private boolean Check(int maphong)
    {
        boolean KT = false;
        int a = 0;
        SQLiteDatabase database= Database.initDatabase(SuaPhongActivity.this,DATABASE_NAME);
        Cursor cursor = database.rawQuery("select * from KhachThue",null);
        for(int i = 0;i<cursor.getCount();i++)
        {
            cursor.moveToPosition(i);
            if(cursor.getInt(7)==maphong)
            {
                a++;
            }
        }
        if(a>0)
            return KT=true;
        return KT = false;
    }
}
