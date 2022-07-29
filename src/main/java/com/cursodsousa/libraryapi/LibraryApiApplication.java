package com.cursodsousa.libraryapi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class LibraryApiApplication {

	@Bean
	public ModelMapper modelMapper(){ // Cria um instância Singleton de ModelMapper para servir toda aplicação.
		return new ModelMapper();
	};

	@Scheduled(cron = "0 30 7 1/1 * ?")
	public void testeAgendamentoTarefas(){
		System.out.println("Agendamento de tarefas funcionando com sucesso!");
	}

	public static void main(String[] args) {
		SpringApplication.run(LibraryApiApplication.class, args);
	}

}
