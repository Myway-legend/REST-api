package com.deanery.service;

import com.deanery.entity.Person;
import com.deanery.entity.Subject;
import com.deanery.repository.SubjectsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectsRepository subjectsRepository;
    private final PersonService personService;

    @Autowired
    @Lazy
    public SubjectServiceImpl(SubjectsRepository subjectsRepository, PersonService personService) {
        this.subjectsRepository = subjectsRepository;
        this.personService = personService;
    }

    @Override
    public List<Subject> readSubjects() {
        return (List<Subject>) subjectsRepository.findAll();
    }

    @Override
    public List<Subject> readSubjectsStartsWith(String prefix) {
        return subjectsRepository.findByNameStartsWith(prefix);
    }

    @Override
    public List<Subject> readSubjectsOrdered(Boolean asc) {
        return asc ? subjectsRepository.orderByName() : subjectsRepository.orderByNameDesc();
    }

    @Override
    public List<Subject> readSubjectsByTeacher(Long teacherId) throws IllegalStateException {
        Person teacher = personService.readPersonById(teacherId);
        if (teacher.getType().equals("S")) {
            throw new IllegalStateException("Invalid teacherId");
        }
        return subjectsRepository.findByTeacher(teacherId);
    }

    @Override
    public Subject readSubjectById(Long id) throws IllegalStateException {
        return subjectsRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Invalid subject ID"));
    }

    @Override
    public Subject readSubjectByName(String name) throws IllegalStateException {
        return subjectsRepository.findByName(name).orElseThrow(
                () -> new IllegalStateException("Invalid subject name")
        );
    }

    @Override
    public void createSubject(String name) throws IllegalStateException {
        if (subjectsRepository.existsByName(name)) {
            throw new IllegalStateException("Subject already exists");
        }
        subjectsRepository.save(new Subject(name));
    }

    @Override
    public void deleteSubjectById(Long id) throws IllegalStateException {
        subjectsRepository.delete(subjectsRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Invalid subject ID")));
    }

    @Override
    public void deleteAllSubjects() {
        subjectsRepository.deleteAll();
    }

    @Override
    public void updateSubjectById(Long id, String newName) throws IllegalStateException, IllegalArgumentException {
        if (subjectsRepository.existsByName(newName) || id == null) {
            throw new IllegalStateException("Subject already exists or invalid ID");
        }
        subjectsRepository.updateNameById(id, newName);
    }
}
