package com.example.quanlyphongtro;

public class Phong
{
    public int maphong;
    public String tenphong;
    public String lau;
    public String tiencoc;
    public int sodien;
    public int sonuoc;
    public String trangthai;

    public Phong() {
    }

    public Phong(int maphong, String tenphong, String lau, String tiencoc, int sodien, int sonuoc, String trangthai) {
        this.maphong = maphong;
        this.tenphong = tenphong;
        this.lau = lau;
        this.tiencoc = tiencoc;
        this.sodien = sodien;
        this.sonuoc = sonuoc;
        this.trangthai = trangthai;
    }

    public int getMaphong() {
        return maphong;
    }

    public void setMaphong(int maphong) {
        this.maphong = maphong;
    }

    public String getTenphong() {
        return tenphong;
    }

    public void setTenphong(String tenphong) {
        this.tenphong = tenphong;
    }

    public String getLau() {
        return lau;
    }

    public void setLau(String lau) {
        this.lau = lau;
    }

    public String getTiencoc() {
        return tiencoc;
    }

    public void setTiencoc(String tiencoc) {
        this.tiencoc = tiencoc;
    }

    public int getSodien() {
        return sodien;
    }

    public void setSodien(int sodien) {
        this.sodien = sodien;
    }

    public int getSonuoc() {
        return sonuoc;
    }

    public void setSonuoc(int sonuoc) {
        this.sonuoc = sonuoc;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }
}


