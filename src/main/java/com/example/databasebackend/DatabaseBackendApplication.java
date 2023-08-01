package com.example.databasebackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.databasebackend.mapper")
public class DatabaseBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatabaseBackendApplication.class, args);
	}

}
