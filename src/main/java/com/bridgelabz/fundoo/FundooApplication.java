package com.bridgelabz.fundoo;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@EnableAutoConfiguration
@SpringBootApplication
public class FundooApplication {

	public static void main(String[] args) {
		System.out.println("application started");
		SpringApplication.run(FundooApplication.class, args);
	}

}
