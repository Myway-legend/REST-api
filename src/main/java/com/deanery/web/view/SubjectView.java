package com.deanery.web.view;

import com.deanery.entity.Subject;
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

@Route
public class SubjectView extends VerticalLayout {

    private final SubjectService subjectService;

    private Grid<Subject> grid = new Grid<>(Subject.class);
    private final TextField filter = new TextField("", "Type to filter");
    private final Button addNewBtn = new Button("Add new");
    private final HorizontalLayout toolbar = new HorizontalLayout(filter, addNewBtn);
    private final SubjectEditor editor;

    @Autowired
    public SubjectView(SubjectService subjectService, SubjectEditor editor) {
        this.subjectService = subjectService;
        this.editor = editor;
        add(toolbar, grid, editor);

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> showSubject(e.getValue()));

        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editSubject(e.getValue());
        });

        addNewBtn.addClickListener(e -> editor.editSubject(new Subject()));

        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            showSubject(filter.getValue());
        });

        grid.setItems(subjectService.readSubjects());
    }

    private void showSubject(String name) {
        if (name.isEmpty()) {
            grid.setItems(subjectService.readSubjects());
        } else {
            grid.setItems(subjectService.readSubjectByName(name));
        }
    }
}
