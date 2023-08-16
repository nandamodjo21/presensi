package com.example.presensi.controllers;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.presensi.dto.AjuanDto;

import net.minidev.json.JSONObject;

@RestController
@Transactional
@RequestMapping("/tukar")

public class Ajuantukar {

    @Autowired

    EntityManager em;

    @PostMapping
    public Object tukarshift(@RequestBody AjuanDto dto) {
        List cekAjuan = em
                .createNativeQuery("SELECT nira from t_ajuan_tukar where nira = :nira and date(tanggal) = date(now())")
                .setParameter("nira", dto.getKode_perawat())
                .getResultList();
        JSONObject js = new JSONObject();
        // Object[] o2 = (Object[]) cekAjuan.get(0);
        if (cekAjuan.isEmpty()) {

            em.createNativeQuery(
                    "INSERT INTO `t_ajuan_tukar` (`id_ajuan_tukar`, `nira`, `tanggal`, `keterangan`) VALUES (UUID(), :nira, now(), :keterangan);")
                    .setParameter("nira", dto.getKode_perawat())
                    .setParameter("keterangan", dto.getKeterangan())
                    .executeUpdate();

            js.put("code", 1);
            js.put("message", "berhasil mengajukan pertukaran");
            return js;
        } else {
            js.put("code", 0);
            js.put("message", "anda sudah melakukan ajuan untuk hari ini");
            return js;
        }
    }

}
