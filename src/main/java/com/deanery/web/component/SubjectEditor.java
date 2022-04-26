package com.deanery.web.component;

import com.deanery.entity.Subject;
import com.deanery.service.SubjectService;
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
public class SubjectEditor extends VerticalLayout implements KeyNotifier {

    private final SubjectService subjectService;

    private Subject subject;

    Binder<Subject> binder = new Binder<>(Subject.class);

    TextField name = new TextField("Name");
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    @Setter
    private ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }

    @Autowired
    public SubjectEditor(SubjectService subjectService) {
        this.subjectService = subjectService;
        add(name, actions);
        binder.bindInstanceFields(this);
        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editSubject(subject));
        setVisible(false);
    }

    private void delete() {
        subjectService.deleteSubjectById(subject.getId());
        changeHandler.onChange();
    }

    private void save() {
        if (isNameValid(subject.getName())) {
            subjectService.createSubject(subject.getName());
        }
        changeHandler.onChange();
    }

    public void editSubject(Subject subject) {
        if (subject == null) {
            setVisible(false);
            return;
        }

        if (subject.getId() != null) {
            try {
                this.subject = subjectService.readSubjectById(subject.getId());
            } catch (IllegalStateException e) {
                this.subject = subject;
            }

            binder.setBean(this.subject);
            setVisible(true);
        }
    }

    private boolean isNameValid(String name) {
        return name.matches("[A-Z][a-z]{0,49}");
    }
}
