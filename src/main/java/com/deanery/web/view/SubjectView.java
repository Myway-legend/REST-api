package com.deanery.web.view;

import com.deanery.entity.Subject;
import com.deanery.security.jwt.JwtTokenProvider;
import com.deanery.service.SubjectService;
import com.deanery.web.component.SubjectEditor;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

@Route("/client/subjects")
public class SubjectView extends VerticalLayout {

    private final SubjectService subjectService;
    private final JwtTokenProvider jwtTokenProvider;

    private final Grid<Subject> grid = new Grid<>(Subject.class);
    private final TextField filter = new TextField("", "Type to filter...");
    private final TextField utility = new TextField("", "Utility field...");
    private final TextField token = new TextField("", "Type your token...");
    private final Button addNewBtn = new Button("Add new");
    private final Button deleteAllBtn = new Button("Delete all");
    private final Button byTeacherBtn = new Button("By teacherId");
    private final HorizontalLayout toolbar =
            new HorizontalLayout(filter, utility, token, addNewBtn, deleteAllBtn, byTeacherBtn);
    private final SubjectEditor editor;

    @Autowired
    public SubjectView(SubjectService subjectService, SubjectEditor editor, JwtTokenProvider jwtTokenProvider) {
        this.subjectService = subjectService;
        this.editor = editor;
        this.jwtTokenProvider = jwtTokenProvider;

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> showSubject(e.getValue()));

        add(toolbar, grid, editor);

        grid.asSingleSelect().addValueChangeListener(e -> {
            if (jwtTokenProvider.checkToken(token.getValue())) {
                editor.editSubject(e.getValue());
            }
        });

        addNewBtn.addClickListener(e -> {
            if (jwtTokenProvider.checkToken(token.getValue())) {
                editor.editSubject(new Subject());
            }
        });
        byTeacherBtn.addClickListener(e -> {
            grid.setItems(subjectService.readSubjectsByTeacher(Long.parseLong(utility.getValue())));
        });
        deleteAllBtn.addClickListener(e -> {
            if (jwtTokenProvider.checkToken(token.getValue())) {
                subjectService.deleteAllSubjects();
                grid.setItems(subjectService.readSubjects());
            }
        });

        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            showSubject(filter.getValue());
        });


        showSubject("");
    }

    private void showSubject(String name) {
        if (name.isEmpty()) {
            grid.setItems(subjectService.readSubjects());
        } else {
            try {
                grid.setItems(subjectService.readSubjectsStartsWith(name));
            } catch (IllegalStateException e) {
                grid.setItems(Collections.emptyList());
            }
        }
    }
}
