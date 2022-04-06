package com.deanery.controller;

import com.deanery.entity.Group;
import com.deanery.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/deanery-api/groups")
public class GroupController {
    
    GroupService groupService;
    
    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping(path = "/all")
    @ResponseBody
    public List<Group> getGroups(@RequestParam(required = false) Optional<String> prefix) {
        return prefix.isPresent() ? groupService.readGroupsStartsWith(prefix.get()) : groupService.readGroups();
    }

    @GetMapping
    @ResponseBody
    public Group getGroup(@RequestParam Long id) {
        return groupService.readGroupById(id);
    }

    @PostMapping
    public void postGroup(@RequestBody Group group) {
        groupService.createGroup(group.getName());
    }

    @DeleteMapping(path = "/all")
    public void deleteGroups(@RequestParam(required = false) Optional<String> prefix) {
        if (prefix.isPresent()) {
            groupService.deleteGroupsStartsWith(prefix.get());
        } else {
            groupService.deleteAllGroups();
        }
    }

    @DeleteMapping
    public void deleteGroup(@RequestParam Long id) {
        groupService.deleteGroupById(id);
    }

    @PutMapping
    public void putGroup(@RequestParam Long id, @RequestParam String newName) {
        groupService.updateGroupById(id, newName);
    }
}
