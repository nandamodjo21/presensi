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
@RequestMapping("/absenmasuk")
public class Scan {

        @Autowired
        EntityManager em;

        @PostMapping
        public Object absensi(@RequestBody AbsenDto dto) {

                List cekdata = em
                                .createNativeQuery(
                                                "SELECT k.kode_konfigurasi, ta.nira FROM tb_konfigurasi k LEFT JOIN tb_tim t ON (k.kd_tim= t.kode_tim) LEFT JOIN t_jenis_tim tj ON (t.id_jenis_tim= tj.id_jenis_tim) LEFT JOIN rawat_inap r on (k.kode_ruangan= r.kode_ruangan) LEFT JOIN tb_shift s on (k.kode_shift= s.kode_shift) JOIN tb_anggota_tim ta ON (k.kd_tim= ta.kd_tim) LEFT JOIN pegawai p on (ta.nira= p.nira) WHERE ta.nira =:nira AND k.tanggal = date(now()) and (s.waktu BETWEEN DATE_SUB(now(),interval 30 minute) and DATE_ADD(now(),interval 30 minute)) AND k.kode_shift IN(1,2,3)")
                                .setParameter("nira", dto.getKode_perawat())
                                .getResultList();

                Object[] o1 = (Object[]) cekdata.get(0);

                List cekAbsen = em
                                .createNativeQuery(
                                                "SELECT * FROM `t_absen` WHERE kode_konfigurasi = :kode_konfigurasi AND nira = :nira")
                                .setParameter("kode_konfigurasi", o1[0])
                                .setParameter("nira", o1[1])
                                .getResultList();

                List cekAsenHdr = em
                                .createNativeQuery(
                                                "SELECT a.kd_absen,a.kode_konfigurasi,a.nira, a.kd_jenis_absen,a.waktu FROM t_absen a, tb_konfigurasi k,tb_shift s WHERE a.kode_konfigurasi = k.kode_konfigurasi AND date(a.waktu) = k.tanggal AND k.kode_shift = s.shift GROUP BY a.kd_absen,a.kode_konfigurasi ORDER BY kd_jenis_absen, a.waktu DESC LIMIT 1")
                                .getResultList();

                JSONObject res = new JSONObject();
                Object o2[] = (Object[]) cekAsenHdr.get(0);
                JSONObject js = new JSONObject();
                js.put("kd_absen", o2[0]);
                js.put("kd_jenis_absen", o2[3]);
                res.put("code", 1);
                res.put("data", js);
                if (!cekdata.isEmpty()) {

                        if (!cekAsenHdr.isEmpty()) {

                                em
                                                .createNativeQuery(
                                                                "INSERT INTO t_absen (kd_absen, kode_konfigurasi,nira, kd_jenis_absen, waktu) VALUES(UUID(), :kode_konfigurasi, :nira, :kd_jenis_absen, now())")
                                                .setParameter("kode_konfigurasi", o2[1])
                                                .setParameter("nira", o2[2])
                                                .setParameter("kd_jenis_absen", cekAbsen.size() + 2)
                                                .executeUpdate();

                                if (cekAbsen.size() != 2) {
                                        em
                                                        .createNativeQuery(
                                                                        "INSERT INTO t_absen (kd_absen, kode_konfigurasi,nira, kd_jenis_absen, waktu) VALUES(UUID(), :kode_konfigurasi, :nira, :kd_jenis_absen, now())")
                                                        .setParameter("kode_konfigurasi", o1[0])
                                                        .setParameter("nira", o1[1])
                                                        .setParameter("kd_jenis_absen", cekAbsen.size() + 1)
                                                        .executeUpdate();

                                        res.put("code", 1);
                                        res.put("message", "sukses");
                                        return res;
                                } else {
                                        res.put("code", 0);
                                        res.put("message", "anda sudah absen");
                                        return res;
                                }

                        } else {
                                res.put("code", 0);
                                res.put("message", "qr code kosong");
                                return res;

                        }
                } else {
                        res.put("code", 0);
                        res.put("message", "data null");
                        return res;
                }

        }
}
