package com.modvi.libros;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MsvcLibrosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcLibrosApplication.class, args);
	}

}
