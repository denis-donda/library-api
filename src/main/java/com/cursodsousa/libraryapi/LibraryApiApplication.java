package com.cursodsousa.libraryapi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LibraryApiApplication {

	@Bean
	public ModelMapper modelMapper(){ // Cria um instância Singleton de ModelMapper para servir toda aplicação.
		return new ModelMapper();
	};

	public static void main(String[] args) {
		SpringApplication.run(LibraryApiApplication.class, args);
	}

}
