package com.deanery.web.view;

import com.deanery.entity.Group;
import com.deanery.security.jwt.JwtTokenProvider;
import com.deanery.service.GroupService;
import com.deanery.web.component.GroupEditor;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

@Route("/client/groups")
public class GroupView extends VerticalLayout {

    private final GroupService groupService;
    private final JwtTokenProvider jwtTokenProvider;

    private final Grid<Group> grid = new Grid<>(Group.class);
    private final TextField filter = new TextField("", "Type to filter...");
    private final TextField utility = new TextField("", "Utility field...");
    private final TextField token = new TextField("", "Type your token...");
    private final Button addNewBtn = new Button("Add new");
    private final Button deleteAllBtn = new Button("Delete all");
    private final Button deleteStartsWithBtn = new Button("Delete starts with");
    private final HorizontalLayout toolbar =
            new HorizontalLayout(filter, utility, token, addNewBtn, deleteAllBtn, deleteStartsWithBtn);
    private final GroupEditor editor;

    @Autowired
    public GroupView(GroupService GroupService, GroupEditor editor, JwtTokenProvider jwtTokenProvider) {
        this.groupService = GroupService;
        this.editor = editor;
        this.jwtTokenProvider = jwtTokenProvider;

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> showGroup(e.getValue()));

        add(toolbar, grid, editor);

        grid.asSingleSelect().addValueChangeListener(e -> {
            if (jwtTokenProvider.checkToken(token.getValue())) {
                editor.editGroup(e.getValue());
            }
        });

        addNewBtn.addClickListener(e -> {
            if (jwtTokenProvider.checkToken(token.getValue())) {
                editor.editGroup(new Group());
            }
        });
        deleteAllBtn.addClickListener(e -> {
            if (jwtTokenProvider.checkToken(token.getValue())) {
                groupService.deleteAllGroups();
                grid.setItems(Collections.emptyList());
            }
        });
        deleteStartsWithBtn.addClickListener(e -> {
            if (jwtTokenProvider.checkToken(token.getValue())) {
                groupService.deleteGroupsStartsWith(utility.getValue());
                grid.setItems(groupService.readGroups());
            }
        });

        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            showGroup(filter.getValue());
        });


        showGroup("");
    }

    private void showGroup(String name) {
        if (name.isEmpty()) {
            grid.setItems(groupService.readGroups());
        } else {
            try {
                grid.setItems(groupService.readGroupsStartsWith(name));
            } catch (IllegalStateException e) {
                grid.setItems(Collections.emptyList());
            }
        }
    }
}
