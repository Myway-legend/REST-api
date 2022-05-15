package com.deanery.web.component;

import com.deanery.entity.Group;
import com.deanery.repository.GroupsRepository;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class GroupEditor extends VerticalLayout implements KeyNotifier {

    private final GroupsRepository groupsRepository;
    private Group group;

    Binder<Group> binder = new Binder<>(Group.class);
    TextField name = new TextField("Name");
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    @Setter
    private GroupEditor.ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }

    @Autowired
    public GroupEditor(GroupsRepository groupsRepository) {
        this.groupsRepository = groupsRepository;
        add(name, actions);
        binder.bindInstanceFields(this);
        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> setVisible(false));
        setVisible(false);
    }

    private void delete() {
        groupsRepository.delete(group);
        changeHandler.onChange();
    }

    private void save() {
        if (isNameValid(group.getName())) {
            groupsRepository.save(group);
        }
        changeHandler.onChange();
    }

    public void editGroup(Group Group) {
        if (Group == null) {
            setVisible(false);
            return;
        }

        if (Group.getId() != null) {
            this.group = groupsRepository.findById(group.getId()).orElse(group);
        } else {
            this.group = Group;
        }
        binder.setBean(this.group);
        setVisible(true);
    }

    static boolean isNameValid(String name) {
        return name.matches("\\d{7}/\\d{5}");
    }
}
