package com.example.presensi.dto;

public class QrKeluarDto {
    public String kd_absen;
    public String kode_konfigurasi;
    public String kode_ruangan;
    public String kode_shift;
    public String kode_perawat;
    public String kd_jenis_absen;
    public String id_user;
    public String username;

    public String nira;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getKd_absen() {
        return kd_absen;
    }

    public void setKd_absen(String kd_absen) {
        this.kd_absen = kd_absen;
    }

    public String getKode_perawat() {
        return kode_perawat;
    }

    public void setKode_perawat(String kode_perawat) {
        this.kode_perawat = kode_perawat;
    }

    public String getKd_jenis_absen() {
        return kd_jenis_absen;
    }

    public void setKd_jenis_absen(String kd_jenis_absen) {
        this.kd_jenis_absen = kd_jenis_absen;
    }

    public String getKode_konfigurasi() {
        return kode_konfigurasi;
    }

    public void setKode_konfigurasi(String kode_konfigurasi) {
        this.kode_konfigurasi = kode_konfigurasi;
    }

    public String getKode_ruangan() {
        return kode_ruangan;
    }

    public void setKode_ruangan(String kode_ruangan) {
        this.kode_ruangan = kode_ruangan;
    }

    public String getKode_shift() {
        return kode_shift;
    }

    public void setKode_shift(String kode_shift) {
        this.kode_shift = kode_shift;
    }

}
