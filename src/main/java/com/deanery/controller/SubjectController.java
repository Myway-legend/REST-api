package com.deanery.controller;

import com.deanery.entity.Subject;
import com.deanery.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/deanery-api/subjects")
public class SubjectController {

    SubjectService subjectService;

    @Autowired
    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping(path = "/all")
    @ResponseBody
    public ResponseEntity<List<Subject>> getSubjects(@RequestParam(required = false) Optional<String> prefix) {

        return prefix.isPresent() ?
                new ResponseEntity<>(subjectService.readSubjectsStartsWith(prefix.get()), HttpStatus.OK) :
                new ResponseEntity<>(subjectService.readSubjects(), HttpStatus.OK);
    }

    @GetMapping(path = "/all-ordered")
    @ResponseBody
    public ResponseEntity<List<Subject>> getSubjectsOrdered(@RequestParam(required = false) Optional<Object> asc) {

        return asc.isPresent() ?
                new ResponseEntity<>(subjectService.readSubjectsOrdered(true), HttpStatus.OK) :
                new ResponseEntity<>(subjectService.readSubjectsOrdered(false), HttpStatus.OK);
    }

    @GetMapping(path = "/by-teacher")
    @ResponseBody
    public ResponseEntity<List<Subject>> getSubjectsByTeacher(@RequestParam Long id) {
        try {
            return new ResponseEntity<>(subjectService.readSubjectsByTeacher(id), HttpStatus.OK);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid teacher");
        }
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<Subject> getSubject(@RequestParam Long id) {
        try {
            return new ResponseEntity<>(subjectService.readSubjectById(id), HttpStatus.OK);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found");
        }
    }

    @PostMapping
    public void postSubject(@RequestBody Subject subject) {
        try {
            subjectService.createSubject(subject.getName());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.FOUND, "Subject already exists");
        }

    }

    @DeleteMapping
    public void deleteSubject(@RequestParam(required = false) Optional<Long> id) {

        try {
            if (id.isPresent()) {
                subjectService.deleteSubjectById(id.get());
            } else {
                subjectService.deleteAllSubjects();
            }
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found");
        }
    }

    @PutMapping
    public void putSubject(@RequestParam Long id, @RequestParam String newName) {
        subjectService.updateSubjectById(id, newName);
    }
}
