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
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

class AdapterChiTietHoaDon extends BaseAdapter {
    final String DATABASE_NAME = "TroNewNew.sqlite";
    Activity context;
    ArrayList<Phong> list;
    Locale localeVN = new Locale("vi", "VN");
    NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
    public AdapterChiTietHoaDon(Activity context, ArrayList<Phong> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.danhsachhoadon,null);

        TextView txttenphong =row.findViewById(R.id.tv_tenphonghoadon);


        final Phong cthd = list.get(position);

        int maphong = cthd.maphong;
        final SQLiteDatabase database = Database.initDatabase(context,DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM Phong where MaPhong = ?",new String[]{maphong+""});
        cursor.moveToFirst();
        String tp = cursor.getString(1);
        txttenphong.setText(tp);


        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Phong phong = list.get(position);
                int maphong = phong.maphong;

                Cursor cursor =  database.rawQuery("select * from Phong where MaPhong = ?",new String[]{maphong+""});
                cursor.moveToFirst();
                String TT = cursor.getString(6);
                if(TT.equals("1"))
                {
                    Intent intent = new Intent(context, LapHoaDonActivity.class);
                    intent.putExtra("MaPhong",maphong);
                    context.startActivity(intent);
                    //context.finish();
                }
                else{
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
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

            }
        });
        return row;
    }
}
