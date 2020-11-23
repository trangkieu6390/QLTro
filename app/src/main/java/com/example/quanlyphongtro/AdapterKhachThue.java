package com.example.quanlyphongtro;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class AdapterKhachThue extends BaseAdapter
{
    final String DATABASE_NAME ="TroNewNew.sqlite";
    Activity context;
    ArrayList<KhachThue> list;

    public AdapterKhachThue(Activity context, ArrayList<KhachThue> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.danhsachthanhvien,null);

        //TextView txtMa = (TextView)row.findViewById(R.id.tv_makhach);
        ImageView imgHinhDD=row.findViewById(R.id.image_AnhDD);
        ImageView imgCmndTruoc=row.findViewById(R.id.imageCmndTruoc);
        ImageView imgCmndSau=row.findViewById(R.id.imageCmndSau);
        TextView txttenkhachthue =row.findViewById(R.id.tv_tenkhach);
        TextView txtgioitinh = row.findViewById(R.id.tv_gioitinh);
        TextView txtngaysinh = row.findViewById(R.id.tv_ngaysinh);
        TextView txtcmnd =row.findViewById(R.id.tv_cmnd);
        TextView txtsdt = row.findViewById(R.id.tv_sdt);
        TextView txtngaythue =row.findViewById(R.id.tv_ngaythue);

        Button btnXoaKhachThue =row.findViewById(R.id.btnXoaKhachThue);
        Button btnSuaKhachThue = row.findViewById(R.id.btnSuaKhachThue);
        Button btnGoiKhach = row.findViewById(R.id.btnGoiKhach);

        final KhachThue khachthue = list.get(position);

        Bitmap bmHinhDD= BitmapFactory.decodeByteArray(khachthue.AnhDD,0,khachthue.AnhDD.length);
        imgHinhDD.setImageBitmap(bmHinhDD);
        Bitmap bmCmndTruoc= BitmapFactory.decodeByteArray(khachthue.CmndTruoc,0,khachthue.CmndTruoc.length);
        imgCmndTruoc.setImageBitmap(bmCmndTruoc);
        Bitmap bmCmndSau= BitmapFactory.decodeByteArray(khachthue.CmndSau,0,khachthue.CmndSau.length);
        imgCmndSau.setImageBitmap(bmCmndSau);
        //txtMa.setText(khachthue.makhach);
        txttenkhachthue.setText(khachthue.tenkhach);
        txtngaysinh.setText(khachthue.ngaysinh);
        txtcmnd.setText(khachthue.cmnd);
        txtsdt.setText(khachthue.sdt);
        txtngaythue.setText(khachthue.ngaythue);
        String GT = khachthue.gioitinh;
        if(GT.equals("1"))
        {
            txtgioitinh.setText("Nam");
        }
        else
        {
            txtgioitinh.setText("Nữ");
        }

        btnSuaKhachThue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SuaKhachThueActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("MaKhach", khachthue.makhach);
                bundle.putInt("MaPhong", khachthue.maphong);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        btnXoaKhachThue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn có chắc chắn xóa phòng?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Xoa(khachthue.makhach);


                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog =builder.create();
                dialog.show();
            }
        });
        btnGoiKhach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sdt = khachthue.sdt;
                if(!TextUtils.isEmpty(sdt)) {
                    String dial = "tel:" + sdt;
                    context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Khách thuê chưa có SDT");

                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dialog =builder.create();
                    dialog.show();
                }
            }
        });
        return row;
    }
    private void Xoa(int idKhachThue)
    {
        SQLiteDatabase database = Database.initDatabase(context,DATABASE_NAME);
        Cursor cursor1 = database.rawQuery("SELECT * FROM KhachThue where MaKhach = ?",new String[]{idKhachThue+""});
        cursor1.moveToFirst();
        int ma = cursor1.getInt(7);
        int maphong = 0;

        database.delete("KhachThue","MaKhach = ?",new String[]{idKhachThue+""});
        Toast.makeText(context, "Xóa thành công", Toast.LENGTH_LONG).show();
        list.clear();

        if(Check(ma) == false)
        {
            update(ma);
        }

        Intent intent = new Intent(context, DanhSachPhongActivity.class);
        intent.putExtra("MaPhong",ma);
        context.startActivity(intent);


        Cursor cursor = database.rawQuery("SELECT * FROM KhachThue",null);
        while (cursor.moveToNext())
        {
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
           maphong = cursor.getInt(10);

            list.add(new KhachThue(makhach,anhDD,tenkhach,gioitinh,ngaysinh,cmnd,sdt,ngaythue,anhTruoc,anhSau,maphong));

        }
        notifyDataSetChanged();
        //context.finish();
    }
    public void update(int maphong)
    {
        SQLiteDatabase database= Database.initDatabase(context,DATABASE_NAME);
        Cursor cursor = database.rawQuery("select * from Phong where MaPhong = ?",new String[]{maphong+""});
        cursor.moveToFirst();
        String tenphong = cursor.getString(1);
        String lau = cursor.getString(2);
        String tiencoc = cursor.getString(3);
        String sodien = cursor.getString(4);
        String sonuoc = cursor.getString(5);

        ContentValues contentValues = new ContentValues();
        contentValues.put("TenPhong",tenphong);
        contentValues.put("Lau",lau);
        contentValues.put("TienCoc",tiencoc);
        contentValues.put("SoDien",sodien);
        contentValues.put("SoNuoc",sonuoc);
        contentValues.put("trangthai",0);
        database = Database.initDatabase(context,DATABASE_NAME);
        database.update("Phong",contentValues,"maphong=?",new String[]{maphong+""});
    }

    private boolean Check(int maphong)
    {
        boolean KT = false;
        int a = 0;
        SQLiteDatabase database= Database.initDatabase(context,DATABASE_NAME);
        Cursor cursor = database.rawQuery("select * from KhachThue",null);
        for(int i = 0;i<cursor.getCount();i++)
        {
            cursor.moveToPosition(i);
            if(cursor.getInt(10)==maphong)
            {
                a++;
            }
        }
        if(a>0)
            return KT=true;
        return KT = false;
    }
}
