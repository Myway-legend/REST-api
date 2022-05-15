package com.deanery.repository;

import com.deanery.entity.Group;
import com.deanery.entity.Person;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface GroupsRepository extends CrudRepository<Group, Long> {

    @Query("SELECT (COUNT(g) > 0) FROM GROUPS g WHERE g.name = ?1")
    boolean existsByName(String name);
    @Query("SELECT g FROM GROUPS g WHERE g.name = ?1")
    Optional<Group> findByName(String name);

    @Query("SELECT g FROM GROUPS g WHERE g.name LIKE CONCAT(?1, '%')")
    List<Group> findByNameStartsWith(String prefix);

    @Query("SELECT g FROM GROUPS g ORDER BY g.name")
    List<Group> orderByName();

    @Query("SELECT g FROM GROUPS g ORDER BY g.name DESC")
    List<Group> orderByNameDesc();

    @Transactional
    @Modifying
    @Query("UPDATE GROUPS g SET g.name = ?2 WHERE g.id = ?1")
    void updateNameById(Long id, String newName);
}
