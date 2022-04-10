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
    public ResponseEntity<List<Subject>> getSubjectsOrdered(@RequestParam(required = false) Optional<?> asc) {
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
    public ResponseEntity<?> postSubject(@RequestBody Subject subject) {
        try {
            subjectService.createSubject(subject.getName());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.FOUND, "Subject already exists");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid subject name");
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteSubjects(@RequestParam(required = false) Optional<Long> id) {
        try {
            if (id.isPresent()) {
                subjectService.deleteSubjectById(id.get());
            } else {
                subjectService.deleteAllSubjects();
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found");
        }
    }

    @PutMapping
    public ResponseEntity<?> putSubject(@RequestBody Subject newSubject) {
        try {
            subjectService.updateSubjectById(newSubject.getId(), newSubject.getName());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_MODIFIED, "Invalid subject name");
        }
    }
}
