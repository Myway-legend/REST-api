package com.deanery.controller;

import com.deanery.entity.Subject;
import com.deanery.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public List<Subject> getSubjects(@RequestParam(required = false) Optional<String> prefix) {

        return prefix.isPresent() ? subjectService.readSubjectsStartsWith(prefix.get()) : subjectService.readSubjects();
    }

    @GetMapping
    @ResponseBody
    public Subject getSubject(@RequestParam Long id) {
        return subjectService.readSubjectById(id);
    }

    @PostMapping
    public void postSubject(@RequestBody Subject subject) {
        subjectService.createSubject(subject.getName());
    }

    @DeleteMapping
    public void deleteSubject(@RequestParam(required = false) Optional<Long> id) {
        if (id.isPresent()) {
            subjectService.deleteSubjectById(id.get());
        } else {
            subjectService.deleteAllSubjects();
        }
    }

    @PutMapping
    public void putSubject(@RequestParam Long id, @RequestParam String newName) {
        subjectService.updateSubjectById(id, newName);
    }
}
