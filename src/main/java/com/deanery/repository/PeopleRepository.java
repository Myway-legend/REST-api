package com.deanery.repository;

import com.deanery.entity.Group;
import com.deanery.entity.Person;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PeopleRepository extends CrudRepository<Person, Long> {

    @Query("SELECT p FROM PEOPLE p WHERE p.type = P")
    List<Person> findTeachers();

    @Query("SELECT p FROM PEOPLE p WHERE p.type = S")
    List<Person> findStudents();

    @Query("SELECT DISTINCT p FROM PEOPLE p INNER JOIN MARKS m ON m.studentId.id = p.id AND m.subjectId.id = ?1")
    List<Person> findStudentsBySubject(Long subjectId);

    @Query("SELECT DISTINCT p FROM PEOPLE p INNER JOIN MARKS m ON m.teacherId.id = p.id AND m.subjectId.id = ?1")
    List<Person> findTeachersBySubject(Long subjectId);

    @Query("SELECT DISTINCT p FROM PEOPLE p INNER JOIN MARKS m ON m.studentId.id = p.id WHERE m.value = 2")
    List<Person> findAdditionalSessionStudents();

    @Query("SELECT DISTINCT p FROM PEOPLE p INNER JOIN MARKS m ON m.studentId.id = p.id WHERE m.teacherId.id = ?1")
    List<Person> findByTeacher(Long teacherId);

    @Query("SELECT p FROM PEOPLE p LEFT JOIN GROUPS g ON p.groupId.id = g.id")
    List<Person> findPeopleByGroups();

    @Query("SELECT p FROM PEOPLE p ORDER BY p.firstName, p.lastName, p.patherName")
    List<Person> orderByName();

    @Query("SELECT p FROM PEOPLE p ORDER BY p.firstName DESC, p.lastName DESC, p.patherName DESC")
    List<Person> orderByNameDesc();

    @Transactional
    @Modifying
    @Query("DELETE FROM PEOPLE p WHERE p.groupId = ?1")
    void deleteByGroupId(Group groupId);
}
