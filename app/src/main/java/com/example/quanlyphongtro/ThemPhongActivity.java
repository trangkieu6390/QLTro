package com.example.quanlyphongtro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class ThemPhongActivity extends AppCompatActivity {

    Context context;
    final String DATABASE_NAME = "TroNewNew.sqlite";
    EditText edtTenPhong,edtLau,edtTienCoc,edtSoDien,edtSoNuoc;
    Button btnThem;
    SQLiteDatabase database;
    Spinner spnTrangThai;

    ListView lsvDanhSachPhong;
    ArrayList<Phong> list;
    AdapterPhong adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_phong);

        AnhXa();
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Them();
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
        btnThem = findViewById(R.id.btnThem);
        spnTrangThai = findViewById(R.id.spnTrangThai);

        ArrayList<String> arrTrangThai = new ArrayList<String>();
        arrTrangThai.add("Trống");
        arrTrangThai.add("Đã cho thuê");
        ArrayAdapter arrayAdapter= new ArrayAdapter(this,android.R.layout.simple_spinner_item,arrTrangThai);
        spnTrangThai.setAdapter(arrayAdapter);
    }
    private void Them()
    {

        database= Database.initDatabase(ThemPhongActivity.this,DATABASE_NAME);
        String tenphong = edtTenPhong.getText().toString();
        String lau = edtLau.getText().toString();
        String tiencoc = edtTienCoc.getText().toString();
        String sodien = edtSoDien.getText().toString();
        String sonuoc = edtSoNuoc.getText().toString();
        String trangthai = spnTrangThai.getSelectedItem().toString();

        ContentValues contentValues = new ContentValues();

        Cursor cursor = database.rawQuery("select * from Phong",null);
        do
        {
            cursor.moveToNext();
            if(cursor.getString(1).equals(tenphong))
            {
                tenphong = tenphong+"(1)";
            }
        }while(cursor.getString(1).equals(tenphong));
        contentValues.put("TenPhong",tenphong);
        contentValues.put("Lau",lau);
        contentValues.put("TienCoc",tiencoc);
        contentValues.put("SoDien",sodien);
        contentValues.put("SoNuoc",sonuoc);
        if(trangthai.equals("Trống"))
        {
            contentValues.put("TrangThai",0);
        }
        else
        {
            contentValues.put("TrangThai",1);
        }

        database = Database.initDatabase(this,DATABASE_NAME);
        database.insert("Phong",null,contentValues);

        Intent intent = new Intent(ThemPhongActivity.this, DanhSachPhongActivity.class);
        startActivity(intent);
        Toast.makeText(ThemPhongActivity.this, "Thêm phòng thành công", Toast.LENGTH_LONG).show();
        onPause();

    }
    protected void onPause(){
        super.onPause();
        //finish();
    }
}
