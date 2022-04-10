package com.deanery.controller;

import com.deanery.entity.Person;
import com.deanery.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/deanery-api/people")
public class PersonController {

    PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping(path = "/all")
    @ResponseBody
    public ResponseEntity<List<Person>> getPeople() {
        return new ResponseEntity<>(personService.readPeople(), HttpStatus.OK);
    }

    @GetMapping(path = "/all-ordered")
    @ResponseBody
    public ResponseEntity<List<Person>> getPeopleOrdered(@RequestParam(required = false) Optional<?> asc) {
        return asc.isPresent() ?
                new ResponseEntity<>(personService.readPeopleOrdered(true), HttpStatus.OK) :
                new ResponseEntity<>(personService.readPeopleOrdered(false), HttpStatus.OK);
    }

    @GetMapping(path = "/by-subject")
    @ResponseBody
    public ResponseEntity<List<Person>> getPeopleBySubject(@RequestParam String type, @RequestParam Long id) {
        return switch (type) {
            case "students" -> new ResponseEntity<>(personService.readStudentsBySubject(id), HttpStatus.OK);
            case "teachers" -> new ResponseEntity<>(personService.readTeachersBySubject(id), HttpStatus.OK);
            default -> new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        };
    }

    @GetMapping(path = "/by-teacher")
    @ResponseBody
    public ResponseEntity<List<Person>> getStudentsByTeacher(@RequestParam Long id) {
        try {
            return new ResponseEntity<>(personService.readStudentsByTeacher(id), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Not a teacher");
        }
    }

    @GetMapping(path = "/additional-session")
    @ResponseBody
    public ResponseEntity<List<Person>> getAdditionalSessionStudents() {
        return new ResponseEntity<>(personService.readAdditionalSessionStudents(), HttpStatus.OK);
    }

    @GetMapping(path = "/by-type")
    @ResponseBody
    public ResponseEntity<List<Person>> getPeopleByType(@RequestParam String type) {
        return switch (type) {
            case "students" -> new ResponseEntity<>(personService.readStudents(), HttpStatus.OK);
            case "teachers" -> new ResponseEntity<>(personService.readTeachers(), HttpStatus.OK);
            default -> new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        };
    }

    @GetMapping(path = "/by-groups")
    @ResponseBody
    public ResponseEntity<List<Person>> getPeopleByGroups() {
        return new ResponseEntity<>(personService.readPeopleByGroups(), HttpStatus.OK);
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<Person> getPerson(@RequestParam Long id) {
        try {
            return new ResponseEntity<>(personService.readPersonById(id), HttpStatus.OK);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found");
        }
    }

    @PostMapping
    public ResponseEntity<?> postPerson(@RequestBody Person person) {
        try {
            if (person.getGroupId() == null) {
                personService.createPerson(person.getFirstName(), person.getLastName(), person.getPatherName(),
                        null, person.getType());
            } else {
                personService.createPerson(person.getFirstName(), person.getLastName(), person.getPatherName(),
                        person.getGroupId().getId(), person.getType());
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid person data");
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deletePeople(@RequestParam(required = false) Optional<Long> id) {
        try {
            if (id.isPresent()) {
                personService.deletePersonById(id.get());
            } else {
                personService.deleteAllPeople();
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found");
        }
    }

    @DeleteMapping(path = "/by-group")
    public ResponseEntity<?> deletePeopleByGroup(@RequestParam Long id) {
        try {
            personService.deletePeopleByGroup(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found");
        }
    }

    @PutMapping
    public ResponseEntity<?> putPerson(@RequestBody Person newPerson) {
        try {
            personService.updatePersonById(newPerson.getId(), newPerson.getFirstName(), newPerson.getLastName(),
                    newPerson.getPatherName(), newPerson.getGroupId().getId(), newPerson.getType());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_MODIFIED, "Invalid person data");
        }
    }
}
