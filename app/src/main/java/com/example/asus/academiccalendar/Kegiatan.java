package com.example.asus.academiccalendar;

import java.io.Serializable;

public class Kegiatan implements Serializable {
    String id, idkat, nama, tglawal, tglakhir, kategori;
    int warna, status, notifikasi, visible;

    public Kegiatan() {

    }

    public Kegiatan(String id, String idkat, String nama, String tglawal, String tglakhir, int status, String kategori, int warna, int notifikasi) {
        this.id = id;
        this.idkat = idkat;
        this.nama = nama;
        this.tglawal = tglawal;
        this.tglakhir = tglakhir;
        this.kategori = kategori;
        this.warna = warna;
        this.status = status;
        this.notifikasi = notifikasi;
    }

    public Kegiatan(String id, String idkat, String nama, String tglakhir, String tglawal, String kategori, int warna, int notifikasi) {
        this.id = id;
        this.idkat = idkat;
        this.nama = nama;
        this.status = 1;
        this.tglakhir = tglakhir;
        this.tglawal = tglawal;
        this.kategori = kategori;
        this.warna = warna;
        this.notifikasi = notifikasi;
    }

    public int getNotifikasi() {
        return notifikasi;
    }

    public void setNotifikasi(int notifikasi) {
        this.notifikasi = notifikasi;
    }

    public String getIdkat() {
        return idkat;
    }

    public void setIdkat(String idkat) {
        this.idkat = idkat;
    }

    public int getWarna() {
        return warna;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getNama() { return nama; }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTglawal() {
        return tglawal;
    }

    public void setTglawal(String tglawal) {
        this.tglawal = tglawal;
    }

    public String getTglakhir() {
        return tglakhir;
    }

    public void setTglakhir(String tglakhir) {
        this.tglakhir = tglakhir;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) { this.kategori = kategori; }

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
