package com.deanery.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name = "SUBJECTS")
@Table(name = "subjects",
       uniqueConstraints = {
       @UniqueConstraint(name = "subject_name_unique",
                         columnNames = "NAME") })
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
                    generator = "subject_sequence")
    @Column(name = "ID",
            nullable = false,
            unique = true)
    private Long id;

    @Column(name = "NAME",
            nullable = false)
    private String name;

    public Subject(String name) {
        this.name = name;
    }
}
