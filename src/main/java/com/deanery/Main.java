package com.deanery;

import com.deanery.service.GroupService;
import com.deanery.service.MarkService;
import com.deanery.service.PersonService;
import com.deanery.service.SubjectService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }


    @Bean
    public CommandLineRunner test(MarkService markService, PersonService personService,
                                  GroupService groupService, SubjectService subjectService) {
        return args -> {
            //subjectService.createSubject("Math");
            //subjectService.createSubject("Proga");
            //groupService.createGroup("3530904/00005");
            //personService.createPerson("Aboba", "Aboba", "Aboba", 1L, 'S');
            //personService.createPerson("Abobaz", "Abobaz", "Abobaz", 1L, 'S');
            //personService.createPerson("Abobasgfhfg", "Abobghfhas", "Abfghfobas", null, 'P');
            //markService.createMark(1L, 1L, 3L, 5);
            //markService.createMark(2L, 1L, 3L, 5);
            //markService.createMark(1L, 1L, 2L, 5);
            //markService.createMark(1L, 1L, 2L, 4);
            //markService.deleteMarkById(5L);
            //System.out.println();
        };
    }
}
