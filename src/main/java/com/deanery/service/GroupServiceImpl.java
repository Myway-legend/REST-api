package com.deanery.service;

import com.deanery.entity.Group;
import com.deanery.repository.GroupsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService{

    private final GroupsRepository groupsRepository;

    @Autowired
    @Lazy
    public GroupServiceImpl(GroupsRepository groupsRepository) {
        this.groupsRepository = groupsRepository;
    }

    @Override
    public List<Group> readGroups() {
        return (List<Group>) groupsRepository.findAll();
    }

    @Override
    public List<Group> readGroupsStartsWith(String prefix) {
        return groupsRepository.findByNameStartsWith(prefix);
    }

    @Override
    public List<Group> readGroupsOrdered(Boolean asc) {
        return asc ? groupsRepository.orderByName() : groupsRepository.orderByNameDesc();
    }

    @Override
    public Group readGroupById(Long id) throws IllegalStateException {
        return groupsRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Invalid group ID"));
    }

    @Override
    public void createGroup(String name) throws IllegalStateException, IllegalArgumentException {
        if (groupsRepository.existsByName(name)) {
            throw new IllegalStateException("Group already exists");
        }
        if (isNameInvalid(name)) {
            throw new IllegalArgumentException("Invalid group name");
        }
        groupsRepository.save(new Group(name));
    }

    @Override
    public void deleteAllGroups() {
        groupsRepository.deleteAll();
    }

    @Override
    public void deleteGroupsStartsWith(String prefix) {
        groupsRepository.deleteAll(groupsRepository.findByNameStartsWith(prefix));
    }

    @Override
    public void deleteGroupById(Long id) throws IllegalStateException {
        groupsRepository.delete(groupsRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Invalid group ID")));
    }

    @Override
    public void updateGroupById(Long id, String newName) throws IllegalStateException, IllegalArgumentException {
        if (groupsRepository.existsByName(newName)) {
            throw new IllegalStateException("Group already exists");
        }
        if (isNameInvalid(newName)) {
            throw new IllegalArgumentException("Invalid group name");
        }
        groupsRepository.updateNameById(id, newName);
    }

    static boolean isNameInvalid(String name) {
        return !name.matches("\\d{7}/\\d{5}");
    }

}
