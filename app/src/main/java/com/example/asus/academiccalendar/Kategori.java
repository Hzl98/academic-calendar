package com.example.asus.academiccalendar;

import java.io.Serializable;

public class Kategori implements Serializable {
    public String nama, id;
    public int notifikasi, warna, status;

    public Kategori(){}

    public Kategori(String id, String nama, int notifikasi, int warna, int status) {
        this.id = id;
        this.nama = nama;
        this.notifikasi = notifikasi;
        this.warna = warna;
        this.status = status;
    }

    public Kategori(String id, String nama, int notifikasi, int warna) {
        this.id = id;
        this.nama = nama;
        this.notifikasi = notifikasi;
        this.warna = warna;
        this.status = 1;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNotifikasi() {
        return notifikasi;
    }

    public void setNotifikasi(int notifikasi) {
        this.notifikasi = notifikasi;
    }

    public int getWarna() {
        return warna;
    }

    public void setWarna(int warna) {
        this.warna = warna;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
