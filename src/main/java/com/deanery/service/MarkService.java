package com.deanery.service;

import com.deanery.entity.Mark;

import java.util.List;

public interface MarkService {

    // READ
    List<Mark> readMarks();
    List<Mark> readMarksOrdered(Boolean asc);
    List<Mark> readMarksByStudentId(Long studentId) throws IllegalStateException;
    Mark readMarkById(Long id) throws IllegalStateException;

    // CREATE
    void createMark(Long studentId, Long subjectId, Long teacherId, Integer value)
            throws IllegalStateException, IllegalArgumentException;

    // DELETE
    void deleteAllMarks();
    void deleteMarksByStudentId(Long studentId) throws IllegalStateException, IllegalArgumentException;
    void deleteMarksByTeacherId(Long teacherId) throws IllegalStateException, IllegalArgumentException;
    void deleteMarksBySubjectId(Long subjectId) throws IllegalStateException;
    void deleteMarkById(Long id) throws IllegalStateException;

    // UPDATE
    void updateMarkById(Long id, Long studentId, Long subjectId, Long teacherId, Integer value)
            throws IllegalStateException, IllegalArgumentException;

}
