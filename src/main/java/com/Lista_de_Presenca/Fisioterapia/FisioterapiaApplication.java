package com.Lista_de_Presenca.Fisioterapia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FisioterapiaApplication {

	public static void main(String[] args) {
		SpringApplication.run(FisioterapiaApplication.class, args);
	}

}
