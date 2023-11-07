package com.example.presensi.controllers;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/find")
public class TodoController {

    @Autowired
    EntityManager em;

    @GetMapping
    public Object all() {

        List cekTodo = em.createNativeQuery("SELECT * FROM t_todo").getResultList();

        return ResponseEntity.status(HttpStatus.OK).body(cekTodo);

    }

}
