package com.deanery.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name = "GROUPS")
@Table(name = "groups",
        uniqueConstraints = {
        @UniqueConstraint(name = "group_name_unique",
                            columnNames = "NAME")
        })
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
                    generator = "group_sequence")
    @Column(name = "ID",
            nullable = false,
            updatable = false)
    private Long id;

    @Column(name = "NAME",
            nullable = false)
    private String name;

    public Group(String name) {
        this.name = name;
    }
}
