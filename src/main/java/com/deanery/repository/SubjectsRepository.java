package com.deanery.repository;

import com.deanery.entity.Subject;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface SubjectsRepository extends CrudRepository<Subject, Long> {

    @Query("SELECT (COUNT(s) > 0) FROM SUBJECTS s WHERE s.name = ?1")
    boolean existsByName(String name);

    @Query("SELECT s FROM SUBJECTS s WHERE s.name = ?1")
    Optional<Subject> findByName(String name);

    @Query("SELECT s FROM SUBJECTS s WHERE s.name LIKE CONCAT(?1, '%')")
    List<Subject> findByNameStartsWith(String prefix);

    @Query("SELECT DISTINCT s FROM SUBJECTS s INNER JOIN MARKS m ON m.subjectId.id = s.id WHERE m.teacherId.id = ?1")
    List<Subject> findByTeacher(Long teacherId);

    @Query("SELECT s FROM SUBJECTS s ORDER BY s.name")
    List<Subject> orderByName();

    @Query("SELECT s FROM SUBJECTS s ORDER BY s.name DESC")
    List<Subject> orderByNameDesc();

    @Transactional
    @Modifying
    @Query("UPDATE SUBJECTS s SET s.name = ?2 WHERE s.id = ?1")
    void updateNameById(Long id, String newName);

}
