package com.example.presensi.controllers;

import java.util.List;

import javax.persistence.EntityManager;
// import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.validation.ObjectError;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// import com.example.presensi.dto.QrKeluarDto;
// import com.example.presensi.dto.StatusDto;

import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/status")
public class Status {

    @Autowired
    EntityManager em;

    @GetMapping("/{kode_perawat}")
    public Object insertout(@PathVariable("kode_perawat") String perawat) {

        // query untuk qr code
        List cekAbsen = em
                .createNativeQuery(
                        "SELECT CASE WHEN k.kd_jenis_absen = 1 THEN 'masuk' WHEN k.kd_jenis_absen = 2 THEN 'pulang' END AS status FROM `t_absen` a, t_jenis_absen k, pegawai p WHERE k.kd_jenis_absen=a.kd_jenis_absen AND p.nira=a.nira AND p.nira=:nira and date(a.waktu) = date(now())")
                .setParameter("nira", perawat)
                .getResultList();

        JSONObject res = new JSONObject();

        if (!cekAbsen.isEmpty()) {

            String data = (String) cekAbsen.get(0);
            JSONObject js = new JSONObject();

            js.put("status", data);
            res.put("code", 1);
            res.put("data", js);
            return res;

        } else {

            res.put("code", 0);
            return res;

        }

    }

    @GetMapping("/jam/{kode_perawat}")
    public Object jam(@PathVariable("kode_perawat") String perawat) {

        // query untuk qr code
        List cekAbsen = em
                .createNativeQuery(
                        "SELECT s.waktu AS waktu FROM tb_konfigurasi k LEFT JOIN tb_tim t ON (k.kd_tim = t.kode_tim) LEFT JOIN t_jenis_tim tj ON ( t.id_jenis_tim = tj.id_jenis_tim ) LEFT JOIN rawat_inap r ON (k.kode_ruangan = r.kode_ruangan) LEFT JOIN tb_shift s ON (k.kode_shift = s.kode_shift) JOIN tb_anggota_tim ta ON (k.kd_tim = ta.kd_tim) LEFT JOIN pegawai p ON (ta.nira = p.nira) WHERE ta.nira =:nira AND k.kode_shift IN(1,2,3) AND k.tanggal = DATE(NOW()) and (s.waktu BETWEEN DATE_SUB(now(),interval 30 minute) and DATE_ADD(now(),interval 30 minute));")
                .setParameter("nira", perawat)
                .getResultList();

        JSONObject res = new JSONObject();

        if (!cekAbsen.isEmpty()) {

            String data = (String) cekAbsen.get(0);
            JSONObject js = new JSONObject();

            js.put("waktu", data);
            res.put("code", 1);
            res.put("data", js);
            return res;

        } else {

            res.put("code", 0);
            return res;

        }

    }

}
