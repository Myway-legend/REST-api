package com.deanery.service;

import com.deanery.entity.Subject;

import java.util.List;

public interface SubjectService {

    // READ
    List<Subject> readSubjects();
    List<Subject> readSubjectsStartsWith(String prefix);
    List<Subject> readSubjectsOrdered(Boolean asc);
    List<Subject> readSubjectsByTeacher(Long teacherId) throws IllegalStateException;
    Subject readSubjectById(Long id) throws IllegalStateException;
    Subject readSubjectByName(String name) throws IllegalStateException;

    // CREATE
    void createSubject(String name) throws IllegalStateException, IllegalArgumentException;

    // DELETE
    void deleteAllSubjects();
    void deleteSubjectById(Long id) throws IllegalStateException;

    // UPDATE
    void updateSubjectById(Long id, String newName) throws IllegalStateException, IllegalArgumentException;
}
