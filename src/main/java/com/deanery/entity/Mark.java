package com.deanery.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name = "MARKS")
@Table(name = "marks")
public class Mark {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
                    generator = "mark_sequence")
    @Column(name = "ID",
            nullable = false,
            updatable = false,
            unique = true)
    private Long id;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH },
               targetEntity = Person.class)
    @JoinColumn(name = "STUDENT_ID",
                foreignKey = @ForeignKey(name = "fk_marks_people1"))
    private Person studentId;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH },
               targetEntity = Subject.class)
    @JoinColumn(name = "SUBJECT_ID",
                foreignKey = @ForeignKey(name = "fk_marks_subjects"))
    private Subject subjectId;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH },
               targetEntity = Person.class)
    @JoinColumn(name = "TEACHER_ID",
                foreignKey = @ForeignKey(name = "fk_marks_people2"))
    private Person teacherId;

    @Column(name = "VALUE",
            nullable = false)
    private Integer value;

    public Mark(Person studentId, Subject subjectId, Person teacherId, Integer value) {
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.teacherId = teacherId;
        this.value = value;
    }
}
