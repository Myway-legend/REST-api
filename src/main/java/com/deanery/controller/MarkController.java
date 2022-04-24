package com.deanery.controller;

import com.deanery.entity.Mark;
import com.deanery.service.MarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/deanery-api/data/marks")
public class MarkController {

    MarkService markService;

    @Autowired
    public MarkController(MarkService markService) {
        this.markService = markService;
    }

    @GetMapping(path = "/all")
    @ResponseBody
    public ResponseEntity<List<Mark>> getMarks() {
        return new ResponseEntity<>(markService.readMarks(), HttpStatus.OK);
    }

    @GetMapping(path = "/all-ordered")
    @ResponseBody
    public ResponseEntity<List<Mark>> getMarksOrdered(@RequestParam(required = false) Optional<?> asc) {
        return asc.isPresent() ?
                new ResponseEntity<>(markService.readMarksOrdered(true), HttpStatus.OK) :
                new ResponseEntity<>(markService.readMarksOrdered(false), HttpStatus.OK);
    }

    @GetMapping(path = "/by-student")
    @ResponseBody
    public ResponseEntity<List<Mark>> getMarksByStudent(@RequestParam Long id) {
        try {
            return new ResponseEntity<>(markService.readMarksByStudentId(id), HttpStatus.OK);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
        }
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<Mark> getMark(@RequestParam Long id) {
        try {
            return new ResponseEntity<>(markService.readMarkById(id), HttpStatus.OK);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mark not found");
        }
    }

    @PostMapping
    public ResponseEntity<?> postMark(@RequestBody Mark mark) {
        try {
            markService.createMark(mark.getStudentId().getId(), mark.getSubjectId().getId(),
                    mark.getTeacherId().getId(), mark.getValue());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid mark data");
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteMarks(@RequestParam(required = false) Optional<Long> id) {
        try {
            if (id.isPresent()) {
                markService.deleteMarkById(id.get());
            } else {
                markService.deleteAllMarks();
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mark not found");
        }
    }

    @DeleteMapping("/by")
    public ResponseEntity<?> deleteMarksBy(@RequestParam String type,  @RequestParam Long id) {
        try {
            switch (type) {
                case "student" -> markService.deleteMarksByStudentId(id);
                case "teacher" -> markService.deleteMarksByTeacherId(id);
                case "subject" -> markService.deleteMarksBySubjectId(id);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person/subject not found");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid person");
        }
    }

    @PutMapping
    public ResponseEntity<?> putMark(@RequestBody Mark newMark) {
        try {
            markService.updateMarkById(newMark.getId(), newMark.getStudentId().getId(), newMark.getSubjectId().getId(),
                    newMark.getTeacherId().getId(), newMark.getValue());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mark not found");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_MODIFIED, "Invalid mark data");
        }
    }

}
