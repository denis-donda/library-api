package com.cursodsousa.libraryapi;

import com.cursodsousa.libraryapi.service.EmailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class LibraryApiApplication {

    @Autowired
    private EmailService emailService;

    @Bean
    public ModelMapper modelMapper() { // Cria um instância Singleton de ModelMapper para servir toda aplicação.
        return new ModelMapper();
    }

    @Bean
    public CommandLineRunner runner() {
        return args -> {
            List<String> emails = Arrays.asList("2badbac231-2a14d4@inbox.mailtrap.io");
            emailService.sendMails("Testando serviço de e-mails", emails);
            System.out.println("EMAILS ENVIADOS");
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(LibraryApiApplication.class, args);
    }

}
