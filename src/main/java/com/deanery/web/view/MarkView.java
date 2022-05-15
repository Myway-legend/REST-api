package com.deanery.web.view;

import com.deanery.entity.Mark;
import com.deanery.security.jwt.JwtTokenProvider;
import com.deanery.service.MarkService;
import com.deanery.web.component.MarkEditor;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

@Route("client/marks")
public class MarkView extends VerticalLayout {
    private final MarkService markService;
    private final JwtTokenProvider jwtTokenProvider;

    private final Grid<Mark> grid = new Grid<>(Mark.class);
    private final TextField filter = new TextField("", "Type to filter...");
    private final TextField utility = new TextField("", "Utility field...");
    private final TextField token = new TextField("", "Type your token...");
    private final Button addNewBtn = new Button("Add new");
    private final Button deleteAllBtn = new Button("Delete all");
    private final Button deleteByStudentBtn = new Button("By studentId");
    private final Button deleteBySubjectBtn = new Button("By subjectId");
    private final Button deleteByTeacherBtn = new Button("By teacherId");
    private final HorizontalLayout toolbar =
            new HorizontalLayout(filter, utility, token,
                    addNewBtn, deleteAllBtn, deleteByStudentBtn,
                    deleteBySubjectBtn, deleteByTeacherBtn
            );
    private final MarkEditor editor;

    @Autowired
    public MarkView(MarkService markService, MarkEditor editor, JwtTokenProvider jwtTokenProvider) {
        this.markService = markService;
        this.editor = editor;
        this.jwtTokenProvider = jwtTokenProvider;

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> showMark(e.getValue()));

        add(toolbar, grid, editor);

        grid.asSingleSelect().addValueChangeListener(e -> {
            if (jwtTokenProvider.checkToken(token.getValue())) {
                editor.editMark(e.getValue());
            }
        });
        addNewBtn.addClickListener(e -> {
            if (jwtTokenProvider.checkToken(token.getValue())) {
                editor.editMark(new Mark());
            }
        });

        deleteAllBtn.addClickListener(e -> {
            if (jwtTokenProvider.checkToken(token.getValue())) {
                markService.deleteAllMarks();
                grid.setItems(markService.readMarks());
            }
        });
        deleteByStudentBtn.addClickListener(e -> {
            if (jwtTokenProvider.checkToken(token.getValue())) {
                markService.deleteMarksByStudentId(Long.parseLong(utility.getValue()));
                grid.setItems(markService.readMarks());
            }
        });
        deleteBySubjectBtn.addClickListener(e -> {
            if (jwtTokenProvider.checkToken(token.getValue())) {
                markService.deleteMarksBySubjectId(Long.parseLong(utility.getValue()));
                grid.setItems(markService.readMarks());
            }
        });
        deleteByTeacherBtn.addClickListener(e -> {
            if (jwtTokenProvider.checkToken(token.getValue())) {
                markService.deleteMarksByTeacherId(Long.parseLong(utility.getValue()));
                grid.setItems(markService.readMarks());
            }
        });
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            showMark(filter.getValue());
        });


        showMark("");
    }

    private void showMark(String studentId) {
        if (studentId.isEmpty()) {
            grid.setItems(markService.readMarks());
        } else {
            try {
                grid.setItems(markService.readMarksByStudentId(Long.parseLong(studentId)));
            } catch (IllegalStateException e) {
                grid.setItems(Collections.emptyList());
            }
        }
    }
}
