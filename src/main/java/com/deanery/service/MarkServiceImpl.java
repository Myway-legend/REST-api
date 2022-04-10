package com.deanery.service;

import com.deanery.entity.Mark;
import com.deanery.entity.Person;
import com.deanery.repository.MarksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MarkServiceImpl implements MarkService {

    private final MarksRepository marksRepository;
    private final PersonService personService;
    private final SubjectService subjectService;

    @Autowired
    public MarkServiceImpl(MarksRepository marksRepository,
                           PersonService personService,
                           SubjectService subjectService) {
        this.marksRepository = marksRepository;
        this.personService = personService;
        this.subjectService = subjectService;
    }

    @Override
    public List<Mark> readMarks() {
        return (List<Mark>) marksRepository.findAll();
    }

    @Override
    public List<Mark> readMarksOrdered(Boolean asc) {
        return asc ? marksRepository.orderByValue() : marksRepository.orderByValueDesc();
    }

    @Override
    public List<Mark> readMarksByStudentId(Long studentId) throws IllegalStateException {
        return marksRepository.findMarksByStudentId(personService.readPersonById(studentId));
    }

    @Override
    public Mark readMarkById(Long id) throws IllegalStateException {
        return marksRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Invalid mark ID"));
    }

    @Override
    public void createMark(Long studentId, Long subjectId, Long teacherId, Integer value)
            throws IllegalStateException, IllegalArgumentException {

        Person student = personService.readPersonById(studentId);
        Person teacher = personService.readPersonById(teacherId);

        if (student.getType() != 'S' || teacher.getType() != 'P') {
            throw new IllegalArgumentException("Invalid people");
        }
        if (isValueInvalid(value)) {
            throw new IllegalArgumentException("Invalid mark value");
        }

        marksRepository.save(new Mark(personService.readPersonById(studentId), subjectService.readSubjectById(subjectId),
                personService.readPersonById(teacherId), value));
    }

    @Override
    public void deleteAllMarks() {
        marksRepository.deleteAll();
    }

    @Override
    public void deleteMarksByStudentId(Long studentId) throws IllegalStateException, IllegalArgumentException {

        Person student = personService.readPersonById(studentId);
        if (student.getType() != 'S') {
            throw new IllegalArgumentException("Invalid person");
        }

        marksRepository.deleteMarksByStudentId(student);
    }

    @Override
    public void deleteMarksByTeacherId(Long teacherId) throws IllegalStateException, IllegalArgumentException {
        Person teacher = personService.readPersonById(teacherId);
        if (teacher.getType() != 'P') {
            throw new IllegalArgumentException("Invalid person");
        }

        marksRepository.deleteMarksByTeacherId(teacher);
    }

    @Override
    public void deleteMarksBySubjectId(Long subjectId) throws IllegalStateException {
        marksRepository.deleteAllBySubjectId(subjectService.readSubjectById(subjectId));
    }

    @Override
    public void deleteMarkById(Long id) throws IllegalStateException {
        marksRepository.delete(marksRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Invalid mark ID")));
    }

    @Override
    public void updateMarkById(Long id, Long studentId, Long subjectId, Long teacherId, Integer value)
            throws IllegalStateException, IllegalArgumentException {

        Mark mark = marksRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Invalid mark ID"));

        if (studentId != null) {
            if (isStudentInvalid(studentId)) {
                throw new IllegalArgumentException("Invalid mark studentId");
            }
            mark.setStudentId(personService.readPersonById(studentId));
            marksRepository.save(mark);
        }
        if (subjectId != null) {
            if (!isSubjectValid(subjectId)) {
                throw new IllegalArgumentException("Invalid mark subjectId");
            }
            mark.setSubjectId(subjectService.readSubjectById(subjectId));
            marksRepository.save(mark);
        }
        if (teacherId != null) {
            if (!isTeacherValid(teacherId)) {
                throw new IllegalArgumentException("Invalid mark teacherId");
            }
            mark.setTeacherId(personService.readPersonById(teacherId));
            marksRepository.save(mark);
        }
        if (value != null) {
            if (isValueInvalid(value)) {
                throw new IllegalArgumentException("Invalid mark value");
            }
            mark.setValue(value);
            marksRepository.save(mark);
        }
    }

    private boolean isValueInvalid(Integer value) {
        return value <= 1 || value >= 6;
    }
    private boolean isStudentInvalid(Long studentId) throws IllegalArgumentException {
        Person person = personService.readPersonById(studentId);
        return person.getType() != 'S';
    }
    private boolean isSubjectValid(Long subjectId) throws IllegalArgumentException {
        subjectService.readSubjectById(subjectId);
        return true;
    }
    private boolean isTeacherValid(Long teacherId) throws IllegalArgumentException {
        Person person = personService.readPersonById(teacherId);
        return person.getType() == 'P';
    }
}
