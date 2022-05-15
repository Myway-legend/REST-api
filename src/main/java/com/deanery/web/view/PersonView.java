package com.deanery.web.view;

import com.deanery.entity.Person;
import com.deanery.security.jwt.JwtTokenProvider;
import com.deanery.service.PersonService;
import com.deanery.web.component.PersonEditor;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Route("client/people")
public class PersonView extends VerticalLayout {
    private final PersonService personService;
    private final JwtTokenProvider jwtTokenProvider;

    private final Grid<Person> grid = new Grid<>(Person.class);
    private final TextField filter = new TextField("", "Type to filter...");
    private final TextField utility = new TextField("", "Utility field...");
    private final TextField token = new TextField("", "Type your token...");
    private final Button addNewBtn = new Button("Add new");
    private final Button deleteAllBtn = new Button("Delete all");
    private final Button studentsBySubjectBtn = new Button("Students by subjectId");
    private final Button studentsByTeacherBtn = new Button("Students by teacherId");
    private final Button teachersBySubjectBtn = new Button("Teachers by subjectId");
    private final Button dopsBtn = new Button("Additional session");
    private final Button teachersBtn = new Button("Teachers");
    private final Button studentsBtn = new Button("Students");
    private final Button peopleByGroupsBtn = new Button("People by groups");
    private final Button deleteByGroupBtn = new Button("Delete by groupId");
    private final HorizontalLayout toolbar1 =
            new HorizontalLayout(
                    filter, utility, token,
                    addNewBtn, deleteAllBtn, studentsBySubjectBtn
            );

    private final HorizontalLayout toolbar2 =
            new HorizontalLayout(
                    studentsByTeacherBtn, teachersBySubjectBtn,
                    dopsBtn, teachersBtn, studentsBtn,
                    peopleByGroupsBtn, deleteByGroupBtn
            );
    private final PersonEditor editor;

    @Autowired
    public PersonView(PersonService personService, PersonEditor editor, JwtTokenProvider jwtTokenProvider) {
        this.personService = personService;
        this.editor = editor;
        this.jwtTokenProvider = jwtTokenProvider;

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> showPerson(e.getValue()));

        List<Grid.Column<Person>> order = new ArrayList<>();
        order.add(grid.getColumnByKey("id"));
        order.add(grid.getColumnByKey("firstName"));
        order.add(grid.getColumnByKey("lastName"));
        order.add(grid.getColumnByKey("patherName"));
        order.add(grid.getColumnByKey("groupId"));
        order.add(grid.getColumnByKey("type"));

        grid.setColumnOrder(order);
        add(toolbar1, toolbar2, grid, editor);

        grid.asSingleSelect().addValueChangeListener(e -> {
            if (jwtTokenProvider.checkToken(token.getValue())) {
                editor.editPerson(e.getValue());
            }
        });
        addNewBtn.addClickListener(e -> {
            if (jwtTokenProvider.checkToken(token.getValue())) {
                editor.editPerson(new Person());
            }
        });

        deleteAllBtn.addClickListener(e -> {
            if (jwtTokenProvider.checkToken(token.getValue())) {
                personService.deleteAllPeople();
                grid.setItems(personService.readPeople());
            }
        });
        deleteByGroupBtn.addClickListener(e -> {
            if (jwtTokenProvider.checkToken(token.getValue())) {
                personService.deletePeopleByGroup(Long.parseLong(utility.getValue()));
                grid.setItems(personService.readPeople());
            }
        });
        studentsBySubjectBtn.addClickListener(e -> {
            try {
                grid.setItems(personService.readStudentsBySubject(Long.parseLong(utility.getValue())));
            } catch (Exception ex) {
                grid.setItems(Collections.emptyList());
            }
        });
        studentsByTeacherBtn.addClickListener(e -> {
            try {
                grid.setItems(personService.readStudentsByTeacher(Long.parseLong(utility.getValue())));
            } catch (Exception ex) {
                grid.setItems(Collections.emptyList());
            }
        });
        teachersBySubjectBtn.addClickListener(e -> {
            try {
                grid.setItems(personService.readTeachersBySubject(Long.parseLong(utility.getValue())));
            } catch (Exception ex) {
                grid.setItems(Collections.emptyList());
            }
        });
        dopsBtn.addClickListener(e -> {
            grid.setItems(personService.readAdditionalSessionStudents());
        });
        teachersBtn.addClickListener(e -> {
            grid.setItems(personService.readTeachers());
        });
        studentsBtn.addClickListener(e -> {
            grid.setItems(personService.readStudents());
        });
        peopleByGroupsBtn.addClickListener(e -> {
            grid.setItems(personService.readPeopleByGroups());
        });

        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            showPerson(filter.getValue());
        });


        showPerson("");
    }

    private void showPerson(String prefix) {
        if (prefix.isEmpty()) {
            grid.setItems(personService.readPeople());
        } else {
            try {
                grid.setItems(personService.readPeopleLastNameStartsWith(prefix));
            } catch (IllegalStateException e) {
                grid.setItems(Collections.emptyList());
            }
        }
    }
}
