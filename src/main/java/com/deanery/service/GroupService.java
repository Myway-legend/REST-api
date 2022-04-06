package com.deanery.service;

import com.deanery.entity.Group;

import java.util.List;

public interface GroupService {
    // READ
    List<Group> readGroups();
    List<Group> readGroupsStartsWith(String prefix);
    List<Group> readGroupsOrdered(Boolean asc);
    Group readGroupById(Long id) throws IllegalStateException;

    // CREATE
    void createGroup(String name) throws IllegalStateException, IllegalArgumentException;

    // DELETE
    void deleteAllGroups();
    void deleteGroupsStartsWith(String prefix);
    void deleteGroupById(Long id) throws IllegalStateException;

    // UPDATE
    void updateGroupById(Long id, String name) throws IllegalStateException, IllegalArgumentException;
}
