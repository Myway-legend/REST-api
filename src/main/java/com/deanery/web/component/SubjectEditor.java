package com.deanery.web.component;

import com.deanery.entity.Subject;
import com.deanery.repository.SubjectsRepository;
import com.deanery.security.jwt.JwtTokenProvider;
import com.deanery.service.SubjectService;
import com.deanery.web.InvalidDataNotification;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class SubjectEditor extends VerticalLayout implements KeyNotifier {

    private final SubjectsRepository subjectsRepository;

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
    public SubjectEditor(SubjectsRepository subjectsRepository) {
        this.subjectsRepository = subjectsRepository;
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
        subjectsRepository.delete(subject);
        changeHandler.onChange();
    }

    private void save() {
        if (isNameValid(subject.getName())) {
            subjectsRepository.save(subject);
        } else {
            InvalidDataNotification.showError("Invalid name");
        }
        changeHandler.onChange();
    }

    public void editSubject(Subject subject) {
        if (subject == null) {
            setVisible(false);
            return;
        }

        if (subject.getId() != null) {
            this.subject = subjectsRepository.findById(subject.getId()).orElse(subject);
        } else {
            this.subject = subject;
        }
        binder.setBean(this.subject);
        setVisible(true);
    }

    private boolean isNameValid(String name) {
        return name.matches("[A-Z][a-z]{0,49}");
    }
}
