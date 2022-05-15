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
    public Mark readMarkByEverything(Long studentId, Long subjectId, Long teacherId) throws IllegalStateException {
        return marksRepository.findByStudentIdAndSubjectIdAndTeacherId(
                personService.readPersonById(studentId),
                subjectService.readSubjectById(subjectId),
                personService.readPersonById(teacherId)
        ).orElseThrow(() -> new IllegalStateException("Invalid mark data"));
    }

    @Override
    public void createMark(Long studentId, Long subjectId, Long teacherId, String value)
            throws IllegalStateException {

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
        if (!student.getType().equals("S")) {
            throw new IllegalArgumentException("Invalid person");
        }

        marksRepository.deleteMarksByStudentId(student);
    }

    @Override
    public void deleteMarksByTeacherId(Long teacherId) throws IllegalStateException, IllegalArgumentException {
        Person teacher = personService.readPersonById(teacherId);
        if (!teacher.getType().equals("P")) {
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
    public void updateMarkById(Long id, Long studentId, Long subjectId, Long teacherId, String value)
            throws IllegalStateException {

        Mark mark = marksRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Invalid mark ID"));

        if (studentId != null) {
            mark.setStudentId(personService.readPersonById(studentId));
            marksRepository.save(mark);
        }
        if (subjectId != null) {
            mark.setSubjectId(subjectService.readSubjectById(subjectId));
            marksRepository.save(mark);
        }
        if (teacherId != null) {
            mark.setTeacherId(personService.readPersonById(teacherId));
            marksRepository.save(mark);
        }
        if (value != null) {
            mark.setValue(value);
            marksRepository.save(mark);
        }
    }
}
