package  com.example.quanlyphongtro;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.example.quanlyphongtro.App.CHANNEL_1_ID;

public class CapNhapGiaDienNuocActivity extends AppCompatActivity {
    EditText edtGiaDien, edtGiaNuoc;
    Button btnCapNhat;
    SQLiteDatabase database;
    final String DATABASE_NAME = "TroNewNew.sqlite";
    FloatingActionButton ftbTrangChu, ftbHoaDon, ftbPhong, ftbBangGia;
    Animation tren, trai, xeo, back_trai, back_tren, back_xeo;
    boolean trove = false;
    private NotificationManagerCompat notificationManagerCompat;
    private NotificationCompat.Builder notBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap_nhap_gia_dien_nuoc);

        AnhXa();
        setUI();
        ThaoTac();

        this.notBuilder = new NotificationCompat.Builder(this);

        // Thông báo sẽ tự động bị hủy khi người dùng click vào Panel

        this.notBuilder.setAutoCancel(true);
        notificationManagerCompat = NotificationManagerCompat.from(this);
    }

    public void sendOnChannel1(View v) {
        String title = "Thông báo";
        String title1 = "Đã cập nhập giá điện nước";
        String message = edtGiaDien.getText().toString();
        String message1 = edtGiaNuoc.getText().toString();

        Intent intent = new Intent(this, DanhSachPhongActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(title)
                .setContentText(title1)
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine("Gía điện đã được cập nhập: " + message)
                        .addLine("Gía nước đã được cập nhập: " + message1))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .setColor(Color.GRAY)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();
        notificationManagerCompat.notify(1, notification);


    }


    private void setUI()
    {
        int ma = 1;
        database= Database.initDatabase(CapNhapGiaDienNuocActivity.this,DATABASE_NAME);
        Cursor cursor = database.rawQuery("select * from BangGia where Ma = ?",new String[]{ma+""});
        cursor.moveToFirst();
        String giadien = cursor.getString(1);
        String gianuoc = cursor.getString(2);
        edtGiaDien.setText(giadien);
        edtGiaNuoc.setText(gianuoc);
    }
    private void SuaGia()
    {
        int ma =1;
        String giadien = edtGiaDien.getText().toString();
        String gianuoc = edtGiaNuoc.getText().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put("GiaDien",giadien);
        contentValues.put("GiaNuoc",gianuoc);

        SQLiteDatabase database = Database.initDatabase(CapNhapGiaDienNuocActivity.this,DATABASE_NAME);
        database.update("BangGia",contentValues,"Ma = ?",new String[]{ma+""});
        Intent intent = new Intent(CapNhapGiaDienNuocActivity.this, DanhSachPhongActivity.class);
        startActivity(intent);
        Toast.makeText(CapNhapGiaDienNuocActivity.this,"Cập nhật giá thành công",Toast.LENGTH_LONG).show();
        finish();
    }
    private void ThaoTac()
    {
        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SuaGia();
            }
        });

        ftbPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CapNhapGiaDienNuocActivity.this,DanhSachPhongActivity.class);
                startActivity(intent);
               // finish();
            }
        });
        ftbBangGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CapNhapGiaDienNuocActivity.this, CapNhapGiaDienNuocActivity.class);
                startActivity(intent);
                //finish();
            }
        });
        ftbHoaDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CapNhapGiaDienNuocActivity.this, DanhSachHoaDonActivity.class);
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
        edtGiaDien =findViewById(R.id.edtGiaDien);
        edtGiaNuoc =findViewById(R.id.edtGiaNuoc);
        btnCapNhat =findViewById(R.id.btnCapNhatDienNuoc);

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
