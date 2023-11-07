package com.example.presensi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.presensi.controllers.AbsenController;

@SpringBootApplication
@RestController
public class RestApiApplication extends SpringBootServletInitializer {

	@GetMapping
	public String get() {
		return "HALO. ENDPOINT DATA /find YAA BOSSKU.";
	}

	public static void main(String[] args) {
		SpringApplication.run(RestApiApplication.class, args);

		System.out.println(AbsenController.class);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// TODO Auto-generated method stub
		return builder.sources(RestApiApplication.class);
	}

}
