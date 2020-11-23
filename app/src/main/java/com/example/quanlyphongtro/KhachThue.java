package com.example.quanlyphongtro;

public class KhachThue {
    public int makhach;
    public byte[]AnhDD;
    public String tenkhach;
    public String gioitinh;
    public String ngaysinh;
    public String cmnd;
    public String sdt;
    public String ngaythue;
    public byte[]CmndTruoc;
    public byte[]CmndSau;
    public int maphong;



    public KhachThue(int makhach,byte[]anhDD, String tenkhach, String gioitinh, String ngaysinh, String cmnd, String sdt, String ngaythue,byte[]AnhTruoc,byte[]AnhSau,int maphong) {
        this.makhach = makhach;
        this.AnhDD=anhDD;
        this.tenkhach = tenkhach;
        this.gioitinh = gioitinh;
        this.ngaysinh = ngaysinh;
        this.cmnd = cmnd;
        this.sdt = sdt;
        this.ngaythue = ngaythue;
        this.CmndTruoc=AnhTruoc;
        this.CmndSau=AnhSau;
        this.maphong = maphong;
    }

    public String getCmnd() {
        return cmnd;
    }

    public void setCmnd(String cmnd) {
        this.cmnd = cmnd;
    }

    public byte[] getAnhDD() {
        return AnhDD;
    }

    public void setAnhDD(byte[] anhDD) {
        AnhDD = anhDD;
    }

    public byte[] getCmndSau() {
        return CmndSau;
    }

    public void setCmndSau(byte[] cmndSau) {
        CmndSau = cmndSau;
    }

    public byte[] getCmndTruoc() {
        return CmndTruoc;
    }

    public String getTenkhach() {
        return tenkhach;
    }

    public void setTenkhach(String tenkhach) {
        this.tenkhach = tenkhach;
    }

    public String getNgaythue() {
        return ngaythue;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public void setNgaythue(String ngaythue) {
        this.ngaythue = ngaythue;
    }

//    public void setCmndTruoc(byte[] cmndTruoc) {
//        CmndTruoc = cmndTruoc;
//    }

    public String getNgaysinh() {
        return ngaysinh;
    }

    public void setNgaysinh(String ngaysinh) {
        this.ngaysinh = ngaysinh;
    }

    public int getMaphong() {
        return maphong;
    }

    public void setMaphong(int maphong) {
        this.maphong = maphong;
    }

    public int getMakhach() {
        return makhach;
    }

    public void setMakhach(int makhach) {
        this.makhach = makhach;
    }

    public String getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(String gioitinh) {
        this.gioitinh = gioitinh;
    }
}


