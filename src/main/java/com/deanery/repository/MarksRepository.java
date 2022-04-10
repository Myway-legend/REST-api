package com.deanery.repository;

import com.deanery.entity.Mark;
import com.deanery.entity.Person;
import com.deanery.entity.Subject;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MarksRepository extends CrudRepository<Mark, Long> {

    @Query("SELECT (COUNT(m) > 0) FROM MARKS m WHERE m.studentId = ?1 AND m.value = ?2")
    boolean existsByStudentIdAndValue(Person studentId, Integer value);

    @Query("SELECT (COUNT(m) > 0) FROM MARKS m WHERE m.studentId = ?1 AND m.subjectId = ?2")
    boolean existsByStudentIdAndSubjectId(Person studentId, Subject subjectId);

    @Query("SELECT m FROM MARKS m ORDER BY m.value")
    List<Mark> orderByValue();

    @Query("SELECT m FROM MARKS m ORDER BY m.value DESC")
    List<Mark> orderByValueDesc();

    @Query("SELECT m FROM MARKS m WHERE m.studentId = ?1")
    List<Mark> findMarksByStudentId(Person studentId);

    @Query("SELECT m FROM MARKS m WHERE m.studentId = ?1")
    List<Mark> findAllByStudentId(Person studentId);

    @Transactional
    @Modifying
    @Query("DELETE FROM MARKS m WHERE m.studentId = ?1")
    void deleteMarksByStudentId(Person studentId);

    @Transactional
    @Modifying
    @Query("DELETE FROM MARKS m WHERE m.teacherId = ?1")
    void deleteMarksByTeacherId(Person teacherId);

    @Transactional
    @Modifying
    @Query("DELETE FROM MARKS m WHERE m.subjectId = ?1")
    void deleteAllBySubjectId(Subject subjectId);
}
