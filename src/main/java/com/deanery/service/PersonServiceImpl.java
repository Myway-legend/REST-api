package com.deanery.service;

import com.deanery.entity.Person;
import com.deanery.repository.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService{

    private final PeopleRepository peopleRepository;
    private final MarkService markService;
    private final GroupService groupService;
    private final SubjectService subjectService;

    @Autowired
    @Lazy
    public PersonServiceImpl(PeopleRepository peopleRepository,
                             MarkService markService,
                             GroupService groupService,
                             SubjectService subjectService) {
        this.peopleRepository = peopleRepository;
        this.markService = markService;
        this.groupService = groupService;
        this.subjectService = subjectService;
    }

    @Override
    public List<Person> readPeople() {
        return (List<Person>) peopleRepository.findAll();
    }

    @Override
    public List<Person> readPeopleOrdered(Boolean asc) {
        return asc ? peopleRepository.orderByName() : peopleRepository.orderByNameDesc();
    }

    @Override
    public List<Person> readStudentsBySubject(Long subjectId) throws IllegalStateException {
        return peopleRepository.findStudentsBySubject(subjectService.readSubjectById(subjectId).getId());
    }

    @Override
    public List<Person> readTeachersBySubject(Long subjectId) {
        return peopleRepository.findTeachersBySubject(subjectService.readSubjectById(subjectId).getId());
    }

    @Override
    public List<Person> readAdditionalSessionStudents() {
        return peopleRepository.findAdditionalSessionStudents();
    }

    @Override
    public List<Person> readTeachers() {
        return peopleRepository.findTeachers();
    }

    @Override
    public List<Person> readStudents() {
        return peopleRepository.findStudents();
    }

    @Override
    public List<Person> readStudentsByTeacher(Long teacherId) throws IllegalArgumentException {
        Optional<Person> teacher = peopleRepository.findById(teacherId);
        if (teacher.isEmpty() || teacher.get().getType() == 'S') {
            throw new IllegalArgumentException("Invalid teacherId");
        }
        return peopleRepository.findByTeacher(teacherId);
    }

    @Override
    public List<Person> readPeopleByGroups() {
        return peopleRepository.findPeopleByGroups();
    }

    @Override
    public Person readPersonById(Long id) throws IllegalStateException {
        return peopleRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Invalid person ID"));
    }

    @Override
    public void createPerson(String firstName, String lastName, String patherName, Long groupId, Character type)
            throws IllegalArgumentException {

        Person person;

        if (groupId == null) {
            person = new Person(firstName, lastName, patherName, null, type);
        } else {
            person = new Person(firstName, lastName, patherName, groupService.readGroupById(groupId), type);
        }
        if (!isPersonValid(person)) {
            throw new IllegalArgumentException("Invalid person data");
        }
        peopleRepository.save(person);
    }

    @Override
    public void deleteAllPeople() {
        peopleRepository.deleteAll();
    }

    @Override
    public void deletePeopleByGroup(Long groupId) throws IllegalStateException {
        peopleRepository.deleteByGroupId(groupService.readGroupById(groupId));
    }

    @Override
    public void deletePersonById(Long id) throws IllegalStateException {
        Person person = peopleRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Invalid person ID"));
        peopleRepository.delete(person);
    }

    @Override
    public void updatePersonById(Long id, String firstName,
                                 String lastName, String patherName, Long groupId, Character type)
            throws IllegalStateException, IllegalArgumentException {

        Person person = peopleRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Invalid person ID"));

        if (firstName != null) {
            if (!isEnglishName(firstName)) {
                throw new IllegalArgumentException("Invalid person firstName");
            }
            person.setFirstName(firstName);
            peopleRepository.save(person);
        }
        if (lastName != null) {
            if (!isEnglishName(lastName)) {
                throw new IllegalArgumentException("Invalid person lastName");
            }
            person.setLastName(lastName);
            peopleRepository.save(person);
        }
        if (patherName != null) {
            if (!isEnglishName(patherName)) {
                throw new IllegalArgumentException("Invalid person patherName");
            }
            person.setPatherName(patherName);
            peopleRepository.save(person);
        }
        if (groupId != null) {
            person.setGroupId(groupService.readGroupById(groupId));
            peopleRepository.save(person);
        }
        if (type != null) {
            if (!isTypeValid(type)) {
                throw new IllegalArgumentException("Invalid person type");
            }
            person.setType(type);
            peopleRepository.save(person);
        }
    }

    private boolean isPersonValid(Person person) {
        if (isEnglishName(person.getFirstName()) && isEnglishName(person.getLastName())) {
            if (isEnglishName(person.getPatherName()) || person.getPatherName() == null) {
                return isTypeValid(person.getType());
            }
        }
        return false;
    }

    private boolean isEnglishName(String name) {
        return name.matches("[A-Z][a-z]{0,19}");
    }

    private boolean isTypeValid(Character type) {
        return type == 'S' || type == 'P';
    }

}
