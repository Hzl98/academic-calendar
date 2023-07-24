package com.example.asus.academiccalendar;

public class ClassBroadcast {
    public String id, pesan, tanggal;

    public ClassBroadcast(String id, String pesan, String tanggal) {
        this.id = id;
        this.pesan = pesan;
        this.tanggal = tanggal;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
