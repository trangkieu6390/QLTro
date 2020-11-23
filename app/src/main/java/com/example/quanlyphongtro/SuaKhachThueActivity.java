package com.example.quanlyphongtro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SuaKhachThueActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    final String DATABASE_NAME = "TroNewNew.sqlite";
    int makhach = -1;
    final int REQUEST_TAKE_PHOTE = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;

    final int REQUEST_TAKE_PHONE_SAU = 3;
    final int REQUEST_CHOOSE_PHOTO_SAU = 4;

    final int REQUEST_TAKE_PHONE_TRUOC = 1;
    final int REQUEST_CHOOSE_PHOTO_TRUOC = 2;

    SQLiteDatabase database;
    EditText edtTenKhach, edtNgaySinh, edtCMND, edtSDT, edtNgayThue;
    Spinner spnGioiTinh, spnPhongThue;
    Button btnSua, btnChonNgaySinh, btnChonNgayThue, btnChonAnhDD, btnChupAnhDD, btnChonTruoc, btnChonSau, btnChupTruoc, btnChupSau;
    RadioGroup rdoGioiTinh;
    RadioButton rdoNam, rdoNu;
    ImageView imgHinhDD, imgAnhTruoc, imgAnhSau;
    Date dateFinish;
    Date hourFinish;

    Calendar cal;
    int maphong = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_khach_thue);
        AnhXa();
        addEnvents();
        initUI();
        getDefaultInfor();
    }
    private void addEnvents() {
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sua();
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
//                takePictureDD();
                takePictureAnhDD();
            }
        });

        btnChonTruoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhotoAnhTruoc();
            }
        });
        btnChupTruoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePictureAnhTruoc();
            }
        });

        btnChonSau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhotoAnhSau();
            }
        });
        btnChupSau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePictureAnhSau();
            }
        });
    }

    private void takePictureDD() {
        initPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(intent);
            } else {
                Toast.makeText(this, "Permision Write File is Denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void initPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                //Permisson don't granted
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.CAMERA)) {
                    Toast.makeText(this, "Permission isn't granted ", Toast.LENGTH_SHORT).show();
                }
                // Permisson don't granted and dont show dialog again.
                else {
                    Toast.makeText(this, "Permisson don't granted and dont show dialog again ", Toast.LENGTH_SHORT).show();
                }
                //Register permission
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);

            }else {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, MY_PERMISSIONS_REQUEST_CAMERA);
                takePictureAnhDD();
//                takePictureAnhTruoc();
            }
        }
    }

    private void AnhXa() {
        edtTenKhach = findViewById(R.id.edtTenNguoiThueSua);
        edtNgaySinh =findViewById(R.id.edtNgaySinhSua);
        edtCMND =findViewById(R.id.edtCMNDSua);
        edtSDT =findViewById(R.id.edtSDTNguoiThueSua);
        edtNgayThue =findViewById(R.id.edtNgayThueSua);

        btnChonNgaySinh = findViewById(R.id.btnChonNgaySinhSua);
        btnChonNgayThue = findViewById(R.id.btnChonNgayThueSua);
        btnSua =findViewById(R.id.btnSuaNguoiThue);
        btnChupAnhDD = findViewById(R.id.btnChupAnhDD);
        btnChonAnhDD =findViewById(R.id.btnChonAnhDD);
        btnChupTruoc = findViewById(R.id.btnChupTruoc);
        btnChonTruoc = findViewById(R.id.btnChonTruoc);
        btnChupSau = findViewById(R.id.btnChupSau);
        btnChonSau =  findViewById(R.id.btnChonSau);

        rdoGioiTinh = findViewById(R.id.rdoGioiTinhSua);
        rdoNam =  findViewById(R.id.rdoNamSua);
        rdoNu = findViewById(R.id.rdoNuSua);

        spnPhongThue = findViewById(R.id.spnPhongThueSua);
        ArrayList<String> arrPhong = new ArrayList<String>();
        database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("select * from Phong ", null);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            String tenphong = cursor.getString(1);
            arrPhong.add(tenphong);
        }
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrPhong);
        spnPhongThue.setAdapter(arrayAdapter1);

        imgHinhDD =  findViewById(R.id.imgAnhDD);
        imgAnhTruoc = findViewById(R.id.imgCmndTruoc);
        imgAnhSau = findViewById(R.id.imgCmndSau);
    }

    private void initUI() {
        Intent intent=getIntent();
        makhach=intent.getIntExtra("MaKhach",-1);
//        Bundle bundle = getIntent().getExtras();
//        makhach = bundle.getInt("MaKhach");
        SQLiteDatabase database = Database.initDatabase(SuaKhachThueActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("select * from KhachThue where MaKhach = ?", new String[]{makhach + ""});
        cursor.moveToFirst();

        String tenkhach = cursor.getString(2);
        String gioitinh = cursor.getString(3);
        String ngaysinh = cursor.getString(4);
        String cmnd = cursor.getString(5);
        String sdt = cursor.getString(6);
        String ngaythue = cursor.getString(7);
        byte[] AnhDD = cursor.getBlob(1);
        byte[] anhTruoc = cursor.getBlob(8);
        byte[] anhSau = cursor.getBlob(9);
        int maphong = cursor.getInt(10);

        edtTenKhach.setText(tenkhach);
        edtNgaySinh.setText(ngaysinh);
        edtCMND.setText(cmnd);
        edtSDT.setText(sdt);
        edtNgayThue.setText(ngaythue);

        Bitmap bitmap = BitmapFactory.decodeByteArray(AnhDD, 0, AnhDD.length);
        imgHinhDD.setImageBitmap(bitmap);
        Bitmap bitmap1 = BitmapFactory.decodeByteArray(anhTruoc, 0, anhTruoc.length);
        imgAnhTruoc.setImageBitmap(bitmap1);
        Bitmap bitmap2 = BitmapFactory.decodeByteArray(anhSau, 0, anhSau.length);
        imgAnhSau.setImageBitmap(bitmap2);

    }

    private void Sua() {
        database = Database.initDatabase(this, DATABASE_NAME);

        byte[] anhDD = getByteArrayFromImage(imgHinhDD);
        String tenkhach = edtTenKhach.getText().toString();
        String ngaysinh = edtNgaySinh.getText().toString();
        String cmnd = edtCMND.getText().toString();
        String sdt = edtSDT.getText().toString();
        String ngaythue = edtNgayThue.getText().toString();
        String tenphong = spnPhongThue.getSelectedItem().toString();
        byte[] anhTruoc = getByteArrayFromImage(imgAnhTruoc);
        byte[] anhSau = getByteArrayFromImage(imgAnhSau);
        int gioitinh = 0;
        int i = rdoGioiTinh.getCheckedRadioButtonId();
        switch (i) {
            case R.id.rdoNamSua:
                gioitinh = 1;
                break;
            case R.id.rdoNuSua:
                gioitinh = 0;
                break;
        }
        Cursor cursor1 = database.rawQuery("select * from Phong where TenPhong = ?", new String[]{tenphong});
        cursor1.moveToFirst();
        int maphong1 = cursor1.getInt(0);

        ContentValues contentValues = new ContentValues();
        contentValues.put("AnhDD", anhDD);
        contentValues.put("TenKhach", tenkhach);
        contentValues.put("CMND", cmnd);
        contentValues.put("SDT", sdt);
        contentValues.put("NgaySinh", ngaysinh);
        contentValues.put("NgayThue", ngaythue);
        contentValues.put("CmndTruoc", anhTruoc);
        contentValues.put("CmndSau", anhSau);
        if (gioitinh == 1) {
            contentValues.put("GioiTinh", 1);
        } else {
            contentValues.put("GioiTinh", 0);
        }
        contentValues.put("MaPhong", String.valueOf(maphong1));
        Bundle bundle = getIntent().getExtras();
        makhach = bundle.getInt("MaKhach");
        int maphongcu = bundle.getInt("MaPhong");
        SQLiteDatabase database = Database.initDatabase(SuaKhachThueActivity.this, DATABASE_NAME);
        database.update("KhachThue", contentValues, "MaKhach = ?", new String[]{makhach + ""});
        if (Check(maphong1) == true) {
            update(maphong1);
        }
        if (Check(maphongcu) == false) {
            update1(maphongcu);
        }
        Intent intent1 = new Intent(SuaKhachThueActivity.this, XemThanhVienActivity.class);
        intent1.putExtra("MaPhong", maphong1);
        startActivity(intent1);
        Toast.makeText(SuaKhachThueActivity.this, "Sửa khách thành công", Toast.LENGTH_LONG).show();
        onPause();
    }
    private byte[] getByteArrayFromImage(ImageView imgv) {
        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
    public void getDefaultInfor() {
        //lấy ngày hiện tại của hệ thống
        cal = Calendar.getInstance();
        SimpleDateFormat dft = null;
        //Định dạng ngày / tháng /năm
        dft = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String strDate = dft.format(cal.getTime());
        //hiển thị lên giao diện
        dateFinish = cal.getTime();
        hourFinish = cal.getTime();
    }
    public void showDatePickerDialogNgaySinh() {
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                edtNgaySinh.setText((dayOfMonth) + "/" + (monthOfYear + 1) + "/" + year);
                cal.set(year, monthOfYear, dayOfMonth);
                dateFinish = cal.getTime();
            }
        };
        String s = edtNgaySinh.getText() + "";
        String strArrtmp[] = s.split("/");
        int ngay = Integer.parseInt(strArrtmp[0]);
        int thang = Integer.parseInt(strArrtmp[1]) - 1;
        int nam = Integer.parseInt(strArrtmp[2]);
        DatePickerDialog pic = new DatePickerDialog(
                SuaKhachThueActivity.this,
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

                edtNgayThue.setText((dayOfMonth) + "/" + (monthOfYear + 1) + "/" + year);
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
                SuaKhachThueActivity.this,
                callback, nam, thang, ngay);
        pic.setTitle("Chọn ngay thue");
        pic.show();
    }
    protected void onPause() {
       super.onPause();
    }

    private boolean Check(int maphong) {
        boolean KT = false;
        int a = 0;
        SQLiteDatabase database = Database.initDatabase(SuaKhachThueActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("select * from KhachThue", null);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            if (cursor.getInt(10) == maphong) {
                a++;
            }
        }
        if (a > 0)
            return KT = true;
        return KT = false;
    }

    public void update(int maphong) {
        SQLiteDatabase database = Database.initDatabase(SuaKhachThueActivity.this, DATABASE_NAME);
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
        contentValues.put("trangthai", "1");
        database = Database.initDatabase(SuaKhachThueActivity.this, DATABASE_NAME);
        database.update("Phong", contentValues, "maphong=?", new String[]{maphong + ""});
    }
    public void update1(int maphong) {
        SQLiteDatabase database = Database.initDatabase(SuaKhachThueActivity.this, DATABASE_NAME);
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
        contentValues.put("trangthai", "0");
        database = Database.initDatabase(SuaKhachThueActivity.this, DATABASE_NAME);
        database.update("Phong", contentValues, "maphong=?", new String[]{maphong + ""});
    }

    private void takePictureAnhDD() {
        Intent intent = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(intent, MY_PERMISSIONS_REQUEST_CAMERA);
    }

    private void choosePhotoAnhDD() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
    }

    private void takePictureAnhTruoc() {
        Intent intent = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(intent, REQUEST_TAKE_PHONE_TRUOC);
    }

    private void choosePhotoAnhTruoc() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO_TRUOC);
    }

    private void takePictureAnhSau() {
        Intent intent = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(intent, REQUEST_TAKE_PHONE_SAU);
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
            } else if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA) {
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
