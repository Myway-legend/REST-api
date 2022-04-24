package com.deanery.controller;

import com.deanery.entity.Group;
import com.deanery.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/deanery-api/data/groups")
public class GroupController {
    
    GroupService groupService;
    
    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping(path = "/all")
    @ResponseBody
    public ResponseEntity<List<Group>> getGroups(@RequestParam(required = false) Optional<String> prefix) {
        return prefix.isPresent() ?
                new ResponseEntity<>(groupService.readGroupsStartsWith(prefix.get()), HttpStatus.OK) :
                new ResponseEntity<>(groupService.readGroups(), HttpStatus.OK);
    }

    @GetMapping(path = "/all-ordered")
    public ResponseEntity<List<Group>> getGroupsOrdered(@RequestParam(required = false) Optional<?> asc) {
        return asc.isPresent() ?
                new ResponseEntity<>(groupService.readGroupsOrdered(true), HttpStatus.OK) :
                new ResponseEntity<>(groupService.readGroupsOrdered(false), HttpStatus.OK);
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<Group> getGroup(@RequestParam Long id) {
        try {
            return new ResponseEntity<>(groupService.readGroupById(id), HttpStatus.OK);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found");
        }
    }

    @PostMapping
    public ResponseEntity<?> postGroup(@RequestBody Group group) {
        try {
            groupService.createGroup(group.getName());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.FOUND, "Group already exists");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid group name");
        }
    }

    @DeleteMapping(path = "/all")
    public ResponseEntity<?> deleteGroups(@RequestParam(required = false) Optional<String> prefix) {
        if (prefix.isPresent()) {
            groupService.deleteGroupsStartsWith(prefix.get());
        } else {
            groupService.deleteAllGroups();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteGroup(@RequestParam Long id) {
        try {
            groupService.deleteGroupById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found");
        }
    }

    @PutMapping
    public ResponseEntity<?> putGroup(@RequestBody Group newGroup) {
        try {
            groupService.updateGroupById(newGroup.getId(), newGroup.getName());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_MODIFIED, "Invalid group name");
        }
    }
}
