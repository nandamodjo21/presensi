package com.example.presensi.controllers;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.presensi.dto.AbsenDto;

import net.minidev.json.JSONObject;

@RestController
@Transactional
@RequestMapping("/qrcods")
public class Cekqr {

    @Autowired
    EntityManager em;

    @PostMapping
    public Object qrcode(@RequestBody AbsenDto dto) {
        List Nnd = em
                .createNativeQuery(
                        "SELECT a.kd_absen,a.kd_jenis_absen FROM t_absen a, tb_konfigurasi k,tb_shift s WHERE a.kode_konfigurasi = k.kode_konfigurasi AND date(a.waktu) = k.tanggal AND k.kode_shift = s.shift GROUP BY a.kd_absen,a.kode_konfigurasi ORDER BY kd_jenis_absen, a.waktu DESC LIMIT 1")
                .getResultList();

        JSONObject res = new JSONObject();

        if (Nnd.isEmpty()) {

            res.put("code", 0);
            res.put("message", "qr code tidak sesuai");
            return res;

        } else {
            Object[] o = (Object[]) Nnd.get(0);
            JSONObject js = new JSONObject();
            js.put("kd_absen", o[0]);
            js.put("kd_jenis_absen", o[1]);
            res.put("code", 1);
            // res.put("message", "");
            res.put("data", js);
            return res;
        }
    }
}
