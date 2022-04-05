package com.vam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

// SpringScheduler 사용하기 위한 Annotation
@EnableScheduling
@SpringBootApplication
public class VamApplication {

	public static void main(String[] args) {
		SpringApplication.run(VamApplication.class, args);
	}

}
