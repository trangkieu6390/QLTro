package com.example.quanlyphongtro;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AdapterHoaDonCuaTungPhong extends BaseAdapter {
    SQLiteDatabase database;
    final String DATABASE_NAME="TroNewNew.sqlite";
    Activity context;
    ArrayList<ChiTietHoaDon>list;
    Locale localeVN=new Locale("vi","VN");
    NumberFormat currencyVN=NumberFormat.getCurrencyInstance(localeVN);
    public AdapterHoaDonCuaTungPhong(Activity context,ArrayList<ChiTietHoaDon>list){
        this.context=context;
        this.list=list;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row=layoutInflater.inflate(R.layout.dshoadoncuaphong,null);

        TextView txtThang = (TextView) row.findViewById(R.id.txtThangHDP);
        TextView txttongtien = (TextView) row.findViewById(R.id.txtTongTienHDP);
        TextView txttrangthai = (TextView) row.findViewById(R.id.txtTrangThaiHDP);
        LinearLayout moto = (LinearLayout) row.findViewById(R.id.motkhung);
        Button btnThanhToan = (Button) row.findViewById(R.id.btnThanhToan);
        final ChiTietHoaDon cthd = list.get(position);

        txtThang.setText(cthd.ngaylaphoadon);
        long Tong = Long.valueOf(cthd.tongtien);
        String str1 = currencyVN.format(Tong);
        txttongtien.setText(str1);
        String TT = cthd.trangthai;
        if(TT.equals("1"))
        {
            txttrangthai.setText("Đã thu");
            moto.setBackgroundResource(R.color.dathue);

        }
        else
        {
            txttrangthai.setText("Chưa thu");
            moto.setBackgroundResource(R.color.phongtrong);
        }

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int macthd = cthd.macthd;

                Intent intent = new Intent(context, ChiTietHoaDonActivity.class);
                intent.putExtra("MaCTHD",macthd);
                context.startActivity(intent);
                //context.finish();
            }
        });
        btnThanhToan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Thông báo");
                builder.setMessage("Thực hiện việc thanh toán");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final int macthd = cthd.macthd;
                        SQLiteDatabase database= Database.initDatabase(context,DATABASE_NAME);
                        Cursor cursor = database.rawQuery("select * from ChiTietHoaDon where MaCTHD = ?",new String[]{macthd+""});
                        cursor.moveToFirst();
                        String tienphong = cursor.getString(1);
                        String tiendien = cursor.getString(2);
                        String tiennuoc = cursor.getString(3);
                        String tieninternet = cursor.getString(4);
                        String tiendvkhac = cursor.getString(5);
                        String lydothu = cursor.getString(12);
                        String ngaylaphoadon = cursor.getString(6);
                        String trangthai = cursor.getString(7);
                        String maphong = cursor.getString(8);
                        String tongtien = cursor.getString(9);
                        String sodien = cursor.getString(10);
                        String sonuoc = cursor.getString(11);
                        if(trangthai.equals("1"))
                        {
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                            builder2.setTitle("Thông báo");
                            builder2.setMessage("Hóa đơn đã thanh toán,không thể thanh toán thêm");
                            builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            AlertDialog dialog2 =builder2.create();
                            dialog2.show();

                        }
                        else
                        {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("TienPhong",tienphong);
                            contentValues.put("TienDien",tiendien);
                            contentValues.put("TienNuoc",tiennuoc);
                            contentValues.put("TienInternet",tieninternet);
                            contentValues.put("TienDVKhac",tiendvkhac);
                            contentValues.put("NgayLapHoaDon",ngaylaphoadon);
                            contentValues.put("TrangThai","1");
                            contentValues.put("MaPhong",maphong);
                            contentValues.put("TongTien",tongtien);
                            contentValues.put("SoDien",sodien);
                            contentValues.put("SoNuoc",sonuoc);
                            contentValues.put("LyDoThu",lydothu);

                            database = Database.initDatabase(context,DATABASE_NAME);
                            database.update("ChiTietHoaDon",contentValues,"MaCTHD=?",new String[]{macthd+""});

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                            builder1.setTitle("Thông báo");
                            builder1.setMessage("Thanh toán thành công");
                            builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int maphong = cthd.maphong;
                                    Intent intent = new Intent(context, HoaDonCuaTungPhongActivity.class);
                                    intent.putExtra("MaPhong",maphong);
                                    context.startActivity(intent);
                                    //context.finish();
                                }
                            });
                            AlertDialog dialog1 = builder1.create();
                            dialog1.show();

                        }
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
        return row;
    }
}
