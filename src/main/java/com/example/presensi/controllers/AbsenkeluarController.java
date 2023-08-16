package com.example.presensi.controllers;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.presensi.dto.QrKeluarDto;

import net.minidev.json.JSONObject;

@RestController
@Transactional
@RequestMapping("/absenpulang")
public class AbsenkeluarController {

    @Autowired
    EntityManager em;

    @PostMapping
    public Object insertout(@RequestBody QrKeluarDto dto) {

        // query untuk qr code
        List cekAbsen = em
                .createNativeQuery(
                        "SELECT a.kd_absen FROM t_absen a,tb_konfigurasi k,tb_shift s WHERE k.kode_konfigurasi = a.kode_konfigurasi AND s.kode_shift = k.kode_shift AND a.nira = :nira AND(DATE(a.waktu) = DATE(NOW()) OR DATE(a.waktu) + INTERVAL 1 DAY) AND s.waktu_pulang BETWEEN DATE_SUB(NOW(), INTERVAL 30 MINUTE) AND DATE_ADD(NOW(), INTERVAL 30 MINUTE)")
                .setParameter("nira", dto.getKode_perawat())
                .getResultList();

        JSONObject res = new JSONObject();

        if (!cekAbsen.isEmpty()) {

            if (cekAbsen.size() != 2) {

                var data = cekAbsen.get(0);

                res.put("code", 1);
                res.put("message", "Silahkan diScan");
                res.put("data", data);
                return res;

            } else {
                res.put("code", 0);
                res.put("message", "anda sudah absen");
                return res;
            }

        } else {

            res.put("code", 0);
            res.put("message", "anda tidak punya akses");
            return res;

        }

    }

}
