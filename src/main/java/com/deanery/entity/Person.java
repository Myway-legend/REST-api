package com.deanery.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name = "PEOPLE")
@Table(name = "people")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
                    generator = "person_sequence")
    @Column(name = "ID",
            nullable = false,
            unique = true)
    private Long id;

    @Column(name = "FIRST_NAME",
            nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME",
            nullable = false)
    private String lastName;

    @Column(name = "PATHER_NAME",
            nullable = false)
    private String patherName;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH },
               targetEntity = Group.class)
    @JoinColumn(name = "GROUP_ID",
                foreignKey = @ForeignKey(name = "fk_people_groups"))
    private Group groupId;

    @Column(name = "TYPE",
            nullable = false)
    private String type;

    public Person(String firstName, String lastName, String patherName, Group groupId, String type) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patherName = patherName;
        this.groupId = groupId;
        this.type = type;
    }
}
