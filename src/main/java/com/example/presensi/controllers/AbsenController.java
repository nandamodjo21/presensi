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
@RequestMapping("/absens")
public class AbsenController {

  @Autowired
  EntityManager em;

  @PostMapping
  public Object insertabsen(@RequestBody AbsenDto dto) {

    List cekJadwal = em
        .createNativeQuery(
            "SELECT k.kode_konfigurasi, ta.nira FROM tb_konfigurasi k LEFT JOIN tb_tim t ON (k.kd_tim = t.kode_tim) LEFT JOIN t_jenis_tim tj ON ( t.id_jenis_tim = tj.id_jenis_tim ) LEFT JOIN rawat_inap r ON (k.kode_ruangan = r.kode_ruangan) LEFT JOIN tb_shift s ON (k.kode_shift = s.kode_shift) JOIN tb_anggota_tim ta ON (k.kd_tim = ta.kd_tim) LEFT JOIN pegawai p ON (ta.nira = p.nira) WHERE ta.nira =:nira AND k.kode_shift IN(1,2,3) AND k.tanggal = DATE(NOW()) and (s.waktu BETWEEN DATE_SUB(now(),interval 30 minute) and DATE_ADD(now(),interval 30 minute))")
        .setParameter("nira", dto.getKode_perawat())
        .getResultList();
    JSONObject res = new JSONObject();
    if (!cekJadwal.isEmpty()) {
      Object[] o1 = (Object[]) cekJadwal.get(0);
      List cekAbsen = em
          .createNativeQuery(
              "SELECT * FROM `t_absen` WHERE nira =:nira AND (date(waktu) = date(now()))")
          .setParameter("nira", o1[1])
          .getResultList();

      // List cekQr = em
      // .createNativeQuery(
      // "select a.kd_absen, a.kd_jenis_absen from t_absen a , tb_konfigurasi k ,
      // tb_shift s where k.kode_konfigurasi = a.kode_konfigurasi and s.kode_shift =
      // k.kode_shift and a.kd_absen = '6e5a5ab6-b990-11ed-915e-dc215c6adf56' and
      // (date(a.waktu) = date(now()) or date(a.waktu) = date_add(now(), interval 1
      // day)) and s.waktu_pulang BETWEEN DATE_SUB(NOW(), INTERVAL 30 MINUTE) AND
      // DATE_ADD(NOW(), INTERVAL 30 MINUTE)")
      // .setParameter("kd_absen", cekAbsen)
      // .getResultList();

      if (cekAbsen.size() != 1) {
        em
            .createNativeQuery(
                "INSERT INTO t_absen (kd_absen, kode_konfigurasi,nira, kd_jenis_absen, waktu) VALUES(UUID(), :kode_konfigurasi, :nira, :kd_jenis_absen, now())")
            .setParameter("kode_konfigurasi", o1[0])
            .setParameter("nira", o1[1])
            .setParameter("kd_jenis_absen", cekAbsen.size() + 1)
            .executeUpdate();

        res.put("code", 1);
        res.put("message", "sukses absen pulang");
        return res;

      } else {
        em
            .createNativeQuery(
                "INSERT INTO t_absen (kd_absen, kode_konfigurasi,nira, kd_jenis_absen, waktu) VALUES(UUID(), :kode_konfigurasi, :nira, :kd_jenis_absen, now())")
            .setParameter("kode_konfigurasi", o1[0])
            .setParameter("nira", o1[1])
            .setParameter("kd_jenis_absen", cekAbsen.size() + 1)
            .executeUpdate();
        res.put("code", 1);
        res.put("message", "sukses absen pulang");
        return res;

      }
    } else {
      res.put("code", 0);
      res.put("message", "anda tidak memiliki jadwal");

      return res;
    }
  }
}
