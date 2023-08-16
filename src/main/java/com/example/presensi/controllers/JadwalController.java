package com.example.presensi.controllers;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@RestController
@Transactional
@RequestMapping("/jadwal")
public class JadwalController {

    @Autowired
    EntityManager em;

    @GetMapping("/{kode_perawat}")
public ResponseEntity<Object> getJadwal(@PathVariable("kode_perawat") String kode_perawat) {
    List<Object> cekjadwal = em.createNativeQuery(
        "SELECT k.kode_konfigurasi, ta.nira,p.nama,s.shift,k.tanggal,k.status FROM tb_konfigurasi k LEFT JOIN tb_tim t ON (k.kd_tim = t.kode_tim) LEFT JOIN t_jenis_tim tj ON ( t.id_jenis_tim = tj.id_jenis_tim ) LEFT JOIN rawat_inap r ON (k.kode_ruangan = r.kode_ruangan) LEFT JOIN tb_shift s ON (k.kode_shift = s.kode_shift) JOIN tb_anggota_tim ta ON (k.kd_tim = ta.kd_tim) LEFT JOIN pegawai p ON (ta.nira = p.nira) WHERE ta.nira = :nira")
        .setParameter("nira", kode_perawat)
        .getResultList();

    JSONArray jadwalArray = new JSONArray();
    for (Object jadwal : cekjadwal) {
        Object[] jadwalArrayObj = (Object[]) jadwal;
        JSONObject jadwalObject = new JSONObject();
        jadwalObject.put("kode_konfigurasi", jadwalArrayObj[0]);
        jadwalObject.put("nira", jadwalArrayObj[1]);
        jadwalObject.put("nama", jadwalArrayObj[2]);
        jadwalObject.put("shift", jadwalArrayObj[3]);
        jadwalObject.put("tanggal", jadwalArrayObj[4]);
        jadwalObject.put("status", jadwalArrayObj[5]);
        // jadwalObject.put("jadwal", jadwalArrayObj[6]); // Menambahkan properti "jadwal"
        jadwalArray.add(jadwalObject);
    }

    JSONObject response = new JSONObject();
    response.put("data", jadwalArray);
    response.put("code", 1);

    return new ResponseEntity<>(response, HttpStatus.OK);
}

}
