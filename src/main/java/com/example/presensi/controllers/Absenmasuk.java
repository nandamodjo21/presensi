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
@RequestMapping("/masuk")
public class Absenmasuk {

        @Autowired
        EntityManager em;

        @PostMapping
        public Object abs(@RequestBody AbsenDto dto) {

                List cekQr = em
                                .createNativeQuery(
                                                "select kd_absen,kd_jenis_absen from t_absen where kd_absen = :kd_absen")
                                .setParameter("kd_absen", dto.getKd_absen())
                                .getResultList();

                List cekAbsen = em
                                .createNativeQuery(
                                                "SELECT kode_konfigurasi,nira from t_absen WHERE nira = :nira AND waktu = date(now())")
                                .setParameter("nira", dto.getKode_perawat())
                                .getResultList();

                JSONObject res = new JSONObject();

                if (!cekQr.isEmpty()) {

                        Object[] o3 = (Object[]) cekQr.get(0);
                        // JSONObject js = new JSONObject();
                        // js.put("kd_absen", o3[0]);
                        // res.put("data", js);
                        System.out.println(o3[0]);

                        // ABSEN PULANG
                        em.createNativeQuery(
                                        "insert into t_absen SELECT uuid() as kd_absen, kode_konfigurasi, nira, 2 as kd_jenis_absen, now() as waktu FROM t_absen WHERE kd_absen =:kd_absen")
                                        .setParameter("kd_absen", o3[0])
                                        .executeUpdate();

                        if (cekAbsen.size() != 2) {

                                List cekjadwal = em
                                                .createNativeQuery(
                                                                "SELECT k.kode_konfigurasi, ta.nira FROM tb_konfigurasi k LEFT JOIN tb_tim t ON (k.kd_tim = t.kode_tim) LEFT JOIN t_jenis_tim tj ON ( t.id_jenis_tim = tj.id_jenis_tim ) LEFT JOIN rawat_inap r ON (k.kode_ruangan = r.kode_ruangan) LEFT JOIN tb_shift s ON (k.kode_shift = s.kode_shift) JOIN tb_anggota_tim ta ON (k.kd_tim = ta.kd_tim) LEFT JOIN pegawai p ON (ta.nira = p.nira) WHERE ta.nira =:nira AND k.kode_shift IN(1,2,3) AND k.tanggal = DATE(NOW()) and (s.waktu BETWEEN DATE_SUB(now(),interval 30 minute) and DATE_ADD(now(),interval 30 minute)) UNION SELECT t.kode_konfigurasi,t.nira FROM t_tukar_shift t LEFT JOIN tb_shift s ON(t.kode_shift=s.kode_shift) WHERE t.kode_shift IN(1,2,3) AND t.nira = :nira AND t.tanggal = DATE(NOW()) AND (s.waktu BETWEEN DATE_SUB(now(), INTERVAL 30 minute) AND DATE_ADD(now(),INTERVAL 30 minute))")
                                                .setParameter("nira", dto.getKode_perawat())
                                                .getResultList();

                                Object[] o1 = (Object[]) cekjadwal.get(0);

                                // ABSEN MASUK
                                em
                                                .createNativeQuery(
                                                                "INSERT INTO t_absen (kd_absen, kode_konfigurasi,nira,kd_jenis_absen, waktu) VALUES(UUID(), :kode_konfigurasi, :nira, :kd_jenis_absen, now())")
                                                .setParameter("kode_konfigurasi", o1[0])
                                                .setParameter("nira", o1[1])
                                                .setParameter("kd_jenis_absen", cekAbsen.size() + 1)
                                                .executeUpdate();

                                res.put("code", 1);
                                res.put("message", "berhasil");
                                return res;
                        } else {
                                res.put("code", 0);
                                res.put("message", "anda sudah absen");
                                return res;
                        }

                } else {
                        res.put("code", 0);
                        res.put("message", "qr code tidak valid");
                        return res;
                }
        }
}
