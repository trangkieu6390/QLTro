package com.example.quanlyphongtro;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AdapterPhong extends BaseAdapter
{
    final String DATABASE_NAME = "TroNewNew.sqlite";
    Activity context;
    ArrayList<Phong> list;
    Locale localeVN = new Locale("vi", "VN");
    NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
    public AdapterPhong(Activity context, ArrayList<Phong> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent)
    {
        final LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.danhsachphong,null);

        TextView txtmaphong = (TextView)row.findViewById(R.id.tv_maphong);
        TextView txttenphong = (TextView)row.findViewById(R.id.tv_tenphong);
        TextView txtlau = (TextView)row.findViewById(R.id.tv_lau);
        TextView txttiencoc = (TextView)row.findViewById(R.id.tv_tiencoc);
        TextView txttrangthai = (TextView)row.findViewById(R.id.tv_trangthai);
        Button btnXoaPhong = (Button) row.findViewById(R.id.btnXoaPhong);
        Button btnSuaPhong = (Button) row.findViewById(R.id.btnSuaPhong);

        final Phong phong = list.get(position);

        txtmaphong.setText(phong.maphong+"");
        txttenphong.setText(phong.tenphong);
        txtlau.setText(phong.lau);
        long Tong = Long.valueOf(phong.tiencoc);
        String str1 = currencyVN.format(Tong);

        txttiencoc.setText(str1);
        String TT = phong.trangthai;


        if(TT.equals("1"))
        {
            txttrangthai.setText("Đã thuê");
            txttenphong.setBackgroundResource(R.color.dathue);
        }
        else
        {
            txttrangthai.setText("Còn trống");
            txttenphong.setBackgroundResource(R.color.phongtrong);
        }

        btnSuaPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SuaPhongActivity.class);
                intent.putExtra("aa",phong.maphong);
                context.startActivity(intent);
                //context.finish();
            }
        });

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Phong phong = list.get(position);
                int maphong = phong.maphong;

                Intent intent = new Intent(context,XemThanhVienActivity.class);
                intent.putExtra("MaPhong",maphong);
                context.startActivity(intent);
                //context.finish();
            }
        });
        btnXoaPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn có chắc chắn xóa phòng?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Xoa(phong.maphong);

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

        /* */

        return row;

    }
    private void Xoa(int idPhong)
    {
        SQLiteDatabase database = Database.initDatabase(context,DATABASE_NAME);
        if(Check(idPhong)==true)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Thông báo");
            builder.setMessage("Bạn không thể xóa phòng vì đã có người thuê?");
            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog dialog =builder.create();
            dialog.show();
        }
        else{
            database.delete("Phong","MaPhong = ?",new String[]{idPhong+""});

            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_LONG).show();
            list.clear();

            Cursor cursor = database.rawQuery("SELECT * FROM Phong",null);
            while (cursor.moveToNext())
            {
                int maphong = cursor.getInt(0);
                String tenphong = cursor.getString(1);
                String lau = cursor.getString(2);
                String tiencoc = cursor.getString(3);
                int sodien = cursor.getInt(4);
                int sonuoc = cursor.getInt(5);
                String trangthai = cursor.getString(6);

                list.add(new Phong(maphong,tenphong,lau,tiencoc,sodien,sonuoc,trangthai));
            }
            notifyDataSetChanged();
        }

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
