package com.deanery;

import com.deanery.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class TestDataInit implements CommandLineRunner {

    GroupService groupService;
    MarkService markService;
    PersonService personService;
    SubjectService subjectService;
    CustomUserDetailsService customUserDetailsService;
    PasswordEncoder pwdEncoder;

    @Autowired
    public TestDataInit(GroupService groupService, MarkService markService,
                        PersonService personService, SubjectService subjectService,
                        CustomUserDetailsService customUserDetailsService, PasswordEncoder pwdEncoder) {
        this.groupService = groupService;
        this.markService = markService;
        this.personService = personService;
        this.subjectService = subjectService;
        this.customUserDetailsService = customUserDetailsService;
        this.pwdEncoder = pwdEncoder;
    }

    @Override
    public void run(String... args) {
        customUserDetailsService.deleteAllUsers();
        markService.deleteAllMarks();
        personService.deleteAllPeople();
        groupService.deleteAllGroups();
        subjectService.deleteAllSubjects();

        customUserDetailsService.createUser("Admin", pwdEncoder.encode("Admin"), Collections.singletonList("ROLE_ADMIN"));
        groupService.createGroup("3530904/00001");
        groupService.createGroup("3530904/00002");
        groupService.createGroup("3530904/00003");
        subjectService.createSubject("Programming");
        subjectService.createSubject("Math");
        subjectService.createSubject("History");
        personService.createPerson("Konstantine", "Samoilenko", "Igorevich", 1L, "S");
        personService.createPerson("Oleg", "Olegov", "Olegovich", 2L, "S");
        personService.createPerson("Igor", "Igorev", "Igorevich", 3L, "S");
        personService.createPerson("Vasya", "Pupkin", "Ignatievich", null, "P");
        personService.createPerson("Innokentiy", "Popov", "Artemovich", null, "P");
        markService.createMark(1L, 1L, 4L, "5");
        markService.createMark(1L, 2L, 4L, "5");
        markService.createMark(1L, 3L, 5L, "5");
        markService.createMark(2L, 1L, 4L, "2");
        markService.createMark(2L, 2L, 4L, "2");
        markService.createMark(3L, 3L, 5L, "5");
    }
}
