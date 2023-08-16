package com.example.presensi.controllers;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.presensi.dto.LoginDto;

import net.minidev.json.JSONObject;

@RestController
@Transactional
@RequestMapping("/logins")
public class LoginController {

    @Autowired
    EntityManager em;

    @PostMapping
    public Object Login(@RequestBody LoginDto dto) {

        List cekUser = em
                .createNativeQuery(
                        "SELECT id_user, id_level, status FROM t_user WHERE username = :username AND password = MD5(:password)")
                .setParameter("username", dto.getUsername())
                .setParameter("password", dto.getPassword())
                .getResultList();

        JSONObject res = new JSONObject();

        if (cekUser.isEmpty()) {

            res.put("code", 0);
            res.put("message", "user belum terdaftar");
            return res;

        } else {
            Object o[] = (Object[]) cekUser.get(0);
            if (o[2].equals(false)) {
                res.put("code", 0);
                res.put("message", "akun belum aktif");

                return res;
            } else {
                List perawat = em
                        .createNativeQuery("SELECT kode_perawat, username from t_user where id_user = :id_user")
                        .setParameter("id_user", o[0])
                        .getResultList();
                Object o_o[] = (Object[]) perawat.get(0);
                JSONObject js = new JSONObject();

                js.put("id_user", o[0]);
                js.put("kode_perawat", o_o[0]);
                js.put("username", o_o[1]);
                res.put("code", 1);
                res.put("message", "berhasil login");
                res.put("data", js);
                System.out.println(js);
                return res;

            }

        }

    }

}
