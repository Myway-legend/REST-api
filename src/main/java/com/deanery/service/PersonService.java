package com.deanery.service;

import com.deanery.entity.Person;

import java.util.List;

public interface PersonService {

    // READ
    List<Person> readPeople();
    List<Person> readPeopleOrdered(Boolean asc);
    List<Person> readStudentsBySubject(Long subjectId);
    List<Person> readStudentsByTeacher(Long teacherId) throws IllegalArgumentException;
    List<Person> readTeachersBySubject(Long subjectId);

    List<Person> readAdditionalSessionStudents();
    List<Person> readTeachers();
    List<Person> readStudents();
    List<Person> readPeopleByGroups();
    Person readPersonById(Long id) throws IllegalStateException;

    // CREATE
    void createPerson(String firstName, String lastName, String patherName, Long groupId, Character type)
            throws IllegalStateException, IllegalArgumentException;

    // DELETE
    void deleteAllPeople();
    void deletePeopleByGroup(Long groupId) throws IllegalStateException;
    void deletePersonById(Long id) throws IllegalStateException;

    // UPDATE
    void updatePersonById(Long id, String firstName, String lastName,
                          String patherName, Long groupId, Character type)
            throws IllegalStateException, IllegalArgumentException;
}
