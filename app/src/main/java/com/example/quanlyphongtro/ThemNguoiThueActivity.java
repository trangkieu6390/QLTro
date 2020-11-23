package com.example.quanlyphongtro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ThemNguoiThueActivity extends AppCompatActivity {

    final String DATABASE_NAME = "TroNewNew.sqlite";

    SQLiteDatabase database;

    EditText edtTen, edtCMND, edtSDT, edtPhongThue, edtNgaySinh, edtNgayThue;
    Button btnThemKhachThue, btnChonNgaySinh, btnChonNgayThue, btnChonAnhDD, btnChupAnhDD, btnChonTruoc, btnChonSau, btnChupTruoc, btnChupSau;

    RadioButton rdoNam, rdoNu;
    RadioGroup rdoGioiTinh;


    Date dateFinish;
    Date hourFinish;

    ArrayList<KhachThue> list;
    Calendar cal;
    int maphong = -1;

    private static int RESULT_LOAD_IMAGE = 1;

    final int REQUEST_TAKE_PHOTE = 100;
    final int REQUEST_CHOOSE_PHOTO = 111;
    //
    final int REQUEST_TAKE_PHONE_SAU = 3;
    final int REQUEST_CHOOSE_PHOTO_SAU = 4;

    final int REQUEST_TAKE_PHONE_TRUOC = 1;
    final int REQUEST_CHOOSE_PHOTO_TRUOC = 2;

    ImageView imgHinhDD, imgAnhTruoc, imgAnhSau;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_nguoi_thue);
        AnhXa();
        getDefaultInfor();
        addEnvent();

    }
    private void addEnvent() {

        btnThemKhachThue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddKhach();
            }
        });
        btnChonNgaySinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialogNgaySinh();
            }
        });
        btnChonNgayThue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialogNgayThue();
            }
        });

        btnChonAnhDD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhotoAnhDD();
            }
        });
        btnChupAnhDD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePictureAnhDD();
            }
        });
        btnChonTruoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               choosePhotoAnhTruoc();
            }
        });
        btnChonSau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhotoAnhSau();
            }
        });
        btnChupTruoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePictureAnhTruoc();
            }
        });
        btnChupSau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePictureAnhSau();
            }
        });
    }

    private void AnhXa() {
        imgHinhDD =  findViewById(R.id.imgThemAnhDD);
        imgAnhTruoc =  findViewById(R.id.imgThemCmndTruoc);
        imgAnhSau = findViewById(R.id.imgThemCmndSau);
        edtTen = findViewById(R.id.edtTenNguoiThue);
        edtSDT =findViewById(R.id.edtSDTNguoiThue);
        edtCMND = findViewById(R.id.edtCMND);
        edtPhongThue =  findViewById(R.id.edtPhongThue);
        edtNgaySinh =  findViewById(R.id.edtNgaySinh);
        edtNgayThue =  findViewById(R.id.edtNgayThue);
        btnThemKhachThue =  findViewById(R.id.btnThemNguoiThue);
        btnChonNgaySinh =  findViewById(R.id.btnChonNgaySinh);
        btnChonNgayThue =  findViewById(R.id.btnChonNgayThue);
        btnChupAnhDD = findViewById(R.id.btnChupThemAnhDD);
        btnChonAnhDD = findViewById(R.id.btnChonThemAnhDD);
        btnChonTruoc = findViewById(R.id.btnChonThemTruoc);
        btnChupTruoc = findViewById(R.id.btnChupThemTruoc);
        btnChupSau = findViewById(R.id.btnChupThemSau);
        btnChonSau = findViewById(R.id.btnChonThemSau);

        rdoGioiTinh =findViewById(R.id.rdoGioiTinh);


        Intent intent = getIntent();
        maphong = intent.getIntExtra("MaPhong1", -1);


        database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("select * from Phong where MaPhong = ?", new String[]{maphong + ""});
        cursor.moveToFirst();
        String tenphong = cursor.getString(1);
        edtPhongThue.setText(tenphong);


    }

    private void AddKhach() {
        byte[] anhDD = getByteArrayFromImage(imgHinhDD);
        byte[] anhTruoc = getByteArrayFromImage(imgAnhTruoc);
        byte[] anhSau = getByteArrayFromImage(imgAnhSau);
        String ten = edtTen.getText().toString();
        String ngaysinh = edtNgaySinh.getText().toString();
        String sdt = edtSDT.getText().toString();
        String cmnd = edtCMND.getText().toString();
        String ngaythue = edtNgayThue.getText().toString();

        int gioitinh = 0;
        int i = rdoGioiTinh.getCheckedRadioButtonId();
        switch (i) {
            case R.id.rdoNam:
                gioitinh = 1;
                break;
            case R.id.rdoNu:
                gioitinh = 0;
                break;
        }

        ContentValues contentValues = new ContentValues();

        contentValues.put("TenKhach", ten);
        if (gioitinh == 1) {
            contentValues.put("GioiTinh", 1);
        } else {
            contentValues.put("GioiTinh", 0);
        }
        contentValues.put("NgaySinh", ngaysinh);
        contentValues.put("CMND", cmnd);
        contentValues.put("SDT", sdt);
        contentValues.put("NgayThue", ngaythue);

        contentValues.put("AnhDD", anhDD);
        contentValues.put("CmndTruoc", anhTruoc);
        contentValues.put("CmndSau", anhSau);
        Intent intent = getIntent();
        maphong = intent.getIntExtra("MaPhong1", -1);

        contentValues.put("MaPhong", String.valueOf(maphong));

        database = Database.initDatabase(this, DATABASE_NAME);
        database.insert("KhachThue", null, contentValues);
        update(maphong);
        Intent intent1 = new Intent(this, XemThanhVienActivity.class);
        intent1.putExtra("MaPhong", maphong);
        startActivity(intent1);
        Toast.makeText(ThemNguoiThueActivity.this, "Thêm người thuê thành công", Toast.LENGTH_LONG).show();

        onPause();
    }

    protected void onPause() {
        super.onPause();
          //finish();
    }

    /*public String getDateFormat(Date d)
    {
        SimpleDateFormat dft=new
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dft.format(d);
    }*/
    public void getDefaultInfor() {
        //lấy ngày hiện tại của hệ thống
        cal = Calendar.getInstance();
        SimpleDateFormat dft = null;
        //Định dạng ngày / tháng /năm
        dft = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String strDate = dft.format(cal.getTime());
        //hiển thị lên giao diện
        edtNgayThue.setText(strDate);
        edtNgaySinh.setText(strDate);

        dateFinish = cal.getTime();
        hourFinish = cal.getTime();
    }

    /**
     * Hàm hiển thị DatePicker dialog
     */
    public void showDatePickerDialogNgaySinh() {
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear,
                                  int dayOfMonth) {
                //Mỗi lần thay đổi ngày tháng năm thì cập nhật lại TextView Date
                edtNgaySinh.setText(
                        (dayOfMonth) + "/" + (monthOfYear + 1) + "/" + year);
                /*txtNgayThue.setText(
                        (dayOfMonth) +"/"+(monthOfYear+1)+"/"+year);*/
                //Lưu vết lại biến ngày hoàn thành
                cal.set(year, monthOfYear, dayOfMonth);
                dateFinish = cal.getTime();
            }
        };
        //các lệnh dưới này xử lý ngày giờ trong DatePickerDialog
        //sẽ giống với trên TextView khi mở nó lên
        String s = edtNgaySinh.getText() + "";
        String strArrtmp[] = s.split("/");
        int ngay = Integer.parseInt(strArrtmp[0]);
        int thang = Integer.parseInt(strArrtmp[1]) - 1;
        int nam = Integer.parseInt(strArrtmp[2]);
        DatePickerDialog pic = new DatePickerDialog(
                ThemNguoiThueActivity.this,
                callback, nam, thang, ngay);
        pic.setTitle("Chọn ngay sinh");
        pic.show();

    }

    public void showDatePickerDialogNgayThue() {
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear,
                                  int dayOfMonth) {
                //Mỗi lần thay đổi ngày tháng năm thì cập nhật lại TextView Date

                edtNgayThue.setText(
                        (dayOfMonth) + "/" + (monthOfYear + 1) + "/" + year);
                //Lưu vết lại biến ngày hoàn thành
                cal.set(year, monthOfYear, dayOfMonth);
                dateFinish = cal.getTime();
            }
        };
        //các lệnh dưới này xử lý ngày giờ trong DatePickerDialog
        //sẽ giống với trên TextView khi mở nó lên
        String s = edtNgayThue.getText() + "";
        String strArrtmp[] = s.split("/");
        int ngay = Integer.parseInt(strArrtmp[0]);
        int thang = Integer.parseInt(strArrtmp[1]) - 1;
        int nam = Integer.parseInt(strArrtmp[2]);
        DatePickerDialog pic = new DatePickerDialog(
                ThemNguoiThueActivity.this,
                callback, nam, thang, ngay);
        pic.setTitle("Chọn ngay thue");
        pic.show();

    }

    public void update(int maphong) {
        database = Database.initDatabase(ThemNguoiThueActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("select * from Phong where MaPhong = ?", new String[]{maphong + ""});
        cursor.moveToFirst();
        String tenphong = cursor.getString(1);
        String lau = cursor.getString(2);
        String tiencoc = cursor.getString(3);
        String sodien = cursor.getString(4);
        String sonuoc = cursor.getString(5);

        ContentValues contentValues = new ContentValues();
        contentValues.put("TenPhong", tenphong);
        contentValues.put("Lau", lau);
        contentValues.put("TienCoc", tiencoc);
        contentValues.put("SoDien", sodien);
        contentValues.put("SoNuoc", sonuoc);
        contentValues.put("trangthai", 1);
        database = Database.initDatabase(ThemNguoiThueActivity.this, DATABASE_NAME);
        database.update("Phong", contentValues, "maphong=?", new String[]{maphong + ""});
    }

    private byte[] getByteArrayFromImage(ImageView imgv) {
        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;

    }

        private void takePictureAnhTruoc(){
        Intent intent=new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
        startActivityForResult(intent,REQUEST_TAKE_PHONE_TRUOC);
    }
    private void choosePhotoAnhTruoc() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO_TRUOC);
    }
    private void takePictureAnhDD(){
        Intent intent=new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
        startActivityForResult(intent,REQUEST_TAKE_PHOTE);
    }
    private void choosePhotoAnhDD() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
    }

    private void takePictureAnhSau(){
        Intent intent=new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
        startActivityForResult(intent,REQUEST_TAKE_PHONE_SAU);
    }
    private void choosePhotoAnhSau() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO_SAU);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_CHOOSE_PHOTO) {
                try {
                    Uri imageUri = data.getData();
                    InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imgHinhDD.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTE) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                imgHinhDD.setImageBitmap(image);
            }else if (requestCode == REQUEST_CHOOSE_PHOTO_TRUOC) {
                try {
                    Uri imageUri = data.getData();
                    InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imgAnhTruoc.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == REQUEST_TAKE_PHONE_TRUOC) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                imgAnhTruoc.setImageBitmap(image);
            }else if (requestCode == REQUEST_CHOOSE_PHOTO_SAU) {
                try {
                    Uri imageUri = data.getData();
                    InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imgAnhSau.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == REQUEST_TAKE_PHONE_SAU) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                imgAnhSau.setImageBitmap(image);
            }
            else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }

        } else{
            Toast.makeText(this, "data null", Toast.LENGTH_SHORT).show();
        }

    }
}